package org.apache.ofbiz.orderfulfillment.supplier;
	
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.apache.ofbiz.entity.GenericDelegator;
	


	public class SupplierServices {
		
		public final static String module = SupplierServices.class.getName();
		
		public static Map<String, Object>  createSupplier(DispatchContext dctx, Map<String, ? extends Object> context)  {
	    	GenericDelegator delegator = (GenericDelegator) dctx.getDelegator();
			LocalDispatcher dispatcher = dctx.getDispatcher();
			Map<String, Object> result = ServiceUtil.returnSuccess();
			GenericValue userLogin = (GenericValue) context.get("userLogin");
	        String partyId = (String) context.get("partyId");
	        Locale locale = (Locale) context.get("locale");
	        String contactMechId ="";
	        String groupName = (String) context.get("groupName");
	        String panId = (String) context.get("USER_PANID");
	        String tinNumber= (String) context.get("USER_TINNUM");
	        String adharNum = (String) context.get("ADR_NUMBER");
	        String address1 = (String) context.get("address1");
	        String address2 = (String) context.get("address2");
	        String city = (String) context.get("city");
	        String stateProvinceGeoId = (String) context.get("state");
	        String districtGeoId = (String) context.get("distic");
	        String postalCode = (String) context.get("postalCode");
			String email = (String) context.get("emailAddress");
			String AltemailAddress = (String) context.get("AltemailAddress");
			String mobileNumber = (String) context.get("mobileNumber");
			String contactNumber =(String)context.get("contactNumber");
			String countryCode = (String) context.get("countryCode");
			String IfscCode= (String) context.get("IfscCode");
			String bankName = (String) context.get("bankName");		
			String branch = (String) context.get("branch");		
			String ifscCode = (String) context.get("ifscCode");		
			String accNo = (String) context.get("accNo");
			String accName = (String) context.get("accName");
			String gstNo = (String) context.get("USER_GSTNO");



			Map<String, Object> outMap = new HashMap<String, Object>();
			
			try {
				result=dispatcher.runSync("createPartyGroup", UtilMisc.toMap("groupName",groupName,"userLogin", context.get("userLogin")));
				if (ServiceUtil.isError(result)) {
	  		  		String errMsg =  ServiceUtil.getErrorMessage(result);
	  		  		Debug.logError(errMsg , module);
	  		  	}
	            partyId = (String) result.get("partyId");
	            if(UtilValidate.isNotEmpty(partyId)){
	            	String defaultRoleType = "SUPPLIER";
	            	Map<String, Object> input = UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "roleTypeId", defaultRoleType);
	            	Map<String, Object>  resultMap = dispatcher.runSync("createPartyRole", input);
	     			if (ServiceUtil.isError(resultMap)) {
	     				Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	                     return resultMap;
	                }
	     				     			
	     			if (UtilValidate.isNotEmpty(address1)){
	     				input.clear();
	    				input = UtilMisc.toMap("userLogin", userLogin, "partyId",partyId, "address1",address1, "address2", address2, "city", city, "stateProvinceGeoId", (String)context.get("stateProvinceGeoId"), "postalCode", postalCode);
	    				input.put("stateProvinceGeoId",stateProvinceGeoId);
	    				input.put("districtGeoId",districtGeoId);
	    				resultMap =  dispatcher.runSync("createPartyPostalAddress", input);
	    				if (ServiceUtil.isError(resultMap)) {
	    					Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	    	                return resultMap;
	    	            }
	    				contactMechId = (String) resultMap.get("contactMechId");
	     				input.clear();
	    				input = UtilMisc.toMap("userLogin", userLogin, "contactMechId", contactMechId, "partyId",partyId, "contactMechPurposeTypeId", "BILLING_LOCATION");
	    				resultMap =  dispatcher.runSync("createPartyContactMechPurpose", input);
	    				if (ServiceUtil.isError(resultMap)) {
	    				    Debug.logError(ServiceUtil.getErrorMessage(resultMap), module);
	    	                return resultMap;
	    	            }
	    			 }
	    			// create phone number
	    			if (UtilValidate.isNotEmpty(mobileNumber)){
	    				if (UtilValidate.isEmpty(countryCode)){
	    					countryCode	="91";
	    				}
	    	            input.clear();
	    	            input = UtilMisc.toMap("userLogin", userLogin, "contactNumber", mobileNumber,"countryCode",countryCode, "partyId",partyId, "contactMechPurposeTypeId", "PRIMARY_PHONE");
	    	            outMap = dispatcher.runSync("createPartyTelecomNumber", input);
	    	            if(ServiceUtil.isError(outMap)){
	    	           	 	Debug.logError("failed service create party contact telecom number:"+ServiceUtil.getErrorMessage(outMap), module);
	    	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	    	            }
	    			}
	                // create landLine number
	    			if (UtilValidate.isNotEmpty(contactNumber)){
	    	            input.clear();
	    	            input = UtilMisc.toMap("userLogin", userLogin, "contactNumber", contactNumber, "partyId",partyId, "contactMechPurposeTypeId", "PHONE_HOME");
	    	            outMap = dispatcher.runSync("createPartyTelecomNumber", input);
	    	            if(ServiceUtil.isError(outMap)){
	    	           	 	Debug.logError("failed service create party contact telecom number:"+ServiceUtil.getErrorMessage(outMap), module);
	    	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	    	            }
	    			}
	                // Create Party Email
	    			if (UtilValidate.isNotEmpty(email)){
	    	            input.clear();
	    	            input = UtilMisc.toMap("userLogin", userLogin, "emailAddress", email, "partyId",partyId,"verified","Y", "fromDate",UtilDateTime.nowTimestamp(),"contactMechPurposeTypeId", "PRIMARY_EMAIL");
	    	            outMap = dispatcher.runSync("createPartyEmailAddress", input);
	    	            if(ServiceUtil.isError(outMap)){
	    	           	 	Debug.logError("faild service create party Email:"+ServiceUtil.getErrorMessage(outMap), module);
	    	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	    	            }
	    			}
	    			
	    			if (UtilValidate.isNotEmpty(AltemailAddress)){
	    	            input.clear();
	    	            input = UtilMisc.toMap("userLogin", userLogin, "emailAddress", AltemailAddress, "partyId",partyId,"verified","Y", "fromDate",UtilDateTime.nowTimestamp(),"contactMechPurposeTypeId", "PRIMARY_EMAIL");
	    	            outMap = dispatcher.runSync("createPartyEmailAddress", input);
	    	            if(ServiceUtil.isError(outMap)){
	    	           	 	Debug.logError("faild service create party Email:"+ServiceUtil.getErrorMessage(outMap), module);
	    	           	 	return ServiceUtil.returnError(ServiceUtil.getErrorMessage(outMap));
	    	            }
	    			}
	    			
		            if(UtilValidate.isNotEmpty(panId)){
		            	 dispatcher.runSync("createPartyIdentification", UtilMisc.toMap("partyIdentificationTypeId","PAN_NUMBER","idValue",panId,"partyId",partyId,"userLogin", context.get("userLogin")));
		       	    }
		            if(UtilValidate.isNotEmpty(tinNumber)){
		              	 dispatcher.runSync("createPartyIdentification", UtilMisc.toMap("partyIdentificationTypeId","TIN_NUMBER","idValue",tinNumber,"partyId",partyId,"userLogin", context.get("userLogin")));
		         	}
		            if(UtilValidate.isNotEmpty(adharNum)){
		             	 dispatcher.runSync("createPartyIdentification", UtilMisc.toMap("partyIdentificationTypeId","ADR_NUMBER","idValue",adharNum,"partyId",partyId,"userLogin", context.get("userLogin")));
		        	}
		            if(UtilValidate.isNotEmpty(gstNo)){
		             	 dispatcher.runSync("createPartyIdentification", UtilMisc.toMap("partyIdentificationTypeId","GST_NUMBER","idValue",gstNo,"partyId",partyId,"userLogin", context.get("userLogin")));
		        	}
		            
	            }
	        } catch (GenericServiceException e) {
	            Debug.logWarning(e.getMessage(), module);
	            Debug.logError(e, "Error creating Group For Vendor",module);
				return ServiceUtil.returnError("Error creating Group For Vendor :"+groupName);
	        }
			
			/*if(UtilValidate.isNotEmpty(partyId)){
				try{
					GenericValue BankAccount = delegator.makeValue("BankAccount");
					BankAccount.set("bankAccountName", accName);
					BankAccount.set("bankAccountCode", accNo);
					BankAccount.set("branchCode", ifscCode);
					BankAccount.set("ifscCode", ifscCode);
					BankAccount.set("ownerPartyId", partyId);
					delegator.createSetNextSeqId(BankAccount);
				}catch (Exception e) {
					Debug.logError(e, module);
					return ServiceUtil.returnError("Error while creating  Bank Details" + e);	
				}
			}*/
			
			result.put("partyId", partyId);
			return result;
		}
		
		
	} 