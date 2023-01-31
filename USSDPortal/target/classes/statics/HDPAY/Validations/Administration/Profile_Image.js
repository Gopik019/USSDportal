var fileUpload;
       	
function Upload()
{
	$("#submit").attr('disabled','disabled');
	fileUpload = document.getElementById("inputGroupFile01");

	if(fileUpload.value == '')
	{
		Sweetalert("warning", "", "Please Select an image to upload");
		
		return false;
	}
	else
	{
   	if (typeof (fileUpload.files) != "undefined")
   	{
           var size = parseFloat(fileUpload.files[0].size / 1024).toFixed(2);
           
			var fileext=fileUpload.files[0].name.split('.').pop().toLowerCase();
			
    		var img = new Image();
    		
		    img.src = window.URL.createObjectURL(fileUpload.files[0]);
		    
	    	var width = img.naturalWidth,height = img.naturalHeight;
	    	
           window.URL.revokeObjectURL( img.src );
		
	        if(!(width <= 300 || height <= 300)) 
	        {
               Sweetalert("warning", "", "Image dimension should be 300x300");
               
				return false;
			}
	        else if ( !(size <= 1024))
	        {
				Sweetalert("warning", "", "Image size should be less than or equal to 1 MB");
				
				return false;
			}
	        else if(!(fileext =='jpg' ||fileext =='jpeg' ||fileext =='png' ||fileext =='bmp') )
	        {
				Sweetalert("warning", "", "Image type should be jpg/jpeg/bmp/png");
				
				return false;
			}
	        else 
	        {
	    	    upload_Image();
   		}
      }
      else
      {
		    Sweetalert("warning", "", "This browser does not support");
	   }
   }
	
	return true;
}

function reset_form(){
	location.reload();
}
		 
function Clear()
{
      remove(fileUpload.selectedIndex);
}
		 
function upload_Image()
{
	var data = new FormData();
	
	var fileInput = document.getElementById('inputGroupFile01');
	
	var file = fileInput.files[0];
	
	data.append('Image', file);  

	$.ajax({		 
		url  :  $("#ContextPath").val() + "/Profile_Image_Upload",
		type :  'POST',
		data :  data,
		enctype: 'multipart/form-data',
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			Swal.close();
			
			if(data.Result == "Success")
			{ 
				Sweetalert("success_load_Current", "", "Image Uploaded Successfully !!");
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
	    	Sweetalert("error", "", "Technical Issue, Try Again Later !!");
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