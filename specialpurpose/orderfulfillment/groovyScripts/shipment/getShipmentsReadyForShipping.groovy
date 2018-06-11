	
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
	
	carrierId = parameters.carrierId;
	
	if(UtilValidate.isEmpty(carrierId)){
		return "success";
	}
	
	Debug.log("carrierId ======================="+parameters.carrierId);
	statusId = parameters.statusId;
	noConditionFind = parameters.noConditionFind;
	
	List condList = [];
	if(UtilValidate.isNotEmpty(carrierId)){
		condList.add(EntityCondition.makeCondition("carrierPartyId" ,EntityOperator.EQUALS, carrierId));
	}
	if(UtilValidate.isNotEmpty(statusId)){
		condList.add(EntityCondition.makeCondition("statusId" ,EntityOperator.EQUALS, statusId));
	}
	else{
		condList.add(EntityCondition.makeCondition("statusId" ,EntityOperator.EQUALS, "SHIPMENT_PICKED"));
	}
	condList.add(EntityCondition.makeCondition("shipmentTypeId" ,EntityOperator.EQUALS, "SALES_SHIPMENT"));
	
	cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
	
	//double totalShipments = delegator.findCountByCondition("OrderShipmentAndShipGroup", cond, null, null);
	//Debug.log("totalShipments ============="+totalShipments);
	
	
	double totalShipments = delegator.findCountByCondition("OrderShipmentAndShipGroup", cond, null, null);
	Debug.log("totalShipments ============="+totalShipments);
	
	
	dctx = dispatcher.getDispatchContext();
	List<String> orderBy = UtilMisc.toList("-shipmentId");
	
	shipmentListIterator = delegator.find("OrderShipmentAndShipGroup", cond, null,null, orderBy, null);
	//Debug.log("resultList ================="+resultList);
	
	shipmentIdList = [];
	shipmentList = [];
	while ((shipment = shipmentListIterator.next())) {
		
		shipmentId = shipment.shipmentId;
		/*if(shipmentIdList){
			shipmentIdList.contains(shipmentId){
				continue;
			}
		}*/
		
		shipmentObject = [:];
		
		shipmentObject.put("shipmentId",shipmentId);
		
		shipmentObject.put("orderId",shipment.orderId);
		shipmentObject.put("orderItemSeqId",shipment.orderItemSeqId);
		
		shipmentObject.put("shipGroupSeqId",shipment.shipGroupSeqId);
		shipmentObject.put("shipmentMethodTypeId",shipment.shipmentMethodTypeId);
		
		shipmentObject.put("carrierPartyId",shipment.carrierPartyId);
		shipmentObject.put("facilityId",shipment.facilityId);
		
		shipmentObject.put("trackingNumber",shipment.trackingNumber);
		shipmentObject.put("statusId",shipment.statusId);
		
		shipmentObject.put("shipmentTypeId",shipment.shipmentTypeId);
		
		shipmentList.add(shipmentObject);
	}
	
	Debug.log("shipmentList ==================="+shipmentList);
	request.setAttribute("shipmentListJSON", shipmentList);
	
	
	if (UtilValidate.isNotEmpty(shipmentListIterator)) {
		try {
			shipmentListIterator.close();
		} catch (Exception e) {
			Debug.logWarning(e, module);
		}
	}
	request.setAttribute("recordsFiltered", totalShipments);
	
	return "success";
	
	
	
	//context.partyOBMap = partyOBMap;