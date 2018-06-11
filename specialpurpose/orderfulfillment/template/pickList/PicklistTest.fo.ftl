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
        margin-top="0.65in" margin-bottom="0.17in" margin-left=".4in" margin-right=".2in">
          <fo:region-body margin-top="0in"/>
          <fo:region-before extent="0in"/>
          <fo:region-after extent="0in"/>
      </fo:simple-page-master>
    </fo:layout-master-set>
    
	<#if pickList?has_content>
		<fo:page-sequence master-reference="main">
          <fo:flow flow-name="xsl-region-body" font-family="sans-serif" font-size="9">
          	<fo:block>
          		<fo:table width="100%" table-layout="fixed">
	                <fo:table-column column-width="2.68in" />
	                <fo:table-column column-width="2.68in"/>
	                <fo:table-column column-width="2.68in"/>
	            	<fo:table-body>
	            	
	            		
	            				
	            			<fo:table-row>
		            			
		            			<#assign columnNo = 0>
		            			<#assign orderItemCount = 0>
		            			<#list pickList as pickListInfo>
		            				<#assign orderItemCount = orderItemCount + 1>
		            				<#if columnNo == 3>
			                    		</fo:table-row>
			                    		<fo:table-row>
			                    		<#assign columnNo = 0>
			                    	</#if>
		            				
			            			<fo:table-cell height="1.1in" padding-bottom="0.2in" >
				                    	<fo:block font-weight="bold" font-size="10">
				                      		&#160;(# ${orderItemCount}) &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;    Pickup ${pickListInfo.itemCount}/${pickListInfo.orderItemSize}
				                      	</fo:block>
				                      	<fo:block>
					                      	<fo:table width="100%" table-layout="fixed">
								                <fo:table-column column-width="0.65in"/>
								                <fo:table-column column-width="1.6in"/>
								            	<fo:table-body>
					                      			<fo:table-row border-style="solid">
					                      				<fo:table-cell border-style="solid">
									                      <fo:block font-weight="bold">
									                      	&#160;Order
									                      </fo:block>
									                    </fo:table-cell>
					                      				<fo:table-cell border-style="solid">
									                      <fo:block>
															<fo:instream-foreign-object>
											                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
											                            message="${pickListInfo.orderId}">
											                        <barcode:code39>
											                            <barcode:height>10mm</barcode:height>
											                        </barcode:code39>
											                    </barcode:barcode>
											                </fo:instream-foreign-object>
									                      </fo:block>
									                    </fo:table-cell>
					                      			</fo:table-row>
					                      			<fo:table-row border-style="solid">
					                      				<fo:table-cell border-style="solid">
									                      <fo:block font-weight="bold">
									                      	&#160;Item
									                      </fo:block>
									                    </fo:table-cell>
					                      				<fo:table-cell border-style="solid">
									                      <fo:block>
									                      	&#160;${pickListInfo.productId}
									                      </fo:block>
									                    </fo:table-cell>
					                      			</fo:table-row>
					                      			<fo:table-row border-style="solid">
					                      				<fo:table-cell border-style="solid">
									                      <fo:block font-weight="bold">
									                      	&#160;Location
									                      </fo:block>
									                    </fo:table-cell>
					                      				<fo:table-cell border-style="solid">
									                      <fo:block>
									                      	&#160;${pickListInfo.location}
									                      </fo:block>
									                    </fo:table-cell>
					                      			</fo:table-row>
					                      		</fo:table-body>
				            				</fo:table>
				                      
				                    	</fo:block>
				                    	<fo:block font-weight="bold" text-align="center" font-size="10">
					                      	<#if (pickListInfo.orderItemSize) != 1>
					                      		Multi PickList
					                      	<#else>	
					                      		Single PickList
					                      	</#if>
					                      	
					                    </fo:block>
				                    </fo:table-cell>
			                    	
			                    	<#assign columnNo = columnNo+1>
			                    </#list>
		            		</fo:table-row>	
	            			
	            			
	            	
	            	
	            	
	            		
	            	
	            	</fo:table-body>
	            
	            </fo:table>    
          	</fo:block>
          </fo:flow>
        </fo:page-sequence>
		
	</#if>



</fo:root>
</#escape>
