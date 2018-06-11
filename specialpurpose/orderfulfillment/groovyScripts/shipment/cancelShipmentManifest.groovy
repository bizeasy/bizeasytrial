	
	import java.sql.Timestamp;
import java.text.SimpleDateFormat;
		
	import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	manifestId = parameters.manifestId;
	
	shipmentManifest = from("ShipmentManifest").where("manifestId", manifestId).queryOne()
	shipmentManifest.set("statusId", "CANCELLED");
	delegator.store(shipmentManifest);
	
	condList = [];
	condList.add(EntityCondition.makeCondition("manifestId", EntityOperator.EQUALS, manifestId));
	condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
	
	shipmentManifestItems = delegator.findList("ShipmentManifestItems", condListExpr, null, null, null, false);
	if(shipmentManifestItems){
		
		for(int i=0; i<shipmentManifestItems.size(); i++){
			shipmentId = shipmentManifestItems[i].shipmentId;
			
			shipment = from("Shipment").where("shipmentId", shipmentId).queryOne()
			shipment.set("statusId", "SHIPMENT_PICKED");
			delegator.store(shipment);
			
			condList = [];
			condList.add(EntityCondition.makeCondition("shipmentId", EntityOperator.EQUALS, shipmentId));
			condList.add(EntityCondition.makeCondition("statusId", EntityOperator.IN, ["SHIPMENT_DELIVERED", "SHIPMENT_PACKED", "SHIPMENT_SHIPPED"]));
			condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
			
			shipmentStatus = delegator.findList("ShipmentStatus", condListExpr, null, null, null, false);
			delegator.removeAll(shipmentStatus);
		}
		
		delegator.removeAll(shipmentManifestItems);
	}
	
	return "success";
	
