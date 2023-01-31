$(document).ready(function() {
	
	$("#t1_add_header").click(function() { 
		
		Add_Header('#t1_headers');
	});
	
	$("#event_creation").click(function() { 
		
		Create_Event();
	});
	
	$("#event_method").change(function() { 
		
		Manage_sql_and_api();			
	});
	
	$("#modal1").on('shown.bs.modal', function(){
		
		var value = $("#sql_id").val();
		
		$("#sql_id2").val(value);
		
	});
	
	$("#modal3").on('shown.bs.modal', function(){
		
		var value = $("#notify_team_id").val();
		
		$("#distribution_id").val(value);
		
		Retrieve_Distribution();
		
	});
	
	Manage_sql_and_api();
	
	Retrieve_Event_Code();
	
	Retrieve_SQL_IDs();
	
	Retrieve_Dlistid();
});

function Manage_sql_and_api()
{
	var event_method = $("#event_method").val();
	
	if(event_method == 'Select')
	{	
		$("#sql_api").hide();
	}
	else
	{
		$("#sql_api").show();
	}
	
	if(event_method == 'DB')
	{
		$("#sql_config, #sql_config_").show();
		$("#api_config, #api_config_").hide();	
	}
		
	if(event_method == 'WS')
	{
		$("#sql_config, #sql_config_").hide();
		$("#api_config, #api_config_").show();	
	}
}

function Create_Event()
{
	var err = 0;
	
	var User_Ids   = $(".user_Id");
	var User_Names = $(".user_name");
	var Mobile_Nos = $(".mob_no");
	var Email_Ids  = $(".emailid");
	var Statuses   = $(".status");
	
	if(!validation("module", "dd"))          { err++; }
	if(!validation("event_code", "txt"))     { err++; }
	if(!validation("event_name", "txt"))     { err++; }
	if(!validation("event_type", "dd"))      { err++; }
	if(!validation("event_sub_type", "dd"))  { err++; }
	if(!validation("event_method", "dd"))    { err++; }
	if(!validation("freq_type", "dd"))       { err++; }
	if(!validation("freq_sub_type", "dd"))   { err++; }
	if(!validation("freq_time", "txt"))      { err++; }
	if(!validation("start_time", "txt"))     { err++; }
	if(!validation("end_time", "txt"))       { err++; }
	if(!validation("notify_team_id", "txt")) { err++; }
	if(!validation("sla_time", "txt"))       { err++; }
	if(!validation("sla_cnt_time", "txt"))   { err++; }
	if(!validation("dup_check_time", "txt")) { err++; }
	
	if($("#event_method").val() == 'DB')
	{
		if(!validation("sql_id", "txt"))     { err++; }
	}
		
	if($("#event_method").val() == 'WS')
	{
		if(!validation("api_id", "txt"))     { err++; }
	}
	
	if($("#distribution_id").val() != '' && $("#distribution_name").val() != '' && User_Ids.length !=0)
	{
		if(!validation("distribution_id", "txt"))      { err++; }
		if(!validation("distribution_name", "txt"))    { err++; }
	  
		for(var i=0;i<User_Ids.length;i++)
		{
		    var User_Id   = User_Ids[i].id;
		    var User_Name = User_Names[i].id;
		    var Mobile_No = Mobile_Nos[i].id;
		    var Email_Id  = Email_Ids[i].id;
		    var Status    = Statuses[i].id;
		    
		    if(!validation(User_Id ,   "txt"))      { err++; }
		    if(!validation(User_Name , "txt"))      { err++; }
		    if(!validation(Mobile_No , "txt"))      { err++; }
		    if(!validation(Email_Id ,  "txt"))      { err++; }
		    if(!validation(Status ,    "txt"))      { err++; }
		}
	}
	
	if(err !=0 )
	{
		return;
	}

	var data = new FormData();
	
	data.append("Module", $("#module").val());
	data.append("Event_Code", $("#event_code").val());
	data.append("Event_Name", $("#event_name").val());
	data.append("Event_Type", $("#event_type").val());
	data.append("Event_Sub_Type", $("#event_sub_type").val());
	data.append("Event_Method", $("#event_method").val());
	data.append("Frequency_Type", $("#freq_type").val());
	data.append("Frequency_Sub_Type", $("#freq_sub_type").val());
	data.append("Frequency_in_Time", $("#freq_time").val());
	data.append("Start_Time", $("#start_time").val());
	data.append("End_Time", $("#end_time").val());
	data.append("Holiday_Execution", $("#holiday_exec").prop("checked") === true ? "1": "0");
	data.append("Weekend_Execution", $("#weekend_exec").prop("checked") === true ? "1": "0");
	data.append("System_Status_Check", $("#sys_stats_check").prop("checked") === true ? "1": "0");
	data.append("Enabled_or_diabled", $("#enabled_disabled").prop("checked") === true ? "1": "0");
	data.append("Pre_Check", $("#pre_check").prop("checked") === true ? "1": "0");
	data.append("Parent_Event", $("#par_event").prop("checked") === true ? "1": "0");
	data.append("Notification_Team_ID", $("#notify_team_id").val());
	data.append("SLA_Time", $("#sla_time").val());
	data.append("SLA_Count_Limit", $("#sla_cnt_time").val());
	data.append("Duplicate_Check", $("#dup_check").prop("checked") === true ? "1": "0");
	data.append("Duplicate_Skip_Time", $("#dup_check_time").val());
	data.append("SMS_Required", $("#sms_req").prop("checked") === true ? "1": "0");
	data.append("Email_Required", $("#email_req").prop("checked") === true ? "1": "0");
	data.append("Call_Required", $("#call_req").prop("checked") === true ? "1": "0");	
	
	data.append("SQL_ID", $("#sql_id").val());
	data.append("SQL_Name", $("#sql_name").val());
	data.append("SQL_SEQ_No", $("#sql_seq").val());
	data.append("SQL_Method", $("#sql_method").val());
	data.append("SQL_Sub_Method", $("#sql_sub_method").val());
	data.append("SQL_Query", $("#sql_query").val());
	data.append("CBD", $("#cbd").val());
	data.append("AC_NO", $("#ac_no").val());
	data.append("CUS_No", $("#cus_no").val());
	data.append("TRAN_REF", $("#tran_ref").val());
	data.append("Trans_Amount", $("#tran_amnt").val());
	
	data.append("API_ID", $("#api_id").val());
	
	data.append("Distribution_Id"   , $("#distribution_id").val());
	data.append("Distribution_Name" , $("#distribution_name").val());

	for(var i=0;i<User_Ids.length;i++)
	{
		var User_Id   = User_Ids[i].value;
	    var User_Name = User_Names[i].value;
	    var Mobile_No = Mobile_Nos[i].value;
	    var Email_Id  = Email_Ids[i].value;
	    var Status    = Statuses[i].value;
	    
	    data.append("User_Ids", User_Id);
	    data.append("User_Names", User_Name);
	    data.append("Mobile_Nos", Mobile_No);
	    data.append("Email_Ids", Email_Id);
	    data.append("Statuses", Status);
	}

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Event_Creation",
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
	    error: function (jqXHR, textStatus, errorThrown) { Sweetalert("warning", "", "errrrr");  }
   });
}

function Retrieve_Event_Code()
{
	$("#event_code").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/Event/suggestions/EventCode",
	            type :  'POST',
	            dataType: "json",
	            data:  
				{    term : request.term,   
	            	 module : $("#module").val() 
				},
	            success: function(data) { response(data); }
	        });
    	},
	    minLength: 1,
	    select: function(event, ui) 
	    {
	    	$("#event_code").val(ui.item.label);
	    	
	    	Retrieve_Event();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function Retrieve_Event()
{
	var data = new FormData();
	
	data.append("Module", $("#module").val());
	data.append("Event_Code", $("#event_code").val());
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Event/Retrieve",
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
				//$("#module").val(data.Module)
				//$("#event_code").val(data.Event_Code);
				$("#event_name").val(data.Event_Name);
				$("#event_type").val(data.Event_Type);
				$("#event_sub_type").val(data.Event_Sub_Type);
				$("#event_name").val(data.Event_Name);
				$("#event_method").val(data.Event_Method);
				$("#freq_type").val(data.Frequency_Type);
				$("#freq_sub_type").val(data.Frequency_Sub_Type);
				$("#freq_time").val(data.Frequency_in_Time);
				$("#start_time").val(data.Start_Time);
				$("#end_time").val(data.End_Time);
				$("#holiday_exec").val(data.End_Time);
				$("#holiday_exec").prop("checked", data.Holiday_Execution === "1" ? true : false);
				$("#weekend_exec").prop("checked", data.Weekend_Execution === "1" ? true : false);
				$("#sys_stats_check").prop("checked", data.System_Status_Check === "1" ? true : false);
				$("#enabled_disabled").prop("checked", data.Enabled_or_diabled === "1" ? true : false);
				$("#pre_check").prop("checked", data.Pre_Check === "1" ? true : false);
				$("#par_event").prop("checked", data.Parent_Event === "1" ? true : false);
				$("#notify_team_id").val(data.Notification_Team_ID);
				$("#sla_time").val(data.SLA_Time);
				$("#sla_cnt_time").val(data.SLA_Count_Limit);
				$("#dup_check").prop("checked", data.Duplicate_Check === "1" ? true : false);
				$("#dup_check_time").val(data.Duplicate_Skip_Time);
				$("#sms_req").prop("checked", data.SMS_Required === "1" ? true : false);
				$("#email_req").prop("checked", data.Email_Required === "1" ? true : false);
				$("#call_req").prop("checked", data.Call_Required === "1" ? true : false);
				$("#sql_id").val(data.SQL_ID);
				$("#api_id").val(data.API_ID);
				
				$("#sql_name").val(data.SQL_Name);
				$("#sql_seq").val(data.SQL_SEQ_No);
				$("#sql_method").val(data.SQL_Method);
				$("#sql_sub_method").val(data.SQL_Sub_Method);
				$("#sql_query").val(data.SQL_Query);
				$("#cbd	").val(data.CBD);
				$("#ac_no").val(data.AC_NO);
				$("#cus_no").val(data.CUS_No);
				$("#tran_ref").val(data.TRAN_REF);
				$("#tran_amnt").val(data.Trans_Amount);
				
				Manage_sql_and_api();
			}
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) {  }
   });
}

function Retrieve_SQL_IDs()
{
	$("#sql_id, #sql_id2").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/Event/suggestions/SQlIDs",
	            type :  'POST',
	            dataType: "json",
	            data:  
				{    term : request.term,   
	            	 module : $("#module").val() 
				},
	            success: function(data) { response(data); }
	        });
    	},
	    minLength: 1,
	    select: function(event, ui) 
	    {
	    	$("#sql_id, #sql_id2").val(ui.item.label);
	    	
	    	//Retrieve_Event();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}


function Retrieve_Dlistid()
{
	$("#notify_team_id").autocomplete({
		source: function(request, response) 
		{
	        $.ajax({
	            url: $("#ContextPath").val()+"/Event/suggestions/DLISTIDs",
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
	    	$("#notify_team_id").val(ui.item.label);
	    	
	    	//Retrieve_Distribution();
	    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		}; 	
}

function Add_Header(Id)
{
	var rnd = randomString(6);
		
	$(Id).append(
		
		'<div id="'+rnd+'" class="row">'+
    		'<div class="col-md-1"></div>'+
			'<div class="col-md-2"></div>'+
				
			'<div class="col-md-3">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control" id="" placeholder="Key">'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-3">'+
				'<div class="form-group">'+
					'<input type="text" class="form-control" id="" placeholder="Value">'+
				'</div>'+
			'</div>'+
			
			'<div class="col-md-2">'+
				'<div class="form-group">'+
					'<button class="btn btn-danger btn-round" onclick="Remove_header(\''+rnd+'\')">Remove</button>'+
				'</div>'+
			'</div>'+							
		'</div>'	
	); 
}

function Remove_header(Id)
{
	$("#"+Id).remove();
}

function randomString(length) 
{
	var chars =  '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	
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