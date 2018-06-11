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
<div class="screenlet">
  
  
  <div class="screenlet-title-bar">
    <ul>
      <li class="h3">${uiLabelMap.ProductFindOrdersToPick}</li>
    </ul>
    <br class="clear"/>
  </div>
  <div class="screenlet-body">
    
    <table cellspacing="0" class="basic-table">
      <#if pickMoveInfoList?has_content || rushOrderInfo?has_content>
        <tr class="header-row">
          <#if !((requestParameters.groupByShippingMethod?? && requestParameters.groupByShippingMethod == "Y") || (requestParameters.groupByWarehouseArea?? && requestParameters.groupByWarehouseArea == "Y") || (requestParameters.groupByNoOfOrderItems?? && requestParameters.groupByNoOfOrderItems == "Y"))>
            <td>${uiLabelMap.OrderOrder} ${uiLabelMap.CommonNbr}</td>
          <#else>
          	<#--
            <td>${uiLabelMap.ProductShipmentMethod}</td>
            <td>${uiLabelMap.ProductWarehouseArea}</td>
            -->
            <td>${uiLabelMap.ProductNumberOfOrderItems}</td>
          </#if>
          <td>${uiLabelMap.ProductReadyToPick}</td>
          <td>${uiLabelMap.ProductNeedStockMove}</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </#if>
      <#if rushOrderInfo?has_content>
        <#assign orderReadyToPickInfoList = rushOrderInfo.orderReadyToPickInfoList!>
        <#assign orderNeedsStockMoveInfoList = rushOrderInfo.orderNeedsStockMoveInfoList!>
        <#assign orderReadyToPickInfoListSize = (orderReadyToPickInfoList.size())?default(0)>
        <#assign orderNeedsStockMoveInfoListSize = (orderNeedsStockMoveInfoList.size())?default(0)>
        <tr>
          <td>[Rush Orders, all Methods]</td>
          <td>${orderReadyToPickInfoListSize}</td>
          <td>${orderNeedsStockMoveInfoListSize}</td>
          <td>
            <#if orderReadyToPickInfoList?has_content>
              <form method="post" action="<@ofbizUrl>createPicklistFromOrders</@ofbizUrl>">
                <input type="hidden" name="facilityId" value="${facilityId}"/>
                <input type="hidden" name="isRushOrder" value="Y"/>
                ${uiLabelMap.ProductPickFirst}:
                <input type="text" size="4" name="maxNumberOfOrders" value="24"/>
                <input type="submit" value="${uiLabelMap.ProductCreatePicklist}"/>
              </form>
            <#else>
              &nbsp;
            </#if>
          </td>
        </tr>
      </#if>
      <#if pickMoveInfoList?has_content>
        <#assign orderReadyToPickInfoListSizeTotal = 0>
        <#assign orderNeedsStockMoveInfoListSizeTotal = 0>
        <#assign alt_row = false>
        <#list pickMoveInfoList as pickMoveInfo>
          <#assign groupName = pickMoveInfo.groupName!>
          <#assign groupName1 = pickMoveInfo.groupName1!>
          <#assign groupName2 = pickMoveInfo.groupName2!>
          <#assign groupName3 = pickMoveInfo.groupName3!>
          <#assign orderReadyToPickInfoList = pickMoveInfo.orderReadyToPickInfoList!>
          <#assign orderNeedsStockMoveInfoList = pickMoveInfo.orderNeedsStockMoveInfoList!>
          <#assign orderReadyToPickInfoListSize = (orderReadyToPickInfoList.size())?default(0)>
          <#assign orderNeedsStockMoveInfoListSize = (orderNeedsStockMoveInfoList.size())?default(0)>
          <#assign orderReadyToPickInfoListSizeTotal = orderReadyToPickInfoListSizeTotal + orderReadyToPickInfoListSize>
          <#assign orderNeedsStockMoveInfoListSizeTotal = orderNeedsStockMoveInfoListSizeTotal + orderNeedsStockMoveInfoListSize>
          <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
                <td>
                    <form name="viewGroupDetail_${pickMoveInfo_index}" action="<@ofbizUrl>PicklistOptions</@ofbizUrl>" method="post">
                      <input type ="hidden" name="viewDetail" value= "${groupName!}"/>
                      <input type="hidden" name="groupByShippingMethod" value="${requestParameters.groupByShippingMethod!}"/>
                      <input type="hidden" name="groupByWarehouseArea" value="${requestParameters.groupByWarehouseArea!}"/>
                      <input type="hidden" name="groupByNoOfOrderItems" value="${requestParameters.groupByNoOfOrderItems!}"/>
                      <input type="hidden" name="facilityId" value="${facilityId!}"/>
                    </form>
	                <#if groupName3?has_content>
	                	<a href="javascript:document.viewGroupDetail_${pickMoveInfo_index}.submit()" class="buttontext">${groupName3}</a></td>
	                </#if>
                </td>
            <td>
              ${orderReadyToPickInfoListSize}
            </td>
            <td>
              ${orderNeedsStockMoveInfoListSize}
            </td>
            <td>
              <#if orderReadyToPickInfoList?has_content>
                <form method="post" action="<@ofbizUrl>createPicklistFromOrders</@ofbizUrl>">
                  <input type="hidden" name="facilityId" value="${facilityId!}"/>
                  <input type="hidden" name="groupByShippingMethod" value="${requestParameters.groupByShippingMethod!}"/>
                  <input type="hidden" name="groupByWarehouseArea" value="${requestParameters.groupByWarehouseArea!}"/>
                  <input type="hidden" name="groupByNoOfOrderItems" value="${requestParameters.groupByNoOfOrderItems!}"/>
                  <input type="hidden" name="orderIdList" value=""/>
                  <#assign orderIdsForPickList = orderReadyToPickInfoList!>
                  <#list orderIdsForPickList as orderIdForPickList>
                    <input type="hidden" name="orderIdList" value="${orderIdForPickList.orderHeader.orderId}"/>
                  </#list>
                  <span class="label">${uiLabelMap.ProductPickFirst}</span>
                  <input type="text" size="4" name="maxNumberOfOrders" value="24"/>
                  <input type="submit" value="${uiLabelMap.ProductCreatePicklist}"/>
                </form>
              <#else>
                &nbsp;
              </#if>
            </td>
            <td>
              <#if orderReadyToPickInfoList?has_content>
                <form method="post" action="<@ofbizUrl>printPickSheets</@ofbizUrl>" target="_blank">
                  <input type="hidden" name="printGroupName" value="${groupName!}"/>
                  <input type="hidden" name="facilityId" value="${facilityId!}"/>
                  <input type="hidden" name="groupByShippingMethod" value="${requestParameters.groupByShippingMethod!}"/>
                  <input type="hidden" name="groupByWarehouseArea" value="${requestParameters.groupByWarehouseArea!}"/>
                  <input type="hidden" name="groupByNoOfOrderItems" value="${requestParameters.groupByNoOfOrderItems!}"/>
                  <span class="label">${uiLabelMap.FormFieldTitle_printPickSheetFirst}</span>
                  <input type="text" size="4" name="maxNumberOfOrdersToPrint" value="24"/>
                  <input type="submit" value="${uiLabelMap.FormFieldTitle_printPickSheet}"/>
                </form>
              <#else>
                &nbsp;
              </#if>
            </td>
          </tr>
          <#-- toggle the row color -->
          <#assign alt_row = !alt_row>
        </#list>
        <tr<#if alt_row> class="alternate-row"</#if>>
            <th>${uiLabelMap.CommonAllMethods}</div></th>
            <th>${orderReadyToPickInfoListSizeTotal}</div></th>
            <th>${orderNeedsStockMoveInfoListSizeTotal}</div></th>
            <td>
              <#if (orderReadyToPickInfoListSizeTotal > 0)>
                <form method="post" action="<@ofbizUrl>createPicklistFromOrders</@ofbizUrl>">
                  <input type="hidden" name="facilityId" value="${facilityId!}"/>
                  <span class="label">${uiLabelMap.ProductPickFirst}</span>
                  <input type="text" size="4" name="maxNumberOfOrders" value="24"/>
                  <input type="submit" value="${uiLabelMap.ProductCreatePicklist}"/>
                </form>
              <#else>
                &nbsp;
              </#if>
            </td>
            <td>
              <#if (orderReadyToPickInfoListSizeTotal > 0)>
                <form method="post" action="<@ofbizUrl>printPickSheets</@ofbizUrl>" target="_blank">
                  <input type="hidden" name="facilityId" value="${facilityId!}"/>
                  <span class="label">${uiLabelMap.FormFieldTitle_printPickSheetFirst}</span>
                  <input type="text" size="4" name="maxNumberOfOrdersToPrint" value="24"/>
                  <input type="submit" value="${uiLabelMap.FormFieldTitle_printPickSheet}"/>
                </form>
              <#else>
                &nbsp;
              </#if>
            </td>
          </tr>
      <#else>
        <tr><td colspan="4"><h3>${uiLabelMap.ProductNoOrdersFoundReadyToPickOrNeedStockMoves}.</h3></td></tr>
      </#if>
    </table>
  </div>
</div>
<#assign viewDetail = requestParameters.viewDetail!>
<#if viewDetail?has_content>
  <#list pickMoveInfoList as pickMoveInfo>
    <#assign groupName = pickMoveInfo.groupName!>
    <#if groupName! == viewDetail>
      <#assign toPickList = pickMoveInfo.orderReadyToPickInfoList!>
    </#if>
  </#list>
</#if>

<#if toPickList?has_content>
  <div class="screenlet">
    <div class="screenlet-title-bar">
      <ul>
        <li class="h3">${uiLabelMap.ProductPickingDetail}</li>
      </ul>
      <br class="clear"/>
    </div>
    <div class="screenlet-body">
      <table cellspacing="0" class="basic-table">
        <tr class="header-row">
          <td>${uiLabelMap.ProductOrderId}</td>
          <td>${uiLabelMap.FormFieldTitle_orderDate}</td>
          <td>${uiLabelMap.ProductChannel}</td>
          <td>${uiLabelMap.ProductOrderItem}</td>
          <td>${uiLabelMap.ProductProductDescription}</td>
          <td>${uiLabelMap.ProductOrderShipGroupId}</td>
          <td>${uiLabelMap.ProductQuantity}</td>
          <td>${uiLabelMap.ProductQuantityNotAvailable}</td>
        </tr>
        <#assign alt_row = false>
        <#list toPickList as toPick>
          <#assign oiasgal = toPick.orderItemShipGrpInvResList>
          <#assign header = toPick.orderHeader>
          <#assign channel = header.getRelatedOne("SalesChannelEnumeration", false)!>
          <#list oiasgal as oiasga>
            <#assign orderProduct = oiasga.getRelatedOne("OrderItem", false).getRelatedOne("Product", false)!>
            <#assign product = oiasga.getRelatedOne("InventoryItem", false).getRelatedOne("Product", false)!>
            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
              <td><a href="/ordermgr/control/orderview?orderId=${oiasga.orderId}${StringUtil.wrapString(externalKeyParam)}" class="buttontext" target="_blank">${oiasga.orderId}</a></td>
              <td>${header.orderDate?string}</td>
              <td>${(channel.description)!}</td>
              <td>${oiasga.orderItemSeqId}</td>
              <td>
                <a href="/catalog/control/EditProduct?productId=${orderProduct.productId!}${StringUtil.wrapString(externalKeyParam)}" class="buttontext" target="_blank">${(orderProduct.internalName)!}</a>
                <#if orderProduct.productId != product.productId>
                  &nbsp;[<a href="/catalog/control/EditProduct?productId=${product.productId!}${StringUtil.wrapString(externalKeyParam)}" class="buttontext" target="_blank">${(product.internalName)!}</a>]
                </#if>
              </td>
              <td>${oiasga.shipGroupSeqId}</td>
              <td>${oiasga.quantity}</td>
              <td>${oiasga.quantityNotAvailable!}</td>
            </tr>
          </#list>
          <#-- toggle the row color -->
          <#assign alt_row = !alt_row>
        </#list>
      </table>
    </div>
  </div>
</#if>
