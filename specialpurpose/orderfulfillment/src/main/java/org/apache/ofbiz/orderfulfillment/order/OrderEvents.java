/*******************************************************************************
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
 *******************************************************************************/
package org.apache.ofbiz.orderfulfillment.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.order.order.OrderChangeHelper;
import org.apache.ofbiz.order.shoppingcart.CheckOutHelper;
import org.apache.ofbiz.order.shoppingcart.product.ProductPromoWorker;
import org.apache.ofbiz.entity.GenericDelegator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartEvents;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.ofbiz.base.conversion.NumberConverters.BigDecimalToString;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilFormatOut;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilNumber;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.security.Security;
import org.apache.ofbiz.party.contact.ContactMechWorker;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.json.JSONException;
import org.apache.ofbiz.entity.util.EntityQuery;

import java.nio.ByteBuffer;
import java.util.Map.Entry;

import org.apache.ofbiz.party.party.PartyHelper;
import org.apache.ofbiz.product.spreadsheetimport.ImportProductHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.ofbiz.entity.transaction.TransactionUtil;

/**
 * Java Helper Class for Janrain Engage
 */
public class OrderEvents {

    public static final String module = OrderEvents.class.getName();
    
    
    public static String processSalesOrderEvent(HttpServletRequest request, HttpServletResponse response) {
		
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		DispatchContext dctx =  dispatcher.getDispatchContext();
		Locale locale = UtilHttp.getLocale(request);
		String customerName = (String) request.getParameter("customerName");
		String emailId = (String) request.getParameter("emailId");
		String phoneNo = (String) request.getParameter("phoneNo");
		String address1 = (String) request.getParameter("address1");
		
		String address2 = (String) request.getParameter("address2");
		String city = (String) request.getParameter("city");
		String postalCode = (String) request.getParameter("postalCode");
		String stateProvinceGeoId = (String) request.getParameter("stateProvinceGeoId");
		String partyId = null;
		String productStoreId = (String) request.getParameter("productStoreId");
		String facilityId = (String) request.getParameter("facilityId");
		String salesChannel = "WEB_SALES_CHANNEL";
		String referenceNo = (String) request.getParameter("referenceNo");
		String paymentMethod = (String) request.getParameter("paymentMethod");
		String countryGeoId = "IND";
		
		
		/*JSONArray invoiceListJSON = new JSONArray();
		invoiceListJSON.*/
		
		if(UtilValidate.isEmpty(customerName) || UtilValidate.isEmpty(emailId) || UtilValidate.isEmpty(phoneNo) || UtilValidate.isEmpty(address1) || UtilValidate.isEmpty(address2) || UtilValidate.isEmpty(city) || UtilValidate.isEmpty(postalCode) || UtilValidate.isEmpty(stateProvinceGeoId) || UtilValidate.isEmpty(productStoreId) || UtilValidate.isEmpty(paymentMethod) || UtilValidate.isEmpty(referenceNo) ){
			Debug.logError("Some of the required inputs are missing", module);
			request.setAttribute("_ERROR_MESSAGE_", "Required inputs are missing.");
			return "error";
		}
		
		
		if(UtilValidate.isNotEmpty(request.getParameter("countryGeoId"))){
			countryGeoId = (String) request.getParameter("countryGeoId");
		}
		
		Debug.log("customerName ============="+customerName);
		Debug.log("emailId ============="+emailId);
		Debug.log("phoneNo ============="+phoneNo);
		Debug.log("address1 ============="+address1);
		Debug.log("address2 ============="+address2);
		Debug.log("city ============="+city);
		Debug.log("postalCode ============="+postalCode);
		Debug.log("stateProvinceGeoId ============="+stateProvinceGeoId);
		
		Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
		Debug.log("paramMap ============="+paramMap);
		int rowCount = UtilHttp.getMultiFormRowCount(paramMap);
		Debug.log("rowCount ============="+rowCount);
		
		
		List<Map<String, Object>> orderProductList = new LinkedList<Map<String,Object>>();
		Map<String, Object> processOrderContext = new HashMap<String, Object>();
				
		for (int i = 0; i < rowCount; i++) {
		
			String productId = null;
			String quantityStr = null;
			String priceStr = null;
			/*String taxType = null;
			String taxRateStr = null;
			String taxAmtStr = null;*/
			Map<String  ,Object> productQtyMap = new HashMap<String, Object>();
			String thisSuffix = "_o_" + i;
			BigDecimal quantity = BigDecimal.ZERO;
			BigDecimal price = BigDecimal.ZERO;
			/*BigDecimal taxRate = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;*/
			
			
			
			if (paramMap.containsKey("productId" + thisSuffix)) {
				if(UtilValidate.isEmpty(paramMap.get("productId" + thisSuffix))){
					continue;
				}
				productId = (String) paramMap.get("productId" + thisSuffix);
			}
			if (paramMap.containsKey("quantity" + thisSuffix)) {
				if(UtilValidate.isEmpty(paramMap.get("quantity" + thisSuffix))){
					continue;
				}
				quantityStr = (String) paramMap.get("quantity" + thisSuffix);
			}
			if (paramMap.containsKey("price" + thisSuffix)) {
				priceStr = (String) paramMap.get("price" + thisSuffix);
			}
			/*if (paramMap.containsKey("taxType" + thisSuffix)) {
				taxType = (String) paramMap.get("taxType" + thisSuffix);
			}
			if (paramMap.containsKey("taxRate" + thisSuffix)) {
				taxRateStr = (String) paramMap.get("taxRate" + thisSuffix);
			}
			if (paramMap.containsKey("taxAmt" + thisSuffix)) {
				taxAmtStr = (String) paramMap.get("taxAmt" + thisSuffix);
			}*/
			
			try {
				if(UtilValidate.isNotEmpty(quantityStr) ){
					quantity = new BigDecimal(quantityStr);
				}
				if(UtilValidate.isNotEmpty(priceStr) ){
					price = new BigDecimal(priceStr);
				}
				/*if(UtilValidate.isNotEmpty(taxRateStr) ){
					taxRate = new BigDecimal(taxRateStr);
				}
				if(UtilValidate.isNotEmpty(taxRateStr) ){
					taxAmt = new BigDecimal(taxRateStr);
				}*/
			} catch (Exception e) {
				Debug.logError(e, "Problems parsing on of quantityStr, priceStr and string", module);
				request.setAttribute("_ERROR_MESSAGE_", "Problems parsing on of quantityStr, priceStr and string");
				return "error";
			}
			
			productQtyMap.put("productId", productId);
			productQtyMap.put("quantity", quantity);
			productQtyMap.put("price", price);
			/*productQtyMap.put("taxRate", taxRate);
			productQtyMap.put("taxType", taxType);
			productQtyMap.put("taxAmt", taxAmt);*/
			
			orderProductList.add(productQtyMap);
		}
		
		processOrderContext.put("userLogin", userLogin);
		processOrderContext.put("orderProductList", orderProductList);
		processOrderContext.put("partyId", partyId);
		//processOrderContext.put("salesChannel", salesChannel);
		//processOrderContext.put("orderTaxType", orderTaxType);
		//processOrderContext.put("orderId", orderId);
		processOrderContext.put("productStoreId", productStoreId);
		processOrderContext.put("facilityId", facilityId);
		processOrderContext.put("referenceNo", referenceNo);
		processOrderContext.put("salesChannel", salesChannel);
		
		processOrderContext.put("customerName", customerName);
		processOrderContext.put("emailId", emailId);
		processOrderContext.put("phoneNo", phoneNo);
		processOrderContext.put("address1", address1);
		processOrderContext.put("address2", address2);
		processOrderContext.put("city", city);
		processOrderContext.put("postalCode", postalCode);
		processOrderContext.put("stateProvinceGeoId", stateProvinceGeoId);
		processOrderContext.put("countryGeoId", countryGeoId);
		processOrderContext.put("paymentMethod", paymentMethod);
		
		
		Map<String, Object> result = ServiceUtil.returnSuccess();
		try{
			result = processSalesOrder(dctx, processOrderContext);
			if(ServiceUtil.isError(result)){
				Debug.logError("Unable to generate order: " + ServiceUtil.getErrorMessage(result), module);
				request.setAttribute("_ERROR_MESSAGE_", "Unable to generate order  For party :" + partyId);
				return "error";
			}
		}catch(Exception e){
			Debug.logError(e, module);
			return "error";
		}
		String sucMsg = "Your order "+result.get("orderId")+" has been successfully processed!";
        request.setAttribute("_EVENT_MESSAGE_", sucMsg);
		return "success";
	}
    
    public static Map<String, Object> processSalesOrder(DispatchContext dctx, Map<String, ? extends Object> context) {
    	
    	Delegator delegator = dctx.getDelegator();
	    LocalDispatcher dispatcher = dctx.getDispatcher();
	    Locale locale = (Locale) context.get("locale");
	    GenericValue userLogin = (GenericValue) context.get("userLogin");
	    Timestamp effectiveDate = UtilDateTime.nowTimestamp();
	    
	    Map<String, Object> result = ServiceUtil.returnSuccess();
	    List<Map> orderProductList = (List) context.get("orderProductList");
	    
	    String partyId = null;
	    String referenceNo = (String) context.get("referenceNo");
	    String customerName = (String) context.get("customerName");
	    String emailId = (String) context.get("emailId");
	    String phoneNo = (String) context.get("phoneNo");
	    String address1 = (String) context.get("address1");
	    String address2 = (String) context.get("address2");
	    String city = (String) context.get("city");
	    String postalCode = (String) context.get("postalCode");
	    String stateProvinceGeoId = (String) context.get("stateProvinceGeoId");
	    String productStoreId = (String) context.get("productStoreId");
	    String facilityId = (String) context.get("facilityId");
	    String salesChannel = (String) context.get("salesChannel");
	    String billToCustomer = null;
	    String countryGeoId = (String) context.get("countryGeoId");
	    String paymentMethod = (String) context.get("paymentMethod");
	    String shipGroupContactMechId = null;
	    
	    Map<String, Object> person = UtilMisc.toMap(
                "firstName", customerName,
                "preferredCurrencyUomId", "INR",
                "statusId", "PARTY_ENABLED",
                "userLogin", userLogin
                );    
		try{
			Map<String, Object> serviceResult = dispatcher.runSync("createPerson", person);
			if(ServiceUtil.isError(serviceResult)){
				Debug.logError("Unable to Create Customer: " + ServiceUtil.getErrorMessage(serviceResult), module);
			}
			partyId = (String) serviceResult.get("partyId");
		}catch(Exception e){
			Debug.logError(e, "Error in creating Party",module);
		}
		Debug.log("partyId ================"+partyId);
		
        // Postal Address
        Map<String, Object> postalAddress = UtilMisc.toMap("userLogin", userLogin);
        postalAddress.put("partyId", partyId);
        postalAddress.put("toName", customerName);
        postalAddress.put("address1", address1);
        postalAddress.put("address2", address2);
        postalAddress.put("postalCode", postalCode);
        postalAddress.put("city", city);
        postalAddress.put("stateProvinceGeoId", stateProvinceGeoId);
        postalAddress.put("countryGeoId", countryGeoId);
        postalAddress.put("userLogin", userLogin);      
        postalAddress.put("contactMechPurposeTypeId", "SHIPPING_LOCATION");    
        try{
        	
            Map<String, Object> summaryResult = dispatcher.runSync("createPartyPostalAddress", postalAddress);
            shipGroupContactMechId = (String) summaryResult.get("contactMechId");
        	
            Map<String, Object> mechContext = new HashMap<String, Object>();
            mechContext.put("partyId", partyId);
            mechContext.put("contactMechId", shipGroupContactMechId);
            mechContext.put("contactMechPurposeTypeId", "BILLING_LOCATION");
            mechContext.put("userLogin", userLogin);
            dispatcher.runSync("createPartyContactMechPurpose", mechContext);
        	
        	// Email Address
            mechContext = new HashMap<String, Object>();
            mechContext.put("contactNumber", phoneNo);
            mechContext.put("partyId", partyId);
            mechContext.put("userLogin", userLogin);
            mechContext.put("contactMechPurposeTypeId", "PHONE_SHIPPING");
            summaryResult = dispatcher.runSync("createPartyTelecomNumber", mechContext);
            
            mechContext.clear();
            mechContext.put("emailAddress", emailId);
            mechContext.put("userLogin", userLogin);
            mechContext.put("contactMechTypeId", "EMAIL_ADDRESS");
            mechContext.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
            mechContext.put("partyId", partyId);
            summaryResult = dispatcher.runSync("createPartyEmailAddress", mechContext);

            // Party Roles
            Map<String, Object> partyRole = UtilMisc.toMap(
                    "partyId", partyId,
                    "roleTypeId", "CUSTOMER", 
                    "userLogin", userLogin
                    );
            dispatcher.runSync("createPartyRole", partyRole);
            
            partyRole.put("roleTypeId", "CONTACT");
            dispatcher.runSync("createPartyRole", partyRole);
            
            partyRole.put("roleTypeId", "BILL_TO_CUSTOMER");
            dispatcher.runSync("createPartyRole", partyRole);
            
            partyRole.put("roleTypeId", "SHIP_TO_CUSTOMER");
            dispatcher.runSync("createPartyRole", partyRole);
        
        }catch(Exception e){
			Debug.logError(e, "Error in creating Party",module);
		}
	    
	    
	    
	    ShoppingCart cart = new ShoppingCart(delegator, productStoreId, locale,"INR");
	    try {
			//get inventoryFacility details through productStore.
			
	    	/*String  inventoryFacilityId="";
			GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
			if(UtilValidate.isNotEmpty(productStore)){
				inventoryFacilityId = productStore.getString("inventoryFacilityId");
			}*/
			cart.setOrderType("SALES_ORDER");
	        cart.setExternalId(referenceNo);
	        cart.setProductStoreId(productStoreId);
			cart.setChannelType(salesChannel);
			//cart.setOrderId(orderId);
			cart.setBillToCustomerPartyId(partyId);
			cart.setFacilityId(facilityId);//for store inventory we need this so that inventoryItem query by this orginFacilityId
			cart.setPlacingCustomerPartyId(partyId);
			cart.setShipToCustomerPartyId(partyId);
			cart.setEndUserCustomerPartyId(partyId);
			cart.setOrderDate(effectiveDate);
			cart.setUserLogin(userLogin, dispatcher);
			//cart.setOrderMessage(orderMessage);
		} catch (Exception e) {
			Debug.logError(e, "Error in setting cart parameters", module);
			return ServiceUtil.returnError("Error in setting cart parameters");
		}
    	
	    for (Map<String, Object> prodQtyMap : orderProductList) {
	    	Debug.log("prodQtyMap =============="+prodQtyMap);
	    	String productId = "";
	    	String taxType = "";
			BigDecimal quantity = BigDecimal.ZERO;
			//BigDecimal basicPrice = BigDecimal.ZERO;
	    	
	    	if(UtilValidate.isNotEmpty(prodQtyMap.get("productId"))){
				productId = (String)prodQtyMap.get("productId");
			}
			if(UtilValidate.isNotEmpty(prodQtyMap.get("quantity"))){
				quantity = (BigDecimal)prodQtyMap.get("quantity");
			}
			/*if(UtilValidate.isNotEmpty(prodQtyMap.get("price"))){
				basicPrice = (BigDecimal)prodQtyMap.get("price");
			}*/
			
			
			
			
			
			try{
				ShoppingCartItem item = null;
				if(UtilValidate.isNotEmpty(prodQtyMap.get("price"))){
					Debug.log("usd =============="+prodQtyMap.get("price"));
					item = ShoppingCartItem.makeItem(null, productId, null, quantity, (BigDecimal)prodQtyMap.get("price"), null, null, null, null, null, null, null, null, null, null, null, dispatcher, cart, null, null, null, Boolean.TRUE, Boolean.TRUE);
					item.setBasePrice((BigDecimal)prodQtyMap.get("price"));
					
				}
				else{
					Debug.log("calculated =============="+prodQtyMap.get("price"));
					item = ShoppingCartItem.makeItem(null, productId, null, quantity, null, null, null, null, null, null, null, null, null, null, null, null, dispatcher, cart, null, null, null, Boolean.TRUE, Boolean.TRUE);
				}
				
				cart.addItemToEnd(item);
			}
			catch (Exception exc) {
				Debug.logError("Error adding product with id " + productId + " to the cart: " + exc.getMessage(), module);
				return ServiceUtil.returnError("Error adding product with id " + productId + " to the cart: ");
	        }
			
	    }
	    
	    cart.setAllShippingContactMechId(shipGroupContactMechId);
		cart.setAllMaySplit(Boolean.FALSE);
		
		String carrierPartyId="UPS";String shipmentMethodTypeId="STANDARD";
		try{
			List<EntityExpr> exprs = UtilMisc.toList(EntityCondition.makeCondition("pincode", EntityOperator.EQUALS, postalCode));
	        EntityCondition cond = EntityCondition.makeCondition(exprs, EntityOperator.AND);
	        List<GenericValue> carrierPincodesPriorities = EntityQuery.use(delegator).from("CarrierPincodesPriority")
	                .where(cond).queryList();
	        if(UtilValidate.isNotEmpty(carrierPincodesPriorities)){
	        	GenericValue carrierPincodesPriority = EntityUtil.getFirst(carrierPincodesPriorities);
	        	if(UtilValidate.isNotEmpty(carrierPincodesPriority)){
	        		carrierPartyId=carrierPincodesPriority.getString("partyId");
	        		shipmentMethodTypeId=carrierPincodesPriority.getString("shipmentMethodTypeId");
	        	}
	        }
		}
		catch (Exception exc) {
			Debug.logError("Error Fetching Carrier and Shipment Method Type" + exc.getMessage(), module);
			return ServiceUtil.returnError("Error Fetching Carrier and Shipment Method Type");
        }
		
		cart.setAllCarrierPartyId(carrierPartyId);
        cart.setAllShipmentMethodTypeId(shipmentMethodTypeId);
        cart.makeAllShipGroupInfos();
	    
	    Map<String, Object> orderCreateResult = new HashMap<String, Object>();
	    cart.setDefaultCheckoutOptions(dispatcher);
		//ProductPromoWorker.doPromotions(cart, dispatcher);
		CheckOutHelper checkout = new CheckOutHelper(dispatcher, delegator, cart);
		try {
			checkout.calcAndAddTax();
			BigDecimal totalAmountPayable = cart.getGrandTotal();
			Debug.log("totalAmountPayable ================="+totalAmountPayable);
			orderCreateResult = checkout.createOrder(userLogin);
			Debug.log("orderCreateResult ================="+orderCreateResult);
			
			String orderIdNew = (String)orderCreateResult.get("orderId");
			Debug.log("orderId ================"+orderIdNew);
			
			Map<String, Object> serviceContext = UtilMisc.<String, Object>toMap("orderId", orderIdNew,"statusId", "PAYMENT_NOT_RECEIVED", "paymentMethodTypeId", paymentMethod, "maxAmount", totalAmountPayable, "userLogin", userLogin);
            try {
            	Map<String, Object> pmResult = dispatcher.runSync("createOrderPaymentPreference", serviceContext);
            } catch (GenericServiceException e) {
                Debug.logError(e, "Problem calling the addPaymentMethodToOrder service", module);
            }
			
			serviceContext = UtilMisc.<String, Object>toMap("orderId", orderIdNew, "statusId", "ORDER_APPROVED", "setItemStatus", "Y", "userLogin", userLogin);
            Map<String, Object> newSttsResult = null;
            try {
                newSttsResult = dispatcher.runSync("changeOrderStatus", serviceContext);
            } catch (GenericServiceException e) {
                Debug.logError(e, "Problem calling the changeOrderStatus service", module);
            }
            result.put("orderId", orderIdNew);		
		} catch (Exception e1) {
		// TODO Auto-generated catch block
			Debug.logError(e1, "Error in CalcAndAddTax",module);
		}
	    
	    return result;
    }
    
    public static String processPurchaseOrderEvent(HttpServletRequest request, HttpServletResponse response) {
		
    	Delegator delegator = (Delegator) request.getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
		HttpSession session = request.getSession();
		GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
		DispatchContext dctx =  dispatcher.getDispatchContext();
		Locale locale = UtilHttp.getLocale(request);
		
		String stateProvinceGeoId = (String) request.getParameter("stateProvinceGeoId");
		String partyId = null;
		String productStoreId = (String) request.getParameter("productStoreId");
		String supplierId = (String) request.getParameter("supplierId");
		String facilityId = (String) request.getParameter("facilityId");
		String salesChannel = "WEB_SALES_CHANNEL";
		String referenceNo = (String) request.getParameter("referenceNo");
		
		Map<String, Object> paramMap = UtilHttp.getParameterMap(request);
		Debug.log("paramMap ============="+paramMap);
		int rowCount = UtilHttp.getMultiFormRowCount(paramMap);
		Debug.log("rowCount ============="+rowCount);
		
		
		List<Map<String, Object>> orderProductList = new LinkedList<Map<String,Object>>();
		Map<String, Object> processOrderContext = new HashMap<String, Object>();
				
		for (int i = 0; i < rowCount; i++) {
		
			String productId = null;
			String quantityStr = null;
			String priceStr = null;
			/*String taxType = null;
			String taxRateStr = null;
			String taxAmtStr = null;*/
			Map<String  ,Object> productQtyMap = new HashMap<String, Object>();
			String thisSuffix = "_o_" + i;
			BigDecimal quantity = BigDecimal.ZERO;
			BigDecimal price = BigDecimal.ZERO;
			/*BigDecimal taxRate = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;*/
			
			if (paramMap.containsKey("productId" + thisSuffix)) {
				productId = (String) paramMap.get("productId" + thisSuffix);
			}
			if (paramMap.containsKey("quantity" + thisSuffix)) {
				quantityStr = (String) paramMap.get("quantity" + thisSuffix);
			}
			if (paramMap.containsKey("price" + thisSuffix)) {
				priceStr = (String) paramMap.get("price" + thisSuffix);
			}
			/*if (paramMap.containsKey("taxType" + thisSuffix)) {
				taxType = (String) paramMap.get("taxType" + thisSuffix);
			}
			if (paramMap.containsKey("taxRate" + thisSuffix)) {
				taxRateStr = (String) paramMap.get("taxRate" + thisSuffix);
			}
			if (paramMap.containsKey("taxAmt" + thisSuffix)) {
				taxAmtStr = (String) paramMap.get("taxAmt" + thisSuffix);
			}*/
			
			try {
				if(UtilValidate.isNotEmpty(quantityStr) ){
					quantity = new BigDecimal(quantityStr);
				}
				if(UtilValidate.isNotEmpty(priceStr) ){
					price = new BigDecimal(priceStr);
				}
				/*if(UtilValidate.isNotEmpty(taxRateStr) ){
					taxRate = new BigDecimal(taxRateStr);
				}
				if(UtilValidate.isNotEmpty(taxRateStr) ){
					taxAmt = new BigDecimal(taxRateStr);
				}*/
			} catch (Exception e) {
				Debug.logError(e, "Problems parsing on of quantityStr, priceStr and string", module);
				request.setAttribute("_ERROR_MESSAGE_", "Problems parsing on of quantityStr, priceStr and string");
				return "error";
			}
			
			productQtyMap.put("productId", productId);
			productQtyMap.put("quantity", quantity);
			productQtyMap.put("price", price);
			/*productQtyMap.put("taxRate", taxRate);
			productQtyMap.put("taxType", taxType);
			productQtyMap.put("taxAmt", taxAmt);*/
			
			orderProductList.add(productQtyMap);
		}
		
		processOrderContext.put("userLogin", userLogin);
		processOrderContext.put("orderProductList", orderProductList);
		processOrderContext.put("partyId", partyId);
		//processOrderContext.put("salesChannel", salesChannel);
		//processOrderContext.put("orderTaxType", orderTaxType);
		//processOrderContext.put("orderId", orderId);
		processOrderContext.put("productStoreId", productStoreId);
		processOrderContext.put("facilityId", facilityId);
		processOrderContext.put("referenceNo", referenceNo);
		processOrderContext.put("salesChannel", salesChannel);
		processOrderContext.put("supplierId", supplierId);
		
		
		
		Map<String, Object> result = ServiceUtil.returnSuccess();
		try{
			result = processPurchaseOrder(dctx, processOrderContext);
			if(ServiceUtil.isError(result)){
				Debug.logError("Unable to generate order: " + ServiceUtil.getErrorMessage(result), module);
				request.setAttribute("_ERROR_MESSAGE_", "Unable to generate order  For party :" + partyId);
				return "error";
			}
		}catch(Exception e){
			Debug.logError(e, module);
			return "error";
		}
		String sucMsg = "Your order "+result.get("orderId")+" has been successfully processed!";
        request.setAttribute("_EVENT_MESSAGE_", sucMsg);
		return "success";
	}
    
    public static Map<String, Object> processPurchaseOrder(DispatchContext dctx, Map<String, ? extends Object> context) {
    	
    	Delegator delegator = dctx.getDelegator();
	    LocalDispatcher dispatcher = dctx.getDispatcher();
	    Locale locale = (Locale) context.get("locale");
	    GenericValue userLogin = (GenericValue) context.get("userLogin");
	    Timestamp effectiveDate = UtilDateTime.nowTimestamp();
	    
	    Map<String, Object> result = ServiceUtil.returnSuccess();
	    List<Map> orderProductList = (List) context.get("orderProductList");
	    
	    String partyId = null;
	    String referenceNo = (String) context.get("referenceNo");
	    String productStoreId = (String) context.get("productStoreId");
	    String facilityId = (String) context.get("facilityId");
	    String supplierId = (String) context.get("supplierId");
	    String salesChannel = (String) context.get("salesChannel");
	    String billToCustomer = null;
	    
	    String  payToPartyId= null;
	    try {
            GenericValue productStore = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryOne();
            if(UtilValidate.isNotEmpty(productStore)){
    			payToPartyId = productStore.getString("payToPartyId");
    		}
        } catch (GenericEntityException e) {
            Debug.logError(e, "Unable to locate ProductStore (" + productStoreId + ")", module);
            return null;
        }
		
	    
	    
	    ShoppingCart cart = new ShoppingCart(delegator, productStoreId, locale,"INR");
	    try {
			//get inventoryFacility details through productStore.
			
	    	cart.setOrderType("PURCHASE_ORDER");
	    	cart.setExternalId(referenceNo);
	        
	    	cart.setProductStoreId(productStoreId);
	    	cart.setChannelType("WEB_SALES_CHANNEL");
	    	cart.setBillFromVendorPartyId(supplierId);
	    	cart.setShipFromVendorPartyId(supplierId);
	    	cart.setSupplierAgentPartyId(supplierId);
			    
	    	cart.setBillToCustomerPartyId(payToPartyId);
	    	cart.setPlacingCustomerPartyId(payToPartyId);
	    	cart.setShipToCustomerPartyId(payToPartyId);
	    	cart.setEndUserCustomerPartyId(payToPartyId);
	    	//cart.setEstimatedDeliveryDate(effectiveDate);
	    	cart.setOrderDate(effectiveDate);
	    	cart.setUserLogin(userLogin, dispatcher);
			
			cart.setFacilityId(facilityId);//for store inventory we need this so that inventoryItem query by this orginFacilityId
			
		} catch (Exception e) {
			Debug.logError(e, "Error in setting cart parameters", module);
			return ServiceUtil.returnError("Error in setting cart parameters");
		}
    	
	    for (Map<String, Object> prodQtyMap : orderProductList) {
	    	String productId = "";
	    	String taxType = "";
			BigDecimal quantity = BigDecimal.ZERO;
	    	
	    	if(UtilValidate.isNotEmpty(prodQtyMap.get("productId"))){
				productId = (String)prodQtyMap.get("productId");
			}
			if(UtilValidate.isNotEmpty(prodQtyMap.get("quantity"))){
				quantity = (BigDecimal)prodQtyMap.get("quantity");
			}
			try{
				ShoppingCartItem item = ShoppingCartItem.makeItem(null, productId, null, quantity, null, null, null, null, null, null, null, null, null, null, null, null, dispatcher, cart, null, null, null, Boolean.TRUE, Boolean.TRUE);
				cart.addItemToEnd(item);
				
			}
			catch (Exception exc) {
				Debug.logError("Error adding product with id " + productId + " to the cart: " + exc.getMessage(), module);
				return ServiceUtil.returnError("Error adding product with id " + productId + " to the cart: ");
	        }
			
	    }
	    
	    Map<String, Object> orderCreateResult = new HashMap<String, Object>();
	    cart.setDefaultCheckoutOptions(dispatcher);
		//ProductPromoWorker.doPromotions(cart, dispatcher);
		CheckOutHelper checkout = new CheckOutHelper(dispatcher, delegator, cart);
		try {
			checkout.calcAndAddTax();
			orderCreateResult = checkout.createOrder(userLogin);
			Debug.log("orderCreateResult ================="+orderCreateResult);
			
			String orderIdNew = (String)orderCreateResult.get("orderId");
			
			Map<String, Object> serviceContext = UtilMisc.<String, Object>toMap("orderId", orderIdNew, "statusId", "ORDER_APPROVED", "setItemStatus", "Y", "userLogin", userLogin);
            Map<String, Object> newSttsResult = null;
            try {
                newSttsResult = dispatcher.runSync("changeOrderStatus", serviceContext);
            } catch (GenericServiceException e) {
                Debug.logError(e, "Problem calling the changeOrderStatus service", module);
            }
            result.put("orderId", orderIdNew);
		} catch (Exception e1) {
		// TODO Auto-generated catch block
			Debug.logError(e1, "Error in CalcAndAddTax",module);
		}
	    
	    return result;
    }
    public static Map<String, Object> uploadSalesOrderFile(DispatchContext dctx, Map<String, ? extends Object> context) 
    {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String uploadedFileName = (String)context.get("_uploadedFile_fileName");
        ByteBuffer uploadBytes = (ByteBuffer) context.get("uploadedFile");
        
        String productStoreId = (String)context.get("productStoreId");
        String facilityId = (String)context.get("facilityId");
        Map result = ServiceUtil.returnSuccess("Orders successfully processed.");
        Map<String, List<Map<String, Object>>> toBeProcessOrders = new HashMap<String, List<Map<String, Object>>>();
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        HSSFSheet sheet=null;
        File file =null;
        if (!uploadedFileName.toUpperCase().endsWith("XLS")) {
        	 return ServiceUtil.returnError("No XLS File Found To Upload.");
        }
        if(UtilValidate.isNotEmpty(uploadedFileName)){
        	String uploadTempDir = System.getProperty("ofbiz.home") + "/runtime/tmp/upload/";
            if (!new File(uploadTempDir).exists()) 
            {
                new File(uploadTempDir).mkdirs();
            }
            
            String filenameToUse = uploadedFileName;
            
            file = new File(uploadTempDir + filenameToUse);
            
            if(file.exists()) 
            {
                file.delete();
            }
            
            try 
            {
                RandomAccessFile out = new RandomAccessFile(file, "rw");
                out.write(uploadBytes.array());
                out.close();
            } 
            catch (FileNotFoundException e) 
            {
                Debug.logError(e, module);
                return ServiceUtil.returnError("Unable to open file for writing: " + file.getAbsolutePath());
            } catch (IOException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError("Unable to write binary data to: " + file.getAbsolutePath());
            }
        }
        
        try {
            fs = new POIFSFileSystem(new FileInputStream(file));
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            wb.close();
        } catch (IOException e) {
            Debug.logError("Unable to read or create workbook from file", module);
            return ServiceUtil.returnError("Unable to read or create workbook from file");
            
        }
     // get first sheet
        
        int sheetLastRowNumber = sheet.getLastRowNum();
        List<Map<String, Object>> toBeProcessOrdersList = new LinkedList<Map<String,Object>>();
        DataFormatter dataFormatter= new DataFormatter();
        for (int j = 1; j <= sheetLastRowNumber; j++) {
            HSSFRow row = sheet.getRow(j);
            if (row != null) {
            	int  cols=row.getPhysicalNumberOfCells();
            	Map<String, Object> tempProcessOrder = new HashMap<String, Object>();
                for (int colIndex = 0; colIndex <cols; colIndex++) {
                	HSSFCell cell=row.getCell(colIndex);
                    if(cell!=null || (colIndex==3) || (colIndex==8)){
                    	String cellValue = null;
                    	if(cell!=null){
                    		cellValue = dataFormatter.formatCellValue(cell);
                    	}
                    	switch(colIndex){
	            	        case 0:
	            	        	tempProcessOrder.put("referenceNo", cellValue);
	          	            break;
	              			case 1:
	              				tempProcessOrder.put("productId", cellValue);
	              				boolean productExists = ImportProductHelper.checkProductExists(cellValue, delegator);
	              				if(!productExists){
	              					return ServiceUtil.returnError("ProductId : "+cellValue+" not exists");
	              				}
	            	      	break;
	              			case 2:
	              				tempProcessOrder.put("quantity", cellValue);
	              			break;
	              			case 3:
	              				Debug.log("cellValue ==================="+cellValue);
	              				tempProcessOrder.put("udPrice", cellValue);
	              			break;
	              			case 4:
	              				tempProcessOrder.put("customerName", cellValue);
	              			break;
	              			case 5:
	              				tempProcessOrder.put("emailId", cellValue);
	              			break;
	              			case 6:
	              				tempProcessOrder.put("phoneNo", cellValue);
	              			break;
	              			case 7:
	              				tempProcessOrder.put("address1", cellValue);
	              			break;
	              			case 8:
	              				tempProcessOrder.put("address2", cellValue);
	              			break;
	              			case 9:
	              				tempProcessOrder.put("city", cellValue);
	              			break;
	              			case 10:
	              				tempProcessOrder.put("postalCode", cellValue);
	              			break;
	              			case 11:
	              				tempProcessOrder.put("stateProvinceGeoId", cellValue);
	              			break;
	              			case 12:
	              				tempProcessOrder.put("countryGeoId", cellValue);
	              			break;
	              			case 13:
	              				tempProcessOrder.put("paymentMethod", cellValue);
	              			break;
	              		}
                    }else{
                    	return ServiceUtil.returnError("Cell values should not be empty.");
                    }
                }
                List<Map<String, Object>> tempOrderProductList = new LinkedList<Map<String,Object>>();
                if(UtilValidate.isEmpty(toBeProcessOrders.get(tempProcessOrder.get("referenceNo")))){
                	tempOrderProductList.add(tempProcessOrder);
                	toBeProcessOrders.put((String)tempProcessOrder.get("referenceNo"), tempOrderProductList);
                }else{
                	tempOrderProductList = (List)toBeProcessOrders.get(tempProcessOrder.get("referenceNo"));
                	Map<String,Object> prevMap = tempOrderProductList.get((tempOrderProductList.size()-1));
                	if(UtilValidate.isNotEmpty(prevMap)){
        				if(!((String)prevMap.get("customerName")).equals((String)(tempProcessOrder).get("customerName")) ||
        				   !((String)prevMap.get("emailId")).equals((String)(tempProcessOrder).get("emailId")) ||
        				   !((String)prevMap.get("phoneNo")).equals((String)(tempProcessOrder).get("phoneNo")) ||
        				   !((String)prevMap.get("address1")).equals((String)(tempProcessOrder).get("address1")) ||
        				   !((String)prevMap.get("address2")).equals((String)(tempProcessOrder).get("address2")) ||
        				   !((String)prevMap.get("city")).equals((String)(tempProcessOrder).get("city")) ||
        				   !((String)prevMap.get("stateProvinceGeoId")).equals((String)(tempProcessOrder).get("stateProvinceGeoId")) ||
        				   !((String)prevMap.get("countryGeoId")).equals((String)(tempProcessOrder).get("countryGeoId")) ||
        				   !((String)prevMap.get("paymentMethod")).equals((String)(tempProcessOrder).get("paymentMethod"))){
        					return ServiceUtil.returnError("Values Not Matched for EXT_ORDER_ID "+tempProcessOrder.get("referenceNo"));
        				}
        			}
                	tempOrderProductList.add(tempProcessOrder);
                	
                	toBeProcessOrders.put((String)tempProcessOrder.get("referenceNo"), tempOrderProductList);
                }
            }
        }
        Debug.log("toBeProcessOrders ==================="+toBeProcessOrders);
        if(UtilValidate.isNotEmpty(toBeProcessOrders)){
        	for (Map.Entry <String, List<Map<String, Object>>> entry : toBeProcessOrders.entrySet()){
        		List<Map<String, Object>> tempOrderProductList = entry.getValue();
        		Map<String, Object> processOrderContext = new HashMap<String, Object>();
        		for(int i=0;i<tempOrderProductList.size();i++){
        			Map<String,Object> currentMap = tempOrderProductList.get(i);
        			Debug.log("currentMap ==================="+currentMap);
        				Map<String,Object> tempProdMap = new HashMap<String, Object>();
        				List<Map<String, Object>> tempProductList = new LinkedList<Map<String,Object>>();
        				tempProdMap.put("productId", currentMap.get("productId"));
        				tempProdMap.put("quantity", new BigDecimal((String)currentMap.get("quantity")));
        				BigDecimal udPrice = null; 
        				if(UtilValidate.isNotEmpty(currentMap.get("udPrice"))){
        					udPrice = new BigDecimal((String)currentMap.get("udPrice"));
        				}
        				
        				tempProdMap.put("price", udPrice);
        				if(UtilValidate.isEmpty(processOrderContext)){
        					tempProductList.add(tempProdMap);
            				processOrderContext.put("userLogin", userLogin);
            				processOrderContext.put("orderProductList", tempProductList);
            				processOrderContext.put("salesChannel", "WEB_SALES_CHANNEL");
            				processOrderContext.put("productStoreId", productStoreId);
            				processOrderContext.put("facilityId", facilityId);
            				processOrderContext.put("referenceNo", currentMap.get("referenceNo"));
            				processOrderContext.put("customerName", currentMap.get("customerName"));
            				processOrderContext.put("emailId", currentMap.get("emailId"));
            				processOrderContext.put("phoneNo", currentMap.get("phoneNo"));
            				processOrderContext.put("address1", currentMap.get("address1"));
            				if(UtilValidate.isNotEmpty(currentMap.get("address2"))){
            					processOrderContext.put("address2", currentMap.get("address2"));
            				}else{
            					processOrderContext.put("address2","");
            				}
            				processOrderContext.put("city", currentMap.get("city"));
            				processOrderContext.put("postalCode", currentMap.get("postalCode"));
            				processOrderContext.put("stateProvinceGeoId", currentMap.get("stateProvinceGeoId"));
            				processOrderContext.put("countryGeoId", currentMap.get("countryGeoId"));
            				processOrderContext.put("paymentMethod", currentMap.get("paymentMethod"));
        				}else{
        					tempProductList = (List)processOrderContext.get("orderProductList");
        					tempProductList.add(tempProdMap);
        					processOrderContext.put("orderProductList", tempProductList);
        				}
        				
        		}
        		toBeProcessOrdersList.add(processOrderContext);
        	}
        }
        if(UtilValidate.isNotEmpty(toBeProcessOrdersList)){
	        try{
	        	TransactionUtil.begin();
	        	for(int i=0;i<toBeProcessOrdersList.size();i++){
					Map orderResult = processSalesOrder(dctx, toBeProcessOrdersList.get(i));
					Debug.log("orderResult ================"+orderResult);
					if(ServiceUtil.isError(orderResult)){
						Debug.logError("Unable to generate order: " + ServiceUtil.getErrorMessage(orderResult), module);
						TransactionUtil.rollback();
						return ServiceUtil.returnError("Unable to process Orders");
					}
	        	}
			}catch(Exception e){
				Debug.logError(e, module);
				return ServiceUtil.returnError("Unable to process Orders");
			}
        }
        
       return result;
    }
    
    public static Map<String, Object> createLocationPrefixConfig(DispatchContext dctx, Map<String, ? extends Object> context){
    	Delegator delegator = dctx.getDelegator();
	    LocalDispatcher dispatcher = dctx.getDispatcher();
	    GenericValue userLogin = (GenericValue) context.get("userLogin");
    	String aisleFormat = (String) context.get("aisleFormat");
    	String sectionFormat = (String) context.get("sectionFormat");
    	String levelFormat = (String) context.get("levelFormat");
    	String binFormat = (String) context.get("binFormat");
    	String facilityId = (String) context.get("facilityId");
    	Map resultMap = new HashMap();
    	List<GenericValue> facilityAttrList = new ArrayList<GenericValue>();
    	List<String> attrList = new ArrayList<String>();
    	Debug.log("came to service");
    	try{
    		GenericValue aisleAttr = delegator.makeValue("FacilityAttribute");
    		aisleAttr.set("facilityId",facilityId);
    		aisleAttr.set("attrName","aisleFormat");
    		aisleAttr.set("attrValue",aisleFormat);
    		facilityAttrList.add(aisleAttr);
    	}catch(Exception e){
    		Debug.logError("Error while setting aisleFormat:"+e,module);
    		return ServiceUtil.returnError("Error while setting aisleFormat:"+e.getMessage());
    	}
    	try{
    		GenericValue sectionAttr = delegator.makeValue("FacilityAttribute");
    		sectionAttr.set("facilityId",facilityId);
    		sectionAttr.set("attrName","sectionFormat");
    		sectionAttr.set("attrValue",sectionFormat);
    		facilityAttrList.add(sectionAttr);
    	}catch(Exception e){
    		Debug.logError("Error while setting rackFormat:"+e,module);
    		return ServiceUtil.returnError("Error while setting rackFormat:"+e.getMessage());
    	}
    	try{
    		GenericValue levelAttr = delegator.makeValue("FacilityAttribute");
    		levelAttr.set("facilityId",facilityId);
    		levelAttr.set("attrName","levelFormat");
    		levelAttr.set("attrValue",levelFormat);
    		facilityAttrList.add(levelAttr);
    	}catch(Exception e){
    		Debug.logError("Error while setting shelfFormat:"+e,module);
    		return ServiceUtil.returnError("Error while setting shelfFormat:"+e.getMessage());
    	}
    	try{
    		GenericValue binAttr = delegator.makeValue("FacilityAttribute");
    		binAttr.set("facilityId",facilityId);
    		binAttr.set("attrName","binFormat");
    		binAttr.set("attrValue",binFormat);
    		facilityAttrList.add(binAttr);
    	}catch(Exception e){
    		Debug.logError("Error while setting binFormat:"+e,module);
    		return ServiceUtil.returnError("Error while setting binFormat:"+e.getMessage());
    	}
    	try{
    		delegator.storeAll(facilityAttrList);
    	}catch(GenericEntityException e){
    		Debug.logError("Error while storing formats"+e,module);
    		return ServiceUtil.returnError("Error while storing formats"+e.getMessage());
    	}
    	resultMap = ServiceUtil.returnSuccess("Successfully updated");
    	return resultMap;
    	
    }
    public static Map<String, Object> createFacilityLocation(DispatchContext dctx, Map<String, ? extends Object> context){
    	Delegator delegator = dctx.getDelegator();
	    LocalDispatcher dispatcher = dctx.getDispatcher();
	    GenericValue userLogin = (GenericValue) context.get("userLogin");
	    Debug.log("WE ARE IN SERVICE=======");
	    String facilityId = (String) context.get("facilityId");
	    String aisleCountStr = (String) context.get("aisleCount");
	    String sectionCountStr= (String) context.get("sectionCount");
	    String levelCountStr = (String) context.get("levelCount");
	    String binCountStr = (String) context.get("binCount");
    	String areaId = (String)context.get("areaId");
	    String aisleStartStr = (String) context.get("aisleStart");
    	String sectionStartStr= (String) context.get("sectionStart");
    	String levelStartStr = (String) context.get("levelStart");
    	String binStartStr = (String) context.get("binStart");
    	
    	// Here we are getting the format details 
    	List<GenericValue> formatList = new ArrayList<GenericValue>();
    	List<String> attrNames = new ArrayList<String>();
    	attrNames.add("aisleFormat");
    	attrNames.add("sectionFormat");
    	attrNames.add("levelFormat");
    	attrNames.add("binFormat");
    	List condList = new ArrayList<>();
    	condList.add(EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS,facilityId));
    	condList.add(EntityCondition.makeCondition("attrName",EntityOperator.IN,attrNames));
    	EntityCondition cond=EntityCondition.makeCondition(condList);
    	try{
    		formatList = delegator.findList("FacilityAttribute",cond,null,null,null,false);
    	}catch(GenericEntityException e){
    		Debug.logError("Error while getting format List"+e,module);
    		return ServiceUtil.returnError("Error while getting format List"+e.getMessage());
    	}
    	if(UtilValidate.isEmpty(formatList)){
    		Debug.logError("Formats not found . Please concact admin for configurations",module);
    		return ServiceUtil.returnError("Formats not found . Please concact admin for configurations");
    	}
    	Map formatMap = new HashMap();
    	
    	for(GenericValue attr : formatList){
    		Map tempMap = new HashMap();
    		String attrVal = attr.getString("attrValue");
    		String prefix = attrVal.substring(0,attrVal.indexOf('#'));
    		tempMap.put("prefix",prefix);
    		tempMap.put("length",(attrVal.length()-prefix.length()));
    		formatMap.put(attr.getString("attrName"),tempMap);
    	}
    	int aisleCount = 0;
	    int sectionCount= 0;
	    int levelCount = 0;
	    int binCount = 0; 
    	if(UtilValidate.isNotEmpty(aisleCountStr)){
    		aisleCount = Integer.parseInt(aisleCountStr);
    	}
    	if(UtilValidate.isNotEmpty(sectionCountStr)){
    		sectionCount = Integer.parseInt(sectionCountStr);
    	}
    	if(UtilValidate.isNotEmpty(levelCountStr)){
    		levelCount = Integer.parseInt(levelCountStr);
    	}
    	if(UtilValidate.isNotEmpty(binCountStr)){
    		binCount = Integer.parseInt(binCountStr);
    	}
    	
    	int aisleStart = 0;
	    int sectionStart= 0;
	    int levelStart = 0;
	    int binStart = 0; 
    	if(UtilValidate.isNotEmpty(aisleStartStr)){
    		aisleStart = Integer.parseInt(aisleStartStr);
    	}
    	if(UtilValidate.isNotEmpty(sectionStartStr)){
    		sectionStart = Integer.parseInt(sectionStartStr);
    	}
    	if(UtilValidate.isNotEmpty(levelStartStr)){
    		levelStart = Integer.parseInt(levelStartStr);
    	}
    	if(UtilValidate.isNotEmpty(binStartStr)){
    		binStart = Integer.parseInt(binStartStr);
    	}
    	List<String> aisleList = new ArrayList<String>();
    	List<String> sectionList = new ArrayList<String>();
    	List<String> levelList = new ArrayList<String>();
    	List<String> binList = new ArrayList<String>();
    	for(int i=0;i<aisleCount;i++){
    		if(UtilValidate.isNotEmpty(aisleStart)){
    			Map aisleMap = new HashMap(); 
    			aisleMap.putAll((Map)formatMap.get("aisleFormat"));
    			int k= aisleStart+i;
    			int aisleLength = (Integer)aisleMap.get("length")-(Integer.toString(k)).length()+1;
    			if(aisleLength<0){
    				aisleLength =0;
    			}
    			String aisleStr = (String)aisleMap.get("prefix")+String.format("%0"+aisleLength+"d", k);;
    			aisleList.add(aisleStr);
    		}
    	}
    	for(int i=0;i<sectionCount;i++){
    		if(UtilValidate.isNotEmpty(sectionStart)){
    			Map sectionMap = new HashMap(); 
    			sectionMap.putAll((Map)formatMap.get("sectionFormat"));
    			int k= sectionStart+i;
    			int sectionLength = (Integer)sectionMap.get("length")-(Integer.toString(k)).length()+1;
    			if(sectionLength<0){
    				sectionLength =0;
    			}
    			String sectionStr = (String)sectionMap.get("prefix")+String.format("%0"+sectionLength+"d", k);;
    			sectionList.add(sectionStr);
    		}
    	}
    	for(int i=0;i<levelCount;i++){
    		if(UtilValidate.isNotEmpty(levelStart)){
    			Map levelMap = new HashMap(); 
    			levelMap.putAll((Map)formatMap.get("levelFormat"));
    			int k= levelStart+i;
    			
    			int levelLength = (Integer)levelMap.get("length")-(Integer.toString(k)).length()+1;
    			if(levelLength<0){
    				levelLength =0;
    			}
    			String levelStr = (String)levelMap.get("prefix")+String.format("%0"+levelLength+"d", k);;
    			levelList.add(levelStr);
    		}
    	}
    	for(int i=0;i<binCount;i++){
    		if(UtilValidate.isNotEmpty(binStart)){
    			Map binMap = new HashMap(); 
    			binMap.putAll((Map)formatMap.get("binFormat"));
    			int k= binStart+i;
    			int binLength = (Integer)binMap.get("length");
    			Debug.log("binLength:"+binMap.get("length")+ " String k"+(Integer.toString(k))+":"+(Integer.toString(k)).length());
    			if(binLength<0){
    				binLength =0;
    			}
    			String binStr = (String)binMap.get("prefix")+String.format("%0"+binLength+"d", k);;
    			binList.add(binStr);
    		}
    	}
    	
    	Map resultMap = new HashMap();
    	Map inputMap = new HashMap();
    	inputMap.put("aisleList",aisleList);
    	inputMap.put("sectionList",sectionList);
    	inputMap.put("levelList",levelList);
    	inputMap.put("binList",binList);
    	inputMap.put("areaId",areaId);
    	inputMap.put("facilityId",facilityId);
    	inputMap.put("userLogin",userLogin);
    	Debug.log("inputMap========"+inputMap);
    	resultMap = generateFacilityLocSeq(dctx, inputMap);
    	return resultMap;
    }	
    private static Map<String, Object> generateFacilityLocSeq(DispatchContext dctx, Map<String, ? extends Object> context){
    	Map resultMap    = new HashMap();
    	Delegator delegator = dctx.getDelegator();
	    LocalDispatcher dispatcher = dctx.getDispatcher();
    	GenericValue userLogin = (GenericValue) context.get("userLogin");
    	List<String> aisleList   = (List)context.get("aisleList");
    	List<String> sectionList = (List)context.get("sectionList");
    	List<String> levelList   = (List)context.get("levelList");
    	List<String> binList     = (List)context.get("binList");
    	String areaId    = (String)context.get("areaId");
    	String facilityId    = (String)context.get("facilityId");
    	Debug.log("aisleList========="+aisleList);
    	Debug.log("sectionList========="+sectionList);
    	Debug.log("levelList=========="+levelList);
    	Debug.log("binList=========="+binList);
    	List<GenericValue> facLocList = new ArrayList<GenericValue>();
    	boolean addArea = false;
    	if(UtilValidate.isNotEmpty(areaId)){
    		addArea=true;
    	}
    	if(UtilValidate.isNotEmpty(aisleList)){
    		for(String aisleId : aisleList){
    			if(UtilValidate.isNotEmpty(sectionList)){
    				for(String sectionId : sectionList){
    					if(UtilValidate.isNotEmpty(levelList)){
    	    				for(String levelId : levelList){
    	    					for(String binId : binList){
    	    						String seqLoc = "";
    	    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
    	    						facLoc.set("facilityId",facilityId);
    	    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
    	    						if(addArea){
    	    							seqLoc = areaId ;
    	    							facLoc.set("areaId",areaId);
    	    						}
    	    						seqLoc = seqLoc+aisleId+sectionId+levelId+binId;
    	    						facLoc.set("locationSeqId",seqLoc);
    	    						facLoc.set("aisleId",aisleId);
    	    						facLoc.set("sectionId",sectionId);
    	    						facLoc.set("levelId",levelId);
    	    						facLoc.set("positionId",binId);
    	    						facLocList.add(facLoc);
    	    					}
    	    				}
    	    			}else{
    	    				for(String binId : binList){
	    						String seqLoc = "";
	    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
	    						facLoc.set("facilityId",facilityId);
	    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
	    						if(addArea){
	    							seqLoc = areaId ;
	    							facLoc.set("areaId",areaId);
	    						}
	    						seqLoc = seqLoc+aisleId+sectionId+binId;
	    						facLoc.set("locationSeqId",seqLoc);
	    						facLoc.set("aisleId",aisleId);
	    						facLoc.set("sectionId",sectionId);
	    						facLoc.set("positionId",binId);
	    						facLocList.add(facLoc);
	    					}
    	    			}
    				}
    			}else{
    				//SECTION LIST EMPTY
    				if(UtilValidate.isNotEmpty(levelList)){
	    				for(String levelId : levelList){
	    					for(String binId : binList){
	    						String seqLoc = "";
	    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
	    						facLoc.set("facilityId",facilityId);
	    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
	    						if(addArea){
	    							seqLoc = areaId ;
	    							facLoc.set("areaId",areaId);
	    						}
	    						seqLoc = seqLoc+aisleId+levelId+binId;
	    						facLoc.set("locationSeqId",seqLoc);
	    						facLoc.set("aisleId",aisleId);
	    						facLoc.set("levelId",levelId);
	    						facLoc.set("positionId",binId);
	    						facLocList.add(facLoc);
	    					}
	    				}
	    			}else{
	    				for(String binId : binList){
    						String seqLoc = "";
    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
    						facLoc.set("facilityId",facilityId);
    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
    						if(addArea){
    							seqLoc = areaId ;
    							facLoc.set("areaId",areaId);
    						}
    						seqLoc = seqLoc+aisleId+binId;
    						facLoc.set("locationSeqId",seqLoc);
    						facLoc.set("aisleId",aisleId);
    						facLoc.set("positionId",binId);
    						facLocList.add(facLoc);
    					}
	    			}
    				
    				
    			}
    		}
    	}else{
			if(UtilValidate.isNotEmpty(sectionList)){
				for(String sectionId : sectionList){
					if(UtilValidate.isNotEmpty(levelList)){
	    				for(String levelId : levelList){
	    					for(String binId : binList){
	    						String seqLoc = "";
	    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
	    						facLoc.set("facilityId",facilityId);
	    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
	    						if(addArea){
	    							seqLoc = areaId ;
	    							facLoc.set("areaId",areaId);
	    						}
	    						seqLoc = seqLoc+sectionId+levelId+binId;
	    						facLoc.set("locationSeqId",seqLoc);
	    						facLoc.set("sectionId",sectionId);
	    						facLoc.set("levelId",levelId);
	    						facLoc.set("positionId",binId);
	    						facLocList.add(facLoc);
	    					}
	    				}
	    			}else{
	    				for(String binId : binList){
    						String seqLoc = "";
    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
    						facLoc.set("facilityId",facilityId);
    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
    						if(addArea){
    							seqLoc = areaId ;
    							facLoc.set("areaId",areaId);
    						}
    						seqLoc = seqLoc+sectionId+binId;
    						facLoc.set("locationSeqId",seqLoc);
    						facLoc.set("sectionId",sectionId);
    						facLoc.set("positionId",binId);
    						facLocList.add(facLoc);
    					}
	    			}
				}
			}else{
				//SECTION LIST EMPTY
				if(UtilValidate.isNotEmpty(levelList)){
    				for(String levelId : levelList){
    					for(String binId : binList){
    						String seqLoc = "";
    						GenericValue facLoc = delegator.makeValue("FacilityLocation");
    						facLoc.set("facilityId",facilityId);
    						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
    						if(addArea){
    							seqLoc = areaId ;
    							facLoc.set("areaId",areaId);
    						}
    						seqLoc = seqLoc+levelId+binId;
    						facLoc.set("locationSeqId",seqLoc);
    						facLoc.set("levelId",levelId);
    						facLoc.set("positionId",binId);
    						facLocList.add(facLoc);
    					}
    				}
    			}else{
    				for(String binId : binList){
						String seqLoc = "";
						GenericValue facLoc = delegator.makeValue("FacilityLocation");
						facLoc.set("facilityId",facilityId);
						facLoc.set("locationTypeEnumId","FLT_PICKLOC");
						if(addArea){
							seqLoc = areaId ;
							facLoc.set("areaId",areaId);
						}
						seqLoc = seqLoc+binId;
						facLoc.set("locationSeqId",seqLoc);
						facLoc.set("positionId",binId);
						facLocList.add(facLoc);
					}
    			}
			}
    	}
    	resultMap = ServiceUtil.returnSuccess("Successfully "+facLocList.size()+" Facility Locations created ");
    	try {
            delegator.storeAll(facLocList);
        } catch (GenericEntityException e) {
            Debug.logWarning("Problem in creating FacilityLocation " + e, module);
            return ServiceUtil.returnError("Problem in creating FacilityLocation " + e.getMessage());
        }
    	return resultMap;
    }
    
}
