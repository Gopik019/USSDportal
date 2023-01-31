$(document).ready(function() {  
	
	$("#channel_action").click(function() { 
		
		Channel_Creation();
	});
	
	$("#Generate_Secret").click(function() { 
		
		Create_Secret();
	});
	
	Retrieve_Payment_Service();
	
	Channel_Code_Suggestions();
});

function Channel_Code_Suggestions()
{
	$("#Channel_Id").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/suggestions/ChannelCode",
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
	    	$("#Channel_Id").val(ui.item.label);
	    	
	    	Retrieve_Channel();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function Channel_Creation()
{
	var err = 0;
	
	if(!validation("Channel_Id", "txt"))          { err++; }
	if(!validation("Channel_Name", "txt"))          { err++; }
	if(!validation("From_Time", "txt"))     { err++; }
	if(!validation("To_Time", "txt"))     { err++; }
	if(!validation("user_id", "txt"))    { err++; }
	if(!validation("Password", "txt"))       { err++; }
	if(!validation("secret_key", "txt"))       { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("SYSCODE", "HP");
	data.append("CHCODE", $("#Channel_Id").val());
	data.append("CHNAME", $("#Channel_Name").val()); 
	data.append("BUS_STARTIME", $("#From_Time").val());
	data.append("BUS_ENDTIME", $("#To_Time").val());
	data.append("USERID", $("#user_id").val());
	data.append("HASHPWD", $("#Password").val());
	data.append("SECRETKEY", $("#secret_key").val());
	
	data.append("STATUS", $("#status").prop('checked') === true ? "1" : "0"); 
	data.append("AMTLIMITREQ", $("#Amount_limit").prop('checked') === true ? "1" : "0");
	data.append("OAUTHVALREQ", $("#oauth").prop('checked') === true ? "1" : "0");
	data.append("AUTO_RECON", $("#auto_recon").prop('checked') === true ? "1" : "0");
	
	var Payment_Gate   = $(".payment_gateway");
	
	for(var i=0;i<Payment_Gate.length;i++)
	{
	    var Code   = Payment_Gate[i].id;
	    var Allowed  = $("#"+Code).prop('checked') === true ? "1" : "0";

		data.append("PAYGATECD", Code);
	    data.append("ALLOWED", Allowed);  
	} 
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Channel_Configuration",
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
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}


function Create_Secret()
{
	var data = new FormData();

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/SecretKey/Generation",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == "Success")
			{
				Swal.close();
				
				$("#secret_key").val(data.SecretKey);
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
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}

function Retrieve_Channel()
{
	var data = new FormData();

	data.append("CHCODE", $("#Channel_Id").val());

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Find/Channel",
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
				var channel_info = data.Info;
				
				$("#Channel_Id").val(channel_info.CHCODE);
				$("#Channel_Name").val(channel_info.CHNAME);
				$("#From_Time").val(channel_info.BUS_STARTIME);
				$("#To_Time").val(channel_info.BUS_ENDTIME);
				$("#user_id").val(channel_info.USERID);
				$("#Password").val(channel_info.HASHPWD);
				$("#secret_key").val(channel_info.SECRETKEY);
				$("#status").prop('checked', channel_info.STATUS === "1" ? true : false);
				$("#Amount_limit").prop('checked', channel_info.AMTLIMITREQ === "1" ? true : false);
				$("#oauth").prop('checked', channel_info.OAUTHVALREQ === "1" ? true : false);
				$("#auto_recon").prop('checked', channel_info.AUTO_RECON === "1" ? true : false);
				
				for(var i=0;i<data.Payment_Service.length;i++)
				{
					var Payment_Code = data.Payment_Service[i].PAYGATECD;
					var Allowed = data.Payment_Service[i].ALLOWED;
					
					$("#"+Payment_Code).prop('checked', Allowed === "1" ? true : false);	
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
	    error: function (jqXHR, textStatus, errorThrown) { 
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}


function Retrieve_Payment_Service()
{
	var data = new FormData();

	data.append("PAYGATECD", "All");

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
				
				for(var i=0;i<Payment_Service.length;i++)
				{
					var PAYGATE_NAME = Payment_Service[i].PAYGATE_NAME;
					var Payment_Code = Payment_Service[i].PAYGATECD;
					var Allowed = Payment_Service[i].ALLOWED;
					
					var content = '<tr>'+
										'<td>'+(i+1)+'</td>'+
										'<td>'+PAYGATE_NAME+'</td>'+
										'<td>'+
											'<div class="form-check">'+
												'<label class="form-check-label">'+
													'<input id="'+Payment_Code+'" class="form-check-input payment_gateway" type="checkbox" value="">'+
													'<span class="form-check-sign"></span>'+
												'</label>'+
												'<label id="'+Payment_Code+'_error" class="text-danger"></label>'+
											'</div>'+
										'</td>'+
									'</tr>';
					
					$("#paygate > tbody").append(content);	
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
	    error: function (jqXHR, textStatus, errorThrown) { 
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