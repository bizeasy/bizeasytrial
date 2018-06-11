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

import java.util.List;

import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.*
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.party.contact.ContactHelper
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil

shipmentId = parameters.shipmentId
if (!shipmentId) {
    shipmentId = request.getAttribute("shipmentId")
}
shipment = from("Shipment").where("shipmentId", shipmentId).queryOne()

context.shipmentId = shipmentId
context.shipment = shipment

Debug.log("shipmentId =============== "+shipmentId);

if (shipment) {
	
	availableAirwayBillNumbers = EntityQuery.use(delegator).from("CarrierAirwayBills").where("shipmentId", shipmentId).orderBy("airwayBillNo").queryList();
	Debug.log("availableAirwayBillNumbers =========="+availableAirwayBillNumbers);
	String airwayBillNumber = "000000000000";
	if(UtilValidate.isNotEmpty(availableAirwayBillNumbers)){
		airwayBillNumber = (String)(EntityUtil.getFirst(availableAirwayBillNumbers)).get("airwayBillNo");
	}
	context.airwayBillNumber = airwayBillNumber;
	
	orderHeader = shipment.getRelatedOne("PrimaryOrderHeader", false);
	orderPaymentPreference = orderHeader.getRelated("OrderPaymentPreference", null, null, false);
	orderItems = orderHeader.getRelated("OrderItem", null, null, false);
	orderItem = orderItems[0];
	
	
	findMap = [orderId: orderHeader.orderId]
	orderItemShipGroup = from("OrderItemShipGroup").where(findMap).orderBy("shipGroupSeqId").queryFirst()
	context.carrierId = orderItemShipGroup.carrierPartyId
	
	
	
	totalWt = 0;
	firstItemWt = 0;
	firstItemUom = "gms";
	noOfPieces = 0;
	for(int i=0; i<orderItems.size(); i++){
		eachItem = orderItems[i];
		quantity = eachItem.quantity
		noOfPieces += quantity;
		eachProduct = from("Product").where("productId", orderItem.productId).queryOne();
		eachProdWt = eachProduct.shippingWeight;
		eachProdWtUom = eachProduct.weightUomId;
		if(eachProdWt){
			if(i==0){
				firstItemWt = eachProdWt;
				if(eachProdWtUom && eachProdWtUom == "WT_kg"){
					firstItemUom = "kgs";
				}
				
			}
			totalWt += (eachProdWt*quantity);
		}
	}
	
	
	product = from("Product").where("productId", orderItem.productId).queryOne()
            
	
	toPerson = shipment.getRelatedOne("ToPerson", false)
	
	String paymentType = "PREPAID";
	if(orderPaymentPreference){
		paymentPref = orderPaymentPreference[0].paymentMethodTypeId;
		if(paymentPref.equals("EXT_COD")){
			paymentType = "CASH ON DELIVERY";
		}
	}
	
	Debug.log("Origin Address ==============="+shipment.getRelatedOne("OriginPostalAddress", false));
	
	context.firstItemUom = firstItemUom
	context.noOfPieces = noOfPieces
	context.totalWt = totalWt
	context.firstItemWt = firstItemWt
    context.shipmentType = shipment.getRelatedOne("ShipmentType", false)
    context.statusItem = shipment.getRelatedOne("StatusItem", false)
    context.primaryOrderHeader = orderHeader
    context.paymentType = paymentType
    context.toPerson = toPerson;
    context.toPartyGroup = shipment.getRelatedOne("ToPartyGroup", false)
    context.fromPerson = shipment.getRelatedOne("FromPerson", false)
    context.fromPartyGroup = shipment.getRelatedOne("FromPartyGroup", false)
    context.originFacility = shipment.getRelatedOne("OriginFacility", false)
    context.destinationFacility = shipment.getRelatedOne("DestinationFacility", false)
    context.originPostalAddress = shipment.getRelatedOne("OriginPostalAddress", false)
    context.destinationPostalAddress = shipment.getRelatedOne("DestinationPostalAddress", false)
    context.product = product
    
    shipToContactMechList = ContactHelper.getContactMech(toPerson, "PHONE_SHIPPING", "TELECOM_NUMBER", false);
    if(UtilValidate.isEmpty(shipToContactMechList)){
    	shipToContactMechList = ContactHelper.getContactMech(toPerson, "PHONE_WORK", "TELECOM_NUMBER", false);
    }
    
    if (shipToContactMechList) {
        shipToTelecomNumber = (EntityUtil.getFirst(shipToContactMechList)).getRelatedOne("TelecomNumber", false)
        Debug.log("shipToTelecomNumber ===================="+shipToTelecomNumber);
        context.shipToTelecomNumber = shipToTelecomNumber
    }
    
}

// check permission
hasPermission = false
if (security.hasEntityPermission("FACILITY", "_VIEW", userLogin)) {
    hasPermission = true
} else {
    if (shipment) {
        if (shipment.primaryOrderId) {
            // allow if userLogin is associated with the primaryOrderId with the SUPPLIER_AGENT roleTypeId
            orderRole = from("OrderRole").where("orderId", shipment.primaryOrderId, "partyId", userLogin.partyId, "roleTypeId", "SUPPLIER_AGENT").queryOne()
            if (orderRole) {
                hasPermission = true
            }
        }
    }
}
context.hasPermission = hasPermission
