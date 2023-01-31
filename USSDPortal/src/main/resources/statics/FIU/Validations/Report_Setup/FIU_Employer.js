$(document).ready(function() {
		
	 $("#nameonly").autocomplete({
		    source:   $("#ContextPath").val()+"/Users/Suggestions",
		    minLength: 1,
		    select: function(event, ui) 
		    {
		    	$("#searchInput").val(ui.item.label);
		    }
	 });
	 
	 $("#namewithimg").autocomplete({
		    source: $("#ContextPath").val()+"/Users/Suggestions",
		    minLength: 1,
		    select: function(event, ui) 
		    {
		    	$("#searchInput").val(ui.item.label);
		    }
	 }).autocomplete( "instance" )._renderItem = function(ul,item) 
	  	{
		     return $( "<li><div><img style='width:40px;' src='"+item.img+"'><span>"+item.label+"</span></div></li>" ).appendTo(ul);
		};	
});

function Demo()
{
	var data = new FormData();
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			
		},
		beforeSend: function( xhr )
		{
			
        },
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
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