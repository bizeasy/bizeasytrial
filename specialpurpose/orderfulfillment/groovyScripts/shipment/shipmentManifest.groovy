import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.party.contact.ContactHelper;
manifestId=manifestId;
carrierId=carrierId;

condList = [];
condList.add(EntityCondition.makeCondition("manifestId", EntityOperator.EQUALS, manifestId));
condList.add(EntityCondition.makeCondition("carrierPartyId", EntityOperator.EQUALS, carrierId));
condListExpr = EntityCondition.makeCondition(condList, EntityOperator.AND);
finalList=[];
OrShipmentShGrpOrItemShManifestItems = delegator.findList("OrderShipmentAndShipGroupAndOrderItemAndShipmentManifestItems", condListExpr, null, null, null, false);
if(OrShipmentShGrpOrItemShManifestItems){
	for(int i=0; i<OrShipmentShGrpOrItemShManifestItems.size(); i++){
		OrShipmentShGrpOrItemShManifestItem=OrShipmentShGrpOrItemShManifestItems[i];
		 tempMap=[:];
		 tempMap.put("trackingNumber", OrShipmentShGrpOrItemShManifestItem.trackingNumber);
		 tempMap.put("orderId", OrShipmentShGrpOrItemShManifestItem.orderId);
		 tempMap.put("externalId", OrShipmentShGrpOrItemShManifestItem.externalId);
		 tempMap.put("carrierPartyId", OrShipmentShGrpOrItemShManifestItem.carrierPartyId);
		 tempMap.put("itemDescription", OrShipmentShGrpOrItemShManifestItem.itemDescription);
		 tempMap.put("quantity", OrShipmentShGrpOrItemShManifestItem.quantity);
		 tempMap.put("declaredValue", OrShipmentShGrpOrItemShManifestItem.unitPrice*OrShipmentShGrpOrItemShManifestItem.quantity);
		 tempMap.put("collectableValue", OrShipmentShGrpOrItemShManifestItem.unitPrice*OrShipmentShGrpOrItemShManifestItem.quantity);
		 tempMap.put("unitListPrice", OrShipmentShGrpOrItemShManifestItem.unitListPrice);
		 tempMap.put("unitPrice", OrShipmentShGrpOrItemShManifestItem.unitPrice);
		 tempMap.put("carrierPartyId", OrShipmentShGrpOrItemShManifestItem.carrierPartyId);
		 
		 shipment = from("Shipment").where("shipmentId", OrShipmentShGrpOrItemShManifestItem.shipmentId).queryOne();
		 product = from("Product").where("productId", OrShipmentShGrpOrItemShManifestItem.productId).queryOne();
		 prodHeight="";prodWeigth="";prodBreadth="";prodLength="";prodWidth="";
		 if(product.productHeight){
			 prodHeight=product.productHeight;
		 }
		 if(product.productWidth){
			 prodBreadth=product.productWidth;
		 }
		 if(product.productDepth){
			 prodLength=product.productDepth;
		 }
		 if(product.productWeight){
			 prodWeigth=product.productWeight;
		 }
		 tempMap.put("prodHeight", prodHeight);
		 tempMap.put("prodWeigth", prodWeigth);
		 tempMap.put("prodWeigth1", prodWeigth);
		 tempMap.put("prodWidth", prodWidth);
		 tempMap.put("prodBreadth", prodBreadth);
		 tempMap.put("prodLength", prodLength);
		 destinationPostalAddress = shipment.getRelatedOne("DestinationPostalAddress", false);
		 tempMap.put("partyName", destinationPostalAddress.toName);
		 tempMap.put("address1", destinationPostalAddress.address1);
		 tempMap.put("address2", destinationPostalAddress.address2);
		 tempMap.put("address3", "");
		 tempMap.put("city", destinationPostalAddress.city);
		 tempMap.put("postalCode", destinationPostalAddress.postalCode);
		 tempMap.put("stateProvinceGeoId", destinationPostalAddress.stateProvinceGeoId);
		 toPerson = shipment.getRelatedOne("ToPerson", false);
		 shipToTelecomNumber="";
		 
		 shipToContactMechList = ContactHelper.getContactMech(toPerson, "PHONE_SHIPPING", "TELECOM_NUMBER", false);
		 if(UtilValidate.isEmpty(shipToContactMechList)){
		   	shipToContactMechList = ContactHelper.getContactMech(toPerson, "PHONE_WORK", "TELECOM_NUMBER", false);
		 }
		 if (shipToContactMechList) {
			 shipToTelecomNumber = (EntityUtil.getFirst(shipToContactMechList)).getRelatedOne("TelecomNumber", false)
			 shipToTelecomNumber=shipToTelecomNumber.contactNumber;
		 }
		 tempMap.put("mobile", shipToTelecomNumber);
		 tempMap.put("telephone", shipToTelecomNumber);
		 tempMap.put("subCustomerID", "351642");
		 tempMap.put("PickupName", "Palred  Technology services Pvt.Limited");
		 tempMap.put("PickupAddress", "Plot -34, G/F, Bomnoli Village,Dwarka, Sector-28, Ph-2,New Delhi-110077 Delhi 07AAHCP9699Q1Z2 Delhi");
		 tempMap.put("PickupPhone", "040-67138879");
		 tempMap.put("ReturnPin", "110077");
		 tempMap.put("ReturnName", "Palred  Technology services Pvt.Limited");
		 tempMap.put("ReturnAddress", "Plot -34, G/F, Bomnoli Village,Dwarka, Sector-28, Ph-2,New Delhi-110077 Delhi 07AAHCP9699Q1Z2 Delhi");
		 tempMap.put("ReturnPhone", "040-67138879");
		 tempMap.put("ReturnPincode", "110077");
		 
		 finalList.add(tempMap);
		}
}
context.finalList=finalList;