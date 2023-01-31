var table = $('#example').DataTable();

$(document).ready(function() {

	LoadPgmId();
	
	$('#txtPgmId').on('change', function () {   
		
		 Load_Result(this.value);
		 
		 table.rows({ selected: true}).deselect();
	});
	
	$('#searchkey').on('keyup', function () {   
		
		 table.rows({ selected: true}).deselect();
		 
		 table.columns(3).search(this.value).draw();
	});
	
	$('#enteredby').on('keyup', function () {   
		
		 table.rows({ selected: true}).deselect();
		 
		 table.columns(4).search(this.value).draw();	 
	});
	
	
});
	
function LoadPgmId()
{
	var domainid = document.getElementById('domainId').value;			
	var userid = document.getElementById('userId').value;
	
	var auguments = domainid+"|"+userid;
	
	var data = new FormData();
	
    data.append('auguments' , auguments);
	//alert(auguments);
    $.ajax({		 
		url  :  $("#ContextPath").val() + "/validateAdmPgmId",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			if(data.Result == 'Success')
			{
				var category = data.CATEGORY;
				
				document.getElementById("txtExtDomain").value = domainid;
				document.getElementById("txtExtDomain").readOnly = true;
				
				var Two = data.PGMID1[0].PGMID;
				
				
				for(let i in data.PGMID1){
					var Two = data.PGMID1[i].PGMID;
					var rtnVal = data.PGMID1[i].PGMID;
					var Three = data.PGMID1[i].DESC;
					
					try{
						$("#txtPgmId").append('<option value="'+rtnVal+'">'+Three+'</option>');
						
					}catch(e){
						$("#txtPgmId").append('<option value="'+rtnVal+'">'+Three+'</option>');
					}
				}
			}
			else
			{
				Sweetalert("warning", "", data.Message);
			}
		},
		beforeSend: function( xhr )
		{
			 $("#txtPgmId").empty();
			 $("#txtPgmId").append($('<option></option>').val("").html("Select"));   
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}

function show_details(pk,pgid)
{
	var data = new FormData();
	
	data.append("pk", pk);
	data.append("pgid", pgid);
	
    $.ajax({		 
		url  :  $("#ContextPath").val() + "/Info/pk/Get_auth003_Info",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			if(data.Result == 'Success')
				{
					data = data.Info;
					
					$("#torgcd").val(data[0].element_1);
					$("#tuserid").val(data[0].element_2);
					$("#tusernme").val(data[0].element_3);
					$("#tbirthdate").val(formatDate2(data[0].element_4));
					$("#tmobile").val(data[0].element_5);
					$("#temail").val(data[0].element_6);
					$("#tpwd").val(data[0].element_7);
					$("#tconfirmpwd").val(data[0].element_8);
					$("#trolecd").val(data[0].element_9);
					$("#tregdate").val(formatDate2(data[0].element_10));
					//$("#tcomaddr").val('');
					$("#branchcd").val(data[0].element_17);
					
					$('#Modal2').modal('show');
				}
				
			else
			{
				Sweetalert("warning", "", data.Message);
			}
		},
		beforeSend: function() 
		{
			$('#torgcd, #tuserid, #tusernme, #tbirthdate, #tmobile, #temail, #tpwd, #tconfirmpwd, #trolecd, #tregdate, #branchcd').val('');
			
			//$('#torgcd').css({'background-color':'#f6f6f6 !important'});
	    },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}
	
function Load_Result(val)
{
	var data = new FormData();
	
	data.append("pgmid", val)
	
    $.ajax({		 
		url  :  $("#ContextPath").val() + "/Info/Get_auth001_Info",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			if(data.Result == 'Success')
			{
			
				table = $('#example').DataTable( {
					 "sDom":"ltipr",
					  "aaData":  data.Info,
					  "autoWidth": false,
					  "aoColumns": [
					   // { "mData": "Checkbox", "bSortable": false },
					    { "mData": "Checkbox","multiselect":false },
					    { "mData": "S_NO" },
					    { "mData": "AUTHQ_PGM_ID"},
					    { "mData": "AUTHQ_MAIN_PK"},
					    { "mData": "AUTHQ_DONE_BY"},
					    { "mData": "AUTHQ_ENTRY_DATE", "render": function ( data, type, row ) { return formatDate(data); } },	    
					    { "mData": "AUTHQ_DISPLAY_DTLS"},
					    { "mData": "STATUS" },
					    { "mData": "View" }
					  ],	
						"columnDefs": [ {
							"width": "5px",
							
				            "targets":   0
				        } ,
						  { "width": "5px", "targets": 1 },
					      { "width": "50px", "targets": 2 },
					      { "width": "100px", "targets": 3 },
					      { "width": "50px", "targets": 4 },
					      { "width": "80px", "targets": 5 },
					      { "width": "100px", "targets": 6 },
					      { "width": "70px", "targets": 7 },
					      { "width": "50px", "targets": 8 }
						],
					  //"order": [[1, 'asc']],
					  "paging":true,
					  "ordering":false,
					  "destroy": true,
					  "deferRender": true,
					  "searching": true,
			  		  "info": true,   
					  "pageLength":5,
					  "lengthChange": true,
					  "columnDefs": [ {
				            "orderable": false,
				            "className": 'select-checkbox',
				            "targets":   0
				        } ],
				        "select": {
				            "style":    'single',
				            "selector": 'td:first-child'
				        },
				        "order": [[ 1, 'asc' ]] ,
				        "lengthMenu": [[1, 5, 10, 50, 75, -1], [1, 5, 10, 50, 75, "All"]]
				});	
				//Sweetalert("success", "", data.Message);
			}
			else
			{
				//$('#example').DataTable().Clear();
				//Sweetalert("warning", "", data.Message);
			}
		},
		beforeSend: function( xhr )
 		{
			table.clear().draw(); 
 			//Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}


var blob_file_name = "";

var blob_url = "";


function getDetails(txtArgs)
{
	var txtArgs1 = txtArgs.split("$");
	var txtArgs2 = txtArgs1[1].split("|");
	
	var invcode = txtArgs2[2];
	var billno = txtArgs2[5];
	var paysl = txtArgs2[7];
	var invdate = txtArgs2[6];
	var uname = document.getElementById("uname").value;
	//const date = new Date(invdate);
	
	var data = new FormData();
	
	data.append("invcode",invcode);
	data.append("billno",billno);
	data.append("paysl",paysl);
	data.append("invdate",invdate);
	data.append("user",uname);
	
	$.ajax({		 
		url  :  $("#ContextPath").val() + "/receipt/download",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			if(data.Result == "Success")
			{
				var file = data.File;
				
				//var bb = new Blob([file],{type : "application/pdf"});
				
				blob_file_name = data.FileName;
				
				blob_url = file;
				
				Download_Report();
				alert(data.Message);
			}
			else
			{
				//Sweetalert("warning", "", data.Message);
				alert(data.Message);
			}
		},
		beforeSend: function( xhr )
		{
			   
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}

function Download_Report()
{
	  var link = document.createElement("a");
	  link.download = blob_file_name;
	  link.href = blob_url;
	  document.body.appendChild(link);
	  link.click();
	  document.body.removeChild(link);
	  delete link;
}

function do_Auth_or_Reject(AuthRejectFlag, txtArgs)
{
	var data = new FormData();
	
	data.append("txtArgs", txtArgs);
	
    $.ajax({		 
		url  :  $("#ContextPath").val() + "/Admin_Approval",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			Swal.close();
			
			$("#auth").attr('disabled',false);
			$("#reject").attr('disabled',false);
			
			var txtPgmId = document.getElementById('txtPgmId').value;
			
			Load_Result(txtPgmId);
			
			if(data.Result == 'Success')
			{
				if(AuthRejectFlag=="Auth")
				{
					if(txtPgmId == "gepgpay")
						getDetails(txtArgs);
					else if(txtPgmId == "mscpaynew")
						getDetails(txtArgs);
					else if(txtPgmId == "gepgpaynew")
						getDetails(txtArgs);
					else if(txtPgmId == "airtelpay")
						getDetails(txtArgs);
					else if(txtPgmId == "bsmart")
						getDetails(txtArgs);
					
					Sweetalert("success", "", "Record Authorized Successfully !!");
					$("#auth").attr('disabled',false);
					$("#reject").attr('disabled',false);
					
				}	
				else 
				{
					
					$("#Modal").modal('hide');
					
					Sweetalert("success", "", "Record Rejected Successfully !!");
					$("#auth").attr('disabled',false);
					$("#reject").attr('disabled',false);
					$("#txtReason").val("");

				}
			}
			else
			{
				Sweetalert("warning", "", data.Message);
			}
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}
	
function update(AuthRejectFlag)
{			
	var flag = 0;  var index;
	  
    table.rows({ selected: true} ).every(function(){
	 
	    index = this.index();
	  
	    flag = 1 ;
	});
	  
	if(flag == 1)
	{ 
		var domainid = document.getElementById('domainId').value;
		var txtPgmId = document.getElementById('txtPgmId').value;
		var userId = document.getElementById('userId').value;
		var calcurrbusDate = get_current_date(); //document.getElementById('calcurrbusDate').value;
		
		var txtRejCode = document.getElementById('txtRejCode').value;

		var txtReason = document.getElementById('txtReason').value;

		var pk =  table.cell( index, 3 ).data();
		var entdby = table.cell( index, 4 ).data();

		if(AuthRejectFlag == "Auth")
		{
			Swal.fire({
				  icon: 'warning',
				  title: '' ,
				  text:  'Do You Want To Authorize This Record ?' ,		 
				  showCancelButton: true,
				  confirmButtonColor: '#3085d6',
				  cancelButtonColor: '#d33',
				  confirmButtonText: 'Yes',	
				  closeOnConfirm: true
				}).then(function (res) {
	              if(res.value)
	              { 
	            	   document.getElementById('txtRejCode').value = "";
						
						document.getElementById('txtReason').value = "";
						
						txtRejCode = " ";
						
						txtReason = " ";
							
						if(document.getElementById('txtReason').value=="") { document.getElementById('txtReason').value=" "; }
							
						var txtArgs = txtPgmId +"$"+pk+"$"+ userId +"$"+calcurrbusDate +"$"+AuthRejectFlag+"$"+txtRejCode +"$"+txtReason +"$"+entdby+"$"+domainid;		
								
						do_Auth_or_Reject(AuthRejectFlag, txtArgs);
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
		else
		{
		   if(flag == 1)
				{
				$('#Modal').modal('show');
				
				}
		}
		 				
	}
	else
	{	
		if(AuthRejectFlag=="Auth")
		{
			Sweetalert("warning", "", "Select one row to Authorize");
		}	
		else 
		{
			Sweetalert("warning", "", "Select one row to Reject");
		}
			
		return false;
	}
}

function Rejectcol(AuthRejectFlag)
{
	
	var flag = 0;  var index;
	  
    table.rows({ selected: true} ).every(function(){
	 
	    index = this.index();
	  
	    flag = 1 ;
	});
    
	var domainid = document.getElementById('domainId').value;
	var txtPgmId = document.getElementById('txtPgmId').value;
	var userId = document.getElementById('userId').value;
	var calcurrbusDate = get_current_date(); //document.getElementById('calcurrbusDate').value;
	
	var txtRejCode = document.getElementById('txtRejCode').value;

	var txtReason = document.getElementById('txtReason').value;

	var pk =  table.cell( index, 3 ).data();
	var entdby = table.cell( index, 4 ).data();
	if(document.getElementById('txtReason').value=="")
		
  	{
  		Sweetalert("warning", "", "Please enter remarks");
  		
  		return false;
  	} 
  	
  	Swal.fire({
	  icon: 'warning',
	  title: '' ,
	  text:  'Do You Want To Reject This Record ?' ,		 
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes',	
	  closeOnConfirm: true
	}).then(function (res) {
		
		
		
      if(res.value)
      { 
    	  	txtRejCode = "Reject";
				
      		var txtArgs = txtPgmId +"$"+pk+"$"+ userId +"$"+calcurrbusDate +"$"+AuthRejectFlag+"$"+txtRejCode +"$"+txtReason +"$"+entdby+"$"+domainid;		
				
      		do_Auth_or_Reject(AuthRejectFlag, txtArgs);
      		
      		/* document.getElementById('txtReason').value=="";	 */
      		
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
  	
  	document.getElementById('txtReason').value = " ";
 
}

function Reset()
{
	
	location.reload();
	//$("#txtPgmId").val('Select');
}
function formatDate(date) 
{
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [day, month, year].join('-');
}

function formatDate2(date) 
{
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}

function get_current_date()
{
	var today = new Date();
	var dd = today.getDate();

	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();
	
	if(dd<10) 
	{
	    dd='0'+dd;
	} 

	if(mm<10) 
	{
	    mm='0'+mm;
	} 
	
	today = dd+'-'+mm+'-'+yyyy; 
	
	return today;
}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text:  Info,		 		  
			  timer: 2000,
			  showConfirmButton: false 
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
	else if(Type == "confirm")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Yes',
			  closeOnConfirm: true
			}).then((res) => {
                if(res.value){ 
                //kannan();
                   return true;
                }else if(res.dismiss == 'cancel'){ 
                    return false;
                }
                else if(res.dismiss == 'esc'){ 
                	 return false;
                }
            });
	}	
}