package org.apache.ofbiz.orderfulfillment.printer;
	
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
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
	


	public class Print {
		
		public final static String module = Print.class.getName();
		protected static final FoFormRenderer foFormRenderer = new FoFormRenderer();
		private static final String FILE_NAME = "/tmp/temp.txt";
		private static final String CUPS_HOST = "localhost";
		private static final int CUPS_PORT = 631;
		//private static final String PRINTER_NAME = "Canon-iR2002-2202-UFRII-LT";
		//private static final String PRINTER_NAME = "Zebra_Technologies_ZTC_ZT230-200dpi_ZPL";
		
		/*public static void main(String[] args) throws Exception {
			CupsClient cupsClient = new CupsClient(CUPS_HOST, CUPS_PORT);
			List<CupsPrinter> printers = cupsClient.getPrinters();
			for (CupsPrinter cupsPrinter : printers) {
				System.out.println(cupsPrinter.getName() + " - " + cupsPrinter.getLocation() + " - " + cupsPrinter.getDescription());
				if(cupsPrinter.getName().equalsIgnoreCase(PRINTER_NAME)) {
					System.out.println("Found " + PRINTER_NAME);
					printFile(FILE_NAME, cupsPrinter);
					break;
				}
			}
		}
	
		private static void printFile(String filename, CupsPrinter printer) throws Exception {
			FileInputStream inputStream = new FileInputStream(new File(FILE_NAME));
			PrintJob.Builder builder = new PrintJob.Builder(inputStream);
			PrintJob job = builder.jobName(FILE_NAME).userName(System.getProperty("user.name")).copies(1).build();
			PrintRequestResult presult = printer.print(job);
			System.out.println(presult.getResultCode() + ": " + presult.getResultDescription());
		}*/
		
		/*public static Map<String, Object> printDocument(DispatchContext dctx, Map<String, ? extends Object> serviceContext) {
	        Locale locale = (Locale) serviceContext.get("locale");
	        Delegator delegator = dctx.getDelegator();
	        GenericValue userLogin = (GenericValue) serviceContext.get("userLogin");
	        Debug.log("userLogin ==============="+userLogin);
	        String shipmentScreenLocation = "component://orderfulfillment/widget/OrderFulfillmentScreens.xml#ShipmentManifest.fo";
	        String invoiceScreenLocation = "component://orderfulfillment/widget/OrderFulfillmentScreens.xml#InvoicePDF.fo";
	        Map<String, Object> screenContext = UtilGenerics.checkMap(serviceContext.remove("screenContext"));
	        String contentType = (String) serviceContext.remove("contentType");
	        
	        
	        screenContext.put("locale", locale);
	        if (UtilValidate.isEmpty(contentType)) {
	            contentType = "application/pdf";
	        }
	        //screenContext.put("shipmentId", "10030");
	        //screenContext.put("invoiceId", "CI10040");

	        try {
	        	
	            MapStack<String> screenContextTmp = MapStack.create();
	            screenContextTmp.put("locale", locale);
	            Debug.log("screenLocation ============="+shipmentScreenLocation);
	            Debug.log("screenContext ============="+screenContext);
	            
	            Writer writer = new StringWriter();
	            // substitute the freemarker variables...
	            ScreenStringRenderer foScreenStringRenderer = new MacroScreenRenderer(EntityUtilProperties.getPropertyValue("widget", "screenfop.name", dctx.getDelegator()),
	                    EntityUtilProperties.getPropertyValue("widget", "screenfop.screenrenderer", dctx.getDelegator()));
	            ScreenRenderer screensAtt = new ScreenRenderer(writer, screenContextTmp, foScreenStringRenderer);
	            screensAtt.populateContextForService(dctx, screenContext);
	            screenContextTmp.putAll(screenContext);
	            Debug.log("screenContextTmp ============="+screenContextTmp);
	            screensAtt.getContext().put("formStringRenderer", foFormRenderer);
	            screensAtt.render(shipmentScreenLocation);

	            // create the input stream for the generation
	            StreamSource src = new StreamSource(new StringReader(writer.toString()));

	            // create the output stream for the generation
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            Fop fop = ApacheFopWorker.createFopInstance(baos, MimeConstants.MIME_PDF);
	            ApacheFopWorker.transform(src, null, fop);

	            baos.flush();
	            baos.close();
	            
	            Debug.log("baos.toByteArray() ============="+baos.toByteArray());
	            
	            DocFlavor psInFormat = new DocFlavor.INPUT_STREAM(contentType);
	            InputStream bais = new ByteArrayInputStream(baos.toByteArray());
	            
	            Doc myDoc = new SimpleDoc(bais, psInFormat, null);
	            PrintRequestAttributeSet praset = new HashPrintRequestAttributeSet();
	            CupsClient cupsClient = new CupsClient(CUPS_HOST, CUPS_PORT);
	            
	            List<CupsPrinter> printers = cupsClient.getPrinters();
				for (CupsPrinter cupsPrinter : printers) {
					System.out.println("Printer Details====================="+cupsPrinter.getName() + " - " + cupsPrinter.getLocation() + " - " + cupsPrinter.getDescription());
					if(cupsPrinter.getName().equalsIgnoreCase(PRINTER_NAME)) {
						System.out.println("=====Found " + PRINTER_NAME);
						PrintJob.Builder builder = new PrintJob.Builder(bais);
						PrintJob job = builder.jobName("ShipmentPrint").userName(System.getProperty("user.name")).copies(1).build();
						PrintRequestResult presult = cupsPrinter.print(job);
						System.out.println(presult.getResultCode() + "=======: " + presult.getResultDescription());
						
						break;
					}
				}

	        } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	        }
	        
	        
	        try {
	        	
	            MapStack<String> screenContextTmp = MapStack.create();
	            screenContextTmp.put("locale", locale);
	            Debug.log("screenLocation ============="+invoiceScreenLocation);
	            Debug.log("screenContext ============="+screenContext);
	            
	            Writer writer = new StringWriter();
	            // substitute the freemarker variables...
	            ScreenStringRenderer foScreenStringRenderer = new MacroScreenRenderer(EntityUtilProperties.getPropertyValue("widget", "screenfop.name", dctx.getDelegator()),
	                    EntityUtilProperties.getPropertyValue("widget", "screenfop.screenrenderer", dctx.getDelegator()));
	            ScreenRenderer screensAtt = new ScreenRenderer(writer, screenContextTmp, foScreenStringRenderer);
	            screensAtt.populateContextForService(dctx, screenContext);
	            screenContextTmp.putAll(screenContext);
	            Debug.log("screenContextTmp ============="+screenContextTmp);
	            screensAtt.getContext().put("formStringRenderer", foFormRenderer);
	            screensAtt.render(invoiceScreenLocation);

	            // create the input stream for the generation
	            StreamSource src = new StreamSource(new StringReader(writer.toString()));

	            // create the output stream for the generation
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            Fop fop = ApacheFopWorker.createFopInstance(baos, MimeConstants.MIME_PDF);
	            ApacheFopWorker.transform(src, null, fop);

	            baos.flush();
	            baos.close();
	            
	            Debug.log("baos.toByteArray() ============="+baos.toByteArray());
	            
	            DocFlavor psInFormat = new DocFlavor.INPUT_STREAM(contentType);
	            InputStream bais = new ByteArrayInputStream(baos.toByteArray());
	            
	            Doc myDoc = new SimpleDoc(bais, psInFormat, null);
	            PrintRequestAttributeSet praset = new HashPrintRequestAttributeSet();
	            CupsClient cupsClient = new CupsClient(CUPS_HOST, CUPS_PORT);
	            
	            List<CupsPrinter> printers = cupsClient.getPrinters();
				for (CupsPrinter cupsPrinter : printers) {
					System.out.println("Printer Details====================="+cupsPrinter.getName() + " - " + cupsPrinter.getLocation() + " - " + cupsPrinter.getDescription());
					if(cupsPrinter.getName().equalsIgnoreCase(PRINTER_NAME)) {
						System.out.println("=====Found " + PRINTER_NAME);
						PrintJob.Builder builder = new PrintJob.Builder(bais);
						PrintJob job = builder.jobName("InvoicePrint").userName(System.getProperty("user.name")).copies(1).build();
						PrintRequestResult presult = cupsPrinter.print(job);
						System.out.println(presult.getResultCode() + "=======: " + presult.getResultDescription());
						
						break;
					}
				}

	        } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	        }
	        
	        

	        return ServiceUtil.returnSuccess();
	    }*/
		
		public static Map<String, Object> convertFoToPdfAndPrintFile(DispatchContext dctx, Map<String, ? extends Object> serviceContext) {
	        Locale locale = (Locale) serviceContext.get("locale");
	        Delegator delegator = dctx.getDelegator();
	        GenericValue userLogin = (GenericValue) serviceContext.get("userLogin");
	        Debug.log("userLogin ==============="+userLogin);
	        //String shipmentScreenLocation = "component://orderfulfillment/widget/OrderFulfillmentScreens.xml#ShipmentManifest.fo";
	       // String invoiceScreenLocation = "component://orderfulfillment/widget/OrderFulfillmentScreens.xml#InvoicePDF.fo";
	        String screenLocation = null;
	        Map<String, Object> screenContext = UtilGenerics.checkMap(serviceContext.remove("screenContext"));
	        String contentType = (String) serviceContext.remove("contentType");
	        String facilityId = (String) serviceContext.remove("facilityId");
	        String printerName = (String) serviceContext.remove("printerName");
	        String documentId = (String) serviceContext.remove("documentId");
	        
	        Debug.log("serviceContext =============="+serviceContext);
	        
	        
	        if(UtilValidate.isNotEmpty(facilityId) && UtilValidate.isNotEmpty(printerName) ){
	        	Debug.log("Unable to print. Either Printer Name or Facility Id should be passed", module);
	        }
	        
        	// Logic to get printer details and Document location
        	try{
        		List<GenericValue> facilityPrinterList = EntityQuery.use(delegator).from("FacilityPrinterAndPrinterAndDocument").where("facilityId", facilityId, "documentId", documentId).queryList();
        		Debug.log("facilityPrinterList =============="+facilityPrinterList);
        		GenericValue facilityPrinter = (GenericValue)EntityUtil.getFirst(facilityPrinterList);
	            printerName = (String)facilityPrinter.get("printerName");
	            Debug.log("printerName =============="+printerName);
	            screenLocation = (String)facilityPrinter.get("documentLocation");
	            Debug.log("screenLocation =============="+screenLocation);
        	}catch(Exception e){
        		Debug.log("Unable to print. Unable to Fetch Facility Printers", module);
        	}
	        
	        screenContext.put("locale", locale);
	        if (UtilValidate.isEmpty(contentType)) {
	            contentType = "application/pdf";
	        }
	        //screenContext.put("shipmentId", "10030");
	        //screenContext.put("invoiceId", "CI10040");

	        try {
	        	
	            MapStack<String> screenContextTmp = MapStack.create();
	            screenContextTmp.put("locale", locale);
	            Debug.log("screenLocation ============="+screenLocation);
	            Debug.log("screenContext ============="+screenContext);
	            
	            Writer writer = new StringWriter();
	            // substitute the freemarker variables...
	            ScreenStringRenderer foScreenStringRenderer = new MacroScreenRenderer(EntityUtilProperties.getPropertyValue("widget", "screenfop.name", dctx.getDelegator()),
	                    EntityUtilProperties.getPropertyValue("widget", "screenfop.screenrenderer", dctx.getDelegator()));
	            ScreenRenderer screensAtt = new ScreenRenderer(writer, screenContextTmp, foScreenStringRenderer);
	            screensAtt.populateContextForService(dctx, screenContext);
	            screenContextTmp.putAll(screenContext);
	            Debug.log("screenContextTmp ============="+screenContextTmp);
	            screensAtt.getContext().put("formStringRenderer", foFormRenderer);
	            screensAtt.render(screenLocation);

	            // create the input stream for the generation
	            StreamSource src = new StreamSource(new StringReader(writer.toString()));

	            // create the output stream for the generation
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            Fop fop = ApacheFopWorker.createFopInstance(baos, MimeConstants.MIME_PDF);
	            ApacheFopWorker.transform(src, null, fop);

	            baos.flush();
	            baos.close();
	            
	            Debug.log("baos.toByteArray() ============="+baos.toByteArray());
	            
	            DocFlavor psInFormat = new DocFlavor.INPUT_STREAM(contentType);
	            InputStream bais = new ByteArrayInputStream(baos.toByteArray());
	            
	            Doc myDoc = new SimpleDoc(bais, psInFormat, null);
	            PrintRequestAttributeSet praset = new HashPrintRequestAttributeSet();
	            CupsClient cupsClient = new CupsClient(CUPS_HOST, CUPS_PORT);
	            
	            List<CupsPrinter> printers = cupsClient.getPrinters();
				for (CupsPrinter cupsPrinter : printers) {
					System.out.println("Printer Details====================="+cupsPrinter.getName() + " - " + cupsPrinter.getLocation() + " - " + cupsPrinter.getDescription());
					if(cupsPrinter.getName().equalsIgnoreCase(printerName)) {
						System.out.println("=====Found " + printerName);
						PrintJob.Builder builder = new PrintJob.Builder(bais);
						PrintJob job = builder.jobName("ShipmentPrint").userName(System.getProperty("user.name")).copies(1).build();
						PrintRequestResult presult = cupsPrinter.print(job);
						System.out.println(presult.getResultCode() + "=======: " + presult.getResultDescription());
						
						break;
					}
				}

	        } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	        }
	        
	        return ServiceUtil.returnSuccess();
	    }
		
		
		
		public static Map<String, Object> printShipmentLabelAndInvoice(DispatchContext dctx, Map<String, ? extends Object> context) {
	        Locale locale = (Locale) context.get("locale");
	        Delegator delegator = dctx.getDelegator();
	        LocalDispatcher dispatcher = dctx.getDispatcher();
	        GenericValue userLogin = (GenericValue) context.get("userLogin");
	        Debug.log("userLogin ==============="+userLogin);
	        Map<String, Object> screenContext = new HashMap<String, Object>();
	        String contentType = "application/pdf";
	        
	        String shipmentId = (String) context.get("shipmentId");
	        String orderId = (String) context.get("orderId");
	        String invoiceId = (String) context.get("invoiceId");
	        String facilityId = (String) context.get("facilityId");
	        
	        try {
	        	
	        	if(UtilValidate.isEmpty(facilityId)){
	        		GenericValue orderHeader = EntityQuery.use(delegator).from("OrderHeader").where("orderId", orderId).queryOne();
		    		facilityId = orderHeader.getString("originFacilityId");
	        	}
	            
	        	if(UtilValidate.isEmpty(invoiceId)){
	        		List<GenericValue> orderItemBilling = EntityQuery.use(delegator).from("OrderItemBilling").where("orderId", orderId).queryList();
		            invoiceId = (String)((GenericValue)EntityUtil.getFirst(orderItemBilling)).get("invoiceId");
		        }
	        	screenContext.put("shipmentId", shipmentId);
		        screenContext.put("invoiceId", invoiceId);
		        screenContext.put("orderId", orderId);
		        screenContext.put("userLogin", userLogin);
		        
		        Map<String, Object> serviceContext = new HashMap<String, Object>();
		        serviceContext.put("screenContext", screenContext);
		        serviceContext.put("documentId", "SHIPPING_LABEL");
		        serviceContext.put("userLogin", userLogin);
		        if(UtilValidate.isNotEmpty(facilityId)){
		        	serviceContext.put("facilityId", facilityId);
		        }
		        Debug.log("serviceContext ==============="+serviceContext);
		        dispatcher.runAsync("convertFoToPdfAndPrintFile", serviceContext);
		        
		        serviceContext.put("documentId", "SALES_INVOICE");
		        Debug.log("serviceContext ==============="+serviceContext);
		        dispatcher.runAsync("convertFoToPdfAndPrintFile", serviceContext);
	        } catch (Exception e) {
	            Debug.logError(e, "Error rendering [" + contentType + "]: " + e.toString(), module);
	        }

	        return ServiceUtil.returnSuccess();
	    }
		
		
		
	} 