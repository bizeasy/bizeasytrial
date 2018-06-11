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

<#assign stateAssocs = Static["org.apache.ofbiz.common.CommonWorkers"].getAssociatedStateList(delegator,"IND")>
<form action="<@ofbizUrl>CreateAirwayBills</@ofbizUrl>" name="OrderInputForm" method="post" class="basic-form">
  
	<table class="basic-table" cellspacing='0'>
		<tr>
	    	<td class="label">Carrier:</td>
    		<td ><input type="text" name="carrierPartyId" size="30" maxlength="60"/></td>
		</tr>
		<tr>
	    	<td class="label">Shipment Method:</td>
    		<td ><input type="text" name="shipmentMethodTypeId" size="30" maxlength="60"/></td>
		</tr>
		<tr>
			<td> &#160;</td>
		</tr>
		<tr>
	    	<td class="label">AWB Prefix:</td>
			<td ><input type="text" name="awbPrefix" id="awbPrefix" size="30" maxlength="20" value=""/></td>
		</tr>
		<tr>
	    	<td class="label">From AWB:</td>
			<td ><input type="number" name="fromAwb" id="fromAwb" size="30" maxlength="20"/></td>
			<td class="label">To AWB:</td>
			<td ><input type="number" name="toAwb" id="toAwb" size="30" maxlength="20"/></td>
		</tr>
	</table>
	
	
	
	
	
	<input type="submit" name="Update" value="Submit" />
	
</form>



<script>
	$("#fromAwb").blur(function(){
	    $("#toAwb").val($("#fromAwb").val());
	});
</script>

