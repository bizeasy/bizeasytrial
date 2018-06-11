	
	import java.sql.Timestamp;
	import java.text.SimpleDateFormat;
		
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.List;

	import org.apache.ofbiz.base.util.*;
	import org.json.JSONArray;
	import org.json.JSONObject;
		
	import org.apache.ofbiz.base.util.UtilMisc;
	import org.apache.ofbiz.entity.condition.EntityCondition;
	import org.apache.ofbiz.entity.condition.EntityOperator;
	import org.apache.ofbiz.entity.util.EntityUtil;
	import org.apache.ofbiz.entity.util.EntityFindOptions;
	import org.apache.ofbiz.entity.GenericValue;
	import org.apache.ofbiz.service.ServiceUtil;
	
	Debug.log("test =================");
	Debug.log("shipmentIds ===================="+parameters.shipmentIds);
	
	shipmentIds = parameters.shipmentIds;
	//carrierId = parameters.carrierId;
			
	Debug.log("productStoreId =========="+parameters.productStoreId);
	Debug.log("facilityId =========="+parameters.facilityId);
	Debug.log("shipmentIds ===================="+request.getParameter("shipmentIds"));
	
	inputStringList = shipmentIds.split(":");
	shipmentIds = inputStringList[0];
	carrierId = inputStringList[1];
	shipmentIdList = shipmentIds.split(",");
	Debug.log("shipmentIdList =========="+shipmentIdList);
	Debug.log("carrierId =========="+carrierId);
	
	/*if(UtilValidate.isEmpty(shipmentIdList)){
		return "success";
	}*/	
	
	shipmentManifest = delegator.makeValue("ShipmentManifest", UtilMisc.toMap("carrierId", carrierId));
	shipmentManifest.set("statusId", "CREATED");
	shipmentManifest.set("generatedDate", UtilDateTime.nowTimestamp());
	Debug.log("shipmentManifest =========="+shipmentManifest);
	shipmentManifestResult = delegator.createSetNextSeqId(shipmentManifest);
	Debug.log("shipmentManifestResult =========="+shipmentManifestResult);
	manifestId = shipmentManifestResult.getString("manifestId");
	Debug.log("manifestId =========="+manifestId);
	
	for(int i=0; i<shipmentIdList.size(); i++){
		shipmentId = shipmentIdList[i];
		shipmentId = shipmentId.replaceAll("\\s","")
		Debug.log("shipmentId ============"+shipmentId);
		
		shipmentManifestItem = delegator.makeValue("ShipmentManifestItems", UtilMisc.toMap("manifestId", manifestId, "statusId","CREATED", "shipmentId", shipmentId));
		delegator.setNextSubSeqId(shipmentManifestItem, "manifestItemSeqId", 5, 1);
		delegator.create(shipmentManifestItem);
		
		mapIn = [:]
		mapIn.userLogin = userLogin
		mapIn.shipmentId = shipmentId
		
		result = dispatcher.runSync("setShipmentStatusPackedAndShipped", mapIn);
		Debug.log("result =============="+result);
		
	}
	
	return "success";
	
	
	
	//context.partyOBMap = partyOBMap;