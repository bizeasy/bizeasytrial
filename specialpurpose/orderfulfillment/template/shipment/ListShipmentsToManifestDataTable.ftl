
<script type="text/javascript" language="javascript" class="init">	

	var carrierId = '${carrierId?if_exists}';
	var noConditionFind = '${noConditionFind?if_exists}';
	var statusId = '${statusId?if_exists}';
	
	var dataJson = {};
	$(document).ready(function () {

  		var url = 'getShipmentsReadyForShipping';
 		dataJson = {"carrierId":carrierId,"noConditionFind":noConditionFind,"statusId":statusId};
  		var table = $('#shipmentListingTable').DataTable({
     		"paging":   false,
     		"processing": false,
		    "serverSide": true,
		    "searching": false,
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
								var shipmentListJSON = result["shipmentListJSON"];
								return shipmentListJSON;
					   		}
					   	}
			       }, 
			     
			   "columnDefs": [
			         {
			            'targets': 0,
			            'checkboxes': {
			               'selectRow': true
			            }
			         }
			      ],
			   "select": {
			       //'style': 'multi'
			       "style":    'multi',
            	   "selector": 'td:first-child'
			   },        
			   "pagingType": "full_numbers",      
	           "fnRowCallback": function(nRow, shipmentListJSON, iDisplayIndex ) {
			     	$('td:eq(0)', nRow).html('<input type="checkbox" name="id[]" value=' + shipmentListJSON.shipmentId + '>');
			     	$('td:eq(1)', nRow).html('<a target="_blank" href="FindShipment?shipmentId=' + shipmentListJSON.shipmentId + '">' + shipmentListJSON.shipmentId + '</a>');
	           		return nRow;
			   },
			   "columns": [
			   		{
				        'render': function (data, type, full, meta){
				             return '<input type="checkbox" name="id[]">';
				         },
				        "className": "dt-body-center"
				        
				    },
		            { "data": "shipmentId"},
		            { "data": "orderId"},
		            { "data": "orderItemSeqId"},
		            { "data": "shipGroupSeqId"},
		            { "data": "shipmentMethodTypeId"},
		            { "data": "carrierPartyId"},
		            { "data": "facilityId"},
		            { "data": "trackingNumber"},
		            { "data": "statusId"},
		            { "data": "shipmentTypeId"}
		            
		       ]
		 });
		 $("#selectAll").click(function () {
            $('#shipmentListingTable tbody input[type="checkbox"]').prop('checked', this.checked);
        });
        
        $('#frm-example').on('submit', function(e){
	      var form = this;
		  var shipmentIds = "";
		  
	      table.$('input[type="checkbox"]').each(function(){
	         if(this.checked){
             	shipmentIds = shipmentIds + this.value + ",";
             }
	      });
	      	
	      shipmentIds = shipmentIds + ":" + carrierId;	
  		  $(form).append(
        	$('<input>')
            	.attr('type', 'hidden')
             	.attr('name', 'shipmentIds')
             	.val(shipmentIds)
       		);
       	 
       		
	   });
	});
	
	
	
	
</script>
<div id = "secondDiv" align="right" style=" height:35px;background-color: #AAD6F6;line-height: 35px;"><b>Total Shipments :</b> <label  align="center" id="totalOrders"style="color: #333333" ></label> &#160;&#160;&#160;&#160;&#160;&#160;</div>	
<form name="frm-example" id="frm-example" method="post" action="<@ofbizUrl>GenerateShipmentManifest</@ofbizUrl>">
	<p class="form-group" align="left">
   		<button type="submit" class="btn btn-primary">Generate Shipment Manifest</button>
	</p>
</form>	
	
	
	<table id="shipmentListingTable" class="display" width="100%" cellspacing="0">
	    <thead>
	        <tr>
	        	<th class="check">
	                <input type="checkbox" id="selectAll" value="">&nbsp;All</input>
	            </th>
	            <th>Shipment Id</th>
	            <th>orderId</th>
	            <th>orderItemSeqId</th>
	            <th>shipGroupSeqId</th>
	            <th>shipmentMethodTypeId</th>
	            <th>carrierPartyId</th>
	            <th>facilityId</th>
	            <th>trackingNumber</th>
	            <th>statusId</th>
	            <th>shipmentTypeId</th>
	            
	        </tr>
	    </thead>
	</table>





