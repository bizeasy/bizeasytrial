

import org.apache.ofbiz.base.util.*;

productStoreGroupMembers =  from("ProductStoreGroupMember").where("productStoreGroupId", "LOGISTICS_CLIENTS").queryList();
context.productStoreGroupMembers = productStoreGroupMembers;

Debug.log("productStoreGroupMembers ==============="+productStoreGroupMembers);

facilitiesList =  from("Facility").where("facilityTypeId", "WAREHOUSE").queryList();
context.facilitiesList = facilitiesList;

supplerPartyRoleAndPartyDetails = from("PartyRoleAndPartyDetail").where(roleTypeId : "SUPPLIER").orderBy("groupName", "firstName").queryList();
context.supplerPartyRoleAndPartyDetails = supplerPartyRoleAndPartyDetails;