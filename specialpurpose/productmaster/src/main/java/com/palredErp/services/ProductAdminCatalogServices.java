package com.palredErp.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.apache.commons.lang.RandomStringUtils;
import org.jdom.JDOMException;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.category.CategoryWorker;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import com.palredErp.util.ProductAdminUtil;


import org.apache.ofbiz.base.util.StringUtil;

public class ProductAdminCatalogServices {
    public static final String module = ProductAdminCatalogServices.class.getName();
    private static final ResourceBundle OSAFE_PROPS = UtilProperties.getResourceBundle("ProductProperties.xml", Locale.getDefault());
    
    public static Map<String, Object> addProductImageAndContent(DispatchContext dctx, Map<String, ? extends Object> context)
    throws IOException, JDOMException {
    Debug.log("hai................."+context);
    LocalDispatcher dispatcher = dctx.getDispatcher();
    Delegator delegator = dctx.getDelegator();
    GenericValue userLogin = (GenericValue) context.get("userLogin");
    String productId = (String) context.get("productId");
    String productContentTypeId = (String) context.get("productContentTypeId");
    ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
    String fileName = (String)context.get("_uploadedFile_fileName");
    String imageFilePath = (String)context.get("imageFilePath");
    String imageUrlRef = (String)context.get("imageUrlRef");
    String imageResourceType = (String)context.get("imageResourceType");
    GenericValue xProductStoreParam = null;
    String imageUrl = "";
    String filenameToUse = "";
    
    if (imageResourceType.equals("file")) 
    {
        String osafeThemeServerPath = FlexibleStringExpander.expandString(OSAFE_PROPS.getString("osafeThemeServer"), context);
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex == -1) {
        	filenameToUse = fileName;
        } else {
        	filenameToUse = fileName.substring(0, extensionIndex);
        }
        filenameToUse = StringUtil.replaceString(filenameToUse, " ", "_");
        
        //String productImageFullPath = ProductAdminUtil.buildProductImagePathExt(productContentTypeId);
        
        List<GenericValue> fileExtension = new LinkedList<GenericValue>();
        try {
            fileExtension = delegator.findByAnd("FileExtension", UtilMisc.toMap("mimeTypeId", (String) context.get("_uploadedFile_contentType")),null,false);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }

        GenericValue extension = EntityUtil.getFirst(fileExtension);
        if (extension != null) {
            filenameToUse += "." + extension.getString("fileExtensionId");
        }

        File file = new File(osafeThemeServerPath + imageFilePath + filenameToUse);
        
        if (!new File(osafeThemeServerPath + imageFilePath).exists()) {
        	new File(osafeThemeServerPath + imageFilePath).mkdirs();
	    }
        
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            out.write(imageData.array());
            out.close();
        } catch (FileNotFoundException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to open file for writing: " + file.getAbsolutePath());
        } catch (IOException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to write binary data to: " + file.getAbsolutePath());
        }

        imageUrl = imageFilePath + filenameToUse;
    }
    else if(imageResourceType.equals("url"))
    {
    	if(UtilValidate.isNotEmpty(imageUrlRef))
    	{
    		imageUrl = imageUrlRef;
    		filenameToUse = imageUrlRef.substring(imageUrlRef.lastIndexOf("/")+1);
    	}
    }
        GenericValue productContent = null;
        String contentId = null;
        try {
            List productContentList = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId",productContentTypeId),null,false);
            if(UtilValidate.isNotEmpty(productContentList)) {
                productContentList = EntityUtil.filterByDate(productContentList);
                productContent = EntityUtil.getFirst(productContentList);
                contentId = (String) productContent.get("contentId");
            }
        } catch (GenericEntityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (UtilValidate.isNotEmpty(imageUrl) && imageUrl.length() > 0) {
            Map<String, Object> dataResourceCtx = new HashMap<String, Object>(); 
            //changed for default image url+imge_url property
            String productStoreId=FlexibleStringExpander.expandString(OSAFE_PROPS.getString("storeProductStoreId"), context);
            String defaultImageUrl=ProductAdminUtil.getProductStoreParm(delegator, productStoreId, "IMAGE_URL");

	            Debug.log("defaultImageUrl"+defaultImageUrl);
            if(defaultImageUrl.contains("https")){
            	defaultImageUrl = defaultImageUrl.substring(defaultImageUrl.indexOf("https"), defaultImageUrl.length());
            	
            }
            Debug.log("1234=====defaultImageUrl========"+defaultImageUrl);

            if(!imageUrl.contains(defaultImageUrl)){
            	if(defaultImageUrl!= null && !defaultImageUrl.isEmpty()){
            		String newImageUrl=defaultImageUrl.concat(imageUrl.replace("/osafe_theme/", "/"));
            		dataResourceCtx.put("objectInfo", newImageUrl);
            	}}
            
            else{
            	dataResourceCtx.put("objectInfo", imageUrl);//commented for new image url..uncomment if new url not required	
            }
          //ended
            dataResourceCtx.put("dataResourceName", filenameToUse);
            dataResourceCtx.put("userLogin", userLogin);

            if (UtilValidate.isNotEmpty(contentId)) {
                GenericValue content = null;
                try {
                    content = delegator.findOne("Content", UtilMisc.toMap("contentId", contentId), false);
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                if (content != null) {
                    GenericValue dataResource = null;
                    try {
                        dataResource = content.getRelatedOne("DataResource");
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(e.getMessage());
                    }

                    if (dataResource != null) {
                        dataResourceCtx.put("dataResourceId", dataResource.getString("dataResourceId"));
                        try {
                            dispatcher.runSync("updateDataResource", dataResourceCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    } else {
                        dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
                        dataResourceCtx.put("mimeTypeId", "text/html");
                        Map<String, Object> dataResourceResult = new HashMap<String, Object>();
                        try {
                            dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }

                        Map<String, Object> contentCtx = new HashMap<String, Object>();
                        contentCtx.put("contentId", contentId);
                        contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
                        contentCtx.put("userLogin", userLogin);
                        try {
                            dispatcher.runSync("updateContent", contentCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }
                }
            } else {
                dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
                dataResourceCtx.put("mimeTypeId", "text/html");
                Map<String, Object> dataResourceResult = new HashMap<String, Object>();
                try {
                    dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                Map<String, Object> contentCtx = new HashMap<String, Object>();
                contentCtx.put("contentTypeId", "DOCUMENT");
                contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
                contentCtx.put("userLogin", userLogin);
                Map<String, Object> contentResult = new HashMap<String, Object>();
                try {
                    contentResult = dispatcher.runSync("createContent", contentCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }
                Map<String, Object> productContentCtx = new HashMap<String, Object>();
                productContentCtx.put("productId", productId);
                productContentCtx.put("productContentTypeId", productContentTypeId);
                productContentCtx.put("fromDate", (Timestamp) context.get("fromDate"));
                productContentCtx.put("thruDate", (Timestamp) context.get("thruDate"));
                productContentCtx.put("userLogin", userLogin);
                productContentCtx.put("contentId", contentResult.get("contentId"));
                try {
                    dispatcher.runSync("createProductContent", productContentCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }
            }
        }
    
       return ServiceUtil.returnSuccess();
}
    
    public static Map<String, Object> addProductCategoryImage(DispatchContext dctx, Map<String, ? extends Object> context)
    throws IOException, JDOMException  {

    LocalDispatcher dispatcher = dctx.getDispatcher();
    Delegator delegator = dctx.getDelegator();
    GenericValue userLogin = (GenericValue) context.get("userLogin");
    String productCategoryId = (String) context.get("productCategoryId");
    ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
    String fileName = (String) context.get("_uploadedFile_fileName");
    
    String imageFilePath = (String)context.get("imageFilePath");
    String imageUrlRef = (String)context.get("imageUrlRef");
    String imageResourceType = (String)context.get("imageResourceType");
    
    String imageUrl = "";
    String filenameToUse = "";
    
    if (imageResourceType.equals("file")) 
    {
        String osafeThemeServerPath = FlexibleStringExpander.expandString(OSAFE_PROPS.getString("osafeThemeServer"), context);
        
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex == -1) {
        	filenameToUse = fileName;
        } else {
        	filenameToUse = fileName.substring(0, extensionIndex);
        }
        
        List<GenericValue> fileExtension = new LinkedList<GenericValue>();
        try {
            fileExtension = delegator.findByAnd("FileExtension", UtilMisc.toMap("mimeTypeId", (String) context.get("_uploadedFile_contentType")),null,false);
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }

        GenericValue extension = EntityUtil.getFirst(fileExtension);
        if (extension != null) {
            filenameToUse += "." + extension.getString("fileExtensionId");
        }
        //String productCategoryImagePath = ProductAdminUtil.buildProductImagePathExt("CATEGORY_IMAGE_URL");
        
        File file = new File(osafeThemeServerPath + imageFilePath + filenameToUse);
        
        if (!new File(osafeThemeServerPath + imageFilePath).exists()) {
        	new File(osafeThemeServerPath + imageFilePath).mkdirs();
	    }
        
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            out.write(imageData.array());
            out.close();
        } catch (FileNotFoundException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to open file for writing: " + file.getAbsolutePath());
        } catch (IOException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to write binary data to: " + file.getAbsolutePath());
        }
        imageUrl = imageFilePath + filenameToUse;
    }
    else if(imageResourceType.equals("url"))
    {
    	if(UtilValidate.isNotEmpty(imageUrlRef))
    	{
    		imageUrl = imageUrlRef;
    	}
    }
  //changed for default image url+imge_url property
    String productStoreId=FlexibleStringExpander.expandString(OSAFE_PROPS.getString("storeProductStoreId"), context);
    String defaultImageUrl=ProductAdminUtil.getProductStoreParm(delegator, productStoreId, "IMAGE_URL");

        Debug.log("defaultImageUrl"+defaultImageUrl);
    if(defaultImageUrl.contains("https")){
    	defaultImageUrl = defaultImageUrl.substring(defaultImageUrl.indexOf("https"), defaultImageUrl.length());
    	
    }
    Debug.log("====defaultImageUrl====="+defaultImageUrl);
    
    if (UtilValidate.isNotEmpty(imageUrl) && imageUrl.length() > 0) 
    {
    	
        Map<String, Object> productCategoryCtx = new HashMap<String, Object>();
    	productCategoryCtx.put("productCategoryId", productCategoryId);
    	//productCategoryCtx.put("categoryImageUrl", imageUrl);
       	productCategoryCtx.put("productCategoryTypeId", "CATALOG_CATEGORY");
    	productCategoryCtx.put("userLogin", userLogin);
    	
    	//image url with bucket
    	 if(!imageUrl.contains(defaultImageUrl)){
         	if(defaultImageUrl!= null && !defaultImageUrl.isEmpty()){
         		String newImageUrl=defaultImageUrl.concat(imageUrl.replace("/osafe_theme/", "/"));
         		productCategoryCtx.put("categoryImageUrl", newImageUrl);
         	}}
         
         else{
         	productCategoryCtx.put("categoryImageUrl",imageUrl);//commented for new image url..uncomment if new url not required	
         }
    	 //ended
    	 
    	try {
    		dispatcher.runSync("updateProductCategory", productCategoryCtx);
    	} catch (GenericServiceException e) {
    		Debug.logError(e, module);
		}
    }
      return ServiceUtil.returnSuccess();
    }
    
    public static List<Map<String, Object>> getRelatedCategories(Delegator delegator, String parentId, List<String> categoryTrail, boolean limitView, boolean excludeEmpty, boolean recursive) 
    {
        List<Map<String, Object>> categories = new LinkedList<Map<String,Object>>();
        if (categoryTrail == null) {
            categoryTrail = new LinkedList<String>();
        }
        categoryTrail.add(parentId);
        if (Debug.verboseOn())
        {
            Debug.logVerbose("getRelatedCategories for: " + parentId, module);
        	
        }

        List<GenericValue> rollups = null;

        try {
        	rollups = delegator.findByAnd("ProductCategoryRollup", UtilMisc.toMap("parentProductCategoryId", parentId), UtilMisc.toList("sequenceNum"),false);
            if (limitView) {
                rollups = EntityUtil.filterByDate(rollups, true);
            }
        } catch (GenericEntityException e) {
            Debug.logWarning(e.getMessage(), module);
        }
        if (rollups != null) 
        {
            for (GenericValue parent : rollups) 
            {
                GenericValue cv = null;
                Map<String, Object> cvMap = new HashMap<String, Object>();

                try {
                    cv = parent.getRelatedOne("CurrentProductCategory");
                } catch (GenericEntityException e) {
                    Debug.logWarning(e.getMessage(), module);
                }
                if (cv != null) {

                    if (excludeEmpty) 
                    {
                        if (!CategoryWorker.isCategoryEmpty(cv)) 
                        {
                            cvMap.put("ProductCategory", cv);
                            cvMap.put("ProductCategoryRollup", parent);
                            categories.add(cvMap);
                            if (recursive) 
                            {
                                categories.addAll(getRelatedCategories(delegator, cv.getString("productCategoryId"), categoryTrail, limitView, excludeEmpty, recursive));
                            }
                            List<String> popList = new LinkedList<String>();
                            popList.addAll(categoryTrail);
                            cvMap.put("categoryTrail", popList);
                            categoryTrail.remove(categoryTrail.size() - 1);
                        }
                    } 
                    else 
                    {
                        cvMap.put("ProductCategory", cv);
                        cvMap.put("ProductCategoryRollup", parent);
                        cvMap.put("parentProductCategoryId", parent.getString("parentProductCategoryId"));
                        categories.add(cvMap);
                        if (recursive) 
                        {
                            categories.addAll(getRelatedCategories(delegator, cv.getString("productCategoryId"), categoryTrail, limitView, excludeEmpty, recursive));
                        }
                        List<String> popList = new LinkedList<String>();
                        popList.addAll(categoryTrail);
                        cvMap.put("categoryTrail", popList);
                        categoryTrail.remove(categoryTrail.size() - 1);
                    }
                }
            }
        }
        return categories;
    }
    
    public static Map<String, Object> addProductVideoAndContent(DispatchContext dctx, Map<String, ? extends Object> context)
    throws IOException, JDOMException {

    LocalDispatcher dispatcher = dctx.getDispatcher();
    Delegator delegator = dctx.getDelegator();
    GenericValue userLogin = (GenericValue) context.get("userLogin");
    String productId = (String) context.get("productId");
    String productContentTypeId = (String) context.get("productContentTypeId");
    ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
    String fileName = (String)context.get("_uploadedFile_fileName");
    String imageFilePath = (String)context.get("imageFilePath");
    String imageUrlRef = (String)context.get("imageUrlRef");
    String imageResourceType = (String)context.get("imageResourceType");
        
    String imageUrl = "";
    String filenameToUse = "";
    if (imageResourceType.equals("file")) {
        String osafeThemeServerPath = FlexibleStringExpander.expandString(OSAFE_PROPS.getString("osafeThemeServer"), context);
        
        filenameToUse = StringUtil.replaceString(fileName, " ", "_");
        
        //String productImageFullPath = ProductAdminUtil.buildProductImagePathExt(productContentTypeId);
        
        File file = new File(osafeThemeServerPath + imageFilePath + filenameToUse);
        
        if (!new File(osafeThemeServerPath + imageFilePath).exists()) 
        {
        	new File(osafeThemeServerPath + imageFilePath).mkdirs();
	    }
        
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rw");
            out.write(imageData.array());
            out.close();
        } catch (FileNotFoundException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to open file for writing: " + file.getAbsolutePath());
        } catch (IOException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Unable to write binary data to: " + file.getAbsolutePath());
        }
        imageUrl = imageFilePath + filenameToUse;
    }
    else if(imageResourceType.equals("url"))
    {
    	if(UtilValidate.isNotEmpty(imageUrlRef))
    	{
    		imageUrl = imageUrlRef;
    		filenameToUse = imageUrlRef.substring(imageUrlRef.lastIndexOf("/")+1);
    	}
    }
        
        GenericValue productContent = null;
        String contentId = null;
        try {
            List productContentList = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId",productContentTypeId),null,false);
            if(UtilValidate.isNotEmpty(productContentList)) {
                productContentList = EntityUtil.filterByDate(productContentList);
                productContent = EntityUtil.getFirst(productContentList);
                contentId = (String) productContent.get("contentId");
            }
        } catch (GenericEntityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (UtilValidate.isNotEmpty(imageUrl) && imageUrl.length() > 0) {
            Map<String, Object> dataResourceCtx = new HashMap<String, Object>();
            dataResourceCtx.put("objectInfo", imageUrl);
            dataResourceCtx.put("dataResourceName", filenameToUse);
            dataResourceCtx.put("userLogin", userLogin);

            if (UtilValidate.isNotEmpty(contentId)) {
                GenericValue content = null;
                try {
                    content = delegator.findOne("Content", UtilMisc.toMap("contentId", contentId), false);
                } catch (GenericEntityException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                if (content != null) {
                    GenericValue dataResource = null;
                    try {
                        dataResource = content.getRelatedOne("DataResource");
                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError(e.getMessage());
                    }

                    if (dataResource != null) {
                        dataResourceCtx.put("dataResourceId", dataResource.getString("dataResourceId"));
                        try {
                            dispatcher.runSync("updateDataResource", dataResourceCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    } else {
                        dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
                        dataResourceCtx.put("mimeTypeId", "text/html");
                        Map<String, Object> dataResourceResult = new HashMap<String, Object>();
                        try {
                            dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }

                        Map<String, Object> contentCtx = new HashMap<String, Object>();
                        contentCtx.put("contentId", contentId);
                        contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
                        contentCtx.put("userLogin", userLogin);
                        try {
                            dispatcher.runSync("updateContent", contentCtx);
                        } catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }
                }
            } else {
                dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
                dataResourceCtx.put("mimeTypeId", "text/html");
                Map<String, Object> dataResourceResult = new HashMap<String, Object>();
                try {
                    dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }

                Map<String, Object> contentCtx = new HashMap<String, Object>();
                contentCtx.put("contentTypeId", "DOCUMENT");
                contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
                contentCtx.put("userLogin", userLogin);
                Map<String, Object> contentResult = new HashMap<String, Object>();
                try {
                    contentResult = dispatcher.runSync("createContent", contentCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }
                Map<String, Object> productContentCtx = new HashMap<String, Object>();
                productContentCtx.put("productId", productId);
                productContentCtx.put("productContentTypeId", productContentTypeId);
                productContentCtx.put("fromDate", (Timestamp) context.get("fromDate"));
                productContentCtx.put("thruDate", (Timestamp) context.get("thruDate"));
                productContentCtx.put("userLogin", userLogin);
                productContentCtx.put("contentId", contentResult.get("contentId"));
                try {
                    dispatcher.runSync("createProductContent", productContentCtx);
                } catch (GenericServiceException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.getMessage());
                }
            }
        }
    
       return ServiceUtil.returnSuccess();
}
   
    public static Map<String, Object> createProductFeatureAppl(DispatchContext dctx, Map<String, ? extends Object> context)
    {
        Delegator delegator = DelegatorFactory.getDelegator("default-no-eca");
        String productId = (String) context.get("productId");
        String productFeatureId = (String) context.get("productFeatureId");
        String productFeatureApplTypeId = (String) context.get("productFeatureApplTypeId");
        Long sequenceNum = (Long) context.get("sequenceNum");
        Timestamp fromDate = (Timestamp) context.get("fromDate");
        Timestamp thruDate = (Timestamp) context.get("thruDate");
        try 
        {
        	GenericValue productFeatureAppl = delegator.findOne("ProductFeatureAppl", UtilMisc.toMap("productId", productId, "productFeatureId", productFeatureId, "fromDate", fromDate),false);
        	if(UtilValidate.isEmpty(productFeatureAppl))
        	{
        		productFeatureAppl = delegator.makeValue("ProductFeatureAppl", UtilMisc.toMap("productId", productId, "productFeatureId", productFeatureId, "productFeatureApplTypeId", productFeatureApplTypeId, "fromDate", fromDate));
        		productFeatureAppl.put("sequenceNum", sequenceNum);
                productFeatureAppl.put("thruDate", thruDate);
                productFeatureAppl.create();
        	}
        	else
        	{
        		productFeatureAppl.put("sequenceNum", sequenceNum);
                productFeatureAppl.put("thruDate", thruDate);
                productFeatureAppl.store();
        	}
		} 
        catch (GenericEntityException e) 
        {
			Debug.logError(e, module);
		}
        return ServiceUtil.returnSuccess();
    }
   
    public static Map<String, Object> updateProductFeatureAppl(DispatchContext dctx, Map<String, ? extends Object> context)
    {
        Delegator delegator = DelegatorFactory.getDelegator("default-no-eca");
        String productId = (String) context.get("productId");
        String productFeatureId = (String) context.get("productFeatureId");
        Long sequenceNum = (Long) context.get("sequenceNum");
        Timestamp fromDate = (Timestamp) context.get("fromDate");
        Timestamp thruDate = (Timestamp) context.get("thruDate");
        try {
        	List<GenericValue> productFeatureAppls = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productFeatureId", productFeatureId, "productId", productId, "fromDate", fromDate),null,false);
        	if(UtilValidate.isNotEmpty(productFeatureAppls)) {
        	    for(GenericValue productFeatureAppl : productFeatureAppls) {
			        productFeatureAppl.set("sequenceNum", sequenceNum);
			        productFeatureAppl.set("thruDate", thruDate);
			        delegator.store(productFeatureAppl);
        	    }
        	}
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
		}
        return ServiceUtil.returnSuccess();
    }   
   
    public static Map<String, Object> updateProductFeatureAppls(DispatchContext dctx, Map<String, ? extends Object> context)
    {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Map<String, Object> resp = null;
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productFeatureId = (String) context.get("productFeatureId");
        Long sequenceNum = (Long) context.get("sequenceNum");
        try {
        	List<GenericValue> productFeatureAppls = delegator.findByAnd("ProductFeatureAppl", UtilMisc.toMap("productFeatureId", productFeatureId),null,false);
        	if(UtilValidate.isNotEmpty(productFeatureAppls)) {
        	    for(GenericValue productFeatureAppl : productFeatureAppls) {
                    Map updateProductFeatureApplParams = UtilMisc.toMap("productId", productFeatureAppl.getString("productId"),
                            "productFeatureId",productFeatureAppl.getString("productFeatureId"),
                            "fromDate",productFeatureAppl.getTimestamp("fromDate"),
                            "sequenceNum", sequenceNum,
                            "userLogin", userLogin);
                   try {
                   Map result = dispatcher.runSync("updateProductFeatureAppl", updateProductFeatureApplParams);
                   } catch(GenericServiceException e){
                       Debug.logError(e, module);
                   }
        	    }
        	}
		} catch (GenericEntityException e) {
			Debug.logError(e, module);
            resp = ServiceUtil.returnError(e.getMessage());
		}
		if (resp == null) resp = ServiceUtil.returnSuccess();
        return resp;
    }
    
    public static Map<String, Object> addPartyImageAndContent(DispatchContext dctx, Map<String, ? extends Object> context)
    	    throws IOException, JDOMException {

    	    LocalDispatcher dispatcher = dctx.getDispatcher();
    	    Delegator delegator = dctx.getDelegator();
    	    GenericValue userLogin = (GenericValue) context.get("userLogin");
    	    String partyId = (String) context.get("partyId");
    	    String partyContentTypeId = (String) context.get("partyContentTypeId");
    	    ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
    	    String fileName = (String)context.get("_uploadedFile_fileName");
    	    String imageFilePath = (String)context.get("imageFilePath");
    	    String imageUrlRef = (String)context.get("imageUrlRef");
    	    String imageResourceType = (String)context.get("imageResourceType");
    	    
    	    String imageUrl = "";
    	    String filenameToUse = "";
    	    
    	    if (imageResourceType.equals("file")) 
    	    {
    	        String osafeThemeServerPath = FlexibleStringExpander.expandString(OSAFE_PROPS.getString("osafeThemeServer"), context);
    	        int extensionIndex = fileName.lastIndexOf(".");
    	        if (extensionIndex == -1) {
    	        	filenameToUse = fileName;
    	        } else {
    	        	filenameToUse = fileName.substring(0, extensionIndex);
    	        }
    	        filenameToUse = StringUtil.replaceString(filenameToUse, " ", "_");
    	        
    	        List<GenericValue> fileExtension = new LinkedList<GenericValue>();
    	        try {
    	            fileExtension = delegator.findByAnd("FileExtension", UtilMisc.toMap("mimeTypeId", (String) context.get("_uploadedFile_contentType")),null,false);
    	        } catch (GenericEntityException e) {
    	            Debug.logError(e, module);
    	            return ServiceUtil.returnError(e.getMessage());
    	        }

    	        GenericValue extension = EntityUtil.getFirst(fileExtension);
    	        if (extension != null) {
    	            filenameToUse += "." + extension.getString("fileExtensionId");
    	        }

    	        File file = new File(osafeThemeServerPath + imageFilePath + filenameToUse);
    	        
    	        if (!new File(osafeThemeServerPath + imageFilePath).exists()) {
    	        	new File(osafeThemeServerPath + imageFilePath).mkdirs();
    		    }
    	        
    	        try {
    	            RandomAccessFile out = new RandomAccessFile(file, "rw");
    	            out.write(imageData.array());
    	            out.close();
    	        } catch (FileNotFoundException e) {
    	            Debug.logError(e, module);
    	            return ServiceUtil.returnError("Unable to open file for writing: " + file.getAbsolutePath());
    	        } catch (IOException e) {
    	            Debug.logError(e, module);
    	            return ServiceUtil.returnError("Unable to write binary data to: " + file.getAbsolutePath());
    	        }

    	        imageUrl = imageFilePath + filenameToUse;
    	    }
    	    else if(imageResourceType.equals("url"))
    	    {
    	    	if(UtilValidate.isNotEmpty(imageUrlRef))
    	    	{
    	    		imageUrl = imageUrlRef;
    	    		filenameToUse = imageUrlRef.substring(imageUrlRef.lastIndexOf("/")+1);
    	    	}
    	    }
    	        GenericValue partyContent = null;
    	        String contentId = null;
    	        try {
    	            List partyContentList = delegator.findByAnd("PartyContent", UtilMisc.toMap("partyId", partyId, "partyContentTypeId",partyContentTypeId),null,false);
    	            if(UtilValidate.isNotEmpty(partyContentList)) {
    	            	partyContentList = EntityUtil.filterByDate(partyContentList);
    	                partyContent = EntityUtil.getFirst(partyContentList);
    	                contentId = (String) partyContent.get("contentId");
    	            }
    	        } catch (GenericEntityException e1) {
    	            // TODO Auto-generated catch block
    	            e1.printStackTrace();
    	        }
    	        if (UtilValidate.isNotEmpty(imageUrl) && imageUrl.length() > 0) {
    	            Map<String, Object> dataResourceCtx = new HashMap<String, Object>();
    	            dataResourceCtx.put("objectInfo", imageUrl);
    	            dataResourceCtx.put("dataResourceName", filenameToUse);
    	            dataResourceCtx.put("userLogin", userLogin);

    	            if (UtilValidate.isNotEmpty(contentId)) {
    	                GenericValue content = null;
    	                try {
    	                    content = delegator.findOne("Content", UtilMisc.toMap("contentId", contentId), false);
    	                } catch (GenericEntityException e) {
    	                    Debug.logError(e, module);
    	                    return ServiceUtil.returnError(e.getMessage());
    	                }

    	                if (content != null) {
    	                    GenericValue dataResource = null;
    	                    try {
    	                        dataResource = content.getRelatedOne("DataResource");
    	                    } catch (GenericEntityException e) {
    	                        Debug.logError(e, module);
    	                        return ServiceUtil.returnError(e.getMessage());
    	                    }

    	                    if (dataResource != null) {
    	                        dataResourceCtx.put("dataResourceId", dataResource.getString("dataResourceId"));
    	                        try {
    	                            dispatcher.runSync("updateDataResource", dataResourceCtx);
    	                        } catch (GenericServiceException e) {
    	                            Debug.logError(e, module);
    	                            return ServiceUtil.returnError(e.getMessage());
    	                        }
    	                    } else {
    	                        dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
    	                        dataResourceCtx.put("mimeTypeId", "text/html");
    	                        Map<String, Object> dataResourceResult = new HashMap<String, Object>();
    	                        try {
    	                            dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
    	                        } catch (GenericServiceException e) {
    	                            Debug.logError(e, module);
    	                            return ServiceUtil.returnError(e.getMessage());
    	                        }

    	                        Map<String, Object> contentCtx = new HashMap<String, Object>();
    	                        contentCtx.put("contentId", contentId);
    	                        contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
    	                        contentCtx.put("userLogin", userLogin);
    	                        try {
    	                            dispatcher.runSync("updateContent", contentCtx);
    	                        } catch (GenericServiceException e) {
    	                            Debug.logError(e, module);
    	                            return ServiceUtil.returnError(e.getMessage());
    	                        }
    	                    }
    	                }
    	            } else {
    	                dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
    	                dataResourceCtx.put("mimeTypeId", "text/html");
    	                Map<String, Object> dataResourceResult = new HashMap<String, Object>();
    	                try {
    	                    dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
    	                } catch (GenericServiceException e) {
    	                    Debug.logError(e, module);
    	                    return ServiceUtil.returnError(e.getMessage());
    	                }

    	                Map<String, Object> contentCtx = new HashMap<String, Object>();
    	                contentCtx.put("contentTypeId", "DOCUMENT");
    	                contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
    	                contentCtx.put("userLogin", userLogin);
    	                Map<String, Object> contentResult = new HashMap<String, Object>();
    	                try {
    	                    contentResult = dispatcher.runSync("createContent", contentCtx);
    	                } catch (GenericServiceException e) {
    	                    Debug.logError(e, module);
    	                    return ServiceUtil.returnError(e.getMessage());
    	                }
    	                Map<String, Object> partyContentCtx = new HashMap<String, Object>();
    	                partyContentCtx.put("partyId", partyId);
    	                partyContentCtx.put("partyContentTypeId", partyContentTypeId);
    	                partyContentCtx.put("fromDate", (Timestamp) context.get("fromDate"));
    	                partyContentCtx.put("thruDate", (Timestamp) context.get("thruDate"));
    	                partyContentCtx.put("userLogin", userLogin);
    	                partyContentCtx.put("contentId", contentResult.get("contentId"));
    	                try {
    	                    dispatcher.runSync("createPartyContent", partyContentCtx);
    	                } catch (GenericServiceException e) {
    	                    Debug.logError(e, module);
    	                    return ServiceUtil.returnError(e.getMessage());
    	                }
    	            }
    	        }
    	    
    	       return ServiceUtil.returnSuccess();
    	}
     
}
