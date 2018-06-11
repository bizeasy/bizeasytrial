
<script type="text/javascript" language="javascript" class="init">	

	var orderId = '${orderId?if_exists}';
	var externalId = '${referenceNo?if_exists}';
	var salesChannelEnumId = '${salesChannel?if_exists}';
	var statusId = '${statusId?if_exists}';
	
	var orderFromDate = '${orderFromDate?if_exists}';
	var orderThruDate = '${orderThruDate?if_exists}';
	var orderDateSort = '${orderDateSort?if_exists}';
	var noConditionFind = '${noConditionFind?if_exists}';
	
	var dataJson = {};
	$(document).ready(function () {

  		var url = 'getSalesOrdersWithPagination';
 		dataJson = {"orderId":orderId,"externalId":externalId,"noConditionFind":noConditionFind,"orderDateSort":orderDateSort,"statusId":statusId,"salesChannelEnumId":salesChannelEnumId,"orderTypeId":"SALES_ORDER","orderFromDate":orderFromDate,"orderThruDate":orderThruDate};
  		var table = $('#orderListingTable').DataTable({
     		
     		"processing": false,
		    "serverSide": true,
		    "pageLength": 10,
		    "searching": true,
		    "bInfo" : false,
		    "ordering": false,
			"ajax": {
					   "url": url,
					   "type": 'POST',
					   "data": dataJson,
					    "dataSrc": function(result){
							if(result["_ERROR_MESSAGE_"] || result["_ERROR_MESSAGE_LIST_"]){
							    alert("Error in order Items");
							}else{
								var orderListJSON = result["orderListJSON"];
								var orderDetails = result["orderListDetails"];
								
								var totalOrders = orderDetails.totalOrders;
								if(totalOrders && totalOrders !=0)
								$("#totalOrders").html(totalOrders);
								
								return orderListJSON;
					   		}
					   	}
			       }, 
			       
			   "pagingType": "full_numbers",      
	           "fnRowCallback": function(nRow, orderListJSON, iDisplayIndex ) {
			     	$('td:eq(0)', nRow).html('<a target="_blank" href="orderview?orderId=' + orderListJSON.orderId + '">' + orderListJSON.orderId + '</a>');
			     	$('td:eq(8)', nRow).html('<a target="_blank" href="FindShipment?shipmentId=' + orderListJSON.shipmentId + '">' + orderListJSON.shipmentId + '</a>');
			     	$('td:eq(9)', nRow).html('<a target="_blank" href="findInvoices?invoiceId=' + orderListJSON.invoiceId + '">' + orderListJSON.invoiceId + '</a>');
			     	
			     	if(orderListJSON.returnId == "Request Return")
				     $('td:eq(10)', nRow).html('<a target="_blank" href="createSalesReturns?orderId=' + orderListJSON.orderId + '">' + orderListJSON.returnId + '</a>');
				    else
				     $('td:eq(10)', nRow).html('<a target="_blank" href="findreturn?returnId=' + orderListJSON.returnId + '">' + orderListJSON.returnId + '</a>');
			     	
			     	$('td:eq(11)', nRow).html('<a target="_blank" href="cancelOrder?orderId=' + orderListJSON.orderId + '">' + orderListJSON.orderId + '</a>');
			     	
	           		return nRow;
			   },
			   "columns": [
		            { "data": "orderId"},
		            { "data": "orderFinalStatus"},
		            { "data": "orderDate"},
		            { "data": "externalId"},
		            { "data": "originFacilityId"},
		            { "data": "productStore"},
		            { "data": "grandTotal"},
		            { "data": "pickListId"},
		            { "data": "shipmentId"},
		            { "data": "invoiceId"},
		            { "data": "returnId"},
		            { "data": "replacementMessage"},
		            { "data": null,
		              "title": "Cancel"	
		            },
		            {
				        "title": "Select",
				        "data": null,
				        "render": function(data, type, row) {
				            return '<input type="checkbox" class="watchlist-checkbox">';
				            return data;
				        },
				        "className": "dt-body-center"
				    }
		       ]
		 });
	});

</script>
<div id = "secondDiv" align="right" style=" height:35px;background-color: #AAD6F6;line-height: 35px;"><b>Total Orders :</b> <label  align="center" id="totalOrders"style="color: #333333" ></label> &#160;&#160;&#160;&#160;&#160;&#160;<b>Select All </b><input type="checkbox" name="allOrders" id="allOrders"   onclick="javascript:checkAllOrders();"></div>	
 <table id="orderListingTable" class="display" width="100%" cellspacing="0">
    <thead>
        <tr>
            <th>Order Id</th>
            <th>Status</th>
            <th>Order Date</th>
            <th>Reference</th>
            <th>Facility</th>
            <th>Store</th>
            <th>Total</th>
            <th>PickList</th>
            <th>Shipment</th>
            <th>Invoice</th>
            <th>Returns</th>
            <th>Comments</th>
            <th>Cancel</th>
            <th>Select</th>
        </tr>
    </thead>
</table>
