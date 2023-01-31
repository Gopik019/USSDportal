$(document).ready(function() { 
	
	alert("add template");
	
	 $("#add_temp").click(function() { 
			
		 alert("add template");
		 
		 Add_Template();
	});
	
}

function Add_Template()
{
	var data = new FormData();

		 	//data.append("SUBORGCODES", "EXIM");
			data.append("TEMPCODE", $("#temp_code").val());
			data.append("GROUPCODE", $("#grp_code").val()); 
			data.append("TEMPLATECONT", $("#temp_comment").val());
			data.append("ATTRIBUTENAME", $(".attrb").val());
			data.append("IDNAME", $(".name").val());
			data.append("VALUEMAP", $(".val_map").val());
			data.append("DEFAULTVALUE", $(".Default_val").val());
	
			alert("$("#Context_Path2").val()+"/HDPAY/Template");
	$.ajax({		 
		url  :  $("#Context_Path2").val()+"/HDPAY/Template",
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