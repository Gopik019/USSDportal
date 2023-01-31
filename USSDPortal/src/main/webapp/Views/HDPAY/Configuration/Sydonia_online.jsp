<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<title>SYDONIA ONLINE</title>

	<link href='<spring:url value="/resources/PULSE/css/atlantis.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/PULSE/css/bootstrap.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/PULSE/css/fonts.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/HDPAY/css/styles.css" />' rel="stylesheet"> 

	<script type="text/javascript" src='<spring:url value="/resources/PULSE/sweetalert/dist/sweetalert.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/ajax/libs/jquery/3.2.1/jquery.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/plugin/webfont/webfont.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/atlantis.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/core/popper.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"/>' ></script>	
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/core/bootstrap.min.js"/>' ></script>
	
</head>   
 
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
			
		<div id="head_card" class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="cus_card-header">
						<div class="triangle-topleft" style="border-top: 50px solid #f1f1f1"></div>
						<div class="triangle-topright"  style="border-top: 50px solid #f1f1f1"></div>
						<div class="row" align="center">	
							<div class="col-md-12"><div class="card-title"><b>SYDONIA ONLINE</b></div></div>
						</div>
					</div>
				</div>
			</div>
		</div>
			
		        <div class="row">
		        
					<div class="col-md-12 mt--1">
		
						<div id="colour_body" class="card">
		
							<div id="tab_card" class="card-body">
								
								<div class="row fluid-container mt--3">	
	
				                	<div class="col-md-12">
										
										<div class="row">	
					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">PAYER ID</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">	
													<input type="text" class="form-control" id="Channel_Code" placeholder="">
													<label id="Channel_Code_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Invoice No</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="row">
													<div class="col-md-9">
														<div class="form-group">		
															<input type="text" class="form-control" id="Service_Id" placeholder="">	
															<label id="Channel_Type_error" class="text-danger"></label>
														</div>
													</div>
													<div class="col-md-3">
														<div class="form-group">	
															<button id="" class="btn btn-secondary">Check</button>	
														</div>
													</div>
												</div>
												
											</div>										
										</div>
	
										<div class="row">					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Declarant Code</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">	
													<input type="text" class="form-control" id="Service_Name" placeholder="">
													<label id="Service_Name_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Declarant Name</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">
													<label id="Service_Id_error" class="text-danger"></label>
												</div>
											</div>	
										</div>
										
										<div class="row">						                	
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Company Code</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">	
												    <input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="Flow_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Company Name</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="Protocol_type_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
									
										<div class="row">					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Assessment Year</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="method_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Assessment Serial</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="format_error" class="text-danger"></label>
												</div>
											</div>										
										</div>
										
										<div class="row">					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Assessment Number</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="method_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Amount</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">	
													<label id="format_error" class="text-danger"></label>
												</div>
											</div>										
										</div>

										<div id="custom_card_action" class="card-action">
											<div class="row">	
												<div class="col-md-6">
													<button id="btn_creation" class="btn btn-secondary float-right"  data-toggle="modal" data-target="#modal1">Send OTP</button>	
													
												</div>
												<div class="col-md-6">
													<button id="form_reset" class="btn btn-danger">Reset</button>	
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
	 </div>
	 </div>
	 </div>	
	 	 
	 <!-- ############### Model 1 for Headers ################ -->
      	
      	<div class="modal fade bd-example-modal" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
  			<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">OTP Screen</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">	        
	             
				   <div class="row">						                		
						<div class="col-md-3">
							<div class="form-group">	
								<label for="">OTP</label>
							</div>
						</div>
						
						<div class="col-md-9">
							<div class="form-group">		
								<input type="text" class="form-control api_key" id="key" >
								<label id="key_error" class="text-danger"></label>
							</div>
						</div>
														
					</div>	
		      </div>
		      
		      <div class="modal-footer">
		      	<div class="row">	
					<div class="col-md-6">
						<button id="btn_creation" class="btn btn-secondary">MAKE A PAYMENT</button>	
						
					</div>
					<div class="col-md-6">
						<button id="form_reset" class="btn btn-danger">RESEND THE OTP</button>	
					</div>
				</div>			        
		      </div>
		    </div>
		  </div>
		</div>
	
		
	</body>
</html>