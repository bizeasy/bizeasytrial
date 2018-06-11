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
package org.apache.ofbiz.orderfulfillment.inventory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityTypeUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import com.ibm.icu.util.Calendar;

/**
 * Inventory Services
 */
public class InventoryServices {

    public final static String module = InventoryServices.class.getName();
    public static final String resource = "ProductUiLabels";
    public static final MathContext generalRounding = new MathContext(10);

    public static Map<String, Object> receiveInventoryToScrap(DispatchContext dctx, Map<String, ? extends Object> context) {
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        List<GenericValue> orderItems = UtilGenerics.checkList(context.get("orderItems"));
        String facilityId = (String) context.get("facilityId");
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Map<String, Object> results = ServiceUtil.returnSuccess();
        
        
        Debug.log("context =================="+context);
        
        String scrapLocationSeqId = (String) context.get("scrapLocationSeqId");
        String orderId = (String) context.get("orderId");
        BigDecimal quantityRejected = (BigDecimal) context.get("quantityRejected");
        String orderItemSeqId = (String) context.get("orderItemSeqId");
        if(UtilValidate.isEmpty(scrapLocationSeqId)){
        	return results;
        }
        
        try {
            Map<String, Object> serviceContext = UtilMisc.<String, Object>toMap("productId", (String)context.get("productId"),
                    "inventoryItemTypeId", "NON_SERIAL_INV_ITEM");
            serviceContext.put("facilityId", (String)context.get("facilityId"));
            serviceContext.put("datetimeReceived", UtilDateTime.nowTimestamp());
            serviceContext.put("datetimeManufactured", UtilDateTime.nowTimestamp());
            serviceContext.put("comments", "Rejected qty to Scrap Location ");
            serviceContext.put("locationSeqId", scrapLocationSeqId);
            
            serviceContext.put("userLogin", userLogin);
            Map<String, Object> resultService = dispatcher.runSync("createInventoryItem", serviceContext);
            String inventoryItemId = (String)resultService.get("inventoryItemId");
            serviceContext.clear();
            serviceContext.put("inventoryItemId", inventoryItemId);
            serviceContext.put("availableToPromiseDiff", quantityRejected);
            serviceContext.put("quantityOnHandDiff", quantityRejected);
            serviceContext.put("orderId", orderId);
            serviceContext.put("userLogin", userLogin);
            resultService = dispatcher.runSync("createInventoryItemDetail", serviceContext);
            // Recompute reservations
            
            
            serviceContext = new HashMap<String, Object>();
            serviceContext.put("inventoryItemId", inventoryItemId);
            serviceContext.put("productId", (String)context.get("productId"));
            serviceContext.put("orderId", orderId);
            serviceContext.put("orderItemSeqId", orderItemSeqId);
            serviceContext.put("quantityAccepted", quantityRejected);
            serviceContext.put("quantityRejected", BigDecimal.ZERO);
            serviceContext.put("userLogin", userLogin);
            resultService = dispatcher.runSync("createShipmentReceipt", serviceContext);
        } catch (GenericServiceException exc) {
            return ServiceUtil.returnError(exc.getMessage());
        } catch (Exception exc) {
            return ServiceUtil.returnError(exc.getMessage());
        }
        
        
        return results;
    }


    

}
