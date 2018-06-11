<div class="screenlet">
    <div class="screenlet-title-bar">
    <form name="chooseProductStore" method="post" action="<@ofbizUrl>main</@ofbizUrl>">
      <label class="h3">Product Store</label>
       <select id="productStoreId" name="productStoreId" onchange="submit();">
       <#list productStores as prodStore>
       		<option value="${prodStore.productStoreId}" <#if prodStore.productStoreId == globalContext.productStoreId> selected</#if>>${prodStore.title?if_exists}</option>
       </#list>
        </select>
        <label class="h3">Origin Facility</label>
       <select id="facilityId" name="facilityId" onchange="submit();">
       <#list facilityList as facility>
       		<option value="${facility.facilityId}" <#if facility.facilityId == globalContext.facilityId> selected</#if>>${facility.facilityName?if_exists}</option>
       </#list>
        </select>
     </form>   
    </div>
</div>    
<script language="javascript" type="text/javascript">

</script>