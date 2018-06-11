import java.util.*;

import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.entity.util.*;
import org.apache.ofbiz.entity.condition.*;
import org.apache.ofbiz.service.ServiceUtil;
if(UtilValidate.isEmpty(parameters.productStoreId) && UtilValidate.isEmpty(globalContext.productStoreId) && UtilValidate.isNotEmpty(session.getAttribute("GlproductStoreId"))){
	parameters.productStoreId=session.getAttribute("GlproductStoreId");
	globalContext.productStoreId= session.getAttribute("GlproductStoreId");
}
if(UtilValidate.isEmpty(parameters.facilityId) && UtilValidate.isEmpty(globalContext.facilityId) && UtilValidate.isNotEmpty(session.getAttribute("GlfacilityId"))){
	parameters.facilityId=session.getAttribute("GlfacilityId");
	globalContext.facilityId= session.getAttribute("GlfacilityId");
}
exprsFrom = [EntityCondition.makeCondition("productStoreGroupId", EntityOperator.EQUALS, "LOGISTICS_CLIENTS")];
ecl = EntityCondition.makeCondition(exprsFrom, EntityOperator.AND);
productStoreGrpMembers = from("ProductStoreGroupMember").where(exprsFrom).queryList();
if(UtilValidate.isNotEmpty(productStoreGrpMembers)){
 productStoreIds = EntityUtil.getFieldListFromEntityList(productStoreGrpMembers,"productStoreId",true);
 if(UtilValidate.isNotEmpty(productStoreIds)){
   exprsFrom1 = [EntityCondition.makeCondition("productStoreId", EntityOperator.IN, productStoreIds)];
  productStores = from("ProductStore").where(exprsFrom1).queryList();
  context.productStores=productStores;
 }
}
exprsFrom2= [EntityCondition.makeCondition("facilityTypeId", EntityOperator.EQUALS, "WAREHOUSE")];
facilityList = from("Facility").where(exprsFrom2).queryList();
context.facilityList=facilityList;
if(UtilValidate.isEmpty(parameters.productStoreId) && UtilValidate.isEmpty(parameters.facilityId)){
	globalContext.productStoreId=EntityUtil.getFirst(productStores).getAt("productStoreId");
	globalContext.facilityId=EntityUtil.getFirst(facilityList).getAt("facilityId");
}else{
	globalContext.productStoreId= parameters.productStoreId;
	globalContext.facilityId = parameters.facilityId;
}

session.setAttribute("GlproductStoreId",globalContext.productStoreId);
session.setAttribute("GlfacilityId",globalContext.facilityId);

