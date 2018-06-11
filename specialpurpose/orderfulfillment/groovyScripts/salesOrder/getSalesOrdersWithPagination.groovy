	
	import java.sql.Timestamp;
	import java.text.SimpleDateFormat;
	
	import java.util.List;
	   
	import org.apache.ofbiz.base.util.*;
	
	import org.json.JSONArray;
	import org.json.JSONObject;
	
	import org.apache.ofbiz.entity.condition.EntityCondition;
	import org.apache.ofbiz.entity.condition.EntityOperator;
	import org.apache.ofbiz.entity.util.EntityUtil;
	import org.apache.ofbiz.entity.util.EntityFindOptions;
	import org.apache.ofbiz.entity.GenericValue;
	import org.apache.ofbiz.service.ServiceUtil;
	
	Debug.log("test =================");
	Debug.log("start ===================="+parameters.start);
	
	start = Integer.parseInt(parameters.start);
	Debug.log("start ===================="+start);
	
	Debug.log("search =============================="+parameters.get("search[value]"));
	
	orderId = parameters.orderId;
	
	Debug.log("orderId ======================="+orderId);
	externalId = parameters.externalId;
	statusId = parameters.statusId;
	salesChannelEnumId = parameters.salesChannel;
	orderTypeId = parameters.orderTypeId;
	orderFromDate = parameters.orderFromDate;
	orderThruDate = parameters.orderThruDate;
	orderDateSort = parameters.orderDateSort;
	noConditionFind = parameters.noConditionFind;
	
	List condList = [];
	if(UtilValidate.isNotEmpty(orderId)){
		condList.add(EntityCondition.makeCondition("orderId" ,EntityOperator.EQUALS,orderId));
	}
	if(UtilValidate.isNotEmpty(externalId)){
		condList.add(EntityCondition.makeCondition("externalId" ,EntityOperator.EQUALS,externalId));
	}
	if(UtilValidate.isNotEmpty(statusId)){
		condList.add(EntityCondition.makeCondition("statusId" ,EntityOperator.EQUALS,statusId));
	}
	
	if(UtilValidate.isNotEmpty(salesChannelEnumId)){
		condList.add(EntityCondition.makeCondition("salesChannelEnumId" ,EntityOperator.EQUALS,salesChannelEnumId));
	}
	if(UtilValidate.isNotEmpty(orderTypeId)){
		condList.add(EntityCondition.makeCondition("orderTypeId" ,EntityOperator.EQUALS,orderTypeId));
	}
	if(UtilValidate.isNotEmpty(statusId)){
		condList.add(EntityCondition.makeCondition("statusId" ,EntityOperator.EQUALS,statusId));
	}
	
	if(UtilValidate.isNotEmpty(orderFromDate)){
		condList.add(EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, orderFromDate));
	}
	if(UtilValidate.isNotEmpty(orderThruDate)){
		condList.add(EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN_EQUAL_TO, orderThruDate));
	}
	
	Debug.log("condList ============="+condList);
	if(orderDateSort)
	dateSort = orderDateSort;
	else
	dateSort = "-orderDate";
	
	cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
	List<String> orderBy = UtilMisc.toList(dateSort);
	
	double totalOrders = delegator.findCountByCondition("OrderHeader", cond, null, null);
	Debug.log("totalOrders ============="+totalOrders);
	
	dctx = dispatcher.getDispatchContext();
	JSONArray invoiceListJSON = new JSONArray();
	
	resultList = delegator.find("OrderHeader", cond, null,null, orderBy, null);
	Debug.log("resultList ================="+resultList);
	
	orderHeaderList = resultList.getPartialList(Integer.valueOf(parameters.start),Integer.valueOf(parameters.length));
	orderList = [];
	
	for (int i=0;i<orderHeaderList.size();i++) {
		
		orderObject = [:];
		
		eachOrder = orderHeaderList.get(i);
		Debug.log("eachOrder ====================="+eachOrder);
		
		orderId = eachOrder.orderId;
		
		condList = [];
		condList.add(EntityCondition.makeCondition("toOrderId", EntityOperator.EQUALS, orderId));
		condList.add(EntityCondition.makeCondition("orderItemAssocTypeId", EntityOperator.EQUALS, "REPLACEMENT"));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		orderItemAssoc = delegator.findList("OrderItemAssoc", condListExpr, UtilMisc.toSet("orderId", "toOrderId"), null, null, false);
		replacementMessage = "";
		if(UtilValidate.isNotEmpty(orderItemAssoc)){
			replacedOrder = EntityUtil.getFirst(orderItemAssoc).get("orderId");
			replacementMessage = "Replacement For " + replacedOrder;
		}
		
		condList = [];
		condList.add(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
		condList.add(EntityCondition.makeCondition("orderItemAssocTypeId", EntityOperator.EQUALS, "REPLACEMENT"));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		orderItemAssoc = delegator.findList("OrderItemAssoc", condListExpr, UtilMisc.toSet("orderId", "toOrderId"), null, null, false);
		if(UtilValidate.isNotEmpty(orderItemAssoc)){
			replacedOrder = EntityUtil.getFirst(orderItemAssoc).get("toOrderId");
			replacementMessage = "Replaced With " + replacedOrder;
		}
		
		
		condList = [];
		condList.add(EntityCondition.makeCondition("primaryOrderId", EntityOperator.EQUALS, orderId));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		pickListAndBin = delegator.findList("PicklistAndBin", condListExpr, UtilMisc.toSet("picklistId", "statusId"), null, null, false);
		pickListStatus = "";
		pickListId = "";
		if(UtilValidate.isNotEmpty(pickListAndBin)){
			pickListId = EntityUtil.getFirst(pickListAndBin).get("picklistId");
			pickListStatus = EntityUtil.getFirst(pickListAndBin).get("statusId");
		}
		
		condList = [];
		condList.add(EntityCondition.makeCondition("primaryOrderId", EntityOperator.EQUALS, orderId));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		shipmentList = delegator.findList("Shipment", condListExpr, UtilMisc.toSet("shipmentId", "statusId"), null, null, false);
		shipmentStatus = "";
		shipmentId = "";
		if(UtilValidate.isNotEmpty(shipmentList)){
			shipmentId = EntityUtil.getFirst(shipmentList).get("shipmentId");
			shipmentStatus = EntityUtil.getFirst(shipmentList).get("statusId");
		}
		
		condList = [];
		condList.add(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		orderItemBilling = delegator.findList("OrderItemBilling", condListExpr, UtilMisc.toSet("invoiceId"), null, null, false);
		invoiceId = "";
		if(UtilValidate.isNotEmpty(orderItemBilling)){
			invoiceId = EntityUtil.getFirst(orderItemBilling).get("invoiceId");
		}
		
		condList = [];
		condList.add(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
		condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
		
		returnHeaderAndItemsList = delegator.findList("ReturnHeaderAndItem", condListExpr, UtilMisc.toSet("returnId", "returnTypeId", "returnReasonId", "statusId"), null, null, false);
		returnId = "";
		returnTypeId = "";
		returnReasonId = "";
		returnStatusId = "";
		if(UtilValidate.isNotEmpty(returnHeaderAndItemsList)){
			returnId = EntityUtil.getFirst(returnHeaderAndItemsList).get("returnId");
			returnTypeId = EntityUtil.getFirst(returnHeaderAndItemsList).get("returnTypeId");
			returnReasonId = EntityUtil.getFirst(returnHeaderAndItemsList).get("returnReasonId");
			returnStatusId = EntityUtil.getFirst(returnHeaderAndItemsList).get("statusId");
		}
		
		
		
		orderFinalStatus = eachOrder.statusId;
		if(UtilValidate.isNotEmpty(pickListStatus)){
			orderFinalStatus = pickListStatus;
		}
		if(UtilValidate.isNotEmpty(shipmentStatus)){
			orderFinalStatus = shipmentStatus;
		}
		if(UtilValidate.isNotEmpty(returnStatusId)){
			orderFinalStatus = returnStatusId;
		}
		
		orderObject.put("orderId",eachOrder.orderId);
		orderObject.put("statusId",eachOrder.statusId);
		
		orderDate = String.valueOf(eachOrder.orderDate).substring(0,10);
		orderObject.put("orderDate",orderDate);
		
		
		orderObject.put("externalId",eachOrder.externalId);
		orderObject.put("originFacilityId",eachOrder.originFacilityId);
		
		orderObject.put("productStore",eachOrder.productStoreId);
		orderObject.put("salesChannelEnumId",eachOrder.salesChannelEnumId);
		
		orderObject.put("pickListId",pickListId);
		orderObject.put("pickListStatus",pickListStatus);
		
		orderObject.put("shipmentId",shipmentId);
		orderObject.put("shipmentStatus",shipmentStatus);
		
		orderObject.put("invoiceId",invoiceId);
		
		orderObject.put("returnId",returnId);
		orderObject.put("returnTypeId",returnTypeId);
		orderObject.put("returnReasonId",returnReasonId);
		orderObject.put("returnStatusId",returnStatusId);
		orderObject.put("replacementMessage",replacementMessage);
		
		orderObject.put("orderFinalStatus",orderFinalStatus);
		
		if(UtilValidate.isNotEmpty(shipmentId) && UtilValidate.isEmpty(returnId) && (shipmentStatus == "SHIPMENT_SHIPPED" || shipmentStatus == "SHIPMENT_DELIVERED")){
			orderObject.put("returnId", "Request Return");
		}
		
		
		
		orderObject.put("grandTotal",eachOrder.grandTotal);
		
		orderList.add(orderObject);
	}
	Debug.log("orderHeaderList ================"+orderList);
	request.setAttribute("orderListJSON", orderList);
	
	
	if (UtilValidate.isNotEmpty(resultList)) {
		try {
			resultList.close();
		} catch (Exception e) {
			Debug.logWarning(e, module);
		}
	}
	
	orderListDetails = [:];
	orderListDetails.put("totalOrders", totalOrders);
	
	
	//request.setAttribute("recordsTotal", 100);
	
	request.setAttribute("recordsFiltered", totalOrders);
	
	request.setAttribute("orderListDetails", orderListDetails);
	
	
	
	
	return "success";
	
	
	
	//context.partyOBMap = partyOBMap;