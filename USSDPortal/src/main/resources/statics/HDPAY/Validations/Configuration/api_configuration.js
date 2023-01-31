$(document).ready(function() {  
	
	$(document).on('click', '#t1_add_header', function() {
	
		Add_Header('#t1_headers');
	});
	
	$(document).on('change', '#auth_type', function() {
	
		show_required_auth();
	});
	
	$("#event_creation").click(function() { 
		
		Create_Event();
	});
	
	$("#form_reset").click(function() { 
		
		Reset();
	});
	
	Init();
});

function Init()
{
	$("#view_exising_header, #view_exising_auth").hide();
	
	API_Code_Suggestions();
}

function API_Code_Suggestions()
{
	$("#Channel_Code").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/suggestions/APIcode",
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
	    	var Id = ui.item.id;
	    	    	
	    	Retrieve_API_Gateway(Id);
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function show_required_auth()
{
    $(".auth_types").hide();
   
	var Auth_Type = $("#auth_type").val();
	
	$("#"+Auth_Type).show();
}

function Create_Event()
{
	var err = 0;
	
	var api_key   = $(".api_key");
	var api_value  = $(".api_value");

	if(!validation("url_end_point", "txt"))      { err++; }
	if(!validation("Service_Name", "txt"))       { err++; }
	if(!validation("Service_Id", "txt"))         { err++; }
	if(!validation("Channel_Code", "txt"))       { err++; }
	if(!validation("Channel_Type", "dd"))        { err++; }
	if(!validation("Flow", "dd"))                { err++; }
	if(!validation("Protocol_type", "dd"))       { err++; }
	if(!validation("method", "dd"))              { err++; }
	if(!validation("format", "dd"))              { err++; }
	//if(!validation("Payload", "txt"))            { err++; }
	//if(!validation("Sign_Payload", "txt"))       { err++; }
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();

	data.append("SUBORGCODE", "EXIM");
	data.append("SYSCODE", "HP");
	data.append("URI", $("#url_end_point").val());
	data.append("SERVNAME", $("#Service_Name").val());
	data.append("SERVICECD", $("#Service_Id").val());
	data.append("CHCODE", $("#Channel_Code").val());
	data.append("CHTYPE", $("#Channel_Type").val());
	data.append("FLOW", $("#Flow").val());
	data.append("PROTOCOL", $("#Protocol_type").val());
	data.append("METHOD", $("#method").val());
	data.append("FORMAT", $("#format").val()); 
	data.append("PAYLOAD", $("#Payload").val());  
	data.append("SIGNPAYLOAD", $("#Sign_Payload").val()); 
	data.append("JOBREQ", $("#job_req").prop("checked") === true ? "1": "0");

	for(var i=0;i<api_key.length;i++)
	{
	    var Key = api_key[i].value;
	    var val = api_value[i].value;
	    
	    data.append("Keys", Key);
	    data.append("Values", val); 
	}
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/API_Configuration",
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
	    error: function (jqXHR, textStatus, errorThrown) 
		{ 
	    	//Sweetalert("warning", "", "errrrr");  
	    }
   });
}

function Add_Header(Id)
{
	var rnd = randomString(8);
		
	$(Id).append(
		
		'<div id="'+rnd+'" class="row">'+
			'<div class="col-md-2"></div>'+
				
			'<div class="col-md-4">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control api_key" id="key_'+rnd+'" placeholder="Key">'+
					'<label id="key_'+rnd+'_error" class="text-danger"></label>'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-4">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control api_value" id="value_'+rnd+'" placeholder="Value">'+
					'<label id="value_'+rnd+'_error" class="text-danger"></label>'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-2">'+
				'<div class="form-group">'+
					'<button type="button" class="btn btn-icon btn-round btn-danger" onclick="Remove_header(\''+rnd+'\')"><i class="fas fa-times"></i></button>'+
				'</div>'+
			'</div>'+							
		'</div>'	
	); 
}

function Retrieve_Header(Id, key, value)
{
	var rnd = randomString(8);
		
	$(Id).append(
		
		'<div id="'+rnd+'" class="row">'+
			'<div class="col-md-2"></div>'+
				
			'<div class="col-md-4">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control api_key" id="key_'+rnd+'" value="'+key+'" placeholder="Key">'+
					'<label id="key_'+rnd+'_error" class="text-danger"></label>'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-4">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control api_value" id="value_'+rnd+'" value="'+value+'" placeholder="Value">'+
					'<label id="value_'+rnd+'_error" class="text-danger"></label>'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-2">'+
				'<div class="form-group">'+
					'<button type="button" class="btn btn-icon btn-round btn-danger" onclick="Remove_header(\''+rnd+'\')"><i class="fas fa-times"></i></button>'+
				'</div>'+
			'</div>'+							
		'</div>'	
	); 
}

function Retrieve_API_Gateway(values)
{
	var data = new FormData();
	
	var Res = values.split("|");
    
	data.append("CHCODE", Res[0]);
	data.append("SERVNAME", Res[1]);
	data.append("SERVICECD", Res[2]);

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Find/API_Service",
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
				$("#Channel_Code").val(data.CHCODE);
				$("#Channel_Type").val(data.CHTYPE);
				$("#Service_Name").val(data.SERVNAME);
				$("#Service_Id").val(data.SERVICECD);
				$("#url_end_point").val(data.URI);
				$("#Flow").val(data.FLOW);
				$("#Protocol_type").val(data.PROTOCOL);
				$("#method").val(data.METHOD);
				$("#format").val(data.FORMAT);
				$("#Payload").val(data.PAYLOAD); 
				$("#Sign_Payload").val(data.SIGNPAYLOAD); 	
				$("#job_req").prop("checked", data.JOBREQ === "1" ? true : false); 
				
				if(data.Headers.length >= 1)
				{
					$(".api_key").eq(0).val(data.Headers[0].Key);  
					$(".api_value").eq(0).val(data.Headers[0].Value);
					
					$("#view_exising_header, #view_exising_auth").show();
				}
				
				for(var i=1;i<data.Headers.length;i++)
				{
					var key = data.Headers[i].Key;
					var val = data.Headers[i].Value;
					
					Retrieve_Header("#t1_headers", key, val);
				}
			}
			else
		    {
				Sweetalert("warning", "", data.Message);
		    }	
		},
		beforeSend: function( xhr )
 		{
			Reset();
			
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

function Reset()
{
	$("#Channel_Code").val('');
	$("#Channel_Type").val('Select');
	$("#Service_Name").val('');
	$("#Service_Id").val('');
	$("#url_end_point").val('');
	$("#Flow").val('Select');
	$("#Protocol_type").val('Select');
	$("#method").val('GET');
	$("#format").val('JSON');
	$("#Payload").val(''); 
	$("#Sign_Payload").val(''); 
	$("#job_req").prop("checked", false);
	
	$(".api_key").eq(0).val('');  
	$(".api_value").eq(0).val('');
	
	$("#t1_headers").html('');
	
	$("#view_exising_header, #view_exising_auth").hide();
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