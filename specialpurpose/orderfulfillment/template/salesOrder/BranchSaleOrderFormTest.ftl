	<link type="text/css" href="<@ofbizContentUrl>/images/jquery/plugins/elrte-1.3/css/smoothness/jquery-ui-1.8.13.custom.css</@ofbizContentUrl>" rel="Stylesheet" />	
	
	<style type="text/css">
	
	 	.labelFontCSS {
	    	font-size: 13px;
		}
		.form-style-8{
		    max-width: 680px;
		    max-height: 200px;
		    max-right: 10px;
		    margin-top: 10px;
			margin-bottom: -15px;
		    padding: 15px;
		    box-shadow: 1px 1px 25px rgba(0, 0, 0, 0.35);
		    border-radius: 20px;
		    border: 1px solid #305A72;
		}
		hr.style17 { 
			  display: none; 
			  content: ""; 
			  height: 5px; 
			  margin-top: -5px; 
			  border-style: solid; 
			  border-color: #8c8b8b; 
			  border-width: 0 0 0 0; 
			  border-radius: 20px; 
  
		} 
		hr.style17:before { 
		  height: 1px; 
		  border-style: solid; 
		  border-color: #8c8b8b; 
		  border-width: 1px 0 0 0; 
		  border-radius: 40px; 
		}
		
		hr.style18 { 
		  height: 1px; 
		  border-style: solid; 
		  border-color: #8c8b8b; 
		  border-width: 1px 0 0 0; 
		  border-radius: 40px; 
		} 
		hr.style18:before { 
			  display: block; 
			  content: ""; 
			  height: 30px; 
			  margin-top: -31px; 
			  border-style: solid; 
			  border-color: #8c8b8b; 
			  border-width: 0 0 1px 0; 
			  border-radius: 20px; 
		}
		#popup{
    		position: fixed;
    		background: white;
    		display: none;
    		top: 300px;
    		right: 30px;
    		left: 680px;
    		width: 200px;
    		height: 200px;
   			border: 1px solid #000;
    		border-radius: 5px;
    		padding: 10px;
    		color: black;
		} 
		
		.loomTypes td {
		    text-align: left;
		}
		
		//.button3 {background-color: #008CBA;} /* Blue */
		
		.button2 {
			background-color:  #008CBA; /* Blue */
		    border: .8px solid green;
		    color: white;
		    padding: .5x 7px;
		    text-align: center;
		    text-decoration: none;
		    display: inline-block;
		    font-size: 10px;
		    cursor: pointer;
		    float: left;
		    border-radius: 5px;
		}   
		.button2:hover {
		    background-color: #3e8e41;
		}
		
		input[type=button] {
			color: white;
		    padding: .5x 7px;
		    background:#008CBA;
		    border: .8px solid green;
		    border:0 none;
		    cursor:pointer;
		    -webkit-border-radius: 5px;
		    border-radius: 5px; 
		}
		input[type=button]:hover {
		    background-color: #3e8e41;
		}
		
		.labelItemHeader {
	    	font-size: 13px;
	    	background:#008CBA;
	    	color: white;
	    	border: .1px solid green;
	    	border-radius: 5px; 
	    	font-size: 11px;
	    	line-height:1.5em;
	    	padding: .5x 7px;
	    	display: inline-block;
	    	text-align: right;
		}
		
		.headerLabel {
	    	font-size: 13px;
	    	color: white;
	    	line-height:1.5em;
	    	padding: .5x 7px;
	    	display: inline-block;
	    	text-align: right;
		}
		
		#exp_outer {
   			margin:0px;outline:none;
   			vertical-align: top;
		}
		#chld1 {
		  float: left;
		}
		#chld2 {
		  float: right;
		}
		#chld3 {
		  left: auto;
		  clear: both;
		}
		
		.grid-header {
		    border: 1px solid gray;
		    border-bottom: 0;
		    border-top: 0;
		    background: url('../images/header-bg.gif') repeat-x center top;
		    color: black;
		    height: 24px;
		    line-height: 24px;
		    border-radius: 7px;
		    background-color: #FFFFFF;
		}
		
		<#--
		DIV.screenlet-body {
		    background-color: #FFFFFF;
		    height: auto !important;
		    height: 1%;
		    padding: 0.4em;
		    border: 0.1em solid #f97103;
		    border-radius: 10px;
		}
		-->
		
	</style>
	
	<script type="text/javascript">
	
	$(document).ready(function(){
		getAllAdjustments();
    	getTaxesList();
	});
	
			var supplierAutoJson = ${StringUtil.wrapString(supplierJSON)!'[]'};	
			var societyAutoJson = ${StringUtil.wrapString(societyJSON)!'[]'};
	
		
		
		
	
		$(document).ready(function(){
		
		//==========for backSpace===============
		
       
       (function (global) { 

    if(typeof (global) === "undefined") {
        throw new Error("window is undefined");
    }

    var _hash = "!";
    var noBackPlease = function () {
        global.location.href += "#";

        // making sure we have the fruit available for juice (^__^)
        global.setTimeout(function () {
            global.location.href += "!";
        }, 50);
    };

    global.onhashchange = function () {
        if (global.location.hash !== _hash) {
            global.location.hash = _hash;
        }
    };

    global.onload = function () {            
        noBackPlease();

        // disables backspace on page except on input fields and textarea..
        document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm  !== 'textarea')) {
                e.preventDefault();
            }
            // stopping event bubbling up the DOM tree..
            e.stopPropagation();
        };          
    }

})(window);
       
       //========================================
	
			 $("#open_popup").click(function(){
               	getShipmentAddress();
		    
             	$("#popup").css("display", "block");
             });
			 $("#close_popup").click(function(){
             	$("#popup").css("display", "none");
           	 });
          
           
			 $("#societyfield").hide();
			 	fillPartyData($('#partyId').val());
			 	$("#editServChgButton").hide();
			if(indententryinit.schemeCategory.value.length > 0){
	  			if ($('#schemeCategory').val() == "General"){
	  				$("#editServChgButton").show();
	  				$('#serviceChargePercent').val(2);
	  				var scPerc = $('#serviceChargePercent').val();
	  				$("#serviceCharge").html("<b>"+scPerc+"% Service Charge is applicable</b>");
	  			}
	  			if ($('#schemeCategory').val() == "MGPS" || $('#schemeCategory').val() == "MGPS_10Pecent"){
	  				$("#editServChgButton").show();
	  			}
	  		}
	  		
           	  		
	  		 	
			$( "#effectiveDate" ).datepicker({
				dateFormat:'d MM, yy',
				changeMonth: true,
				numberOfMonths: 1,
				changeYear : true,
				//changeDate : true,
				//minDate: new Date(),
				maxDate: 14,
				onSelect: function( selectedDate ) {
					$( "#effectiveDate" ).datepicker("option", selectedDate);
					fillPartyData($('#partyId').val());
				}
				
			});
			
			
			
			$( "#orderDate" ).datepicker({
				dateFormat:'d MM, yy',
				changeMonth: true,
				numberOfMonths: 1,
				//minDate: new Date(),
				//maxDate: 14,
				onSelect: function( selectedDate ) {
					$( "#orderDate" ).datepicker("option", selectedDate);
				}
			});
			$("#indentReceivedDate").datepicker({
				dateFormat:'d MM, yy',
				changeMonth: true,
				numberOfMonths: 1,
				changeYear: true,
				//minDate: new Date(),
				//maxDate: 14,
				//maxDate: new Date(),
				onSelect: function( selectedDate ) {
   		         
   		         
   		            var rmonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                    var ryear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                    var date = $("#indentReceivedDate").datepicker( 'getDate' );
   		            var vdate = date.toString();
   		            var vdateArr = vdate.split(" ");
                    var rvday = vdateArr[2];

                    var indentDate = $("#effectiveDate").datepicker( 'getDate' );

                    var iYear    = indentDate.getFullYear(); 
					var imonth   = indentDate.getMonth(); 
					var idateStr = indentDate.toString();
					var idateArr = idateStr.split(" ");
                    var ivday    = idateArr[2];
					
                    if(parseInt(ryear) > parseInt(iYear)){
                      alertForDate();
                    }else if(parseInt(ryear) == parseInt(iYear) && parseInt(rmonth) > parseInt(imonth)){
                        alertForDate();
                    }else if(parseInt(rmonth) == parseInt(imonth) && parseInt(rvday) > parseInt(ivday)){
                        alertForDate();
                    }
                    
                    
                
                }
			});
			
			
			$('#ui-datepicker-div').css('clip', 'auto');
				if (e.keyCode === 13){
				}
		 	});
		    $('#productStoreId').keypress(function (e) { 
				$("#productStoreId").autocomplete({ source: branchAutoJson , select: function( event, ui ) {
					$('span#branchName').html('<label>'+ui.item.label+'</label>');
				} });	
		    });
		    $('#societyPartyId').keypress(function (e) { 
				$("#societyPartyId").autocomplete({ source: societyAutoJson , select: function( event, ui ) {
					$('span#societyPartyName').html('<label>'+ui.item.label+'</label>');
				} });	
		    });
			var productStoreObjOnload=$('#productStoreIdFrom');
			if (productStoreObjOnload != null && productStoreObjOnload.val() != undefined ){
				showStoreCatalog(productStoreObjOnload);
			}
			
		});
		
		
		var globalCatgoryOptionList=[];
		var catagoryList=[];
		function showStoreCatalog(productStoreObj) {
			
			var productStoreId=$(productStoreObj).val();
			
	       	var dataString="productStoreId=" + productStoreId ;
	      	$.ajax({
	             type: "POST",
	             url: "getDepotStoreCatalogCatagory",
	           	 data: dataString ,
	           	 dataType: 'json',
	           	 async: false,
	        	 success: function(result) {
	              if(result["_ERROR_MESSAGE_"] || result["_ERROR_MESSAGE_LIST_"]){            	  
	       	  		 alert(result["_ERROR_MESSAGE_"]);
	          			}else{
	       	  				  catagoryList =result["catagoryList"];
	       	  				  var catgoryOptionList=[];
	       	  				 
	       	  				 // alert("catagoryList=========="+catagoryList);
	       	  					if(catagoryList != undefined && catagoryList != ""){
									$.each(catagoryList, function(key, item){
									if(item.value=="BYPROD"){
										catgoryOptionList.push('<option value="'+item.value+'" selected="selected">' +item.text+'</option>');
									}else{
									 catgoryOptionList.push('<option value="'+item.value+'">' +item.text+'</option>');
									 }
										});
					           }
					           $('#productCatageoryId').html(catgoryOptionList.join(''));   
						            $("#productCatageoryId").multiselect({
						   			minWidth : 180,
						   			height: 100,
						   			selectedList: 4,
						   			show: ["bounce", 100],
						   			position: {
						      			my: 'left bottom',
						      			at: 'left top'
						      		}
						   		});
						   		 $("#productCatageoryId").multiselect("refresh");
					          // alert("==globalCatgoryOptionList=="+globalCatgoryOptionList);
					         
	      	 	 
	      			}
	               
	          	} ,
	         	 error: function() {
	          	 	alert(result["_ERROR_MESSAGE_"]);
	         	 }
	        }); 
	           
	    }
	    


      	
	 	
	 	
	</script>
	
	
	<div class="top" id="exp_outer">
		<div class="full"  style="margin: auto; position: relative;">
			<div class="lefthalf" id="chld1">
				<div class="screenlet" style="width:173%">
					<div class="screenlet-title-bar">
		         		<div class="grid-header" style="width:100%">
							<ul>
					         <li class="h3">Indent Header</li>
					         <li class="expanded"><a onclick="javascript:toggleScreenlet(this, 'indentHeader', 'true', '${uiLabelMap.CommonExpand}', '${uiLabelMap.CommonCollapse}');" title="Collapse">&nbsp;</a></li>
					       </ul>
						</div>
				     </div>
      
    				<div class="screenlet-body" id="indentHeader">
    		  			<#assign frmAction="BranchSalesOrder">
					    <#if parameters.formAction?has_content>
					    	 <#assign frmAction=parameters.formAction>
					    </#if>
	    
	    
	    				<form method="post" name="indententryinit" action="<@ofbizUrl>${frmAction}</@ofbizUrl>" id="indententryinit" onsubmit="validateParty()">
		
				      		<table width="100%" border="0" cellspacing="0" cellpadding="0">
				               	
				               	<input type="hidden" name="onbehalfOff" id="onbehalfOff" value="NOonbehalfOff"/>
				               	
				               	<input type="hidden" name="editDestination" id="editDestination" />
				               	<input type="hidden" name="changeDesti" id="changeDesti" />
				               	<input type="hidden" name="productHSNCode" id="productHSNCode"/>
				               	<input type="hidden" name="prodCnt" id="prodCnt"/>
				               	<input type="hidden" name="hsnCnt" id="hsnCnt"/>
				               	<input type="hidden" name="editHsnFlag" id="editHsnFlag" value="${editHsnFlag?if_exists}"/>
				               	
				               	
				               	<tr>
						           	<td>&nbsp;</td>
									<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>${uiLabelMap.Branch}:<font color="red">*</font></div></td>
						          	<#if changeFlag?exists && changeFlag=='EditDepotSales'>
										<#if productStoreId?exists && productStoreId?has_content>  
								  	  		<input type="hidden" name="productStoreId" id="productStoreId" value="${productStoreId?if_exists}"/>  
							          		<td valign='middle'>
							            		<div ><font color="green">
							               			${productStoreId}    <#--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:processChangeIndentParty()" class="buttontext">Party Change</a>-->             
							            		</div>
							          		</td>       
							          	</#if>
							    	<#else>
										<#if parameters.productStoreId?exists && parameters.productStoreId?has_content>  
								  	  		<input type="hidden" name="productStoreId" id="productStoreId" value="${parameters.productStoreId?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${parameters.productStoreId}           
							            		</div>
							          		</td>       
							          		
							          		<#if parameters.cfcs?exists && parameters.cfcs?has_content>  
							          			<td align='left' valign='middle' nowrap="nowrap" colspan="5"><div class='h4'>CFC:<font color="red">*</font></div>
									  	  		<input type="hidden" name="cfcs" id="cfcs" value="${parameters.cfcs?if_exists}"/>  
								            		<div><font color="green">
								               			${parameters.cfcs}           
								            		</div>
								          		</td>  
							          		</#if>
							          		
							          		
							          	<#else>
							          		<td valign='middle' colspan="5">
							          			<input type="text" name="productStoreId" id="productStoreId"/>
							          			<span class="tooltip" id="branchName"></span>
							          			<label class='CFC_TD' style='display:none;'><b>CFC:</label>
							          			<select name="cfcs" id="cfcs" style='display:none;' class='CFC_TD' >
				          						          					
						          				</select>
							          		</td>
							          	</#if>
						        	</#if>
					       	  		<#--<td><span class="tooltip" id="branchName"></span></td>-->
				               	</tr>
				               	
				               	<tr>
					       	  		
					       			<td>&nbsp;</td>
					       			
					       			<input type="hidden" name="billingType" id="billingType" value="Direct"/>  
					       			<#if parameters.partyGeoId?exists && parameters.partyGeoId?has_content>  
					       				<input type="hidden" name="partyGeoId" id="partyGeoId" value="${partyGeoId?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="partyGeoId" id="partyGeoId" value=""/>
						          	</#if>
						          	<#if parameters.branchGeoId?exists && parameters.branchGeoId?has_content>  
					       				<input type="hidden" name="branchGeoId" id="branchGeoId" value="${branchGeoId?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="branchGeoId" id="branchGeoId" value=""/>
						          	</#if>
						          	<#if parameters.supplierGeoId?exists && parameters.supplierGeoId?has_content>  
					       				<input type="hidden" name="supplierGeoId" id="supplierGeoId" value="${supplierGeoId?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="supplierGeoId" id="supplierGeoId" value=""/>
						          	</#if>
						          	<#if parameters.partyGeoLocation?exists && parameters.partyGeoLocation?has_content>  
					       				<input type="hidden" name="partyGeoLocation" id="partyGeoLocation" value="${partyGeoLocation?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="partyGeoLocation" id="partyGeoLocation" value=""/>
						          	</#if>
						          	<#if parameters.supplierGeoLocation?exists && parameters.supplierGeoLocation?has_content>  
					       				<input type="hidden" name="supplierGeoLocation" id="supplierGeoLocation" value="${supplierGeoLocation?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="supplierGeoLocation" id="supplierGeoLocation" value=""/>
						          	</#if>
						          	<#if parameters.branchGeoLocation?exists && parameters.branchGeoLocation?has_content>  
					       				<input type="hidden" name="branchGeoLocation" id="branchGeoLocation" value="${branchGeoLocation?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="branchGeoLocation" id="branchGeoLocation" value=""/>
						          	</#if>
						          	
					       			<input type="hidden" name="taxTypeApplicable" id="taxTypeApplicable" value=""/> 
					       			<#--<input type="hidden" name="supplierGeoId" id="supplierGeoId" value=""/>-->  
					       			<#--<input type="hidden" name="branchGeoId" id="branchGeoId" value=""/>-->
					       			<input type="hidden" name="e2FormCheck" id="e2FormCheck" value=""/>
					       			<input type="hidden" name="orderTaxType" id="orderTaxType" value="${orderTaxType?if_exists}"/>
					       			<input type="hidden" name="poTaxType" id="poTaxType" value="${poTaxType?if_exists}"/>
					       			<input type="hidden" name="serviceChargePercent" id="serviceChargePercent" value="0"/> 
					       			<#if parameters.contactMechId?exists && parameters.contactMechId?has_content>  
					       				<input type="hidden" name="contactMechId" id="contactMechId" value="${contactMechId?if_exists}"/>
					       			 <#else>               
						          		<input type="hidden" name="contactMechId" id="contactMechId"/>
						          	</#if>
						          	
					       			<td align='left' valign='middle' nowrap="nowrap"><div class='h4'><#if changeFlag?exists && changeFlag=='AdhocSaleNew'>Retailer:<#elseif changeFlag?exists && changeFlag=='InterUnitTransferSale'>KMF Unit ID:<#else>${uiLabelMap.Customer}:</#if><font color="red">*</font></div></td>
							        <#if changeFlag?exists && changeFlag=='EditDepotSales'>
										<#if partyId?exists && partyId?has_content>  
								  	  		<input type="hidden" name="partyId" id="partyId" value="${partyId?if_exists}"/>  
							          		<td valign='middle'>
							            		<div ><font color="green">
							               			${partyId} [ ${partyName?if_exists} ] <#--${partyAddress?if_exists}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:processChangeIndentParty()" class="buttontext">Party Change</a>-->             
							            		</div>
							          		</td>       
							          	</#if>
							    	<#else>
									 	<#if party?exists && party?has_content>  
								  	  		<input type="hidden" name="partyId" id="partyId" value="${party.partyId.toUpperCase()}"/>  
								  	  		<input type="hidden" name="disableAcctgFlag" id="disableAcctgFlag" value="${disableAcctgFlag?if_exists}"/>
							          		<td valign='middle' colspan="6">
							            		<div ><font color="green">
							            		    <#assign partyIdentification = delegator.findOne("PartyIdentification", {"partyId" :party.partyId,"partyIdentificationTypeId":"PSB_NUMBER"}, true)?if_exists>
			         								<#assign passBookDetails=partyIdentification?if_exists>
							               			${party.groupName?if_exists} ${party.firstName?if_exists}${party.lastName?if_exists} [ ${passBookDetails.idValue?if_exists}] <#--${partyAddress?if_exists} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:processChangeIndentParty()" class="buttontext">Party Change</a>-->             
							            		</div>
							          		</td>       
							       		<#else>               
							          		<td valign='middle' colspan="6">
							          			<input type='text' id='partyId' name='partyId' onfocus='javascript:autoCompletePartyId();' size='13'/><span class="tooltip" id='partyTooltip'></span>
							          		</td>
							          	</#if>
						        	</#if>
									
				               	</tr>
				               	
				               	<tr>
				               		<td>&nbsp;</td>
				               	</tr>
				               	
				               	<tr>
					       	  		<td>&nbsp;</td>
					       	  		<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>Sales Channel:</div></td>
					       			<#if parameters.salesChannel?exists && parameters.salesChannel?has_content>  
						  	  			<input type="hidden" name="salesChannel" id="salesChannel" value="${parameters.salesChannel?if_exists}"/>  
					          			<td valign='middle'>
					            			<div><font color="green">${parameters.salesChannel?if_exists}</div>
					          			</td>       	
					       			<#else>      	         
					          			<td valign='middle'>
					          				<select name="salesChannel" id="salesChannel" class='h4' style="width:162px">
					          					<option value="WALKIN_SALES_CHANNEL">Walk-In Sales Channel</option>
					          					<option value="WEB_SALES_CHANNEL">Web Channel</option>
					          					<option value="POS_SALES_CHANNEL">POS Channel</option>
					          					<option value="PHONE_SALES_CHANNEL">Phone Channel</option>
					          					<option value="FAX_SALES_CHANNEL">Fax Channel</option>
					          					<option value="EMAIL_SALES_CHANNEL">E-Mail Channel</option>	          					
					          				</select>
					          			</td>
					       			</#if>
					       			<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>${uiLabelMap.SchemeCategory}:</div></td>
					       			<#if parameters.schemeCategory?exists && parameters.schemeCategory?has_content>  
						  	  			<input type="hidden" name="schemeCategory" id="schemeCategory" value="${parameters.schemeCategory?if_exists}"/>  
					          			<td valign='middle'>
					            			<div><font color="green"><#if parameters.schemeCategory == "MGPS_10Pecent">MGPS + 10% <#else>${parameters.schemeCategory?if_exists}</#if></div>
					          			</td>       	
					       			<#else>      	         
					          			<td valign='middle'>
					          				<select name="schemeCategory" id="schemeCategory" class='h4'  style="width:162px">
					          						          					
					          				</select>
					          			</td>
					       			</#if>
					       		</tr>
					       		
					       		
				               	
				               	
			                    <tr>  
					       	  		<td>&nbsp;</td>
					       	  		<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>${uiLabelMap.IndentDate}:</div></td>
						           		<input type="hidden" name="productSubscriptionTypeId"  value="CASH" />
					          			<input type="hidden" name="isFormSubmitted"  value="YES" />
								      	<input type="hidden" name="changeFlag"  value="${changeFlag?if_exists}" />
								      	<#if changeFlag?exists && changeFlag=="EditDepotSales">
										 	<input type="hidden" name="productStoreId" id="productStoreId" value="${productStoreId?if_exists}"/>  
										 	<input type="hidden" name="shipmentTypeId" id="shipmentTypeId" value="BRANCH_SHIPMENT"/> 
							           	</#if>
								        <#if changeFlag?exists && changeFlag=='DepotSales'>
								         	<input type="hidden" name="shipmentTypeId" id="shipmentTypeId" value="BRANCH_SHIPMENT"/> 
								        <#else>
								          	<input type="hidden" name="shipmentTypeId" id="shipmentTypeId" value="RM_DIRECT_SHIPMENT"/>
								          	<input type="hidden" name="salesChannel" id="salesChannel" value="RM_DIRECT_CHANNEL"/>
								        </#if>
						          		<#if effectiveDate?exists && effectiveDate?has_content>  
							  	  			<input type="hidden" name="effectiveDate" id="effectiveDate" value="${effectiveDate}"/>  
							  	  		<#if manualQuota?exists && manualQuota?has_content>
							  	  			<input type="hidden" name="manualQuota" id="manualQuota" value="${manualQuota}"/>
							  	  		</#if>
							          		<td align='left' valign='middle'>
							            		<div><font color="green">${effectiveDate}         
							            		</div>
							          		</td>       
						       	  		<#else> 
							          		<td valign='left' id='effectiveDateTd'>          
							            		<input class='h4' type="text" name="effectiveDate" id="effectiveDate" value="${defaultEffectiveDate}"/>           		
							            	</td>
						       	  		</#if>
						       	  		
					       	  		<#if changeFlag?exists && changeFlag != "EditDepotSales">
										<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>Recd Date by NHDC:</div></td>
										<#if indentReceivedDate?exists && indentReceivedDate?has_content>  
							  				<input type="hidden" name="indentReceivedDate" id="indentReceivedDate" value="${indentReceivedDate}"/>  
							   				<td valign='middle'>
												<div ><font color="green">${indentReceivedDate}         
												</div>
							   				</td>  
										<#else> 
							 				<td valign='left'>          
												<input class='h4' type="text" name="indentReceivedDate" id="indentReceivedDate" value="${defaultEffectiveDate}"/>    
							 				</td>
										</#if>
									</#if>
					       	  </tr>	
					       	  
					       	 
				               	
				               	<tr>
					       	  		
					       			<td>&nbsp;</td>
					       			<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>Reference No :</div></td>
						          	<#if changeFlag?exists && changeFlag=='EditDepotSales'>
										<#if referenceNo?exists && referenceNo?has_content>  
								  	  		<input type="hidden" name="referenceNo" id="referenceNo" value="${referenceNo?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${referenceNo}               
							            		</div>
							          		</td>       
							          	</#if>
							    	<#else>
										<#if parameters.referenceNo?exists && parameters.referenceNo?has_content>  
								  	  		<input type="hidden" name="referenceNo" id="referenceNo" value="${parameters.referenceNo?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${parameters.referenceNo}              
							            		</div>
							          		</td>       
							          	<#else>
							          		<td valign='middle'>
							          			<input type="text" name="referenceNo" id="referenceNo"/>
							          			<#--<span class="tooltip">Input Supplier and Press Enter</span>-->
							          		</td>
							          		
							          	</#if>
						        	</#if>
						        	<#if !isDBTScreen?has_content>
							        	<td align='left' valign='middle' nowrap="nowrap"><div class='h4'> Tally Reference No :</div></td>
											<#if tallyRefNumber?exists && tallyRefNumber?has_content>  
								          		<td valign='middle'>
								            		<div><font color="green">
								                      <input type="text" name="tallyReferenceNo" id="tallyReferenceNo" value="${tallyRefNumber?if_exists}" onblur=tallyRefMethod() />
								                      <input type="hidden" name="ediTallyRefNo" id="ediTallyRefNo" />  
								                      
								                        
								            		</div>
								          		</td>       
								    	<#else>
											<#if parameters.tallyReferenceNo?exists && parameters.tallyReferenceNo?has_content>  
									  	  		<input type="hidden" name="tallyReferenceNo" id="tallyReferenceNo" value="${parameters.tallyReferenceNo?if_exists}"/>  
								          		<td valign='middle'>
								            		<div><font color="green">
								               			${parameters.tallyReferenceNo}              
								            		</div>
								          		</td>       
								          	<#else>
								          		<td valign='middle'>
								          			<input type="text" name="tallyReferenceNo" id="tallyReferenceNo" onblur=tallyRefMethod() />
								          			 <input type="hidden" name="ediTallyRefNo" id="ediTallyRefNo" />  
								          		</td>
								          		
								          	</#if>
										</#if>
									</#if>
				               	</tr>
				               	<tr>
				               		<td>&nbsp;</td>
				               	</tr>	
				               	<#--	         
				               	<tr>
					       			<td>&nbsp;</td>
					       			<td align='left' valign='middle' nowrap="nowrap"><div class='h3'> Tally Reference No :</div></td>
						          	<#if changeFlag?exists && changeFlag=='EditDepotSales'>
										<#if tallyReferenceNo?exists && tallyReferenceNo?has_content>  
								  	  		<input type="hidden" name="tallyReferenceNo" id="tallyReferenceNo" value="${tallyReferenceNo?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${tallyReferenceNo}               
							            		</div>
							          		</td>       
							          	</#if>
							    	<#else>
										<#if parameters.tallyReferenceNo?exists && parameters.tallyReferenceNo?has_content>  
								  	  		<input type="hidden" name="tallyReferenceNo" id="tallyReferenceNo" value="${parameters.tallyReferenceNo?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${parameters.tallyReferenceNo}              
							            		</div>
							          		</td>       
							          	<#else>
							          		<td valign='middle'>
							          			<input type="text" name="tallyReferenceNo" id="tallyReferenceNo"/>
							          		</td>
							          		
							          	</#if>
						        	</#if>
				               	</tr>	               
				               	-->      	
				               	<tr>
					       			<td>&nbsp;</td>
					       			<td align='left' valign='middle' nowrap="nowrap"><div class='h4'>${uiLabelMap.ProductSupplier} :<font color="red">*</font></div></td>
						          	<#if changeFlag?exists && changeFlag=='EditDepotSales'>
										<#if suplierPartyId?exists && suplierPartyId?has_content>  
								  	  		<input type="hidden" name="suplierPartyId" id="suplierPartyId" value="${suplierPartyId?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${suplierPartyId}  [${suplierPartyName}]  <#--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:processChangeIndentParty()" class="buttontext">Party Change</a>-->             
							            		</div>
							          		</td>       
							          	</#if>
							    	<#else>
										<#if parameters.suplierPartyId?exists && parameters.suplierPartyId?has_content>  
								  	  		<input type="hidden" name="suplierPartyId" id="suplierPartyId" value="${parameters.suplierPartyId?if_exists}"/>  
							          		<td valign='middle'>
							            		<div><font color="green">
							               			${parameters.suplierPartyId} [${suppPartyName?if_exists}] <#--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:processChangeIndentParty()" class="buttontext">Party Change</a>-->             
							            		</div>
							          		</td>       
							          	<#else>
							          		<td valign='middle'>
							          			<input type="text" name="suplierPartyId" id="suplierPartyId"  />
							          			<input type="hidden" name="tabButtonItem" id="tabButtonItem" value="${parameters.tabButtonItem?if_exists}"/>
							          			<input type="hidden" name="isDBTScreen" id="isDBTScreen" value="${parameters.isDBTScreen?if_exists}"/>
							          			<#--<span class="tooltip">Input Supplier and Press Enter</span>-->
							          			<input type="submit" style="padding:.3em" value="submit" name="submit" id="submit" onclick= 'javascript:formSubmit(this);' />
							          		</td>
							          		
							          	</#if>
						        	</#if>
						        	
						        	<#if parameters.suplierAdd?exists && parameters.suplierAdd?has_content>  
						        	  <td width="10%" keep-together="always" align="left"> Supplier Address : </td><td width="50%"> <label  align="left" id="supplierAddress" style="color: green">${parameters.suplierAdd}</label></td>
									<#else>
									<#if (supplierAddress?has_content)>
									  <td width="10%" align="left"> Supplier Address : </td><td width="50%"> <label  align="left" id="supplierAdd" style="color: green">${supplierAddress?if_exists}</label></td>
									<#else>
									  <td width="10%" keep-together="always" align="left"><font color="#000000" > Supplier Address : </font></td><td width="50%"><span  align="left" id="suplierPartyName" style="color: blue"></span> <p><label  align="left" id="supplierAddress" style="color: blue"></label><p></td>
									  <input type="hidden" name="suplierAdd" id="suplierAdd" />  
									</#if>
									</#if>
									
				               	</tr>
				               	<#if parameters.suplierPartyId?exists && parameters.suplierPartyId?has_content>
								<tr>
								</tr>
								<#else>
				               		<tr>
					       	  		<td>&nbsp;</td>
					       			<td>&nbsp;</td>
					       			<td align='left' valign='middle' nowrap="nowrap">
					       				<#-->	<input type="submit" style="padding:.3em" value="submit" name="submit" id="submit" onclick= 'javascript:formSubmit(this);' /> -->
					       			</td>
				               		
									</tr>
				               	</#if>
				                 <#--	
				               	<tr>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td>&nbsp;</td>
				               		<td><span class="tooltip">Input party code and press Enter</span></td>
				               	</tr>
				               	-->  
	               	
	      					</table>
		    			<div id="sOFieldsDiv" >
						</div> 
					</form>
					<#--
					<form method="post" class="form-style-8" name="taxationDetails" action="updateTaxHeader" id="taxationDetails">
				      	<table width="100%" border="0" cellspacing="0" cellpadding="0">
				               	
				            <tr>
						      	<td>&nbsp;</td>
						    </tr>  	
						    <tr>
				       	  		<td>&nbsp;</td>
				       	  		<td align='left' valign='middle' nowrap="nowrap"><div class='h3'>Party Geo:</div></td>
				       			<td valign='middle'>
				            		<div><font color="green">
				               			<label id="partyGeoLocationDesc">${branchGeoId?if_exists}</label>            
				            		</div>
				          		</td>
				          		
				          		<td>&nbsp;</td>
				       	  		<td align='left' valign='middle' nowrap="nowrap"><div class='h3'>Branch Geo:</div></td>
				       			<td valign='middle'>
				            		<div><font color="green">
				               			<label id="branchGeoLocationDesc"></label>            
				            		</div>
				          		</td>
				          		
				          		<td>&nbsp;</td>
				       	  		<td align='left' valign='middle' nowrap="nowrap"><div class='h3'>Party Geo:</div></td>
				       			<td valign='middle'>
				            		<div><font color="green">
				               			<label id="supplierGeoLocationDesc"></label>            
				            		</div>
				          		</td>
				       			
				       		</tr>
						</table>
					</form>
					-->
					
					
				    <form method="post" id="indententry" action="<@ofbizUrl>IndentEntryInit</@ofbizUrl>">  
						<input type="hidden" name="effectiveDate" id="effectiveDate" value="${parameters.effectiveDate?if_exists}"/>
						<input type="hidden" name="boothId" id="boothId" value="${parameters.boothId?if_exists}"/>
						<input type="hidden" name="productSubscriptionTypeId" id="productSubscriptionTypeId" value="${parameters.productSubscriptionTypeId?if_exists}"/>   	   	   	   
						<input type="hidden" name="subscriptionTypeId" id="subscriptionTypeId" value="${parameters.subscriptionTypeId?if_exists}"/>
						<input type="hidden" name="destinationFacilityId" id="destinationFacilityId" value="${parameters.destinationFacilityId?if_exists}"/>
						<input type="hidden" name="shipmentTypeId" id="shipmentTypeId" value="${parameters.shipmentTypeId?if_exists}"/>
						<input type="hidden" name="vehicleId" id="vehicleId" value="${parameters.vehicleId?if_exists}"/>
						<input type="hidden" name="salesChannel" id="salesChannel" value="${parameters.salesChannel?if_exists}"/>
						<input type="hidden" name="referenceNo" id="referenceNo" value="${parameters.referenceNo?if_exists}"/>
						<input type="hidden" name="tallyReferenceNo" id="tallyReferenceNo" value="${parameters.tallyReferenceNo?if_exists}"/>
						<input type="hidden" name="billToCustomer" id="billToCustomer" value="${parameters.billToCustomer?if_exists}"/>
						<input type="hidden" name="branchGeoId" id="branchGeoId" value="${parameters.branchGeoId?if_exists}"/>
						<input type="hidden" name="supplierGeoId" id="supplierGeoId" value="${parameters.supplierGeoId?if_exists}"/>
						<input type="hidden" name="serviceChargePercent" id="serviceChargePercent" value="${parameters.serviceChargePercent?if_exists}"/>
						<input type="hidden" name="contactMechId" id="contactMechId" value="${parameters.contactMechId?if_exists}" />
						<input type="hidden" name="manualQuota" id="manualQuota" value="${parameters.manualQuota?if_exists}" />
						<input type="hidden" name="supplierAddress" id="supplierAddress" value="${parameters.supplierAddress?if_exists}" />
						<input type="hidden" name="isDBTScreen" id="isDBTScreen" value="${parameters.isDBTScreen?if_exists}"/>
						<input type="hidden" name="tabButtonItem" id="tabButtonItem" value="${parameters.tabButtonItem?if_exists}"/>
						<br>
					</form>    
				</div>
			</div>
		</div>
		
		

		
		
		
		<div class="bottom" style="margin: auto; position: relative;" id="chld3">
			<div class="screenlet" >
				<div class="grid-header" style="margin-left:auto; margin-right:0;">
					<span style="float:left; margin-left:0px;" id="serviceCharge" class="serviceCharge"></span>
					<#--<a style="float:left; margin-left:0px;" href="javascript:changeServiceChargePercent()" class="button2" id="editServChgButton">Edit Service Charge</a>-->
					<#if parameters.schemeCategory?exists && parameters.schemeCategory == "General">
						<input type="button" style="float:left" class="buttonText" id="editServChgButton" value="Edit Service Charge/Edit Destination" onclick="javascript:changeServiceChargePercent();" />
					<#else>
						<input type="button" style="float:left" class="buttonText" id="editServChgButton" value="Edit Destination" onclick="javascript:changeServiceChargePercent();" />
					</#if>
					<input type="button" style="float:left" class="buttonText" id="addHsnCode" value="ADD HSN CODES" onclick="javascript:getHsnCodesList();" />
					
					
					<label style="float:left" id="itemsSelected" class="labelItemHeader"></label>
					<label style="float:left" id="totalAmount" class="labelItemHeader"></label>
					<label style="float:left" id="totalDiscount" class="labelItemHeader"></label>
					<label style="float:left" id="totalPayable" class="labelItemHeader"></label>
					<label style="float:left" id="totalQtyKgs" class="labelItemHeader"></label>
				</div>
			    <div class="screenlet-body">
					<div id="myGrid1" style="width:100%;height:210px;"></div>
						  
						<#assign formAction='processBranchSalesOrder'>			
						
						
						<#if booth?exists || party?exists || partyId?exists >
			 		    	<#--
			 		    	<div class="screenlet-title-bar">
								<div class="grid-header" style="width:35%">
									<label>Other Charges</label><span id="totalAmount"></span>
								</div>
								<div id="myGrid2" style="width:35%;height:150px;">
									<div class="grid-header" style="width:35%">
									</div>
								</div>
							</div>	
							-->
					    	<div align="center">
					    		<input type="submit" style="padding:.3em" id="changeSave" value="${uiLabelMap.CommonSubmit}" onclick="javascript:processIndentEntry('indententry','<@ofbizUrl>${formAction}</@ofbizUrl>');"  />
					    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    		<input type="submit" style="padding:.3em" id="changeCancel" value="Cancel" onclick="javascript:processIndentEntry('indententry','<@ofbizUrl>processOrdersBranchSales</@ofbizUrl>');"/>   	
					    	</div>     
						</#if>
						
					</div>
				</div>     
			</div>
		</div>
	
	
	</div>
	
	
	
	

	