	import org.apache.ofbiz.base.util.UtilDateTime;
	
	
	import java.sql.Timestamp;
	import java.text.SimpleDateFormat;
	
	import java.sql.Timestamp;
	import java.text.ParseException;
	import java.text.SimpleDateFormat;
	import java.util.List;
	import org.apache.ofbiz.base.util.*;
	import org.apache.ofbiz.entity.condition.EntityCondition;
	import org.apache.ofbiz.entity.condition.EntityOperator;
	import org.apache.ofbiz.entity.util.EntityUtil;
	import org.apache.ofbiz.entity.util.EntityFindOptions;
	import org.apache.ofbiz.entity.GenericValue;
	
	dctx = dispatcher.getDispatchContext();
	Map boothsPaymentsDetail = [:];
	
	partyId = userLogin.get("partyId");
	salesChannel = parameters.salesChannelEnumId;
	orderId = parameters.orderId;
	productStoreId = parameters.productStoreId;
	referenceNo = parameters.referenceNo;
	statusId = parameters.statusId;
	
	startDate = parameters.estimatedDeliveryDate;
	endDate = parameters.estimatedDeliveryThruDate;
	
	orderList=[];
	
	condList = [];
	if(UtilValidate.isNotEmpty(orderId)){
		condList.add(EntityCondition.makeCondition("orderId" ,EntityOperator.LIKE, "%"+orderId + "%"));
	}
	if(UtilValidate.isNotEmpty(statusId)){
		condList.add(EntityCondition.makeCondition("statusId" ,EntityOperator.EQUALS, statusId));
	}
	if(UtilValidate.isNotEmpty(endDate)){
		condList.add(EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN_EQUAL_TO, endDate));
	}
	if(UtilValidate.isNotEmpty(endDate)){
		condList.add(EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, startDate));
	}
	List<String> orderBy = UtilMisc.toList("-orderDate");
	cond = EntityCondition.makeCondition(condList, EntityOperator.AND);
	orderHeaderList = delegator.findList("OrderHeader", cond, null, orderBy, null ,false);
	
	Debug.log("orderHeaderList ============"+orderHeaderList);
	
	context.orderList = orderHeaderList;
