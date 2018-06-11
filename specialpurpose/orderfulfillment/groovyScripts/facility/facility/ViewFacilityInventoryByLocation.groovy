/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.*
import java.sql.Timestamp

import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.entity.transaction.*
import org.apache.ofbiz.entity.model.DynamicViewEntity
import org.apache.ofbiz.entity.model.ModelKeyMap
import org.apache.ofbiz.entity.util.EntityFindOptions
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.inventory.*
import org.apache.ofbiz.service.ServiceUtil;

import java.math.BigDecimal;

facilityId=session.getAttribute("GlfacilityId");
action = request.getParameter("action")
statusId = request.getParameter("statusId")
searchParameterString = ""
searchParameterString = "action=Y&facilityId=" + facilityId

offsetQOH = -1
offsetATP = -1
hasOffsetQOH = false
hasOffsetATP = false

rows = [] as ArrayList
int listSize = 0 // The complete size of the list of result (for pagination)

if (action) {
    // ------------------------------
    prodView = new DynamicViewEntity()
    conditionMap = [facilityId : facilityId]

    if (offsetQOHQty) {
        try {
            offsetQOH = Integer.parseInt(offsetQOHQty)
            hasOffsetQOH = true
            searchParameterString = searchParameterString + "&offsetQOHQty=" + offsetQOH
        } catch (NumberFormatException nfe) {
        }
    }
    if (offsetATPQty) {
        try {
            offsetATP = Integer.parseInt(offsetATPQty)
            hasOffsetATP = true
            searchParameterString = searchParameterString + "&offsetATPQty=" + offsetATP
        } catch (NumberFormatException nfe) {
        }
    }

    prodView.addMemberEntity("PRFA", "ProductFacility")
    prodView.addAliasAll("PRFA", null, null)

    prodView.addMemberEntity("PROD", "Product")
    prodView.addViewLink("PROD", "PRFA", Boolean.FALSE, ModelKeyMap.makeKeyMapList("productId"))
    prodView.addAlias("PROD", "internalName")
    prodView.addAlias("PROD", "isVirtual")
    prodView.addAlias("PROD", "salesDiscontinuationDate")
    if (productTypeId) {
        prodView.addAlias("PROD", "productTypeId")
        conditionMap.productTypeId = productTypeId
        searchParameterString = searchParameterString + "&productTypeId=" + productTypeId
    }
    if (searchInProductCategoryId) {
        prodView.addMemberEntity("PRCA", "ProductCategoryMember")
        prodView.addViewLink("PRFA", "PRCA", Boolean.FALSE, ModelKeyMap.makeKeyMapList("productId"))
        prodView.addAlias("PRCA", "productCategoryId")
        conditionMap.productCategoryId = searchInProductCategoryId
        searchParameterString = searchParameterString + "&searchInProductCategoryId=" + searchInProductCategoryId
    }

    if (productSupplierId) {
        prodView.addMemberEntity("SPPR", "SupplierProduct")
        prodView.addViewLink("PRFA", "SPPR", Boolean.FALSE, ModelKeyMap.makeKeyMapList("productId"))
        prodView.addAlias("SPPR", "partyId")
        conditionMap.partyId = productSupplierId
        searchParameterString = searchParameterString + "&productSupplierId=" + productSupplierId
    }

    // set distinct on so we only get one row per product
    searchCondition = EntityCondition.makeCondition(conditionMap, EntityOperator.AND)
    notVirtualCondition = EntityCondition.makeCondition(EntityCondition.makeCondition("isVirtual", EntityOperator.EQUALS, null),
                                                         EntityOperator.OR,
                                                         EntityCondition.makeCondition("isVirtual", EntityOperator.NOT_EQUAL, "Y"))

    whereConditionsList = [searchCondition, notVirtualCondition]
    // add the discontinuation date condition
    if (productsSoldThruTimestamp) {
        discontinuationDateCondition = EntityCondition.makeCondition(
               [
                EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, null),
                EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.GREATER_THAN, productsSoldThruTimestamp)
               ],
               EntityOperator.OR)
        whereConditionsList.add(discontinuationDateCondition)
        searchParameterString = searchParameterString + "&productsSoldThruTimestamp=" + productsSoldThruTimestamp
    }

    // add search on internal name
    if (internalName) {
        whereConditionsList.add(EntityCondition.makeCondition("internalName", EntityOperator.LIKE, "%" + internalName + "%"))
        searchParameterString = searchParameterString + "&internalName=" + internalName
    }

    // add search on productId
    if (productId) {
        whereConditionsList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId))
        searchParameterString = searchParameterString + "&productId=" + productId
    }
    // add statusId in search parametters
    if (statusId) {
        searchParameterString = searchParameterString + "&statusId=" + statusId;
    }
    
        whereCondition = EntityCondition.makeCondition(whereConditionsList, EntityOperator.AND)

    beganTransaction = false
    // get the indexes for the partial list
    lowIndex = ((viewIndex.intValue() * viewSize.intValue()) + 1)
    highIndex = (viewIndex.intValue() + 1) * viewSize.intValue()
    // add viewSize and viewIndex in search parameters
    searchParameterString = searchParameterString + "&VIEW_SIZE=" + viewSize + "&VIEW_INDEX=" + viewIndex;
    List prods = null
	List facilityLocations=null
	List productPrices=null
    try {
        beganTransaction = TransactionUtil.begin()
        prodsEli = from(prodView).where(whereCondition).orderBy("productId").cursorScrollInsensitive().distinct().queryIterator()
		
		if(locatedAtLocationSeqId){
			
			facilityLocations =from("FacilityLocation").where(EntityCondition.makeCondition([EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,facilityId),EntityCondition.makeCondition("locationSeqId",EntityOperator.EQUALS,locatedAtLocationSeqId)],EntityOperator.AND)).queryList();
		}else{
			facilityLocations =from("FacilityLocation").where(EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,facilityId)).queryList();
		}
		prods = prodsEli.getPartialList(lowIndex, highIndex)
		productPrices=from("ProductPrice").where(EntityCondition.makeCondition("productId",EntityOperator.IN,EntityUtil.getFieldListFromEntityList(prods, "productId", true))).orderBy("-fromDate").queryList();
		
		        
        listSize = prodsEli.getResultsSizeAfterPartialList()
        prodsEli.close()
    } catch (GenericEntityException e) {
        errMsg = "Failure in operation, rolling back transaction"
        Debug.logError(e, errMsg, "ViewFacilityInventoryByProduct")
        try {
            // only rollback the transaction if we started one...
            TransactionUtil.rollback(beganTransaction, errMsg, e)
        } catch (GenericEntityException e2) {
            Debug.logError(e2, "Could not rollback transaction: " + e2.toString(), "ViewFacilityInventoryByProduct")
        }
        // after rolling back, rethrow the exception
        throw e
    } finally {
        // only commit the transaction if we started one... this will throw an exception if it fails
        TransactionUtil.commit(beganTransaction)
    }

    // If the user has specified a number of months over which to sum usage quantities, define the correct timestamp
    Timestamp checkTime = null
    monthsInPastLimitStr = request.getParameter("monthsInPastLimit")
    if (monthsInPastLimitStr) {
        try {
            monthsInPastLimit = Integer.parseInt(monthsInPastLimitStr)
            cal = UtilDateTime.toCalendar(null)
            cal.add(Calendar.MONTH, 0 - monthsInPastLimit)
            checkTime = UtilDateTime.toTimestamp(cal.getTime())
            searchParameterString += "&monthsInPastLimit=" + monthsInPastLimitStr
        } catch (Exception e) {
            // Ignore
        }
    }
	mainCond = EntityCondition.makeCondition(
		[
		 EntityCondition.makeCondition("productId", EntityOperator.IN,EntityUtil.getFieldListFromEntityList(prods, "productId", true)),
		 EntityCondition.makeCondition("locationSeqId", EntityOperator.IN, EntityUtil.getFieldListFromEntityList(facilityLocations, "locationSeqId", true)),
		 EntityCondition.makeCondition("facilityId", EntityOperator.EQUALS,facilityId)
		],
		EntityOperator.AND)
	List inventoryItems=from("InventoryItemAndDetail").where(mainCond).queryList();
	List orderIdsList = EntityUtil.getFieldListFromEntityList(inventoryItems,"orderId",true);
	
	orderItemCond = EntityCondition.makeCondition(
		[
		 EntityCondition.makeCondition("orderId", EntityOperator.IN,orderIdsList),
		 EntityCondition.makeCondition("orderStatusId", EntityOperator.EQUALS, "ORDER_APPROVED"),
		],
		EntityOperator.AND)
	List orderHeaderAndItems=from("OrderHeaderAndItems").where(orderItemCond).queryList();
	
	List purchaseOrders = EntityUtil.filterByCondition(orderHeaderAndItems, EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "PURCHASE_ORDER"));
	List salesOrders = EntityUtil.filterByCondition(orderHeaderAndItems, EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER"));
	
	
	
	finalMap=[:];
	if(inventoryItems){
		inventoryItems.each{ inventoryItem ->
			
			Debug.log("inventoryItem ============"+inventoryItem);
			listPrice=BigDecimal.ZERO;defaultPrice=BigDecimal.ZERO;wholeSalePrice=BigDecimal.ZERO;
			prodPrice = EntityUtil.filterByCondition(productPrices,EntityCondition.makeCondition("productId",EntityOperator.EQUALS,inventoryItem.productId))
			if(prodPrice){
				prodPrice.each{price ->
					if (price.getString("productPriceTypeId").equals("DEFAULT_PRICE")) { //defaultPrice
						defaultPrice= price.price;
					} else if (price.getString("productPriceTypeId").equals("WHOLESALE_PRICE")) {//
						wholeSalePrice = price.price;
					} else if (price.getString("productPriceTypeId").equals("LIST_PRICE")) {//listPrice
						listPrice= price.price;
					} else {
						listPrice= price.price;
						defaultPrice= price.price;
						wholeSalePrice = price.price;
					}
				}
			}
			tempMap=[:];
			tempMap.locationSeqId=inventoryItem.locationSeqId;
			tempMap.listPrice=listPrice;
			tempMap.defaultPrice=defaultPrice;
			tempMap.wholeSalePrice=wholeSalePrice;
			tempMap.productId=inventoryItem.productId;
			tempMap.totalAvailableToPromise=inventoryItem.availableToPromiseDiff;
			tempMap.totalQuantityOnHand=inventoryItem.quantityOnHandDiff;
			tempMap.poDetailsMap = [:];
			tempMap.soDetailsMap = [:];
			
			// Test otders
			soCount=0;
			poCount=0;
			diffQOH=0;
			diffATP=0;
			tempOrderMap = [:];
			
			Debug.log("orderId ============"+inventoryItem.orderId);
			if(inventoryItem.orderId){
				
				invOrderItemCond = EntityCondition.makeCondition(
					[
					 EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, inventoryItem.orderId),
					 EntityCondition.makeCondition("productId", EntityOperator.EQUALS, inventoryItem.productId)
					],
					EntityOperator.AND)
				
				List invPurchaseOrders = EntityUtil.filterByCondition(purchaseOrders, invOrderItemCond);
				List invSalesOrders = EntityUtil.filterByCondition(salesOrders, invOrderItemCond);
				Debug.log("invPurchaseOrders=========="+invPurchaseOrders);
				Debug.log("invSalesOrders=========="+invSalesOrders);
				if(invPurchaseOrders){
					poCount = 1;
					tempOrderMap = [:];
				}	
				if(invSalesOrders){
					soCount = 1;
					itemQty = 0;
					for(int i=0; i<invSalesOrders.size(); i++){
						itemQty += invSalesOrders[i].quantity;
					}
					diffQOH = inventoryItem.quantityOnHandDiff;
					diffATP = inventoryItem.availableToPromiseDiff;
					soDiffQty = diffQOH-diffATP;
					tempOrderDetailsMap = [:];
					tempOrderMap.soCount = soCount;
					tempOrderMap.itemQty = itemQty;
					tempOrderMap.diffQOH=diffQOH;
					tempOrderMap.diffATP=diffATP;
					
					tempOrderDetailsMap.put(inventoryItem.orderId, tempOrderMap);
					tempMap.soDetailsMap = tempOrderDetailsMap;
				}
				
				Debug.log("soCount=========="+soCount);
				Debug.log("poCount=========="+poCount);
				
			}
			Debug.log("tempMap ============"+tempMap);
			
			
			
			
			if(UtilValidate.isEmpty(finalMap[inventoryItem.locationSeqId])){
				tempProdMap=[:];
				tempProdMap[inventoryItem.productId]=tempMap;
				finalMap[inventoryItem.locationSeqId]=tempProdMap;
			}else{
				tempProdMap = finalMap[inventoryItem.locationSeqId];
				if(UtilValidate.isEmpty(tempProdMap[inventoryItem.productId])){
					tempProdMap[inventoryItem.productId]=tempMap;
					finalMap[inventoryItem.locationSeqId]=tempProdMap;
				}else{
					tempMap = tempProdMap[inventoryItem.productId];
					tempMap.totalAvailableToPromise=tempMap.totalAvailableToPromise+inventoryItem.availableToPromiseDiff;
					tempMap.totalQuantityOnHand=tempMap.totalQuantityOnHand+inventoryItem.quantityOnHandDiff;
					tempOrderDetails=[:];
					tempOrderDetails = tempMap.soDetailsMap;
					Debug.log("tempOrderDetails=========="+tempOrderDetails);
					if(soCount>0){
						Debug.log("tempOrderDetails values=========="+tempOrderDetails[inventoryItem.orderId]);
						if(UtilValidate.isEmpty(tempOrderDetails[inventoryItem.orderId])){
							tempOrderDetails[inventoryItem.orderId] = tempOrderMap;
							tempMap.soDetailsMap=tempOrderDetails;
						}
						else{
							tempOrderQtyMap=[:];
							tempOrderQtyMap = tempOrderDetails[inventoryItem.orderId];
							Debug.log("tempOrderQtyMap=========="+tempOrderQtyMap.soCount);
							tempOrderQtyMap.soCount = tempOrderQtyMap.soCount + soCount;
							tempOrderQtyMap.diffQOH = tempOrderQtyMap.diffQOH + diffQOH;
							tempOrderQtyMap.diffATP = tempOrderQtyMap.diffATP + diffATP;
							
							tempOrderDetails[inventoryItem.orderId] = tempOrderQtyMap;
							tempMap.soDetailsMap = tempOrderDetails;
							Debug.log("tempMap order details=========="+tempMap);
						}
					}
					
					
					tempProdMap[inventoryItem.productId]=tempMap;
					finalMap[inventoryItem.locationSeqId]=tempProdMap;
					Debug.log("finalMap====="+finalMap)
				}
			}
			
		}
		
	}
	Debug.log("finalMap====="+finalMap)
	if(finalMap){
		for (Map.Entry entry : finalMap.entrySet()) {
			String locationSeqId = entry.getKey();
			for (Map.Entry innerEntry : entry.getValue().entrySet()) {
				
				Map soDetailsMap = innerEntry.getValue().get("soDetailsMap");
				Debug.log("soDetailsMap====="+soDetailsMap)
				count = soDetailsMap.keySet().size();
				Debug.log("count====="+count)
				
				innerEntry.getValue().putAt("openSO", count);
				
				rows.add(innerEntry.getValue());
			}
		}
	}
	
	}

context.overrideListSize = listSize
context.inventoryByLocation = rows
context.searchParameterString = searchParameterString
