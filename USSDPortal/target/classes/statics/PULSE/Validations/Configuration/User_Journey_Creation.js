let Row_Num = 1; var table;

$(document).ready(function() {
	
	/*table = $('#example').DataTable( {
			 "dom": 't',
			 "ordering": false,
			 "fnRowCallback" : function(nRow, aData, iDisplayIndex) {
	             $("td:first", nRow).html(iDisplayIndex +1);
	             return nRow;
	         }
		});   */
	
	$("#event_creation").click(function() { 
		
		Create_Event();
	});
	
	$("#Add_Row").click(function() { 
		
		Add_Row();
	});
	
	User_Journey_Code_Autocomplete();
});

function Create_Event()
{
	var err = 0;
	
	var Event_Codes = $(".evtcode");
	var Event_Names = $(".evtname");
	
	if(!validation("user_jouery_type", "dd"))      { err++; }
	if(!validation("user_jouery_code", "txt"))     { err++; }
	if(!validation("user_jouery_name", "txt"))     { err++; }
	if(!validation("freq_in_sec", "txt"))          { err++; }
	
	for(var i=0;i<Event_Codes.length;i++)
	{
	    var Event_Code_Id = Event_Codes[i].id;
	    var Event_Name_Id = Event_Names[i].id;
	    
	    if(!validation(Event_Code_Id , "dd"))      { err++; }
	    if(!validation(Event_Name_Id , "dd"))      { err++; }
	}
	
	if(err !=0 )
	{
		return;
	}
	
	var data = new FormData();
	
	data.append("User_Journey_Type", $("#user_jouery_type").val());
	data.append("User_Journey_Code", $("#user_jouery_code").val());
	data.append("User_Journey_Name", $("#user_jouery_name").val());
	data.append("Frequency_in_Seconds", $("#freq_in_sec").val());

	for(var i=0;i<Event_Codes.length;i++)
	{
	    var Event_Code = Event_Codes[i].value;
	    var Event_Name = Event_Names[i].value;
	    
	    data.append("Event_Codes", Event_Code);
	    data.append("Event_Names", Event_Name);
	}

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/User_Journey_Creation",
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
	 
	 var evtcode_Id = randomString(8);
	 
	 var evtname_Id = randomString(8);
		
	 var Row =  "<tr id=\""+Id+"\">"+
					"<td class=\"w-10\"><div align=\"center\">"+Row_Num+"</div></td>"+ 
					"<td class=\"w-30\"><div class=\"form-group mb-1\"><input id=\""+evtcode_Id+"\" type=\"text\" class=\"form-control evtcode\"><label id=\""+evtcode_Id+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td class=\"w-50\"><div class=\"form-group mb-1\"><input type=\"text\" id=\""+evtname_Id+"\" class=\"form-control evtname\"><label id=\""+evtname_Id+"\_error\" class=\"text-danger\"></label></div>"+
					"<td class=\"w-10\"><div align=\"center\"><button class=\"btn btn-danger\" onclick=\"Remove_Row('"+Id+"')\"><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button></div></td>"+
				"</tr>";
	 Row_Num++;
	
     $("table tbody").append(Row);  
	
     Intialize_Autocomplete(evtcode_Id, evtname_Id);
     
     $("tbody").animate({ scrollTop: $('tbody')[0].scrollHeight - $('tbody')[0].clientHeight}, 500);
}

function Remove_Row(Id)
{
	//Row_Num--;
	
	$("#"+Id).remove();
}

function Intialize_Autocomplete(evtcode_Id, evtname_Id)
{
	$("#"+evtcode_Id).autocomplete( {
			minLength: 1,
			source: function(request, response) 
			{
		        $.ajax({
		            url: $("#ContextPath").val()+"/Event/suggestions/User_Journey/EventCode",
		            type :  'POST',
		            dataType: "json",
		            data:  
					{    term : request.term,
		            	 User_Journey_Type : $("#user_jouery_type").val()
					},
		            success: function(data) { response(data); }
		        });
			},    
			select: function(event, ui) 
 			{ 
 				$("#"+evtcode_Id).val(ui.item.value);
 				$("#"+evtname_Id).val(ui.item.id);
 			}
	   });
}

function User_Journey_Code_Autocomplete()
{
	$("#user_jouery_code").autocomplete( {
			minLength: 1,
			source: function(request, response) 
			{
		        $.ajax({
		            url: $("#ContextPath").val()+"/Event/suggestions/User_Journey_Codes",
		            type :  'POST',
		            dataType: "json",
		            data:  
					{    term : request.term,
		            	 User_Journey_Type: $("#user_jouery_type").val()  
					},
		            success: function(data) { response(data); }
		        });
			},    
			select: function(event, ui) 
 			{ 
 				$("#user_jouery_code").val(ui.item.value);
 				
 				Retrieve_Event();
 			}
	   });
}

function Retrieve_Event()
{
	var data = new FormData();
	
	data.append("User_Journey_Type", $("#user_jouery_type").val());
	data.append("User_Journey_Code", $("#user_jouery_code").val());  
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/User_Journey/Retrieve",
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
				$("#user_jouery_type").val(data.MODULEID);
				$("#user_jouery_code").val(data.UJCODE);
				$("#user_jouery_name").val(data.UJNAME);
				$("#freq_in_sec").val(data.FREQ1);
				
				//alert(data.Execution_Sequence.length);
				
				for(var i=0;i<data.Execution_Sequence.length;i++)
				{
					var module_Id = data.Execution_Sequence[i].MODULEID;
					var ujcode = data.Execution_Sequence[i].UJCODE;
					var evtseq = data.Execution_Sequence[i].UJSEQ;
					var evtcode = data.Execution_Sequence[i].EVTCODE;
					var evtname = data.Execution_Sequence[i].EVTNAME;

					Add_Row_values(evtseq, evtcode,evtname);
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

function Add_Row_values(row, evtcode, evtname)
{
	 var Id = randomString(6);
	 
	 var evtcode_Id = randomString(8);
	 
	 var evtname_Id = randomString(8);
		
	 var Row =  "<tr id=\""+Id+"\">"+
					"<td class=\"w-10\"><div align=\"center\">"+row+"</div></td>"+ 
					"<td class=\"w-30\"><div class=\"form-group mb-1\"><input id=\""+evtcode_Id+"\" type=\"text\" value=\""+evtcode+"\" class=\"form-control evtcode\"><label id=\""+evtcode_Id+"\_error\" class=\"text-danger\"></label></div></td>"+
					"<td class=\"w-50\"><div class=\"form-group mb-1\"><input type=\"text\" id=\""+evtname_Id+"\" value=\""+evtname+"\" class=\"form-control evtname\"><label id=\""+evtname_Id+"\_error\" class=\"text-danger\"></label></div>"+
					"<td class=\"w-10\"><div align=\"center\"><button class=\"btn btn-danger\" onclick=\"Remove_Row('"+Id+"')\"><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></button></div></td>"+
				"</tr>";
	 
     $("table tbody").append(Row);  
     
     Row_Num++;
    
     Intialize_Autocomplete(evtcode_Id, evtname_Id);
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