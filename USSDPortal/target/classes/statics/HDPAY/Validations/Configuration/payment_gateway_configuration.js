$(document).ready(function() {  
	
	$("#gateway_action").click(function() { 
		
		Gateway_Creation();
	});
	
	Gateway_Code_Suggestions();
});

function Gateway_Code_Suggestions()
{
	$("#Gateway_Id").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/suggestions/Gatewaycode",
	            type :  'POST',
	            dataType: "json",
	            data:  
				{    term : request.term	    
				},
	            success: function(data) { response(data); }
	        });
    	},
	    minLength: 1,
	    select: function(event, ui) 
	    {
	    	$("#Gateway_Id").val(ui.item.label);
	    	
	    	Retrieve_Gateway();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function Gateway_Creation()
{
	var err = 0;
	
	if(!validation("Gateway_Id", "txt"))          { err++; }
	if(!validation("Gateway_Name", "txt"))          { err++; }
	if(!validation("From_Time", "txt"))     { err++; }
	if(!validation("To_Time", "txt"))     { err++; }
	if(!validation("Protocol", "dd"))    { err++; }
	if(!validation("format", "dd"))       { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("SYSCODE", "HP");
	data.append("PAYGATECD", $("#Gateway_Id").val());
	data.append("PAYGATE_NAME", $("#Gateway_Name").val()); 
	data.append("BUS_STARTIME", $("#From_Time").val());
	data.append("BUS_ENDTIME", $("#To_Time").val());
	data.append("PROTOCOL", $("#Protocol").val());
	data.append("FORMAT", $("#format").val());
	
	data.append("STATUS", $("#status").prop('checked') === true ? "1" : "0");
	
	data.append("FEE_REQ", $("#Transaction_Fee").prop('checked') === true ? "1" : "0");
	data.append("AMTLIMITREQ", $("#Amount_limit").prop('checked') === true ? "1" : "0");
	data.append("AUTO_RECON", $("#auto_recon").prop('checked') === true ? "1" : "0");
	data.append("REVERSAL", $("#Reversal").prop('checked') === true ? "1" : "0");
	data.append("REVERSAL_CHRG", $("#Reversal_Fee").prop('checked') === true ? "1" : "0");
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Payement_Gateway_Configuration",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{
				Sweetalert("success_load_Current", "", data.Message);
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

function Retrieve_Gateway()
{
	var data = new FormData();

	data.append("PAYGATECD", $("#Gateway_Id").val());

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Find/Gateway",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.Result == "Success")
			{
				var Payment_Service = data.Info;
				
				$("#Gateway_Id").val(Payment_Service[0].PAYGATECD);
				$("#Gateway_Name").val(Payment_Service[0].PAYGATE_NAME);
				$("#From_Time").val(Payment_Service[0].BUS_STARTIME);
				$("#To_Time").val(Payment_Service[0].BUS_ENDTIME);
				$("#Protocol").val(Payment_Service[0].PROTOCOL);
				$("#format").val(Payment_Service[0].FORMAT);
				
				$("#status").prop('checked', Payment_Service[0].STATUS === "1" ? true : false);
				$("#Transaction_Fee").prop('checked', Payment_Service[0].FEE_REQ === "1" ? true : false);
				$("#Amount_limit").prop('checked', Payment_Service[0].AMTLIMITREQ === "1" ? true : false)
				$("#auto_recon").prop('checked', Payment_Service[0].AUTO_RECON === "1" ? true : false);
				$("#Reversal").prop('checked', Payment_Service[0].REVERSAL === "1" ? true : false);
				$("#Reversal_Fee").prop('checked', Payment_Service[0].REVERSAL_CHRG === "1" ? true : false);
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
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}

function Remove_header(Id)
{
	$("#"+Id).remove();
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