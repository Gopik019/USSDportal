$(document).ready(function() {  
	
	$("#get_details").click(function() { 
		
		 Get_details();
	});
	
	$("#Check_Availability").click(function() { 
		
		Checking_Availabitity();
	});
	
	$("#identifier_action").click(function() { 
		
		Submit_Identifier();
	});
});

function Get_details()
{
	var err = 0;
	
	if(!validation("Paygate", "dd"))         { err++; } 
	if(!validation("Ac_No", "txt"))          { err++; }
	if(!validation("Mobile_No", "txt"))      { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("Ac_No", $("#Ac_No").val());
	data.append("Mobile_No", $("#Mobile_No").val()); 
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Account/Details",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.Result == "success")
			{
				var info = data.Informations;
				
				var AC_NO = info.AC_NO;
				var CUSTOMERNAME = info.CUSTOMERNAME;
				var CURRENCY = info.CURRENCY;
				var CUSTOMERNO = info.CUSTOMERNO;
				var ACNTS_AC_TYPE = info.ACNTS_AC_TYPE;
				var MOBILENO = info.MOBILENO;
				var EMAIL_ID = info.EMAIL_ID;
				
				$("#Customer_No").val(CUSTOMERNO);
				$("#Full_Name").val(CUSTOMERNAME);
				$("#NIDA_Id").val('');
				$("#Email_Id").val(EMAIL_ID);
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
	    error: function (jqXHR, textStatus, errorThrown) { 
	    	
	    }
   });
}

function Checking_Availabitity()
{
	var data = new FormData();
	
	var err = 0;
	
	if(!validation("Paygate", "dd"))         { err++; } 
	if(!validation("Ac_No", "txt"))          { err++; }
	if(!validation("Mobile_No", "txt"))      { err++; }
	if(!validation("Identifier", "txt"))      { err++; }
	
	if(err !=0)
	{
		return;
	}
	
	data.append("Ac_No", $("#Ac_No").val());
	data.append("Mobile_No", $("#Mobile_No").val()); 
	data.append("Identifier", $("#Identifier").val()); 

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/Availability",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.result == "success" && data.stscode == "success")
			{
				$("#not_available").show();
				
				$("#available").show();
				
				$("#Identifier_error").show();
				
				$("#Identifier_error").html("This Identifier is already taken !!");
			}
			else if(data.result == "failed" && data.stscode == "400")
			{
				$("#Identifier_error").hide();
				
				$("#not_available").hide();
				
				$("#available").show();
				
				Sweetalert("warning", "", data.Message);
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
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
}

function Submit_Identifier()
{
	var data = new FormData();
	
	var err = 0;
	
	if(!validation("Paygate", "dd"))         { err++; } 
	if(!validation("Ac_No", "txt"))          { err++; }
	if(!validation("Mobile_No", "txt"))      { err++; }
	if(!validation("Ac_type", "dd"))         { err++; }
	if(!validation("Customer_No", "txt"))    { err++; }
	if(!validation("Identifier", "txt"))     { err++; }
	
	if(err !=0)
	{
		return;
	}
	
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("ACCOUNTNO", $("#Ac_No").val());
	data.append("MSISDN", $("#Mobile_No").val()); 
	data.append("PAYTYPE", $("#Paygate").val());
	data.append("CUSTNO", $("#Customer_No").val()); 
	data.append("IDENTIFIERNAME", $("#Full_Name").val()); 
	data.append("IDTYPE1", 'NIN'); 
	data.append("IDTYPEVALUE1", $("#NIDA_Id").val()); 
	data.append("EMAIL", $("#Email_Id").val()); 
	data.append("IDENTIFIERNO", $("#Identifier").val()); 
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/TIPS/Identifier/Creation",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.result == "success" && data.stscode == "success")
			{
				
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
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
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