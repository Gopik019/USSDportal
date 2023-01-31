<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Configuration/CSS_&_JS.jsp" %>   
 
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
		
							<div id="tab_card" class="card-body">
								
								<div class="row fluid-container  mt--3">	
	
				                	<div class="col-md-12">
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Channel Code</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">	
													<input type="text" class="form-control" id="Channel_Code" placeholder="">
													<label id="Channel_Code_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Channel Type</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="Channel_Type" class="form-control">
														<option>Select</option>
														<option>Payment Gateway</option>
														<option>Bank Channel System</option>									
													</select>
													<label id="Channel_Type_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
										
					                	
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Service Name</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">	
													<input type="text" class="form-control" id="Service_Name" placeholder="">
													<label id="Service_Name_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Service Id</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="text" class="form-control" id="Service_Id" placeholder="">
													<label id="Service_Id_error" class="text-danger"></label>
												</div>
											</div>	
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">URL / End Point</label>
												</div>
											</div>
											<div class="col-md-7">
												<div class="form-group">		
													<input type="text" class="form-control" id="url_end_point" placeholder="">
													<label id="url_end_point_error" class="text-danger"></label>
												</div>
											</div>											
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Flow</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="Flow" class="form-control">
														<option>Select</option>
														<option>Inward</option>
														<option>Outward</option>									
													</select>
													<label id="Flow_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Protocol</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="Protocol_type" class="form-control">
														<option>Select</option>
														<option>REST</option>
														<option>SOAP</option>
														<option>TCP-IP</option>
														<option>HTTP</option>
														<option>HTTPS</option>
														<option>MQ</option>												
													</select>
													<label id="Protocol_type_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
									
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Method</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="method" class="form-control">
														<option>GET</option>
														<option>POST</option>
														<option>PUT</option>
														<option>DELETE</option>
													</select>
													<label id="method_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Format</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="format" class="form-control">
														<option>JSON</option>
														<option>XML</option>
													</select>
													<label id="format_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Authorization</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="text" class="form-control" id="user_id" placeholder="User Id">
													<label id="user_id_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-3">
												<div class="form-group">	
													<input type="text" class="form-control" id="Password" placeholder="Password">
													<label id="Password_error" class="text-danger"></label>
												</div>
											</div>
				
											<div class="col-md-1"></div>	
										</div>
										
										<div id="t1_headers">
											<div class="row">	
						                		<div class="col-md-1"></div>
												<div class="col-md-2">
													<div class="form-group">	
														<label for="">Headers</label>
													</div>
												</div>
												
												<div class="col-md-3">
													<div class="form-group">		
														<input type="text" class="form-control api_key" id="key" placeholder="Key">
														<label id="key_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-3">
													<div class="form-group">	
														<input type="text" class="form-control api_value" id="value" placeholder="Value">
														<label id="value_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-2">
													<div class="form-group"> 														
														<button type="button" id="t1_add_header" class="btn btn-icon btn-round btn-primary">
															<i class="fas fa-plus"></i>
														</button>
													</div>
												</div>											
											</div>
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Payload</label>
												</div>
											</div>
											
											<div class="col-md-7">
												<div class="form-group">		
													<textarea rows="5" class="form-control" id="Payload"></textarea>
													<label id="Payload_error" class="text-danger"></label>
												</div>
											</div>				
										</div>
	
									<div id="custom_card_action" class="card-action">
										<div class="row">	
											<div class="col-md-12" align="center">
												<button id="event_creation" class="btn btn-secondary">Submit</button>	
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
	</body>
</html>