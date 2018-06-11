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

<#escape x as x?xml>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<fo:layout-master-set>
      <fo:simple-page-master master-name="main" page-height="11.69in" page-width="8.27in"
        margin-top=".5in" margin-bottom="1in" margin-left=".2in" margin-right=".2in">
          <fo:region-body margin-top="0.3cm"/>
          <fo:region-before extent="0.3cm"/>
          <fo:region-after extent="0.3cm"/>
      </fo:simple-page-master>
    </fo:layout-master-set>
    <#assign fileName = "${externalOrderId}_${orderId}_${invoice.invoiceId}_IN.pdf"/>
    ${setRequestAttribute("OUTPUT_FILENAME", fileName)}
		<fo:page-sequence master-reference="main">
          <fo:flow flow-name="xsl-region-body" font-family="sans-serif" font-size="8pt">
          	<fo:block text-align="left" font-weight="bold" font-size="13">
				<fo:external-graphic src="http://www.palred.com/img/Logo.png" height="35px" content-height="scale-to-fit"/>
				
				&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Tax Invoice
		  	</fo:block>
          	<fo:block>
          		<fo:table width="100%" table-layout="fixed" border-style="solid">
	                <fo:table-column column-width="100%"/>
	            	<fo:table-body>
	            	  	
	            		<fo:table-row>
	            			<fo:table-cell>
		                      <fo:block text-align="left">
            				     <fo:table width="100%" table-layout="fixed" border-style="solid">
					                <fo:table-column column-width="50%"/>
					                <fo:table-column column-width="50%"/>
					            	<fo:table-body>
            							<fo:table-row>
					            			<fo:table-cell padding-bottom="0.05in" padding-top="0.05in" padding-left="0.05in" border-style="solid">
						                       <fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in" padding-left="0.05in">
						                       		Order ID: ${externalOrderId}
						                       		
						                       		
				            				   </fo:block>
				            				   <fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in">
						                       		Order Date: ${Static["org.apache.ofbiz.base.util.UtilDateTime"].toDateString(orderHeader.orderDate, "dd/MM/yyyy")} 
				            				   </fo:block>
				            				   <fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in">
						                       		GSTIN: 07AAHCP9699Q1Z2
				            				   </fo:block>
						                    </fo:table-cell>
						                    <fo:table-cell padding-bottom="0.05in" padding-top="0.05in" padding-left="0.05in" border-style="solid">
						                    	<fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in">
						                       		PTS Order ID: ${orderId}
				            				   </fo:block>
						                       <fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in">
						                       		Tax Invoice No: ${invoice.invoiceId}
				            				   </fo:block>
				            				   <fo:block text-align="left" padding-bottom="0.03in" padding-top="0.03in">
						                       		Invoice Date: ${invoiceDate} 
				            				   </fo:block>
				            				   
						                    </fo:table-cell>	
					            		</fo:table-row>
					            		
					            		<fo:table-row>
					            			<fo:table-cell border-style="solid" padding-bottom="0.1in" padding-top="0.05in" padding-left="0.05in"> 
						                       <fo:block text-align="left">
						                       		SELLER
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		&#160; 
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		Palred Technology Services Private Limited
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		Plot-34, G/F, Bamnoli Village
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		Dwaraka, Sec-28, PH-2
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		Delhi
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		110077
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		IND
				            				   </fo:block>
				            				   <#--
				            				   <fo:block text-align="left">
						                       		Tel: 040-67138879
				            				   </fo:block>
				            				   -->
				            				   <fo:block text-align="left">
						                       		GSTIN: 07AAHCP9699Q1Z2
				            				   </fo:block>
				            				   
				            				   
						                    </fo:table-cell>
						                    <fo:table-cell border-style="solid" padding-bottom="0.1in" padding-top="0.05in" padding-left="0.05in"> 
						                       <fo:block text-align="left">
						                       		Bill To/ Ship To
				            				   </fo:block>
				            				   <fo:block text-align="left">
						                       		&#160; 
				            				   </fo:block>
				            				   <#if billingAddress?has_content>
									                <#assign billToPartyNameResult = dispatcher.runSync("getPartyNameForDate", Static["org.apache.ofbiz.base.util.UtilMisc"].toMap("partyId", billToParty.partyId, "compareDate", invoice.invoiceDate, "userLogin", userLogin))/>
									                <fo:block>${billToPartyNameResult.fullName?default(billingAddress.toName)?default("Billing Name Not Found")}</fo:block>
									                <#if billingAddress.attnName??>
									                    <fo:block>${billingAddress.attnName}</fo:block>
									                </#if>
									                    <fo:block>${billingAddress.address1!}</fo:block>
									                <#if billingAddress.address2??>
									                    <fo:block>${billingAddress.address2}</fo:block>
									                </#if>
									                <fo:block>${billingAddress.city!} ${billingAddress.stateProvinceGeoId!} ${billingAddress.postalCode!}</fo:block>
									                <#if billToPartyTaxId?has_content>
									                    <fo:block>${uiLabelMap.PartyTaxId}: ${billToPartyTaxId}</fo:block>
									                </#if>
									            <#else>
									                <fo:block>${uiLabelMap.AccountingNoGenBilAddressFound}${billToParty.partyId}</fo:block>
									            </#if>
				            				   
						                    </fo:table-cell>	
					            		</fo:table-row>		
            						
            							<fo:table-row>
					            			<fo:table-cell number-columns-spanned="2"> 
						                       <fo:block>
						                       	  <fo:table width="100%" table-layout="fixed">
									                <fo:table-column column-width="3%"/>
									                <fo:table-column column-width="20%"/>
									                <fo:table-column column-width="4%"/>
									                <fo:table-column column-width="7%"/>
									                <fo:table-column column-width="9%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="8%"/>
									                <fo:table-column column-width="9%"/>
									            	<fo:table-body>
				            							<fo:table-row>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">S.No</fo:block>
				            								</fo:table-cell>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Description of Goods</fo:block>
				            								</fo:table-cell>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Qty</fo:block>
				            								</fo:table-cell>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Price</fo:block>
				            								</fo:table-cell>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Amount</fo:block>
				            								</fo:table-cell>
				            								<fo:table-cell number-columns-spanned="2">
				            									<fo:table width="100%" table-layout="fixed">
				            										<fo:table-column column-width="50%"/>
									                				<fo:table-column column-width="50%"/>
			            											<fo:table-body>
			            												<fo:table-row>
			            													<fo:table-cell number-columns-spanned="2" border-style="solid">
			            														<fo:block text-align="center" font-size="8pt" font-weight="bold">CGST</fo:block>
				            												</fo:table-cell>
			            												</fo:table-row>
			            												<fo:table-row>
			            													<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Rate</fo:block>
								            								</fo:table-cell>
								            								<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Amount</fo:block>
								            								</fo:table-cell>
			            												</fo:table-row>
			            											</fo:table-body>
	            				 				  				</fo:table>
				            								</fo:table-cell>
				            								<fo:table-cell number-columns-spanned="2">
				            									<fo:table width="100%" table-layout="fixed">
				            										<fo:table-column column-width="50%"/>
									                				<fo:table-column column-width="50%"/>
			            											<fo:table-body>
			            												<fo:table-row>
			            													<fo:table-cell number-columns-spanned="2" border-style="solid">
			            														<fo:block text-align="center" font-size="8pt" font-weight="bold">SGST</fo:block>
				            												</fo:table-cell>
			            												</fo:table-row>
			            												<fo:table-row>
			            													<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Rate</fo:block>
								            								</fo:table-cell>
								            								<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Amount</fo:block>
								            								</fo:table-cell>
			            												</fo:table-row>
			            											</fo:table-body>
	            				 				  				</fo:table>
				            								</fo:table-cell>
				            								<fo:table-cell number-columns-spanned="2">
				            									<fo:table width="100%" table-layout="fixed">
				            										<fo:table-column column-width="50%"/>
									                				<fo:table-column column-width="50%"/>
			            											<fo:table-body>
			            												<fo:table-row>
			            													<fo:table-cell number-columns-spanned="2" border-style="solid">
			            														<fo:block text-align="center" font-size="8pt" font-weight="bold">IGST</fo:block>
				            												</fo:table-cell>
			            												</fo:table-row>
			            												<fo:table-row>
			            													<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Rate</fo:block>
								            								</fo:table-cell>
								            								<fo:table-cell border-style="solid">
								            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Amount</fo:block>
								            								</fo:table-cell>
			            												</fo:table-row>
			            											</fo:table-body>
	            				 				  				</fo:table>
				            								</fo:table-cell>
				            								<fo:table-cell border-style="solid">
				            									<fo:block text-align="center" font-size="8pt" font-weight="bold">Total Amt</fo:block>
				            								</fo:table-cell>
				            							</fo:table-row>
				            							<#list displayInvItemList as invoiceItem>
					            							<fo:table-row>
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.sNo}</fo:block>
					            								</fo:table-cell>
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.productName}</fo:block>
					            									<fo:block text-align="center" font-size="8pt">HSN Code: ${invoiceItem.hsnCode?if_exists}</fo:block>
					            								</fo:table-cell>
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.quantity}</fo:block>
					            								</fo:table-cell>
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.itemPrice?string("0.00")}</fo:block>
					            								</fo:table-cell>
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.itemAmount?string("0.00")}</fo:block>
					            								</fo:table-cell>
					            								
					            								<#if invoiceItem.CGST?has_content>
					            									<#assign tax = invoiceItem.CGST/>
					            									<#assign taxRate = invoiceItem.CGST_rateProduct/>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${taxRate.taxPercentage?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${tax.amount?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            								
					            								<#else>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            								</#if>
					            								<#if invoiceItem.SGST?has_content>
					            									<#assign tax = invoiceItem.SGST/>
					            									<#assign taxRate = invoiceItem.SGST_rateProduct/>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${taxRate.taxPercentage?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${tax.amount?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            								
					            								<#else>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            								</#if>
					            								<#if invoiceItem.IGST?has_content>
					            									<#assign tax = invoiceItem.IGST/>
					            									<#assign taxRate = invoiceItem.IGST_rateProduct/>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${taxRate.taxPercentage?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">${tax.amount?string("0.00")}</fo:block>
					            									</fo:table-cell>
					            								
					            								<#else>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            									<fo:table-cell border-style="solid">
					            										<fo:block text-align="center" font-size="8pt">0.00</fo:block>
					            									</fo:table-cell>
					            								</#if>
					            								
					            								<fo:table-cell border-style="solid">
					            									<fo:block text-align="center" font-size="8pt">${invoiceItem.itemTotal?string("0.00")}</fo:block>
					            								</fo:table-cell>
					            							</fo:table-row>	
				            							</#list>
				            							<fo:table-row>
				            								<fo:table-cell border-style="solid">
        														<fo:block text-align="center" font-size="8pt" font-weight="bold">&#160;</fo:block>
            												</fo:table-cell>
            												<fo:table-cell number-columns-spanned="10" border-style="solid">
        														<fo:block text-align="right" font-size="8pt" font-weight="bold">Grand Total</fo:block>
            												</fo:table-cell>
            												<fo:table-cell border-style="solid">
        														<fo:block text-align="center" font-size="8pt" font-weight="bold">${invoiceTotal?string("0.00")}</fo:block>
            												</fo:table-cell>
				            							</fo:table-row>
				            							
				            						</fo:table-body>
	            				 				  </fo:table>	
						                       </fo:block>
						                     </fo:table-cell>	
					            		</fo:table-row>   
            							
            							
            							
            							
            						</fo:table-body>
	            				 </fo:table>
            					
            				  </fo:block>
		                    </fo:table-cell>	
	            		</fo:table-row>	
	            		
	            		<fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">Declaration:</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
						
						<fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt">
                                    We declare that this Invoice shows the actual price of the goods described and that all particulars are true and correct.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
	            		
	            		<#--
	            		
	            		<fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">Customer Acknowledgement:</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt">
                                    I confirm that the above products are being purchased for my internal/personal consumption and not for resale. I further understand and agree to PTS Terms and Conditions.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
	            		
	            		
	            		<fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">Return Policy:</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell padding-left="0.05in">
                                <fo:block text-align="left" font-size="8pt">
                                    a) Products below Rs 150/- cannot be returned
                                </fo:block>  
                                <fo:block text-align="left" font-size="8pt">  
                                    b) 3 day return policy on products between the price range of Rs 151/- to Rs 300/-
                                </fo:block>  
                                <fo:block text-align="left" font-size="8pt">    
                                    c) 7 day return policy on non-electronic products above Rs 301/-
                                </fo:block>  
                                <fo:block text-align="left" font-size="8pt">    
                                    d) Electronics/electrical products above Rs 301/- can be returned as per manufacturer's warranty
                                </fo:block>  
                                <fo:block text-align="left" font-size="8pt">    
                                    e) Products purchased on discount during Mega Sale are not eligible for returns/refunds*
                                </fo:block>  
                                <fo:block text-align="left" font-size="8pt">    
                                    f) For more details, please visit http://www.palred.com/cancellation-refund.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        -->
                       
                       <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="center" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                         <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                       
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="center" font-size="8pt" font-weight="bold">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160; Signature</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                         <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="8pt" font-weight="bold">&#160;</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
	            	</fo:table-body>
	            
	            </fo:table>    
          	</fo:block>
          </fo:flow>
        </fo:page-sequence>
		



</fo:root>
</#escape>
