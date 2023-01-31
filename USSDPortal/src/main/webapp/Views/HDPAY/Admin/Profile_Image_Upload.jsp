<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS5.jsp" %>
 
<body data-background-color="${Menu.get('body_color').getAsString()}">
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
		</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">	
			
			<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %> 
			
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
														<img id="preview" class="img-upload-preview" width="200" src="<spring:url value="/resources/HDPAY/img/avatar-01.jpeg" />" alt="..." >
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
											<button class="btn btn-secondary float-right" onclick="Upload()">Submit</button>
										</div>
										<div class="col-md-6">	
											<button class="btn btn-danger float-left" onclick="reset_form()">Reset</button>
										</div>
									</div>	
								</div>
						</div>
							
					</div>
				</div>	
			</div>   
		</div>	
    </div> 	</div>
	</body>
</html>