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
      <fo:simple-page-master master-name="main" page-height="15cm" page-width="10cm"
        margin-top="0.3cm" margin-bottom="0.3cm" margin-left=".3cm" margin-right=".3cm">
          <fo:region-body margin-top="0.3cm"/>
          <fo:region-before extent="0.3cm"/>
          <fo:region-after extent="0.3cm"/>
      </fo:simple-page-master>
    </fo:layout-master-set>
    <#assign fileName = "${primaryOrderHeader.externalId}_${primaryOrderHeader.orderId}_${shipmentId}_SL.pdf"/>
    ${setRequestAttribute("OUTPUT_FILENAME", fileName)}
		<fo:page-sequence master-reference="main">
          <fo:flow flow-name="xsl-region-body" font-family="sans-serif" font-size="10">
          	<fo:block>
          		<fo:table width="100%" table-layout="fixed">
	                <fo:table-column column-width="100%"/>
	            	<fo:table-body>
	        			<fo:table-row border-bottom-style="solid">
	            			<fo:table-cell padding-bottom="0.05in" padding-top="0.1in">
		                      <fo:block text-align="center" font-size="15pt">
		                      	
		                      	CASH ON DELIVERY
		                      	<#--
		                      	<#if paymentType?has_content>
		                      		${paymentType}
		                      	<#else>
		                      		PREPAID	
		                      	</#if>
		                      	-->
		                      	
		                      </fo:block>
		                      <fo:block text-align="center" font-size="12pt" font-weight="bold">
		                      	Order Amount: Rs ${primaryOrderHeader.grandTotal}  
		                      </fo:block>
		                      <fo:block text-align="center" font-size="12pt">
		                      	<fo:instream-foreign-object>
				                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
				                            message="${primaryOrderHeader.orderId}">
				                        <barcode:code39>
				                            <barcode:height>11mm</barcode:height>
				                            <barcode:module-width>0.3mm</barcode:module-width>
				                        </barcode:code39>
				                    </barcode:barcode>
				                </fo:instream-foreign-object>
		                      </fo:block>
		                    </fo:table-cell>	
		                    
	            			
	            		</fo:table-row>	
	            		<fo:table-row border-bottom-style="solid">
	            			<fo:table-cell padding-bottom="0.05in" padding-top="0.05in">
		                      <fo:block>
		                      	<fo:table width="100%" table-layout="fixed">
					                <fo:table-column column-width="70%"/>
					                <fo:table-column column-width="30%"/>
					            	<fo:table-body>
					        			<fo:table-row>
		                      				<fo:table-cell font-size="8pt">
		                      					<fo:block font-size="8pt">
		                      						To:
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						&#160;
		                      					</fo:block> 
		                      					<fo:block font-size="8pt">
		                      						${destinationPostalAddress.toName?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						${destinationPostalAddress.address1?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						${destinationPostalAddress.address2?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						${destinationPostalAddress.city?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						Pin: ${destinationPostalAddress.postalCode?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						<#assign stateGeo = (delegator.findOne("Geo", {"geoId", destinationPostalAddress.stateProvinceGeoId!}, false))! />
		                      						<#assign countryGeo = (delegator.findOne("Geo", {"geoId", destinationPostalAddress.countryGeoId!}, false))! />
		                      						
		                      						${stateGeo.geoName?if_exists},  ${countryGeo.geoName?if_exists}
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						&#160;
		                      					</fo:block>
		                      					<fo:block font-size="8pt">
		                      						Phone: ${shipToTelecomNumber.contactNumber?if_exists}
		                      					</fo:block>
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt">
		                      						Date: ${Static["org.apache.ofbiz.base.util.UtilDateTime"].toDateString(primaryOrderHeader.orderDate, "dd/MM/yyyy")}
		                      					</fo:block>
					            				<fo:block>
					            					<fo:external-graphic src="http://www.palred.com/img/Logo.png" height="20px" content-height="scale-to-fit"/>
					            				</fo:block>
					            			</fo:table-cell>	
	            						</fo:table-row>	
	            					</fo:table-body>	
		                      	</fo:table>  
		                      </fo:block>
		                     
		                      
		                    </fo:table-cell>	
	            		</fo:table-row>	
	            		<fo:table-row border-bottom-style="solid">
	            			<fo:table-cell height="15pt" padding-bottom="0.05in" padding-top="0.05in">
		                      <fo:block text-align="center" font-size="12pt">
		                      	<fo:instream-foreign-object>
				                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
				                            message="${airwayBillNumber}">
				                        <barcode:code39>
				                            <barcode:height>11mm</barcode:height>
				                            <barcode:module-width>0.3mm</barcode:module-width>
				                            <barcode:font-name>arial</barcode:font-name>
				                        </barcode:code39>
				                    </barcode:barcode>
				                </fo:instream-foreign-object>
		                      </fo:block>
		                      
		                      <fo:block>
		                      	<fo:table width="100%" table-layout="fixed">
					                <fo:table-column column-width="60%"/>
					                <fo:table-column column-width="40%"/>
					            	<fo:table-body>
					        			<fo:table-row>
		                      				<fo:table-cell font-size="8pt" height="15pt">
		                      					<fo:block font-size="8pt">
		                      						AWB # : ${airwayBillNumber}
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt" font-weight="bold" text-align="right">
		                      						${carrierId}
		                      					</fo:block>
					            				
					            			</fo:table-cell>	
	            						</fo:table-row>	
	            					</fo:table-body>	
		                      	</fo:table>  
		                      </fo:block>
		                      <fo:block>
		                      	<fo:table width="100%" table-layout="fixed">
					                <fo:table-column column-width="30%"/>
					                <fo:table-column column-width="30%"/>
					                <fo:table-column column-width="40%"/>
					            	<fo:table-body>
					        			<fo:table-row>
		                      				<fo:table-cell font-size="8pt">
		                      					<fo:block font-size="8pt">
		                      						No of pieces: ${noOfPieces}
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt" text-align="center">
		                      						Quantity: ${noOfPieces}
		                      					</fo:block>
					            			</fo:table-cell>
					            			<fo:table-cell>
		                      					<fo:block font-size="8pt" text-align="right">
		                      						Total Weight(${firstItemUom}): ${totalWt?string("0.00")}
		                      					</fo:block>
					            			</fo:table-cell>	
	            						</fo:table-row>	
	            					</fo:table-body>	
		                      	</fo:table>  
		                      </fo:block>
		                    </fo:table-cell>	
		                    
	            			
	            		</fo:table-row>
	            		
	            		<fo:table-row border-bottom-style="solid">
	            			<fo:table-cell padding-bottom="0.05in" padding-top="0.05in">
		                      	                      
		                      <fo:block>
		                      	<fo:table width="100%" table-layout="fixed">
					                <fo:table-column column-width="80%"/>
					                <fo:table-column column-width="20%"/>
					            	<fo:table-body>
					        			<fo:table-row>
		                      				<fo:table-cell font-size="8pt" height="15pt">
		                      					<fo:block font-size="8pt">
		                      						Description :
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt" text-align="right">
		                      						Weight(${firstItemUom})
		                      					</fo:block>
					            				
					            			</fo:table-cell>	
	            						</fo:table-row>	
	            						<fo:table-row>
		                      				<fo:table-cell font-size="8pt">
		                      					<fo:block font-size="8pt">
		                      						${product.productName}
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt" text-align="right">
		                      						${firstItemWt?string("0.00")}
		                      					</fo:block>
					            				
					            			</fo:table-cell>	
	            						</fo:table-row>
	            					</fo:table-body>	
		                      	</fo:table>  
		                      </fo:block>
		                      
		                    </fo:table-cell>	
		                    
	            			
	            		</fo:table-row>
	            		
	            		
	            		<fo:table-row>
	            			<fo:table-cell padding-bottom="0.05in" padding-top="0.05in">
		                      <fo:block>
		                      	<fo:table width="100%" table-layout="fixed">
					                <fo:table-column column-width="12%"/>
					                <fo:table-column column-width="88%"/>
					            	<fo:table-body>
					        			<fo:table-row>
		                      				<fo:table-cell font-size="8pt">
		                      					<fo:block font-size="8pt">
		                      						From:
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell>
		                      					<fo:block font-size="8pt">
		                      						Palred Technology Services Private Limited
		                      					</fo:block>
					            				
					            			</fo:table-cell>	
	            						</fo:table-row>	
	            						<fo:table-row>
		                      				<fo:table-cell font-size="8pt">
		                      					<fo:block font-size="8pt">
		                      						RTO:
		                      					</fo:block> 
		                      				</fo:table-cell>
		                      				<fo:table-cell font-size="8pt">
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
												<fo:block font-size="8pt">	
													TIN NO : 07AAHCP9699Q1Z2
		                      					</fo:block>
					            				
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
          </fo:flow>
        </fo:page-sequence>
		



</fo:root>
</#escape>
