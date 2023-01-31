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
								
								<div class="row fluid-container mt--3">	
	
				                	<div class="col-md-9">
										
										<div class="row">	
					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Channel Code</label>
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
													<label for="">Channel Type</label>
												</div>
											</div>
											<div class="col-md-4">
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
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Service Name</label>
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
													<label for="">Service Id</label>
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
													<label for="">URL / End Point</label>
												</div>
											</div>
											<div class="col-md-10">
												<div class="form-group">		
													<input type="text" class="form-control" id="url_end_point" placeholder="">
													<label id="url_end_point_error" class="text-danger"></label>
												</div>
											</div>											
										</div>
										
										<div class="row">						                	
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Flow</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">		
													<select id="Flow" class="form-control">
														<option>Select</option>
														<option value="I">Inward</option>
														<option value="O">Outward</option>									
													</select>
													<label id="Flow_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Protocol</label>
												</div>
											</div>
											<div class="col-md-4">
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
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Method</label>
												</div>
											</div>
											<div class="col-md-4">
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
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Format</label>
												</div>
											</div>
											<div class="col-md-4">
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
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Payload</label>
												</div>
											</div>
											
											<div class="col-md-10">
												<div class="form-group">		
													<textarea rows="3" class="form-control" id="Payload"></textarea>
													<label id="Payload_error" class="text-danger"></label>
												</div>
											</div>																	
										</div>
										
										<div class="row">						                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Signature Payload</label>
												</div>
											</div>
											
											<div class="col-md-10">
												<div class="form-group">		
													<textarea rows="3" class="form-control" id="Sign_Payload"></textarea>
													<label id="Sign_Payload_error" class="text-danger"></label>
												</div>
											</div>																	
										</div>
	
										<div id="custom_card_action" class="card-action">
											<div class="row">	
												<div class="col-md-6">
													<button id="event_creation" class="btn btn-secondary float-right">Submit</button>	
												</div>
												<div class="col-md-6">
													<button id="form_reset" class="btn btn-danger">Reset</button>	
												</div>
											</div>	
										</div>
									</div>
																
								<div class="col-md-3">																	
									<div class="row">				                		
										<div class="col-lg-6 col-md-12">
											<div class="form-group">	
												<label for="">Authorization</label>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">																																			
												<button id="view_exising_auth" class="btn btn-secondary btn-round" data-toggle="modal" data-target="#modal2">
													<span class="btn-label" data-toggle="tooltip" data-placement="bottom" title="View Configured Authorization"><i class="fa fa-eye"></i></span>													
												</button>												
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">																																			
												<button id="add_new_auth" class="btn btn-secondary btn-round" data-toggle="modal" data-target="#modal2">
													<span class="btn-label" data-toggle="tooltip" data-placement="bottom" title="Configure New Authorization"><i class="fa fa-cog"></i></span>													
												</button>												
											</div>
										</div>	
									</div>
										
								    <div class="row">		
										<div class="col-md-6">
											<div class="form-group">	
												<label for="">Header</label>
											</div>				
										</div>
										
										<div class="col-md-3">
											<div class="form-group">																																			
												<button id="view_exising_header" class="btn btn-secondary btn-round" data-toggle="modal" data-target="#modal1">
													<span class="btn-label" data-toggle="tooltip" data-placement="bottom" title="View Configured Headers"><i class="fa fa-eye"></i></span>					
												</button>
											</div>
										</div>	
										<div class="col-md-3">
											<div class="form-group">																																			
												<button id="add_new_header" class="btn btn-secondary btn-round" data-toggle="modal" data-target="#modal1">
													<span class="btn-label" data-toggle="tooltip" data-placement="bottom" title="Configure New Headers"><i class="fa fa-cog"></i></span>														
												</button>
											</div>
										</div>	
									</div>	
									
									
									<div class="row">		
										<div class="col-md-6">
											<div class="form-group">	
												<label for="">Job Required</label>
											</div>				
										</div>
										
										<div class="col-md-4">
											<div class="form-check">
												<label class="form-check-label">
													<input id="job_req" class="form-check-input" type="checkbox" value="">
													<span class="form-check-sign"></span>
												</label>
												<label id="job_req_error" class="text-danger"></label>
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
      	
      	<div class="modal fade bd-example-modal-lg" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">Headers Configuration</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">	        
	             
				   <div id="sample_api_header"  class="row">						                		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">Headers</label>
							</div>
						</div>
						
						<div class="col-md-4">
							<div class="form-group">		
								<input type="text" class="form-control api_key" id="key" placeholder="Key">
								<label id="key_error" class="text-danger"></label>
							</div>
						</div>
						
						<div class="col-md-4">
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
					<div id="t1_headers"></div>		
		      </div>
		      
		      <div class="modal-footer">
		        
		      </div>
		    </div>
		  </div>
		</div>
		
		
		<!-- ############### Model 2 for Authorizations ################ -->
      	
      	<div class="modal fade bd-example-modal-lg" id="modal2" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">Authorization</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">	        
 	
	             <div class="row mb-3">				
						<div class="col-md-2"></div>
            			<div class="col-md-2">
            				<div class="form-group">	
							<h6>Type</h6>
						</div>	
            			</div>
             				                		
						<div class="col-md-3">
							<select id="auth_type" class="form-control">
								<option value="No_Auth">No Auth</option>
								<option value="Basic_Auth">Basic Auth</option>
								<option value="OAuth_1">OAuth 1.0</option>
								<option value="OAuth_2">OAuth 2.0</option>							
								<option value="Bearer_Token">Bearer Token</option>							
								<option value="Digest_Auth">Digest Auth</option>							
								<option value="Hawk_Authentication">Hawk Authentication</option>
								<option value="AWS_Signature">AWS Signature</option>
								<option value="Akamai_EdgeGrid">Akamai EdgeGrid</option>
							</select>
						</div>
					</div>

					<div id="OAuth_1" class="auth_types" style="display:none">
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Signature Method</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<select class="form-control">
										<option>Select</option>
										<option>HMAC-SHA1</option>
										<option>HMAC-SHA256</option>
										<option>HMAC-SHA512</option>
										<option>RSA-SHA1</option>
										<option>RSA-SHA256</option>
										<option>RSA-SHA512</option>
										<option>PLAINTEXT</option>
									</select>
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Consumer Key</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Consumer Key">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Consumer Secret</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Consumer Secret">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Access Token</label><hr>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Access Token">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Token Secret</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Token Secret">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
  
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label class="bg-primary">Advanced</label>
								</div>							
							</div>
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Callback URL</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Callback URL">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Verifier</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Verifier">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Timestamp</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Timestamp">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Nonce</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Nonce">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Version</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Version">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Realm</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
					</div>	
					
					<div id="OAuth_2" class="auth_types" style="display:none">
					
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label>Current Token</label>
								</div>
							</div>
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Access Token</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="Access Token">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Header Prefix</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label>Configure New Token</label>
								</div>
							</div>
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Token Name</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Grant Type</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<select class="form-control">
										<option>Select</option>
										<option>Authorization Code</option>
										<option>Authorization Code (With PKCE)</option>
										<option>HMAC-SHA512</option>
										<option>Implicit</option>
										<option>Password Credentials</option>
										<option>Client Credentials</option>									
									</select>
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Auth URL</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label>Access Token URL</label>
								</div>
							</div>
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client ID</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client Secret</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Scope</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">State</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client Authentication</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<select class="form-control">
										<option>Select</option>
										<option>Send as Basic Auth Header</option>
										<option>Send Client Credential body</option>														
									</select>
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
					</div>	
					
					<div id="Bearer_Token" class="auth_types" style="display:none">
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Token</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>					
					</div>	
					
					<div id="Basic_Auth" class="auth_types" style="display:none">
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">User Name</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="user_id" placeholder="">
									<label id="user_id_error" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Password</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="password" class="form-control" id="Password" placeholder="">
									<label id="Password_error" class="text-danger"></label>
								</div>
							</div>	
						</div>					
					</div>	
					
					<div id="Digest_Auth" class="auth_types" style="display:none">
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">User Name</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Password</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label for="">Advanced</label>
								</div>
							</div>		
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Realm</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Nonce</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Algorithm</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">qop</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Nonce Count</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client Nonce</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Opaque</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
										
					</div>	
					
					<div id="Hawk_Authentication" class="auth_types" style="display:none">
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Hawk Auth ID</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Hawk Auth Key</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Algorithm</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label for="">Advanced</label>
								</div>
							</div>		
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">User</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Nonce</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">ext</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">app</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">dlg</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Timestamp</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
												
					</div>	
					
					<div id="AWS_Signature" class="auth_types" style="display:none">
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">AccessKey</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">SecretKey</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label for="">Advanced</label>
								</div>
							</div>		
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">AWS Region</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Service Name</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Session Token</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>												
					</div>	
					
					<div id="Akamai_EdgeGrid" class="auth_types" style="display:none">
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Access Token</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client Token</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Client Secret</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-12" align="center">
								<div class="form-group">	
									<label for="">Advanced</label>
								</div>
							</div>		
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Nonce</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Timestamp</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Base URL</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>	
						
						<div class="row">				                		
							<div class="col-md-2">
								<div class="form-group">	
									<label for="">Headers to sign</label>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">		
									<input type="text" class="form-control" id="" placeholder="">
									<label id="" class="text-danger"></label>
								</div>
							</div>	
						</div>												
					</div>	
								
		      </div>
		      
		      <div class="modal-footer">
		        
		      </div>
		    </div>
		  </div>
		</div>
		
	</body>
</html>