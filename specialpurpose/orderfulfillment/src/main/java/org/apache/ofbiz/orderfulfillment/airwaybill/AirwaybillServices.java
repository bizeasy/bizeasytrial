package org.apache.ofbiz.orderfulfillment.airwaybill;
	
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.cups4j.PrintRequestResult;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.PageSequenceResults;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.collections.MapStack;
import org.apache.ofbiz.content.output.OutputServices;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.view.ApacheFopWorker;
import org.apache.ofbiz.widget.renderer.ScreenRenderer;
import org.apache.ofbiz.widget.renderer.ScreenStringRenderer;
import org.apache.ofbiz.widget.renderer.fo.FoFormRenderer;
import org.apache.ofbiz.widget.renderer.macro.MacroScreenRenderer;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil;
	


	public class AirwaybillServices {
		
		public final static String module = AirwaybillServices.class.getName();
		
		public static Map<String, Object> assignAirwayBillsForShipment(DispatchContext dctx, Map<String, ? extends Object> context) {
	        Locale locale = (Locale) context.get("locale");
	        Delegator delegator = dctx.getDelegator();
	        LocalDispatcher dispatcher = dctx.getDispatcher();
	        GenericValue userLogin = (GenericValue) context.get("userLogin");
	        Debug.log("userLogin ==============="+userLogin);
	        Map<String, Object> screenContext = new HashMap<String, Object>();
	        Map<String, Object> result = new HashMap<String, Object>();
	        String contentType = "application/pdf";
	        
	        String shipmentId = (String) context.get("shipmentId");
	        String orderId = (String) context.get("orderId");
	        
	        try {
	        
		        List<GenericValue> orderItemShipGroupList = EntityQuery.use(delegator).from("OrderItemShipGroup")
	                    .where("orderId", orderId).queryList();
	            
	            for(int i=0; i<orderItemShipGroupList.size(); i++){
	            	GenericValue shipGroupInfo = (GenericValue)orderItemShipGroupList.get(i);
	            	Debug.log("shipGroupInfo =========="+shipGroupInfo);
	            	
	            	if(UtilValidate.isEmpty(shipGroupInfo.get("carrierPartyId")) || UtilValidate.isEmpty(shipGroupInfo.get("shipmentMethodTypeId"))){
	            		Debug.logError("CarrierPartyId or shipmentMethodTypeId is missing. Cannot Assign AirwayBill Number ", module);
	            		return ServiceUtil.returnError("CarrierPartyId or shipmentMethodTypeId is missing. Cannot Assign AirwayBill Number");
	            	}
	            	
	            	String carrierPartyId = (String) shipGroupInfo.get("carrierPartyId");
	            	String shipmentMethodTypeId = (String) shipGroupInfo.get("shipmentMethodTypeId");
	            	
	            	EntityConditionList<EntityExpr> statusCond = EntityCondition.makeCondition(UtilMisc.toList(
	                        EntityCondition.makeCondition("status", EntityOperator.EQUALS, null),
	                        EntityCondition.makeCondition("status", EntityOperator.EQUALS, "NEW")), EntityOperator.OR);
	                EntityConditionList<EntityCondition> conditions = EntityCondition.makeCondition(UtilMisc.toList(
	                		statusCond,
	                        EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, carrierPartyId),
	                        EntityCondition.makeCondition("shipmentMethodTypeId", EntityOperator.EQUALS, shipmentMethodTypeId)
	                		),
	                        EntityOperator.AND);
	                
	            	List<GenericValue> availableAirwayBillNumbers = EntityQuery.use(delegator).from("CarrierAirwayBills").where(conditions).orderBy("airwayBillNo").queryList();
	            	Debug.log("availableAirwayBillNumbers =========="+availableAirwayBillNumbers);
	            	
	            	if(UtilValidate.isEmpty(availableAirwayBillNumbers)){
	            		Debug.logError("No Airwaybills available for CarrierPartyId: "+ carrierPartyId + " or shipmentMethodTypeId: "+ shipmentMethodTypeId +". Cannot Assign AirwayBill Number ", module);
	            		return ServiceUtil.returnError("No Airwaybills available for CarrierPartyId: "+ carrierPartyId + " or shipmentMethodTypeId: "+ shipmentMethodTypeId +". Cannot Assign AirwayBill Number ");
	            	}
	            	GenericValue assigningAwb = EntityUtil.getFirst(availableAirwayBillNumbers);
	            	String airwayBillNumber = (String)(assigningAwb).get("airwayBillNo");
            	
	            	shipGroupInfo.set("trackingNumber", airwayBillNumber);
	            	delegator.store(shipGroupInfo);
	            	
	            	assigningAwb.set("status", "Assigned");
	            	assigningAwb.set("shipmentId", shipmentId);
	            	assigningAwb.set("orderId", orderId);
	            	
	            	delegator.store(assigningAwb);
	            }
            } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	        }
	        return result;
	    }
		
		public static Map<String, Object> createAirwayBills(DispatchContext dctx, Map<String, ? extends Object> context) {
	        Locale locale = (Locale) context.get("locale");
	        Delegator delegator = dctx.getDelegator();
	        LocalDispatcher dispatcher = dctx.getDispatcher();
	        GenericValue userLogin = (GenericValue) context.get("userLogin");
	        Map<String, Object> screenContext = new HashMap<String, Object>();
	        Map<String, Object> result = ServiceUtil.returnSuccess();
	        String contentType = "application/pdf";
	        
	        String carrierPartyId = (String) context.get("carrierPartyId");
	        String shipmentMethodTypeId = (String) context.get("shipmentMethodTypeId");
	        String awbPrefix = "";
	        if(UtilValidate.isNotEmpty(context.get("awbPrefix"))){
	        	awbPrefix = (String) context.get("awbPrefix");
	        }
	        
	        String fromAwb = (String) context.get("fromAwb");
	        String toAwb = (String) context.get("toAwb");
	        try {
	        	
	        	List airwayBillsToBeStored = new LinkedList();
	        	GenericValue carrierAirwayBill = delegator.makeValue("CarrierAirwayBills");
	        	carrierAirwayBill.put("partyId", carrierPartyId);
	        	carrierAirwayBill.put("shipmentMethodTypeId", shipmentMethodTypeId);
	        	carrierAirwayBill.put("airwayBillNo", awbPrefix + fromAwb);
	        	airwayBillsToBeStored.add(carrierAirwayBill);
	        	
	        	int difference = Integer.parseInt(toAwb) - Integer.parseInt(fromAwb);
	        	
	        	for(int i=0; i<difference; i++){
	        		String incremented = String.format("%0" + fromAwb.length() + "d",
			                Integer.parseInt(fromAwb) + 1);
	        		fromAwb = incremented;
	        		
	        		GenericValue carrierAirwayBills = delegator.makeValue("CarrierAirwayBills");
		        	carrierAirwayBills.put("partyId", carrierPartyId);
		        	carrierAirwayBills.put("shipmentMethodTypeId", shipmentMethodTypeId);
	        		carrierAirwayBills.put("airwayBillNo", awbPrefix + fromAwb);
	        		
	        		//GenericValue tempVal;
	        		//tempVal.putAll(carrierAirwayBills);
	        		
	        		airwayBillsToBeStored.add(carrierAirwayBills);
	        	}
	        	int noOfAwbs = airwayBillsToBeStored.size();
	        	delegator.storeAll(airwayBillsToBeStored);
	        	Debug.log("airwayBillsToBeStored =========="+airwayBillsToBeStored);
		        
            } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	            return ServiceUtil.returnError("Problems Creating AirwayBill");
	        }
	        return result;
	    }
		
		
		
		
	} 