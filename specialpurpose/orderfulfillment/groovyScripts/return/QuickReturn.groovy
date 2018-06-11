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

import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.order.order.*
import org.apache.ofbiz.party.contact.*
import org.apache.ofbiz.product.store.*

orderId = parameters.orderId
context.orderId = orderId

returnHeaderTypeId = "CUSTOMER_RETURN";
context.returnHeaderTypeId = returnHeaderTypeId

//partyId = parameters.party_id
partyId = null;
party = null;


if (orderId) {
	returnRes = runService('getReturnableItems', [orderId : orderId])
	context.returnableItems = returnRes.returnableItems
	orderHeader = from("OrderHeader").where("orderId", orderId).queryOne()
	context.orderHeader = orderHeader
	
	orh = new OrderReadHelper(orderHeader)
	context.orh = orh
	context.orderHeaderAdjustments = orh.getAvailableOrderHeaderAdjustments()
	
	party = orh.getBillToParty();
	context.party = party
	
	partyId = party.get("partyId");
	
	productStore = orderHeader.getRelatedOne("ProductStore", false)
	if (productStore) {
		if (("VENDOR_RETURN").equals(returnHeaderTypeId)) {
			context.partyId = productStore.payToPartyId
		} else {
			context.destinationFacilityId = ProductStoreWorker.determineSingleFacilityForStore(delegator, productStore.productStoreId)
			context.toPartyId = productStore.payToPartyId
			context.partyId = partyId
		}
	}
	
}

returnHeaders = from("ReturnHeader").where("statusId", "RETURN_REQUESTED").queryList()
context.returnHeaders = returnHeaders


// payment method info
if (partyId) {
    creditCardList = from("PaymentMethodAndCreditCard").where("partyId", partyId).filterByDate().queryList()
    if (creditCardList) {
        context.creditCardList = creditCardList
    }
    eftAccountList = from("PaymentMethodAndEftAccount").where("partyId", partyId).filterByDate().queryList()
    if (eftAccountList) {
        context.eftAccountList = eftAccountList
    }
}


returnTypes = from("ReturnType").orderBy("sequenceId").queryList()
context.returnTypes = returnTypes

returnReasons = from("ReturnReason").orderBy("sequenceId").queryList()
context.returnReasons = returnReasons

itemStts = from("StatusItem").where("statusTypeId", "INV_SERIALIZED_STTS").orderBy("sequenceId").queryList()
context.itemStts = itemStts

typeMap = [:]
returnItemTypeMap = from("ReturnItemTypeMap").where("returnHeaderTypeId", returnHeaderTypeId).queryList()
returnItemTypeMap.each { value ->
    typeMap[value.returnItemMapKey] = value.returnItemTypeId
}
context.returnItemTypeMap = typeMap



context.shippingContactMechList = ContactHelper.getContactMech(party, "SHIPPING_LOCATION", "POSTAL_ADDRESS", false)
