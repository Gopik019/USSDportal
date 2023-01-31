let Row_Num = 1; var table; 

$(document).ready(function() {
	
	$("#distribution_creation").click(function() { 
		
		Create_distribution_Event();
	});
	
	$("#Add_Row").click(function() { 
		
		Add_Row();
	});
	
	Distribution_Id_Autocomplete();
});

function Create_distribution_Event()
{
	var err = 0;
	
	if(!validation("distribution_id", "txt"))      { err++; }
	if(!validation("distribution_name", "txt"))    { err++; }
	
	var User_Ids   = $(".user_Id");
	var User_Names = $(".user_name");
	var Mobile_Nos = $(".mob_no");
	var Email_Ids  = $(".emailid");
	var Statuses   = $(".status");
	  
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
	
	if(err !=0 )
	{
		return;
	}
	
	var data = new FormData();
	
	data.append("Distribution_Id"   , $("#distribution_id").val());
	data.append("Distribution_Name" , $("#distribution_name").val());
	data.append("Total_list_Size"   , User_Ids.length);
	
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
		url  :  $("#ContextPath").val()+"/Distribution_list_Creation",
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
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}

function Add_Row()
{
	 var Id = randomString(6);
	 
	 var user_Id = randomString(8);
	 var user_name = randomString(8);   
	 var mob_no = randomString(8);
	 var emailid = randomString(8);
	 var status_Id = randomString(8);
	 
	 var Row =  "<tr id=\""+Id+"\">"+
					"<td width=\"8%\"><div align=\"center\">"+Row_Num+"</div></td>"+ 
					"<td width=\"12%\"><div class=\"form-group mb-1\"><input id=\""+user_Id+"\" type=\"text\" class=\"form-control user_Id\"><label id=\""+user_Id+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"18%\"><div class=\"form-group mb-1\"><input id=\""+user_name+"\" type=\"text\" class=\"form-control user_name\"><label id=\""+user_name+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"18%\"><div class=\"form-group mb-1\"><input id=\""+mob_no+"\" type=\"text\" class=\"form-control mob_no\"><label id=\""+mob_no+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"20%\"><div class=\"form-group mb-1\"><input id=\""+emailid+"\" type=\"text\" class=\"form-control emailid\"><label id=\""+emailid+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"14%\">" +
						"<div class=\"form-group mb-1\">"+
							"<select id=\""+status_Id+"\" class=\"form-control status\">"+
								"<option value=\"1\">Enabled</option>"+
								"<option value=\"0\">Disabled</option>"+
							 "</select>" +
							 "<label id=\""+status_Id+"\_error\" class=\"text-danger\"></label>" +
						"</div>" +
					"</td>"+
					"<td width=\"10%\"><div align=\"center\"><button class=\"btn btn-danger\" onclick=\"Remove_Row('"+Id+"')\"><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button></div></td>"+
				"</tr>";
	 Row_Num++;
	
     $("table tbody").append(Row);  
	
     $("tbody").animate({ scrollTop: $('tbody')[0].scrollHeight - $('tbody')[0].clientHeight}, 500);
}

function Remove_Row(Id)
{
	//Row_Num--;
	
	$("#"+Id).remove();
}

function Distribution_Id_Autocomplete()
{
	$("#distribution_id").autocomplete( {
			minLength: 1,
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
			select: function(event, ui) 
 			{ 
 				$("#distribution_id").val(ui.item.value);
 				
 				Retrieve_Distribution();
 			}
	   });
}

function Retrieve_Distribution()
{
	var data = new FormData();
	
	data.append("Distribution_Id" , $("#distribution_id").val());
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Distribution_list/Retrieve",
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
				$("#distribution_id").val(data.Distribution_Id);
				$("#distribution_name").val(data.Distribution_Name);
				
				for(var i=0;i<data.Distribution_list.length;i++)
				{
					var User_Id = data.Distribution_list[i].User_Id;
					var User_Name = data.Distribution_list[i].User_Name;
					var Mobile_No = data.Distribution_list[i].Mobile_No;
					var Email_Id = data.Distribution_list[i].Email_Id;
					var Status = data.Distribution_list[i].Status;
					
					var SLNO = data.Distribution_list[i].SLNO;

					Add_Row_values(User_Id, User_Name, Mobile_No, Email_Id, Status, SLNO);
				}
			}
		},
		beforeSend: function( xhr )
 		{
			Row_Num = 1;
			
			$("#tbodyid").empty();
			
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) {  }
   });
}

function Add_Row_values(Usr_Id, Usr_Name, Mobile_No, Email_Id, Status, SLNO)
{
	 var Id = randomString(6);
	 
	 var user_Id = randomString(8);
	 var user_name = randomString(8);   
	 var mob_no = randomString(8);
	 var emailid = randomString(8);
	 var status_Id = randomString(8);
	 
	 var Enabled_class  = Status == '1' ? 'selected' : '';
	 var Disabled_class = Status == '0' ? 'selected' : '';
		
	 var Row =  "<tr id=\""+Id+"\">"+
					"<td width=\"8%\"><div align=\"center\">"+Row_Num+"</div></td>"+ 
					"<td width=\"12%\"><div class=\"form-group mb-1\"><input id=\""+user_Id+"\" type=\"text\" value=\""+Usr_Id+"\" class=\"form-control user_Id\"><label id=\""+user_Id+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"18%\"><div class=\"form-group mb-1\"><input id=\""+user_name+"\" type=\"text\" value=\""+Usr_Name+"\" class=\"form-control user_name\"><label id=\""+user_name+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"18%\"><div class=\"form-group mb-1\"><input id=\""+mob_no+"\" type=\"text\" value=\""+Mobile_No+"\" class=\"form-control mob_no\"><label id=\""+mob_no+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"20%\"><div class=\"form-group mb-1\"><input id=\""+emailid+"\" type=\"text\" value=\""+Email_Id+"\" class=\"form-control emailid\"><label id=\""+emailid+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td width=\"14%\">" +
						"<div class=\"form-group mb-1\">"+
							"<select id=\""+status_Id+"\" class=\"form-control status\">"+
								"<option value=\"1\" "+Enabled_class+">Enabled</option>"+
								"<option value=\"0\" "+Disabled_class+">Disabled</option>"+
							 "</select>" +
							 "<label id=\""+status_Id+"\_error\" class=\"text-danger\"></label>" +
						"</div>" +
					"</td>"+
					"<td width=\"10%\"><div align=\"center\"><button class=\"btn btn-danger\" onclick=\"Remove_Row('"+Id+"')\"><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button></div></td>"+
				"</tr>";
	 
     $("table tbody").append(Row);  
     
     Row_Num++;
    
     //Intialize_Autocomplete(evtcode_Id, evtname_Id);
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