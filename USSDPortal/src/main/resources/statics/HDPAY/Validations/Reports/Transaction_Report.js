var table;

$(document).ready(function() {
	
	table = $('#example').DataTable();
	
	 Generate_Report();
	
	 Search_on_header();
	
	 $(document).on('click', '#filter_search', function() {  
			
		 if($('#min').val() != "" || $('#max').val() != "")
		 { 
			  future_date();
			 
		 }
		 else
		 {
			 return;
		 }
	 });
	 
	 $(document).on('click', '#filter_reset', function() {  
		
		 Reset();
	 });
	 
	 $(document).on('click', '#hold_submit', function() {  
			
		 Hold_Request();
	 }); 
});

function Date_Range_Search()
{
	$.fn.dataTable.ext.search.push(function( settings, data, dataIndex ) { 
	    
    	var min =  moment($('#min').val()).format('YYYY-MM-DD');
    	var max =  moment($('#max').val()).format('YYYY-MM-DD');
    	var date =  moment(data[4], 'DD/MM/YYYY').format('YYYY-MM-DD');
    
    	if(( min === "" && max === "" ) || ( min === null && max === null ) ||  ( min === null && date <= max ) || ( min <= date   && max === null ) ||  ( min <= date   && date <= max ))
    	{
    		return true;
    	}
    
    	return false;
	});
}

function Search_on_header()
{
	$('#example thead tr').clone(true).appendTo( '#example thead' );
   
	$('#example thead tr:eq(1) th').each( function (i) {
    	
        var title = $(this).text();
        
        $(this).html('<input type="text" placeholder="Search '+title+'" />');
 
        $('input', this).on( 'keyup change', function () {
        	
            if(table.column(i).search() !== this.value ) 
            {
                table.column(i).search(this.value).draw();
            }
        });
    });
}

function Search_on_footer()
{
	 $('#example tfoot th').each( function (i) {
		 
	        var title = $(this).text();
	        
	        $(this).html('<input type="text" placeholder="Search '+title+'" />');
	        
	        $('input', this ).on( 'keyup change', function () {
	        	
	             if(table.column(i).search() !== this.value) 
	             {
	                 table.column(i).search( this.value) .draw();     
	             }
	        });
	    }); 
}

function Generate_Report()
{	
	var data = new FormData();

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Transaction_Reports",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			Swal.close();
			
			if(data.Result == 'Success')
			{
				var reports = data.Transaction_Reports;

				reports = eval(reports);
				
				table = $('#example').DataTable( {
					  "aaData": reports,
					  "aoColumns": [
							{ "mData": "ROWNUM"			},			
							{ "mData": "PAYTYPE"		},  	
							{ "mData": "CHCODE"			},		
							{ "mData": "FLOW"  		    },  	   
							{ "mData": "PAYDATE"		},	        
							{ "mData": "REQREFNO"		},		
							{ "mData": "TRANREFNO"		},		
							{ "mData": "S_ACCOUNT" 		},	
							{ "mData": "D_ACCOUNT"   	},	   	    		
							{ "mData": "TRANAMT"      	},			
							{ "mData": "TRANCURR"     	},		
							{ "mData": "INVOICENO"   	},	
							{ "mData": "STATUS"       	},
							{ "mData": "RESCODE"       	},
							{ "mData": "RESPDESC"   	},
							{ "mData": "REQSL"			}, 
							{ "mData": "ERRCD"			}, 
							{ "mData": "ERRDESC"		},
					  ],
					  "scrollX": true,
					  "scrollY": "330px",
				      "scrollCollapse": true,
					  "paging":true,
					  "destroy": true,
					  "deferRender": true,
					  "responsive": true,
					  "autoWidth": false,
					  //"bSort": false,
					  "dom": "<'row'<'col-sm-2'l><'col-sm-6 text-center'<'toolbar'>><'col-sm-1 text-center'<'resetbar'>><'col-sm-3'f>>"+
					     	 "<'row'<'col-sm-12'tr>>" +
					     	 "<'row'<'col-sm-4'i><'col-sm-4 text-center'B><'col-sm-4'p>>",
			          "buttons": [
		                    {
		                        extend: 'excelHtml5',
		                        title: 'Transaction_Reports',
		                        text:'Excel',         
		                        className: 'btn btn-secondary mt-3' 
		                    },
		                    {
		                        extend: 'pdfHtml5',
		                        title: 'Transaction_Reports',
		                        text: 'PDF',
		                        className: 'btn btn-secondary mt-3',
		                        orientation: 'landscape',
		                        pageSize: 'LEGAL',
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
		                        title: 'Transaction_Reports',
		                        text: 'CSV',
		                        className: 'btn btn-secondary mt-3'			                       
		                    },
		                    {
		                        extend: 'copy',
		                        title: 'Transaction_Reports',
		                        text: 'COPY',
		                        className: 'btn btn-secondary mt-3'			                       
		                    }
			            ],
			            "columnDefs" : [
						   {
					    	 "targets" : 13,
					   		 "render"  : function (data, type, row, meta)
					    	  {
						        	if (type === 'display')
						        	{
						        		if(row.ERRCD != '')
						        	    {
						        			data = row.ERRCD;					        
						        	    }
						        		else
						        		{
						        			data = row.RESCODE;
						        		}						        		
						        	}

						        	return data;
					    	  }
						  },
						  {
					    	 "targets" : 14,
					   		 "render"  : function (data, type, row, meta)
					    	  {
					        	if (type === 'display')
					        	{
					        		if(row.ERRCD != '')
					        	    {
					        			if(row.RESPDESC == '')
					        			{
					        				data = row.ERRDESC;
					        			}
					        			else
					        			{
					        				data = row.RESPDESC;
					        			}
					        	    }
					        		else
					        		{
					        			data = row.RESPDESC;
					        		}						        		
					        	 }

					        		return data;
					    	  	}
							  },
							  {
						    	 "targets" : 3,
						   		 "render"  : function (data, type, row, meta)
						    	  {
						        	if (type === 'display')
						        	{
						        		if(row.FLOW == 'I')
						        	    {
						        			data = 'C';
						        	    }
						        		else 
						        		{
						        			data = 'D';
						        		}	
						        	}

						        	return data;
						    	 }
						   },	
						   {
						    	"targets" : [ 15, 16, 17],
						   		"visible" : false
						   }	
						],
			          "lengthMenu": [[5, 10, 50, 75, -1], [5, 10, 50, 75, "All"]],
					  "pageLength": 10						 
				}); 
				
				$('.dataTables_paginate').addClass('mt-3');
				
				var Max_date = get_currentDate();
				
				var content = '<div class="row">' +
									'<div class="col-md-2 col-sm-6"><label><b> From: <b></label></div>'+ 
								'<div class="col-md-4 col-sm-6">'+ 
								  	'<input type="date" id="min" class="form-control" max="'+Max_date+'">' +  
								'</div>'+
								'<div class="col-md-2 col-sm-6"><label><b> To : </b></label></div>'+ 
								'<div class="col-md-4 col-sm-6">'+ 
								  	'<input type="date" id="max" class="form-control" max="'+Max_date+'">' + 
								'</div>'+
						    '</div>'; 
				
				var Reset_Content = '<div class="row">' +
									'<div class="col-md-6 col-sm-6">'+ 
										'<button id="filter_search" type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="tooltip" data-placement="top" title="Search">'+
											'<i class="fas fa-search"></i> </button>'+ 
									'</div>'+
									'<div class="col-md-6 col-sm-6">'+ 
									'<button id="filter_reset" type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="tooltip" data-placement="top" title="Clear filters">'+
										'<i class="fas fa-undo"></i> </button>'+ 
								'</div>'+
							    '</div>'; 
				
				$("div.toolbar").html(content);

				$("div.resetbar").html(Reset_Content);
			}
			else 
			{
				if(data.Message == "Session Expired")
				{
					location.reload();
				}
			}
		},
		beforeSend: function( xhr )
		{	
			Sweetalert("load", "", "Please Wait..");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
}

function future_date()
{
	var cur_date = new Date();
	
	var fdate = document.getElementById("min").value;
	var ftdate = document.getElementById("max").value;
	
	var to_date = new Date(ftdate);
	
	var from_date = new Date(fdate);
	
	//alert(z<=y && x<=z);
	
	if((from_date <= to_date && from_date <= cur_date) == false)
	{
		Sweetalert("error", "", "Please Enter Valid Date");
		return false;
		
	}
	if((to_date >= from_date && to_date <= cur_date) == false)
	{
		Sweetalert("error", "", "Please Enter Valid Date");
		return false;
	
	}
		Date_Range_Search();
		table.draw();
		
		
}


function get_currentDate()
{
	var dtToday = new Date();

    var month = dtToday.getMonth() + 1;
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();
    
    if(month < 10)
    {
    	month = '0' + month.toString();
    }
        
    if(day < 10)
    {
    	day = '0' + day.toString();
    }
        
    var maxDate = year + '-' + month + '-' + day;    
    
	return maxDate;
}

function Hold_Request()
{
	var err = 0;
	
	if(!validation("holdReason", "txt"))  { err++; }

	if(err !=0 )
	{
		return;
	}
	
	Swal.fire({
		  icon: 'warning',
		  title: '' ,
		  text:  'Do You Want To Hold This Payment ?' ,		 
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes',	
		  closeOnConfirm: true
		}).then(function (res) {
        if(res.value)
        { 
        	Send_Hold_Request();		
        }
        else if(res.dismiss == 'cancel')
        { 
            return false;
        }
        else if(res.dismiss == 'esc')
        { 
        	 return false;
        }
    });
}

function Send_Hold_Request()
{
	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("CHCODE", $("#CHCODE").val());
	data.append("PAYTYPE", $("#PAYTYPE").val());
	data.append("REQSL", $("#REQSL").val());
	data.append("HOLD_REASON", $("#holdReason").val());

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/PAYMENT/REVERSAL/POST",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.result == "success")
			{
				Sweetalert("success_load_Current", "", data.message);
			}
			else
		    {
				Sweetalert("warning", "", data.message);
		    }	
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}

function Show_Hold_popup(CHCODE, PAYTYPE, REQSL)
{
	$('#CHCODE').val(CHCODE);
	$('#PAYTYPE').val(PAYTYPE);
	$('#REQSL').val(REQSL);
	
	$('#Modal').modal('show');
}

function Reset()
{
	location.reload();
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
			  text:  Info, 	 
			  timer: 2000
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