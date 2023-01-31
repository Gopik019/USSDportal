$(document).ready(function() {
	
	//var Interval = 3 * 1000;
	
	//setInterval(SessionManager, Interval);
});

function SessionManager()
{
	var data = new FormData();
	
	$.ajax({		 
		url  :  $("#Context_Path").val()+"/SessionManager",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result == 'Failed')
			{
				Sweetalert("warning_load_location", "", data.Message, data.URL);  
			}
		},
	    error: function (jqXHR, textStatus, errorThrown)  { }
   });
}

function Sweetalert(Type, Title, Info, URL_Link)
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
			  timer: 3000
		});
	}
	else if(Type == "warning_load_location")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info,
			  timer: 3000
		}).then(function (result) {
			  if (true)
			  {							  
				  window.location = URL_Link;
			  }
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