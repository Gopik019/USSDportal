$(document).ready(function() {

	$("#Submit").hide();
	
	$("#Dedicated_Thread, #Event_Thread, #Notify_Thread").change(function() { 
		
		$("#Submit").show();
	});
	
	$("#Submit").click(function() { 
		
		Update_Settings();
	});
});

function Update_Settings()
{
	var data = new FormData();
	
	data.append("Dedicated_Thread", $("#Dedicated_Thread").prop("checked") === true ? true : false);
	data.append("Event_Thread",     $("#Event_Thread").prop("checked")     === true ? true : false);
	data.append("Notify_Thread",    $("#Notify_Thread").prop("checked")    === true ? true : false);
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Manage/Threads",
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