var table; var newFileList = []; 

$(document).ready(function() {
	
	$(document).on('click', '#add_excel', function() {  
			
		$('#modal1').modal('show');
	});
	
	$(document).on('click', '#identifier_Update', function() {  
		
		Validate_Excel_data();
	});
	
	$(document).on('change', '#approve_all', function(e) {  
		
		 e.preventDefault();
		
		 Approve_all_Identifiers();
		
	});
	
	$(document).on('click', '#identifier_action', function(e) {  
		
		 e.preventDefault();
		
		 Confirm_Submit();
	});
	
	$(document).on('change', '#old_queue', function(e) {  
		
		 e.preventDefault();
		 
		 if($("#old_queue").prop('checked') === true) 
		 {
			 View_Identifiers_Queue();
		 }
		 else
		 {
			 table.clear().draw();
		 }
	});
	
	$("#inputGroupFile012").change(function(e)  {   
	    	
	 	  var fileUpload = document.getElementById("inputGroupFile012");

	      var size = parseFloat(fileUpload.files[0].size / 1024).toFixed(2);
		    
	      if(size > 15360)
	      {
	  		  Sweetalert("warning", "", "Image size should be less than or equal to 15 MB");
	  		
	  		  return false;
	  	  }
	 	
		  newFileList = Array.from(e.target.files);

		  Files_Preview();   
	});
	 
	table = $('#example').DataTable(); 
	
	Create_Layout();	

});

function Create_Layout()
{
	table = $('#example').DataTable({
		  "paging":true,
		  "destroy": true,
		  "deferRender": false,
		  "responsive": true,
		  "dom": "<'row'<'col-sm-2'l><'col-sm-6 text-center'<'toolbar'>><'col-sm-1 text-center'<'resetbar'>><'col-sm-3'f>>"+
		     	 "<'row'<'col-sm-12'tr>>" +
		     	 "<'row'<'col-sm-6'i><'col-sm-6'p>>",
       "lengthMenu": [[5, 10, 50, 75, -1], [5, 10, 50, 75, "All"]],
		  "pageLength": 10						 
	}); 
	
	var content = '<div class="row">' +
					 '<div class="col-md-2 col-sm-6"><label><b> View Queue <b></label></div>'+ 
						 '<div class="col-md-2 col-sm-6 mt--2 mr-auto">'+ 
							'<div class="form-check" align="center">'+ 
								'<label class="switch small"> '+ 
									'<input id="old_queue" type="checkbox">'+ 
									'<span class="slider round small"></span>'+ 
								'</label>'+ 
							'</div>'+ 
					  	'</div>'+
			       '</div>'; 
	
	var Reset_Content = '<div class="row">' +
							'<div class="col-md-4 col-sm-6 ml-auto">'+ 
								'<button id="add_excel" type="button" class="btn btn-sm btn-icon btn-rectangle btn-secondary" data-toggle="tooltip" data-placement="top" title="Excel Upload">'+
									'<i class="fas fa-plus"></i>'+
								'</button>'+  
							'</div>'+
				    '</div>'; 
	
	$("div.toolbar").html(content);

	$("div.resetbar").html(Reset_Content);
	
	$("#custom_card_action").hide();
}

function Validate_Excel_data()
{
	if($('#Paygate').val() == "Select")  
	{ 
		Sweetalert("warning", "", "Please Select the Payment Gateway !!"); 
		
		return;
	}
	
	if($('#inputGroupFile012').val() == "" || $('#File_name').html() == "Choose file")  
	{ 
		Sweetalert("warning", "", "Please Select the File !!"); 
		
		return;
	}
	
	var data = new FormData();
	
	if($('#inputGroupFile012').val() != "" || $('#File_name').html() != "Choose file")  
	{ 
		data.append("File", $('#inputGroupFile012')[0].files[0]);	
	}
	
	data.append("PAYTYPE", $('#Paygate').val());	
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/Bulk/Excel/Upload",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		enctype: 'multipart/form-data',
		success: function (data) 
		{   
			Swal.close();
			
			if(data.result == "success")
			{
				 $('#modal1').modal('hide');
				
				 //$("#right, #left, #update_attendance").removeClass("no-show");
				
				 Generate_Report(data, 'EXCEL');	
			}
			else
		    {
				var content = '';
				
				var Validation_errors = data.Validation_errors;
				
				for(var i=0; i < Validation_errors.length; i++)
				{
					content += Validation_errors[i];
				}
				
				//table.clear().draw();
				
				Sweetalert("warning2", "", content);
		    }	
		},
		beforeSend: function( xhr )
 		{
			//$("#right, #left, #update_attendance").addClass("no-show");
			
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	 
	    }
   });
}

function Confirm_Submit()
{
	/*Swal.fire({
		  title: 'Confirm to Submit ?',
		  text: "",
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#57ab03',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes'
		}).then((result) => {
		  if (result.value) 
		  {	
			  Submit_Excel_Data();
		  }
	 });
	*/
	
/*	let timerInterval;
	
	Swal.fire({
	  title: 'Auto close alert!',
	  html: 'I will close in <b></b> milliseconds.',
	  timer: 2000,
	  timerProgressBar: true,
	  didOpen: () => {
	    Swal.showLoading()
	    const b = Swal.getHtmlContainer().querySelector('b')
	    timerInterval = setInterval(() => {
	      b.textContent = Swal.getTimerLeft()
	    }, 100)
	  },
	  willClose: () => {
	    clearInterval(timerInterval)
	  }
	}).then((result) => {
	   if (result.dismiss === Swal.DismissReason.timer) 
	   {
		  //console.log('I was closed by the timer')
		  
	   }
	}) 
	*/
	
	Swal.fire({
		  title: 'Confirm to Submit ?',
		  text: "",
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#57ab03',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes'
		}).then((result) => {
		  if (result.value) 
		  {	
			  Submit_Excel_Data();
		  }
	 });
	
	//Sweetalert("load", "", "Please Wait");
		
	//Submit_Excel_Data();
}

function Submit_Excel_Data()
{
	var all = [];
	
	var Page_Length = table.page.len();
	
	if(Page_Length != -1)
	{
		 table.page.len( -1 ).draw();
	}
	
	$('#example > tbody > tr').each(function () {
		
		 var Reason = $(this).find('td').eq(11).text();
		
		 if(Reason == '-')
		 {
			 var data  =  {
			       SERIAL       	: 	$(this).find('td').eq(0).text(),
			       CHCODE			:   "HP",
			       FSPID			: 	"013",
			       FSP_SRCID 		: 	"013",
			       FSP_DESID	    :   "tips",
			       ACTYPE			:	"BANK",
			       IDENTIFIERTYPE	: 	"ALIAS",
			       IDENTIFIERNO 	: 	$(this).find('td').eq(1).text(),
			       ACCOUNTNO 		:	$(this).find('td').eq(2).text(),
			       ACCAT 			: 	$(this).find('td').eq(3).text(),
			       IDENTIFIERNAME 	: 	$(this).find('td').eq(4).text(),
			       CURRENCY  	    : 	$(this).find('td').eq(5).text(),
			       MSISDN 			: 	$(this).find('td').eq(6).text(),
			       CUSTNO 			: 	$(this).find('td').eq(7).text(),
			       EMAIL 			: 	$(this).find('td').eq(8).text(),
			       IDTYPE1 			: 	"NIN",
			       IDTYPEVALUE1 	: 	$(this).find('td').eq(9).text(),
			       APPROVED 		: 	$(this).find('td').eq(12).find('input[type="checkbox"]').prop('checked') === true ? "1" : "0"  
		  	 };

		 	 all.push(data);
		 } 
	});
	
	var Total_data = {
			
         PAYTYPE: $('#Paygate').val(),
         Info: all
    };

	Total_data = JSON.stringify(Total_data);

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/Bulk/Excel/data/Upload",
		type :  'POST',
		data :  Total_data,
		contentType: "application/json",
		cache : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.result == "success")
			{
				 Sweetalert("success_load_Current", "", data.message);
				
				 //$("#right, #left, #update_attendance").removeClass("no-show");
				
				 //Generate_Report(data);	
			}
			else
		    {
				//table.clear().draw();
				
				Sweetalert("warning", "", data.message);
		    }	
		},
		beforeSend: function( xhr )
 		{
			//$("#right, #left, #update_attendance").addClass("no-show");
			
 			// Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	 
	    }
   });
}

function Generate_Report(data, Type)
{	
	var Informations = data.Identifiers;
	
	var Total_Valid = data.Total_Valid;
		
	var Total_InValid = data.Total_InValid;
	
	Informations = eval(Informations);

	table = $('#example').DataTable( {
			  "aaData": Informations,
			  "aoColumns": [
					{ "mData": "SERIAL"			},	
					{ "mData": "IDENTIFIERNO"	}, 
					{ "mData": "ACCOUNTNO"		},
					{ "mData": "ACCAT"			},	
					{ "mData": "IDENTIFIERNAME"	},	
					{ "mData": "CURRENCY"	    },		
					{ "mData": "MSISDN"			}, 	   
					{ "mData": "CUSTNO"			},
					{ "mData": "EMAIL"			},
					{ "mData": "IDTYPEVALUE1"	},
					{ "mData": "VALID"			}, 
					{ "mData": "REASON"			},
					{ "mData": "APPROVED"		}
			  ],
			  "paging":true,
			  "destroy": true,
			  "deferRender": false,
			  "responsive": true,
			  "bSort": false,
			  "dom": (Type == 'EXCEL' ? "<'row'<'col-sm-2'l><'col-sm-5 text-center'<'toolbar'>><'col-sm-2'<'apprbar'>><'col-sm-3'f>>" : "<'row'<'col-sm-2'l><'col-sm-6 text-center'<'toolbar'>><'col-sm-1 text-center'<'resetbar'>><'col-sm-3'f>>")+
			  		 "<'row'<'col-sm-12'tr>>" +
			  		 "<'row'<'col-sm-6'i><'col-sm-6'p>>",
              "columnDefs" : [
            	  {	
	   	   			  "targets" : 0,
	   	   			  "responsivePriority" : 1
				  },
      	   		  {	
      	   			  "targets" : 1,
      	   			  "responsivePriority" : 2
				  },
				  {	
      	   			  "targets" : 2,
      	   			  "responsivePriority" : 3
				  },
				  {	
      	   			  "targets" : 4,
      	   			  "responsivePriority" : 4
				  }, 
				  {	
      	   			  "targets" : 5,
      	   			  "responsivePriority" : 5
				  }, 
				  {	
      	   			  "targets" : 6,
      	   			  "responsivePriority" : 6
				  }, 
				  {	
      	   			  "targets" : 10,
      	   			  "responsivePriority" : 7
				  }, 
				  {	
      	   			  "targets" : 12,
      	   			  "responsivePriority" : 8
				  }, 
				  {	
			    	  "targets" : 10,
			   		  "render"  : function (data, type, row, meta)
			    	   {
			        		if (type === 'display')
			        		{
			        			if(data === "0")
				   				{
				   					data = '<button type="button" class="btn btn-sm2 btn-icon btn-round btn-danger"><i class="fa fa-times"></i></button>';
				   				}	
				   				else
				   				{
				   					data = '<button type="button" class="btn btn-sm2 btn-icon btn-round btn-success"><i class="fa fa-check"></i></button>';
				   				}	 			   
			        		}
	
			        		return data;
			    	 	}
				   },
				   {	
				    	  "targets" : 12,
				    	  "visible" : Type == 'EXCEL' ? true : false,
				   		  "render"  : function (data, type, row, meta)
				    	   {
				        		if (type === 'display')
				        		{
				        			if(row.VALID == "1")
					   				{				
					   					data = '<div class="row"><label class="switch small heigh_alt"> '+ 
													'<input name="appr_status" type="checkbox" class="approved">'+ 
													'<span class="slider round small"></span>'+ 
												'</label></div>'; 	
					   				}	 
				        			else
				        			{
				        				data = '';
				        			}
				        		}
		
				        		return data;
				    	 	}		
					}
			  ],
          "lengthMenu": [[10, 50, 100, 1000, -1], [10, 50, 100, 1000, "All"]],
		  "pageLength": 10						 
	}); 
	
	if(Type == 'EXCEL')
	{
		var Approval_Content = '<div class="row">' +
								 '<div class="col-md-7 col-sm-6 ml-auto"><label><b> Approve all <b></label></div>'+ 
									 '<div class="col-md-5 col-sm-6 mt--2 mr-auto">'+ 
										'<div class="form-check" align="center">'+ 
											'<label class="switch small"> '+ 
												'<input id="approve_all" type="checkbox">'+ 
												'<span class="slider round small"></span>'+ 
											'</label>'+ 
										'</div>'+ 
								  	'</div>'+
							  '</div>'; 
		
		var content = '<div class="row">' +
						 '<div class="col-md-6 col-sm-6"><label><b> Total Valid Identifiers : &nbsp; &nbsp;  '+ Total_Valid + ' <b></label></div>'+ 
						 '<div class="col-md-6 col-sm-6"><label><b> Total Invalid Identifiers : &nbsp; &nbsp; '+ Total_InValid + ' <b></label></div>'+ 
					  '</div>'; 
		
		$("div.toolbar").html(content);
		
		$("div.apprbar").html(Approval_Content);
		
		$("#custom_card_action").show();
	}
	else
	{
		var content = '<div class="row">' +
						 '<div class="col-md-2 col-sm-6"><label><b> View Queue <b></label></div>'+ 
							 '<div class="col-md-2 col-sm-6 mt--2 mr-auto">'+ 
								'<div class="form-check" align="center">'+ 
									'<label class="switch small"> '+ 
										'<input id="old_queue" type="checkbox">'+ 
										'<span class="slider round small"></span>'+ 
									'</label>'+ 
								'</div>'+ 
						  	'</div>'+
						'</div>'; 

		var Reset_Content = '<div class="row">' +
								'<div class="col-md-4 col-sm-6 ml-auto">'+ 
									'<button id="add_excel" type="button" class="btn btn-sm btn-icon btn-rectangle btn-secondary" data-toggle="tooltip" data-placement="top" title="Excel Upload">'+
										'<i class="fas fa-plus"></i>'+
									'</button>'+  
								'</div>'+
							'</div>'; 
		
		$("div.toolbar").html(content);
		
		$("div.resetbar").html(Reset_Content);
		
		$("#custom_card_action").hide();
	}
}

function View_Identifiers_Queue()
{	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/Queue",
		type :  'POST',
		data :  '',
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.result == "success")
			{
				 Generate_Report(data, 'Queue');	
			}
			else
		    {
				table.clear().draw();
		    }	
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	 
	    }
   });
}

function Approve_all_Identifiers()
{
	var rows = table.rows().nodes();
	 
    $('input[type="checkbox"]', rows).prop('checked', true);
}

function Files_Preview()
{
    var lg = newFileList.length; 
    var items = newFileList;
    var fragment = "";

	if (lg > 0) 
	{
		//$("#attachemnt").val('Yes');
		
    	for(var i = 0; i < lg; i++)
		{
        	var fileName = items[i].name; 
       	 	var fileSize = items[i].size; 
        	var fileType = items[i].type; 

			var temp_id = "file_"+i;
		
			var url = get_icon(fileType);
			
			if(url == "N/A")
			{
				url =  URL.createObjectURL(items[i]);
			}
		
			fragment +=  "<div id=\""+temp_id+"\" class='row'>"+
				    		"<div class='col-2'>"+
				    			"<img src='"+url+"' alt='your image' title=''  />"+
							"</div>"+
			 		    	"<div class='col-4 text-truncate'>"+ fileName +"<input type='hidden' name='files_selected' value='"+ fileName +"' /></div>"+
			 		/*    	"<div class='col-4'>" + fileType + "</div>"+ */
							"<div class='col-3'>" + bytesToSize(fileSize) + "</div>"+ 
							"<div class='col-2 float-left'>"+
								"<div class='close_btn' onclick='remove_file(\""+temp_id+"\", \""+fileName+"\")'>"+ 
									  "<span aria-hidden='true'>x</span>"+
								 "</div>"+
			 		    	"</div>"+
			 		    "</div> ";
        }

		$("#File_name").html(lg == 1 ? "1 file Selected" : lg+" files Selected");

		var hr = "<div class='row'><div class='col-12'><hr></div></div>";
		
		var header = "<div id='file_tb' class='row'>"+
		 		    	"<div class='col-2'><b>File</b></div>"+
		 		    	"<div class='col-4'><b>Name</b></div>"+
		 		    	"<div class='col-3'><b>Size</b></div>"+  
						"<div class='col-2 float-left'><b>Action</b></div>"+
		 		    "</div>";

    	$("#selected").html(hr + header + fragment);
	 } 
	 else
	 {
		 $("#attachemnt").val('No');
	 }
}

function get_icon(Type)
{
	if(Type == 'application/pdf')
	{
		return $("#ContextPath2").val()+"/resources/HDPAY/img/attachment_icons/pdf.png"; 
	}
	else if(Type == 'application/vnd.ms-excel' || Type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
	{
		return $("#ContextPath2").val()+"/resources/HDPAY/img/attachment_icons/Excel.png";
	}
	else if(Type == 'application/msword' || Type == 'application/vnd.openxmlformats-officedocument.wordprocessingml.document')
	{								
		return $("#ContextPath2").val()+"/resources//HDPAY/img/attachment_icons/ms_doc.png";
	}
	else if(Type == 'text/plain')
	{
		return $("#ContextPath2").val()+"/resources/img/attachment_icons/txt.png"; 
	}
	else if(Type == 'application/zip' || Type == 'application/vnd.rar' || Type == 'application/x-rar-compressed' || Type == 'application/x-zip-compressed')
	{
		return $("#ContextPath2").val()+"/resources/HDPAY/img/attachment_icons/zip.png"; 
	}
	else
	{
		return "N/A";
	}
}

function bytesToSize(bytes)
{
   var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];

   if (bytes == 0) return '0 Byte';

   var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));

   return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
}

function remove_file(file_id, file_name)
{
	var fp = $("#inputGroupFile012");
	
    var lg = fp[0].files.length; 

    var items = fp[0].files;

	if (lg > 0) 
	{
    	for (var i = 0; i < lg; i++)
		{
			if(items[i].name == file_name)
			{
				newFileList.splice(i,1);
			}
		}
	}	
	
	var val = $("#File_name").html().split(" ")[0];
	
	if(isNumeric(val))
	{
		val = Number(val) - 1;

		if(val == 0)
		{
			$("#selected").html('');
			
			$("#File_name").html("Choose file");	
			
			$('#inputGroupFile012').val('');
		}
		else
		{
			$("#File_name").html(val == 1 ? "1 file Selected" : val+" files Selected");	
		}
	}
	
	$("#"+file_id).remove(); 
}

function isNumeric(num)
{
  return !isNaN(num)
}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text: Info ,			 
			  timer: 3000
		});
	}
	else if(Type == "success_load_Current")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,
			  timer: 5000
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
			  timer: 3000
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info, 	 
			  timer: 3000
		});
	}
	else if(Type == "warning2")
	{
		Swal.fire({
			icon: 'warning',
			title: Title,
			text:  Info, 	 
			showClass: {
				popup: 'animate__animated animate__fadeInDown'
			},
			hideClass: {
				popup: 'animate__animated animate__fadeOutUp'
			}
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