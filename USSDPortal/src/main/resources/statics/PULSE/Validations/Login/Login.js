$(document).ready(function() {
	
	$("#login").submit(function(e) 
	{  
		e.preventDefault();

		$("#login").submit();

	}); 
	
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

$(document).ready(function() {
	
	$("#inputGroupFile01").change(function(event) {  
		  RecurFadeIn();
		  readURL(this);    
	});
	
	$("#inputGroupFile01").on('click',function(event) {
		 RecurFadeIn();
	});
	
	function readURL(input)
	{    
		  if(input.files && input.files[0]) 
		  {   
			    var reader = new FileReader();
			    var filename = $("#inputGroupFile01").val();
			    filename = filename.substring(filename.lastIndexOf('\\')+1);
			    reader.onload = function(e) {
			      debugger;      
			      $('#preview').attr('src', e.target.result);
			      $('#preview').hide();
			      $('#preview').fadeIn(500);      
			      $('.custom-file-label').text(filename);             
			    }
			    reader.readAsDataURL(input.files[0]);    
		  } 
		  
		  $(".alert").removeClass("loading").hide();
	}
	
	function RecurFadeIn()
	{ 
	  console.log('ran');
	  FadeInAlert("Wait for it...");  
	}
	
	function FadeInAlert(text)
	{
	  $(".alert").show();
	  $(".alert").text(text).addClass("loading");  
	}
	
});