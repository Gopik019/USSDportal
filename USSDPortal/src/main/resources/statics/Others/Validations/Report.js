$(document).ready(function() {  
	
	$(document).on('click', '#report_action', function() {
	
		 Generate_Report();
	});
	
	$(document).on('change', '#Report_type', function() {
	
		 Load_Report_Inputs();
	}); 
	
	/*$(document).on('click', '#popup_action', function() {
		
		  Render_Chart();
		
		  $('#Modal2').modal('show'); 	
	});  */
	
	Load_Report_Types();
	
	Intialize_Suggestions();
	
});

function Load_Report_Types()
{
	var data = new FormData();

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Report/Types",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{
				var details = data.details;
				
				for(var i=0; i<details.length; i++)
				{
					var SUBORGCODE = details[i].SUBORGCODE;
					var REPORT_CODE = details[i].REPORT_CODE;
					var REPORT_DESC = details[i].REPORT_DESC;
					var REPORT_SRC = details[i].REPORT_SRC;
					var REPORT_SRC_PARAM_CNT = details[i].REPORT_SRC_PARAM_CNT;
					var REPORT_OP_COL_CNT = details[i].REPORT_OP_COL_CNT;
					var STATUS = details[i].STATUS;
					
					var Value = SUBORGCODE +'|'+ REPORT_CODE +'|'+ REPORT_SRC +'|'+ REPORT_SRC_PARAM_CNT +'|'+ REPORT_OP_COL_CNT +'|'+ STATUS;
					
                    $("#Report_type").append($("<option  />").val(Value).text(REPORT_DESC));
				}
			}
			else
		    {
				Sweetalert("warning", "", data.Message);
		    }	
		},
		beforeSend: function( xhr )
 		{
 			
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	  
	    }
   });
}

function Load_Report_Inputs()
{
	 var data = new FormData();
	 
	 var value = $("#Report_type").val();
		
	 var text = $("#Report_type option:selected").text();
	
	 var arr = value.split("|");
	  
	 data.append("SUBORGCODE", arr[0]);
	 data.append("REPORT_CODE", arr[1]);
	 
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Report/Inputs",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{
				var details = data.details;
				
				Add_Dynamic_Inputs(details);
			}
			else
		    {
				$("#dyna_inputs").html('');
		    }	
		},
		beforeSend: function( xhr )
 		{
			
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	  
	    }
   });
}

function Intialize_Suggestions()
{
	$(document).on('keypress', '.TH', function() {
		
		var arr = $(this).attr('name');
		
		arr = arr.split("|");
		
		var FIELDNAME = $(this).attr('id');
		var REPORTCODE = arr[0];
		
		$(this).autocomplete({
			source: function(request, response) 
			{
		        $.ajax({
		            url: $("#ContextPath").val()+"/Report/Field/Suggestions",
		            type :  'POST',
		            dataType: "json",
		            data:  
					{    term : request.term,
		            	 FIELDNAME : FIELDNAME,
		            	 REPORTCODE : REPORTCODE
					},
		            success: function(data) { response(data); }
		        });
	    	},
		    minLength: 1,
	        }).autocomplete( "instance" )._renderItem = function(ul,item) 
		  	{
			 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
			}; 
	 }); 
}

function Add_Dynamic_Inputs(details)
{
	var content = '';
	
	for(var i=0; i<details.length; i++)
	{
		var SUBORGCODE = details[i].SUBORGCODE;
		var REPORT_CODE = details[i].REPORT_CODE;
		var REPROT_SL = details[i].REPROT_SL;
		var REPORT_FIELD = details[i].REPORT_FIELD;
		var REPORT_FIELD_DESC = details[i].REPORT_FIELD_DESC;
		var REPROT_FIELD_TYPE = details[i].REPROT_FIELD_TYPE;
		
		var Input_Type = REPROT_FIELD_TYPE == 'D' ? "date" : "text";
		
		var sub_content = '<div class="row justify-content-center">'+					                		
								'<div class="col-md-2">'+
									'<div class="form-group">'+	
										'<label for="">'+REPORT_FIELD_DESC+'</label>'+
									'</div>'+
								 '</div>'+									
								 '<div class="col-md-3">'+
								 	'<div class="form-group">'+
								 		'<input type="'+Input_Type+'" name="'+REPORT_CODE+'|'+REPROT_FIELD_TYPE+'" id="'+REPORT_FIELD+'" class="form-control inputs '+REPROT_FIELD_TYPE+'">'+
								 		'<label id="'+REPORT_FIELD+'_error" class="text-danger"></label>'+
									'</div>'+
								 '</div>'+																																					
							'</div>';
			
		content += sub_content;		
	}
	
	$("#dyna_inputs").html(content);
}

function Generate_Report()
{
	 var data = new FormData();
	 
	 var value = $("#Report_type").val();
		
	 var text = $("#Report_type option:selected").text();
	
	 var arr = value.split("|");
	  
	 data.append("Procedure_Name", arr[2]);
	 data.append("Total_Columns", Number(arr[4]));
	 
	 if($(".inputs").length != 0)
	 {
		 data.append("Inputs_labels", "SUBORGCODE");
		 data.append("Inputs_values", arr[0]);
		 data.append("Inputs_types", "T");
		 
		 for(var i=0; i<$(".inputs").length; i++)
		 {
			 var val = $(".inputs").eq(i).val();
			
			 var id = $(".inputs").eq(i).attr('id');
			
			 var name = $(".inputs").eq(i).attr('name');
			
			 var arr = name.split("|");
			 
			 data.append("Inputs_labels", id);
			 data.append("Inputs_values", val);
			 data.append("Inputs_types", arr[1]);
		 }
	 }
	 
	 $.ajax({		 
		url  :  $("#ContextPath").val()+"/Report/Generate",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{
				var details = data;    
				
				load_data(details);
				
				Swal.close();
			}
			else
			{
				Sweetalert("warning", "", data.Message);
			}
		},
		beforeSend: function( xhr )
		{
			 Sweetalert("load", "", "Generating Report...")
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	  
	    }
   });
}

function load_data(details)
{
	$('.data_report').html('<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" aria-describedby="example_info"></table>');
	
	var Heading_details = details.Heading_details;
	var Columns_details = details.Columns_details;
	var Report_details = details.Report_details;

	var len = Columns_details.length - 1;
	
	Columns_details = eval(Columns_details);
	Report_details = eval(Report_details);
	
	var table = $('#example').DataTable( {
		  "aaData": Report_details,
		  "aoColumns": Columns_details,
		  "paging":true,
		  "destroy": true,
		  "deferRender": true,
		  "responsive": true,
		  "ordering": false,
		  "dom" :
			    "<'row'<'col-sm-4'l><'col-sm-4 text-center'B><'col-sm-4'f>>"+
			    "<'row'<'col-sm-12'tr>>" +
			    "<'row'<'col-sm-6'i><'col-sm-6'p>>",
		  "buttons" : [
				  {
	                  extend: 'excelHtml5',
	                  text:'EXCEL',
	                  className: 'btn btn-secondary',
	                  exportOptions: {
	                	  columns: ':visible'
	                   }
	              },
	              {
	                  extend: 'pdfHtml5',
	                  text:'PDF',
	                  orientation: 'landscape',
	                  pageSize: 'LEGAL',
	                  className: 'btn btn-secondary',
	                  exportOptions: {
	                	  columns: ':visible'
	                   },
	                  customize: function ( doc ) {
                          doc.content.splice( 0, 0, {
                              margin: [ 0, 0, 0, 6 ],
                              alignment: 'left',
                              image: 'data:image/png;base64,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'
                          } ); 
                      }			
	              },
	              {
	                  extend: 'csvHtml5',
	                  text:'CSV',
	                  className: 'btn btn-secondary',
	                  exportOptions: {
	                	  columns: ':visible'
	                   }
	              },
	              {
	                  extend: 'print',
	                  text:'PRINT',
	                  className: 'btn btn-secondary',
	                  orientation: 'landscape',
                      pageSize: 'LEGAL',
                      exportOptions: {
                    	  columns: ':visible'
	                   }
	              }
		        ],
		        "columnDefs": [
	            {
	                "targets": [ 0 ],
	                "visible": false,
	                "searchable": false
	            },
	            {	
			    	  "targets" : [ len ],
			    	  "className": "flex_td",
			   		  "render"  : function (data, type, row, meta)
			    	   {
				   			if(type === 'display')
			        		{				  
				   				data = '<a href="#" class="text-secondary" onclick="open_chart(\''+data+'\')">View</a>';
			        		}
				   			
				   			return data;
			    	   }		
				},
		    ],
          "lengthMenu": [[5, 10, 50, 100, -1], [5, 10, 50, 100, "All"]],
		  "pageLength": 10					 
	}); 
	
	// $('#view').show();
	
	//$('#Modal2').modal('show'); 	
} 

function open_chart()
{
	Render_Chart();
	
	$('#Modal2').modal('show');	
}

function randomString(length) 
{
	var chars =  'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	
    var result = '';

    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];

    return result;
}

function validation(Id, Type)
{
	var valid = false;
	
	if(Type == "dd")
	{
		if($("#"+Id).val() != "Select" && $("#"+Id).val() != "") 
		{  
			valid = true;
		}
	}
	
	if(Type == "txt" && $("#"+Id).val() != '')
	{
		valid = true;
	}
	
	if(!valid)    
	{  
		$("#"+Id+"_error").html('Required');  
		$("#"+Id+"_error").show();	
		return false;
	}
	else
	{ 
		$("#"+Id+"_error").hide();	
		return true;
	}
}

function Render_Chart()
{
	var doughnutChart = document.getElementById('doughnutChart').getContext('2d');
	
	var doughnutChart2 = document.getElementById('doughnutChart2').getContext('2d');
	
	var myDoughnutChart = new Chart(doughnutChart, {
		type: 'doughnut',
		data: {
			datasets: [{
				data: [10, 20, 30],
				backgroundColor: ['#f3545d','#fdaf4b','#1d7af3']
			}],
			labels: [ 'Disbursed Amount','Paid Amount','Outstanding']
		},
		options: {
			responsive: true, 
			maintainAspectRatio: false,
			legend : {
				position: 'bottom'
			},
			layout: {
				padding: {
				left: 20,
				right: 20,
				top: 20,
				bottom: 20
				}
			}
		}
	});
	
	var myDoughnutChart2 = new Chart(doughnutChart2, {
		type: 'doughnut',
		data: {
			datasets: [{
				data: [10, 20, 30],
				backgroundColor: ['#f3545d','#fdaf4b','#1d7af3']
			}],
			labels: [ 'Principle','Interest','Penalty']
		},
		options: {
			responsive: true, 
			maintainAspectRatio: false,
			legend : {
				position: 'bottom'
			},
			layout: {
				padding: {
				left: 20,
				right: 20,
				top: 20,
				bottom: 20
				}
			}
		}
	});
}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text: Info ,			 
			  timer: 2000
		});
	}
	else if(Type == "success_load_Current")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,
			  timer: 2000
		}).then(function (result) {
			  if (true)
			  {							  
				  location.reload();
			  }
		});		
	}
	else if(Type == "error")
	{
		Swal.fire({
			  icon: 'error',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
		});
	}
	else if(Type == "validation")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  html: Info
		});
	}
	else if(Type == "load")
	{
		Swal.fire({
			  title: Title,
			  html:  Info,
			  timerProgressBar: true,
			  allowOutsideClick: false,
			  onBeforeOpen: () => {
			    Swal.showLoading()
			  },
			  onClose: () => {
			  }
		});	
	}	
}