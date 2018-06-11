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


import org.apache.ofbiz.accounting.invoice.InvoiceWorker
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilNumber
import org.apache.ofbiz.base.util.Debug
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;

import java.text.DateFormat

invoiceId = parameters.get("invoiceId")

invoice = from('Invoice').where('invoiceId', invoiceId).queryOne()
context.invoice = invoice

currency = parameters.currency // allow the display of the invoice in the original currency, the default is to display the invoice in the default currency
BigDecimal conversionRate = new BigDecimal("1")
decimals = UtilNumber.getBigDecimalScale("invoice.decimals")
rounding = UtilNumber.getBigDecimalRoundingMode("invoice.rounding")

if (invoice) {
    // each invoice of course has two billing addresses, but the one that is relevant for purchase invoices is the PAYMENT_LOCATION of the invoice
    // (ie Accounts Payable address for the supplier), while the right one for sales invoices is the BILLING_LOCATION (ie Accounts Receivable or
    // home of the customer.)
    if ("PURCHASE_INVOICE".equals(invoice.invoiceTypeId)) {
        billingAddress = InvoiceWorker.getSendFromAddress(invoice)
    } else {
        billingAddress = InvoiceWorker.getBillToAddress(invoice)
    }
    if (billingAddress) {
        context.billingAddress = billingAddress
    }
    billToParty = InvoiceWorker.getBillToParty(invoice)
    context.billToParty = billToParty
    sendingParty = InvoiceWorker.getSendFromParty(invoice)
    context.sendingParty = sendingParty

    if (currency && !invoice.getString("currencyUomId").equals(currency)) {
        conversionRate = InvoiceWorker.getInvoiceCurrencyConversionRate(invoice)
        invoice.currencyUomId = currency
        invoice.invoiceMessage = " converted from original with a rate of: " + conversionRate.setScale(8, rounding)
    }

    invoiceItems = invoice.getRelated("InvoiceItem", null, ["invoiceItemSeqId"], false)
    
	taxInvoiceItems = EntityUtil.filterByOr(
		invoiceItems, UtilMisc.toList(
				EntityCondition.makeCondition("invoiceItemTypeId", EntityOperator.EQUALS, "IGST"),
				EntityCondition.makeCondition("invoiceItemTypeId", EntityOperator.EQUALS, "CGST"),
				EntityCondition.makeCondition("invoiceItemTypeId", EntityOperator.EQUALS, "SGST")));
			
	lineItems = EntityUtil.filterByOr(
		invoiceItems, UtilMisc.toList(
				EntityCondition.makeCondition("invoiceItemTypeId", EntityOperator.EQUALS, "INV_FPROD_ITEM")));		
			
	displayInvItemList = [];
	count = 0;
	lineItems.each { eachItem ->
		invoiceItemMap = [:];
		itemPrice = eachItem.getBigDecimal("amount").multiply(conversionRate).setScale(decimals, rounding)
		product = from("Product").where("productId", eachItem.productId).queryOne()
		hsnGoodsIdentification = from("GoodIdentification").where("productId", eachItem.productId, "goodIdentificationTypeId", "HS_CODE").queryOne()
		
		
		count = count+1;
		invoiceItemMap.put("sNo", count);
		invoiceItemMap.put("itemSeqId", eachItem.invoiceItemSeqId);
		invoiceItemMap.put("productId", eachItem.productId);
		invoiceItemMap.put("productName", product.productName);
		invoiceItemMap.put("quantity", eachItem.quantity);
		invoiceItemMap.put("itemPrice", itemPrice);
		invoiceItemMap.put("itemAmount", itemPrice*eachItem.quantity);
		
		hsnCode = "";
		if(hsnGoodsIdentification){
		 hsnCode = hsnGoodsIdentification.idValue;
		}
		invoiceItemMap.put("hsnCode", hsnCode);
		
		invoiceItemTaxes = EntityUtil.filterByOr(
			taxInvoiceItems, UtilMisc.toList(
					EntityCondition.makeCondition("parentInvoiceItemSeqId", EntityOperator.EQUALS, eachItem.invoiceItemSeqId)
					));
		taxTotal = 0
		if(invoiceItemTaxes){
			
			invoiceItemTaxes.each { eachTaxItem ->
				
				taxTotal = taxTotal + eachTaxItem.amount
				invoiceItemMap.put(eachTaxItem.invoiceItemTypeId, eachTaxItem);
				taxRate = eachTaxItem.getRelatedOne("TaxAuthorityRateProduct", false)
				invoiceItemMap.put(eachTaxItem.invoiceItemTypeId+"_rateProduct", taxRate);
			}
			
			
		}		
		
		//taxTotal = InvoiceWorker.getInvoiceTotal(invoice);
				
		invoiceItemMap.put("itemTotal", itemPrice*eachItem.quantity + taxTotal);
		
		tempInvItemMap = [:];
		tempInvItemMap.putAll(invoiceItemMap);
		
		displayInvItemList.add(tempInvItemMap);
		
	}
			
	
	
	/*invoiceItemsConv = []
    vatTaxesByType = [:]
    invoiceItems.each { invoiceItem ->
        invoiceItem.amount = invoiceItem.getBigDecimal("amount").multiply(conversionRate).setScale(decimals, rounding)
        invoiceItemsConv.add(invoiceItem)
        // get party tax id for VAT taxes: they are required in invoices by EU
        // also create a map with tax grand total amount by VAT tax: it is also required in invoices by UE
        taxRate = invoiceItem.getRelatedOne("TaxAuthorityRateProduct", false)
        if (taxRate && "VAT_TAX".equals(taxRate.taxAuthorityRateTypeId)) {
            taxInfo = from("PartyTaxAuthInfo")
                .where('partyId', billToParty.partyId, 'taxAuthGeoId', taxRate.taxAuthGeoId, 'taxAuthPartyId', taxRate.taxAuthPartyId)
                .filterByDate(invoice.invoiceDate)
                .queryFirst()
            if (taxInfo) {
                context.billToPartyTaxId = taxInfo.partyTaxId
            }
            taxInfo = from("PartyTaxAuthInfo")
                .where('partyId', sendingParty.partyId, 'taxAuthGeoId', taxRate.taxAuthGeoId, 'taxAuthPartyId', taxRate.taxAuthPartyId)
                .filterByDate(invoice.invoiceDate)
                .queryFirst()
            if (taxInfo) {
                context.sendingPartyTaxId = taxInfo.partyTaxId
            }
            vatTaxesByTypeAmount = vatTaxesByType[taxRate.taxAuthorityRateSeqId]
            if (!vatTaxesByTypeAmount) {
                vatTaxesByTypeAmount = 0.0
            }
            vatTaxesByType.put(taxRate.taxAuthorityRateSeqId, vatTaxesByTypeAmount + invoiceItem.amount)
        }
    }
    context.vatTaxesByType = vatTaxesByType
    context.vatTaxIds = vatTaxesByType.keySet().asList()

    context.invoiceItems = invoiceItemsConv*/

    invoiceTotal = InvoiceWorker.getInvoiceTotal(invoice).multiply(conversionRate).setScale(decimals, rounding)
    invoiceNoTaxTotal = InvoiceWorker.getInvoiceNoTaxTotal(invoice).multiply(conversionRate).setScale(decimals, rounding)
    context.invoiceTotal = invoiceTotal
    context.invoiceNoTaxTotal = invoiceNoTaxTotal

                //*________________this snippet was added for adding Tax ID in invoice header if needed _________________

               sendingTaxInfos = sendingParty.getRelated("PartyTaxAuthInfo", null, null, false)
               billingTaxInfos = billToParty.getRelated("PartyTaxAuthInfo", null, null, false)
               sendingPartyTaxId = null
               billToPartyTaxId = null

               if (billingAddress) {
                   sendingTaxInfos.eachWithIndex { sendingTaxInfo, i ->
                       if (sendingTaxInfo.taxAuthGeoId.equals(billingAddress.countryGeoId)) {
                            sendingPartyTaxId = sendingTaxInfos[i-1].partyTaxId
                       }
                   }
                   billingTaxInfos.eachWithIndex { billingTaxInfo, i ->
                       if (billingTaxInfo.taxAuthGeoId.equals(billingAddress.countryGeoId)) {
                            billToPartyTaxId = billingTaxInfos[i-1].partyTaxId
                       }
                   }
               }
               if (sendingPartyTaxId) {
                   context.sendingPartyTaxId = sendingPartyTaxId
               }
               if (billToPartyTaxId && !context.billToPartyTaxId) {
                   context.billToPartyTaxId = billToPartyTaxId
               }
               //________________this snippet was added for adding Tax ID in invoice header if needed _________________*/


    terms = invoice.getRelated("InvoiceTerm", null, null, false)
    context.terms = terms

    paymentAppls = from("PaymentApplication").where('invoiceId', invoiceId).queryList()
    context.payments = paymentAppls

    orderItemBillings = from("OrderItemBilling").where('invoiceId', invoiceId).orderBy('orderId').queryList()
    orders = new LinkedHashSet()
    orderItemBillings.each { orderIb ->
        orders.add(orderIb.orderId)
    }
    context.orders = orders
	
	Debug.log("orders ==================="+orders[0]);
	context.orderId = orders[0]
	
	orderHeader = from("OrderHeader").where('orderId', orders[0]).orderBy('orderId').queryList()
	Debug.log("orderHeader ==========="+orderHeader);
	context.orderHeader = orderHeader[0]
	context.externalOrderId = orderHeader[0].externalId;
	
	serviceItems = [];
	orderItems = orderHeader[0].getRelated("OrderItem", null, null, false);
	for(i=0; i<orderItems.size(); i++){
		orderItem = orderItems[i];
		Debug.log("orderItem ================="+orderItem);
		if(orderItem.unitPrice > 0){
			continue;
		}
		
		prodId = orderItem.productId;
		product = orderItem.getRelatedOne("Product", false)
		Debug.log("product ================="+product);
		productType = product.productTypeId;
		
		if(productType == "SERVICE"){
			serviceItems.add(orderItem);
			
			count = count+1;
			invoiceItemMap = [:];
			invoiceItemMap.put("sNo", count);
			invoiceItemMap.put("itemSeqId", orderItem.orderItemSeqId);
			invoiceItemMap.put("productId", orderItem.productId);
			invoiceItemMap.put("productName", product.productName);
			invoiceItemMap.put("quantity", orderItem.quantity);
			invoiceItemMap.put("itemPrice", orderItem.unitPrice);
			invoiceItemMap.put("itemAmount", orderItem.unitPrice*orderItem.quantity);
			invoiceItemMap.put("itemTotal", orderItem.unitPrice*orderItem.quantity);
			tempInvItemMap = [:];
			tempInvItemMap.putAll(invoiceItemMap);
			
			displayInvItemList.add(tempInvItemMap);
		}
		
	}
	Debug.log("serviceItems ================="+serviceItems);
	
	Debug.log("displayInvItemList ==================="+displayInvItemList);
	context.displayInvItemList =displayInvItemList;
	
	context.serviceItems = serviceItems;
    invoiceStatus = invoice.getRelatedOne("StatusItem", false)
    context.invoiceStatus = invoiceStatus

    edit = parameters.editInvoice
    if ("true".equalsIgnoreCase(edit)) {
        invoiceItemTypes = from("InvoiceItemType").queryList()
        context.invoiceItemTypes = invoiceItemTypes
        context.editInvoice = true
    }

    // format the date
    if (invoice.invoiceDate) {
        invoiceDate = DateFormat.getDateInstance(DateFormat.LONG).format(invoice.invoiceDate)
        context.invoiceDate = invoiceDate
    } else {
        context.invoiceDate = "N/A"
    }
}
