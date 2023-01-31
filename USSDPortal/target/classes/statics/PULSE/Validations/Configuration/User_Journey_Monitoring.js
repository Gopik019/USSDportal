$(document).ready(function() {
	
	$("#Module_Id").change(function() { 
		
		$("#msform").html('');
		
		Find_User_Journey();
	});
	
	$("#Add_user_Journey").click(function() { 
		
		window.location.href = $("#ContextPath").val()+"/User_Journey_Creation";

	});
	
	Find_User_Journey();
});

function Find_User_Journey()
{
	var Module_Id = $("#Module_Id").val(); 
	
	if(Module_Id == 'Select')
	{
		return;
	}
	
	var data = new FormData();
	
	data.append("Module_Id" , Module_Id);

	$.ajax({		 
		url  :  $("#ContextPath").val()+"/User_Journey/Find",
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
				for(var i=0;i<data.User_Journies.length;i++)
				{
					var module_Id = data.User_Journies[i].MODULEID;
					var ujcode = data.User_Journies[i].UJCODE;
					var UJNAME = data.User_Journies[i].UJNAME;
					var FREQ1 = data.User_Journies[i].FREQ1;
						
					Add_Row_values(UJNAME, data[ujcode]);
				}
			}
			else
		    {
				$("#msform").html('');
		    }	
		},
		beforeSend: function( xhr )
 		{
 			Sweetalert("load", "", "Please Wait");
        },
	    error: function (jqXHR, textStatus, errorThrown) { }
   });
}

function Add_Row_values(UJNAME, data)
{
	var heading = '<div class="row heading">'+UJNAME+'</div>';
	
	var Processes = '';
	
	for(var i=0;i<data.length;i++)
	{
		var style = i==0 ? "start" : i == (data.length-1) ? "end" : "";
		
		Processes+= '<li class="fas fa-check '+style+' active" data-toggle="modal" data-target="#modal1"><h5>'+data[i].EVTNAME+'</h5></li>';
	}
	
	var content = '<div class="col-md-12">'+ heading + '<ul class="progressbar">' +Processes + '</ul>' + '</div>' ;
			
	$("#msform").append(content);
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