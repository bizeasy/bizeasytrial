<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->
<#-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> 
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>-->
 
<div class="screenlet">
  <div class="screenlet-title-bar">
    <ul>
      <li class="h3">Order Entry</li>
    </ul>
    <br class="clear"/>
  </div>
  <div class="screenlet-body">
<#assign stateAssocs = Static["org.apache.ofbiz.common.CommonWorkers"].getAssociatedStateList(delegator,"IND")>
<form action="<@ofbizUrl>CreateOrder</@ofbizUrl>" name="OrderInputForm" method="post" class="basic-form">
  
	<table class="basic-table" cellspacing='0'>
  		<#--<tr>
  			<td class="label">Product Store:</td>
  			<td width='10%'>
		          ><#if currentStore?has_content>
		            <div><#if currentStore.storeName??>${currentStore.storeName}<#else>${currentStore.productStoreId}</#if></div>
		            <input type="hidden" name="productStoreId" value="${currentStore.productStoreId}" />
		         <#else>
		            <select name="productStoreId">
		              <#list productStoreGroupMembers as productStore>
		                <option value="${productStore.productStoreId}">${productStore.productStoreId}</option>
		                
		              </#list>
		            </select>
		         </#if>
		         
	        </td>
	        <td class="label">Origin Facility:</td>
  			<td width='10%'>
		        <select name="facilityId">
	              <#list facilitiesList as facility>
	                <option value="${facility.facilityId}">${facility.facilityName}</option>
	              </#list>
	            </select>
	        </td>
  		</tr>-->
  		<input type="hidden" name="productStoreId" id="productStoreId" value="${globalContext.productStoreId}"/>
		         <input type="hidden" name="facilityId" id="facilityId" value="${globalContext.facilityId}"/>
		<tr>
	    	<td class="label">Customer Name:</td>
    		<td ><input type="text" name="customerName" size="30" maxlength="60" required/></td>
    		<td class="label">Reference No:</td>
    		<td ><input type="text" name="referenceNo" id="referenceNo" size="10" maxlength="10" required/></td>
		</tr>
		<tr>
	    	<td class="label">Email Id:</td>
    		<td ><input type="text" name="emailId" size="30" maxlength="60" required/></td>
    		<td class="label">Phone No:</td>
    		<td ><input type="text" name="phoneNo" size="30" maxlength="60" required/></td>
		</tr>
  		
		<tr>
	    	<td class="label">Address Line1:</td>
    		<td ><input type="text" name="address1" id="address1" size="30" maxlength="60" required/></td>
    		<td class="label">Address Line2:</td>
    		<td ><input type="text" name="address2" id="address2" size="30" maxlength="60" required/></td>
		</tr>
		
		
		
		<tr>
	    	<td class="label">City:</td>
    		<td ><input type="text" name="city" id="city" size="20" maxlength="20" required/></td>
    		<td class="label">Postal Code:</td>
    		<td ><input type="text" name="postalCode" id="postalCode" size="10" maxlength="10" required/></td>
		</tr>
				
		<tr>
	    	<td class="label">State:</td>
	    	<td >
    		<select name="stateProvinceGeoId" id="stateProvinceGeoId" required>
	    		<#list stateAssocs as stateAssoc>
				    <option value='${stateAssoc.geoId}'>${stateAssoc.geoName}</option>
				</#list>
			</select>
			<input type="hidden" name="countryGeoId" id="countryGeoId" value="IND"/>
			</td>
    		
		</tr>
		<tr>
	    	<td class="label">Payment Method:</td>
	    	<td >
    		<select name="paymentMethod" id="paymentMethod">
	    		 <option value='EXT_COD'>Cash On Delivery</option>
	    		 <option value='EXT_PREPAID'>Prepaid</option>
			</select>
			</td>
    		
		</tr>
    	
	</table>
	<hr>
	<table class="form-table" id="orderItems">
		 <thead>
	        <tr>
	         <th>Product Id</th>
	         <th>Quantity</th>
	         <#--
	         <th>Price</th>
	         <th>Tax Type</th>
	         <th>Tax Rate</th>
	         <th>Total</th>
	         -->
	         <th></th>
	        </tr>
	     </thead>
		 <tbody>  
	    	<tr>
		        <td><@htmlTemplate.lookupField id="productId_o_0" value="${requestParameters.productId!}" formName="OrderInputForm" name="productId_o_0" id="OrderInputForm" fieldFormName="LookupProduct"/></td>
		        <#-- <td><input id="productId_o_0" class="input-medium" name="productId_o_0" type="text"/></td>-->
		        <td><input id="quantity_o_0" class="input-medium" name="quantity_o_0" type="number" size='10'/></td>
		        <#--
		        <td><input id="price_o_0" class="input-medium" name="price_o_0" type="number" size='5'/></td>
		        <td>
					<select name="taxType" id="taxType">
				        <option value="Inter-State">Inter-State</option>
				        <option value="Intra-State">Intra-State</option>
				    </select>
				</td>
				<td><input id="taxRate_o_0" class="input-medium" name="taxRate_o_0" type="number" size='10'/></td>
				<td><input id="taxAmt_o_0" class="input-medium" name="taxAmt_o_0" type="number" size='10'/></td>
				<td><input id="total_o_0" class="input-medium" name="total_o_0" type="number" size='5'/></td>
		        -->
		        <td><a id="addnew" href="" >add</a></td>
		    </tr>
		</tbody>
	</table>
	<hr>
	<br>
	<input type="submit" name="Update" value="Submit" />
	
</form>
</div>
</div>


<script>
	$(document).ready(function(){
	// bind a click event to the "addnew" link
		$('#addnew').click(function() {
			
					
		    // increment the counter
		    newRowNum = $(orderItems).children('tbody').children('tr').length;
		    var addRow = $(this).parent().parent();
		
		    var newRow = addRow.clone();
		    $('input', addRow).val('');
		
			$('td:last-child', newRow).html('<a href="" class="remove"><i class="icon-minus">Remove<\/i><\/a>');
		
		    addRow.before(newRow);
		
		    // add the remove function to the new row
		    $('a.remove', newRow).click(function() {
		        $(this).closest('tr').remove();
		        return false;
		    });
		
		    $('#0_lookupId_OrderInputForm', newRow).each(function(i) {
		        $(this).attr('id', newRowNum + '_lookupId_OrderInputForm');
		        $(this).attr('name', 'productId_o_' + newRowNum);
		        
		        var descSpan = $('#0_lookupId_OrderInputForm_lookupDescription');
		        descSpan.attr('id', newRowNum + '_lookupId_OrderInputForm_lookupDescription');
		        
		        $('#0_lookupId_OrderInputForm_lookupDescription').html('');
		    });
		    
		    $('#quantity_o_0', newRow).each(function(i) {
		        var newID = 'quantity_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    
		    <#--
		    $('#price_o_0', newRow).each(function(i) {
		        var newID = 'price_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    
		    $('#taxType_o_0', newRow).each(function(i) {
		        var newID = 'taxType_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    
		    $('#taxRate_o_0', newRow).each(function(i) {
		        var newID = 'taxRate_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    
		    $('#taxAmt_o_0', newRow).each(function(i) {
		        var newID = 'taxAmt_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    
		    $('#total_o_0', newRow).each(function(i) {
		        var newID = 'total_o_' + newRowNum;
		        $(this).attr('id', newID);
		        $(this).attr('name', newID);
		    });
		    -->
		
		    return false;
		});
	});
</script>

