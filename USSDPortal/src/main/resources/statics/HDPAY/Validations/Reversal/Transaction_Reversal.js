var table; var Pay_members; var Rev_Member;

$(document).ready(function() {
	
	 table = $('#example').DataTable({ "dom" : "t" });
	
	 $("#get_details").click(function() { 
		
		 Get_Details();
	 });
	 
	// $(document).on('click', '#Rev_action', function() {  
			
		//  Initiate_Request();
	// }); 
	 
	// $(document).on('focusout', '.choices', function() {  
		 
		//  var index = $(this).index();
	//	  
	///	  alert(index);
		  
		  //alert($("#Rev_reason").val());
//	 });
	 
	 $(document).on('click', '#Rev_action2', function() {  
			
		 //alert($("#Rev_reason").val());
		 
		   Confirm_Hold_Request();
	 }); 
	 
	 $(document).on('click', '#Rev_action3', function() {  
		
		 Initiate_Reversal_Approval();
	 }); 
	 
	 Pay_members = new Choices('#payments', {
		 delimiter: ',',
		 removeItemButton: true,
		 shouldSort: 0,
		 maxItemCount:5,
		 searchResultLimit:5,
		 renderChoiceLimit:5
	 });
	  
	 Rev_Member = new Choices('#Rev_reason', {
		  //addItems: 0
		  maxItemCount : 1,
		  renderSelectedChoices : 'auto',
		  addItemText : ''
	 });
});

function Get_Details()
{	
	var err = 0;
	
	if(!validation("Tran_date", "txt"))   { err++; } 
	if(!validation("Paygate", "dd"))      { err++; }
	if(!validation("Channel", "dd"))      { err++; }
	if(!validation("Txn_Id", "txt"))      { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("TRANDATE", $("#Tran_date").val());
	data.append("PAYTYPE",  $("#Paygate").val()); 
	data.append("CHCODE",   $("#Channel").val()); 
	data.append("CHREFNO",  $("#Txn_Id").val()); 
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Reversal/Tran/Details",       
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
				
				Hold_Options_Enable(data.Additional);
				
				var Informations = data.Informations;

				Informations = eval(Informations);
				
				table = $('#example').DataTable( {
					  "dom" : "t",
					  "aaData": Informations,
					  "aoColumns": [
							{ "mData": "SERIAL" },			
							{ "mData": "AMOUNTTYPE" },		
							{ "mData": "TRANDATE" },  	   
							{ "mData": "TRANREFNO" },	        
							{ "mData": "SYSAMOUNT" },	
							{ "mData": "SYSCURR" },
							{ "mData": "DEBITAC" },
							{ "mData": "CREDITAC" },
							{ "mData": "PAYSTATUS" },
							{ "mData": "HOLDSTATUS" },
							{ "mData": "REVSTATUS" },
							{ "mData": "CHCODE" },
							{ "mData": "REMARKS" },
							{ "mData": "REQREFNO" },
							{ "mData": "PAYERID" },
							{ "mData": "PAYEEREF" }
					  ],
					  "paging":true,
					  "destroy": true,
					  "deferRender": true,
					  "responsive": true,
					  "bSort": false,
		              /*  "columnDefs" : [
						  {	
					    	  "targets" : 7,
					   		  "render"  : function (data, type, row, meta)
					    	   {
					        		if (type === 'display')
					        		{
					        			if(row.STATUS === 'Active')
					        			{
					        				data = '<button type="button" class="btn btn-success btn-sm2" onclick="Status_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-power-off"></i></button>' +
					            			   '<button type="button" class="btn btn-secondary btn-sm2 ml-2" onclick="Get_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-eye"></i></button>';
					        			}
					        			else
					        			{
					        				data = '<button type="button" class="btn btn-danger btn-sm2" onclick="Status_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-power-off"></i></button>' +
					            			   '<button type="button" class="btn btn-secondary btn-sm2 ml-2" onclick="Get_details(\''+row.IDENTIFIERNO+'\',\''+row.ACCOUNTNO+'\',\''+row.MSISDN+'\')"><i class="fas fa-eye"></i></button>';
					        			}		 			   
					        		}
			
					        		return data;
					    	 	}		
						   }	
					  ], */
		          "lengthMenu": [[5, 10, 50, 75, -1], [5, 10, 50, 75, "All"]],
				  "pageLength":10						 
			   }); 
				
			   Possible_Reversal_Payment_Types(data.Possible_PayTypes);
			   
			   console.log("Affter :::"+data);
			   console.log("Affter :::"+data.FLOW);
			}
			else
			{
				table.clear().draw(); 
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

function Hold_Options_Enable(Info)
{
	var Rev_Flag = Info.Rev_Flag;
	
	$("#REQSL").val(Info.REQSL);
	$("#FLOW").val(Info.FLOW);
	
	if(Rev_Flag === true)
	{
		//$(".hold_rev").show();
		
		console.log("Before :::"+Info.REQSL); 
		console.log("Before :::"+Info.FLOW);
		
		$("#hold").val(Info.Hold_Status);
		$("#rev").val(Info.Rev_Status);
		
		$("#Rev_reason").val(Info.REV_REASON);
		
	  //$("#HOLDREJREASON").val(Info.HOLDREJREASON);
	  //$("#REVREJREASON").val(Info.REVREJREASON);
	}
	else
	{
		//$(".hold_rev").hide();
		
		//$("#REQSL").val('');
		//$("#Rev_reason").val('');
		//$("#FLOW").val('');
	}
}

function Possible_Reversal_Payment_Types(Possible_PayTypes)
{	
   if(Possible_PayTypes.length !=0)
   {
		var members = [];
		 
		Pay_members.clearChoices();
		
		var Informations = Possible_PayTypes;
		
		for(var i=0;i<Informations.length;i++)
		{
			 var val = { value : Informations[i], label : Informations[i] , selected : false } ;
			 
			 members.push(val);
		}
		
		Pay_members.setChoices( members, 'value', 'label');
	}
	else
	{
		Pay_members.clearStore();
	}
}

function Get_Payment_Types()
{	
	var err = 0;
	
	if(!validation("Paygate", "dd"))  { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("PAYTYPE",  $("#Paygate").val()); 

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Reversal/Trantypes",       
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
				var members = [];
				 
				Pay_members.clearChoices();
				
				var Informations = data.Informations;
				
				for(var i=0;i<Informations.length;i++)
				{
					 var val = { value : Informations[i], label : Informations[i] , selected : false } ;
					 
					 members.push(val);
				}
				
				Pay_members.setChoices( members, 'value', 'label');
			}
			else
			{
				Pay_members.clearStore();
			}
		},
		beforeSend: function( xhr )
		{	
			Pay_members.clearStore();
			
			Sweetalert("load", "", "Please Wait..");
        },
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
}

/*
function Initiate_Request()
{
	var err = 0;
	
	if(!validation("payments", "txt"))    { err++; }
	if(!validation("Rev_reason", "txt"))  { err++; }

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
*/

/*
function Send_Hold_Request()
{
	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("CHCODE", $("#Channel").val());
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("FLOW", $("#FLOW").val());
	data.append("REQSL", $("#REQSL").val());
	data.append("HOLD_REASON", $("#Rev_reason").val());
	data.append("PAYMENTS", $("#payments").val());
	//data.append("HOLD_APPROVAL", $("#hold").val());
	//data.append("HOLD_REJECT_REASON", $("#hold_Rej_reason").val());
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/PAYMENT/HOLD/INITIATE",
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
*/

function Confirm_Hold_Request() 
{
	var err = 0;
	
	if(!validation("Paygate", "dd"))      { err++; }
	if(!validation("FLOW", "txt"))         { err++; }	
	if(!validation("hold", "dd"))         { err++; }
	if(!validation("Rev_reason", "txt"))  { err++; }
	if(!validation("payments", "txt"))    { err++; }
	if(!validation("Rev_reason", "txt"))  { err++; }
	
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
        	Approve_Hold_Request();		
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

function Approve_Hold_Request()
{
	var data = new FormData();

	/*data.append("SUBORGCODE", "EXIM");
	data.append("CHCODE", $("#Channel").val());
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("FLOW", $("#FLOW").val());
	data.append("REQSL", $("#REQSL").val());
	data.append("HOLD_APPROVAL", $("#hold").val());
	data.append("HOLD_REJECT_REASON", $("#hold_Rej_reason").val());
	*/
	
	data.append("SUBORGCODE", "EXIM");
	data.append("CHCODE", $("#Channel").val());
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("FLOW", $("#FLOW").val());
	data.append("REQSL", $("#REQSL").val());
	data.append("HOLD_APPROVAL", $("#hold").val());
	data.append("HOLD_REASON", $("#Rev_reason").val());
	data.append("HOLD_REJECT_REASON", $("#hold_Rej_reason").val());
	data.append("PAYMENTS", $("#payments").val());

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/PAYMENT/HOLD/APPROVAL",
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


function Initiate_Reversal_Approval()
{
	var err = 0;
	
	if(!validation("Rev_reason", "txt"))  { err++; }

	if(err !=0 )
	{
		return;
	}
	
	Swal.fire({
		  icon: 'warning',
		  title: '' ,
		  text:  'Do You Want To Reverse This Payment ?' ,		 
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes',	
		  closeOnConfirm: true
		}).then(function (res) {
        if(res.value)
        { 
        	Approve_Reversal_Request();		
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

function Approve_Reversal_Request()
{
	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("CHCODE", $("#Channel").val());
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("FLOW", $("#FLOW").val());
	data.append("REQSL", $("#REQSL").val());
	data.append("REVERSAL_APPROVAL", $("#rev").val());
	data.append("REVERSAL_REJECT_REASON", $("#rev_rej_reason").val());

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/PAYMENT/REVERSAL/APPROVAL",
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

function Status_details()
{
	$('#modal2').modal('show');
	
	return;
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

/*
$(document).ready(function() {	   
	
	var currSeconds = 0;  var max_time = 0;
	
	$(this).mousemove(function (e) {
		currSeconds = max_time;
    });
	
	$(this).keypress(function (e) {
		currSeconds = max_time;
    });

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Session/Interval",       
		type :  'POST',
		data :  '',
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			max_time = Number(data.Interval);
			
			currSeconds = max_time;
		
		    var timer = setInterval(function() {
	    	 
			    currSeconds--;  
			   	   
				if(currSeconds < 1 )
			    {
					clearInterval(timer);
					
					$(this).bind("contextmenu", function(e) {
		                e.preventDefault();
		            });
				  
				    Swal.fire({
						  icon: 'warning',
						  title: '' ,
						  text:  'Session Timeout !',
						  allowOutsideClick: false
					 }).then(function (result) {
						  if (true)
						  {							  
							  window.location.href = $("#ContextPath").val()+ "/logout";	
						  }
					  });		   
			    }
		    
			 }, 1000);
		},
		error: function (jqXHR, textStatus, errorThrown)  {}
	 });
}); */