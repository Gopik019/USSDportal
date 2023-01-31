<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Administration/CSS_&_JS3.jsp" %>   
 
 <%
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/Datavision/";

	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
				
				<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   
						
					<div class="row">
							<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">

								<div class="card-body">
								
									<div class="row">	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="email2">Profile Photo</label>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">	
												<div class="input-file input-file-image">
													<div class="avatar avatar-xxl">
														<img id="preview" class="img-upload-preview" width="200" src="<spring:url value="/resources/FIU/img/avatar-01.jpeg" />" alt="..." >
													</div>
												</div>	
											</div>	
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="email2">Upload Photo <span class="required-label">*</span></label>
											</div>
										</div>
										
										<div class="col-md-6">
											<div class="form-group">		
												<input type="file" id="inputGroupFile01" class="imgInp custom-file-input" aria-describedby="inputGroupFileAddon01"  accept="image/*">
												<label class="custom-file-label" for="inputGroupFile01">Choose file</label> 
											</div>
										</div>
									</div>
								</div>
								
								<div class="card-action">
									<div class="row">											
										<div class="col-md-6">
											<button class="btn btn-indigo float-right" onclick="Upload()">Submit</button>
										</div>
										<div class="col-md-6">	
											<button class="btn btn-danger float-left">Cancel</button>
										</div>
									</div>	
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>		
	</div>
</div>

<script type="text/javascript">
		
		var serbasePath="<%=basePath%>" ;

       	var fileUpload;
       	
		 function Upload()
		 {

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
		 		url  :  serbasePath + "/Profile_Image_Upload",
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
		 
		/*window.onload=function(){
			
			objHttpVal.clearMap();
			objHttpVal.setValue("Class","profileImgUpldbean");
			objHttpVal.setValue("Package","invoice");;
			objHttpVal.setValue("Method","loadImage");
			objHttpVal.setValue("CSRFTOKEN",document.getElementById("CSRFTOKEN").value);
			objHttpVal.sendAndReceive();
			if (objHttpVal.getValue("sucFlg")=="1" ) {
			var img1 =objHttpVal.getValue("imgData");
			
	
			return true;
			}else{
		
			return false;
			}
	
	}*/
        
       
                            			
    	</script>

</body>
</html>