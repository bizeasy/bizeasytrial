/*
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
 */

import org.apache.ofbiz.base.util.UtilProperties
import org.apache.ofbiz.entity.util.EntityUtil
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.order.order.OrderReadHelper
import org.apache.ofbiz.shipment.verify.VerifyPickSession
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.base.util.Debug;

picklistId = parameters.picklistId

pickedConditions = EntityCondition.makeCondition(UtilMisc.toList(
        EntityCondition.makeCondition("picklistId", EntityOperator.EQUALS, picklistId),
        EntityCondition.makeCondition("itemStatusId", EntityOperator.EQUALS, "PICKITEM_PENDING")),
        EntityOperator.AND);

pickListItems = delegator.findList("PicklistAndBinAndItemAndLocation", pickedConditions, null, ["-locationSeqId", "primaryOrderId"], null, false);

orderIdsList = EntityUtil.getFieldListFromEntityList(pickListItems, "orderId", true);

pickList = [];
orderWisePickList = [:];
for(int i=0; i<orderIdsList.size(); i++){
	orderId = orderIdsList.get(i);
	orderIdWisePickList = EntityUtil.filterByCondition(pickListItems, EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
	//itemSize = orderIdWisePickList.size();
	
	itemSize = 0; 
	for(int j=0; j<orderIdWisePickList.size(); j++){
		orderItem = orderIdWisePickList.get(j);
		quantity = orderItem.get("quantity");
		itemSize = itemSize + quantity;
	}
	
	int itemCount = 0;
	for(int j=0; j<orderIdWisePickList.size(); j++){
		orderItem = orderIdWisePickList.get(j);
		
		
		quantity = orderItem.get("quantity");
		
		for(int k=0; k<quantity; k++){
			itemCount = itemCount + 1;
			orderWisePickList.put("orderId", orderId);
			orderWisePickList.put("itemCount", itemCount);
			orderWisePickList.put("orderItemSize", itemSize);
			orderWisePickList.put("productId", orderItem.productId);
			orderWisePickList.put("location", orderItem.locationSeqId);
			
			tempMap = [:];
			tempMap.putAll(orderWisePickList);
			pickList.add(tempMap);
		}
		
	}
	
	
	
}

context.pickList = pickList;

