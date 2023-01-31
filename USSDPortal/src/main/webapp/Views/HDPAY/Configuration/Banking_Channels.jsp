<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Configuration/CSS_&_JS2.jsp" %>   
 
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
								
								<div class="row mt--3">	
	
				                	<div class="col-md-12">
	
										<div class="row">	
					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Channel Id / Name</label>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">	
													<input type="text" class="form-control" id="Channel_Id" placeholder="Id">
													<label id="Channel_Id_error" class="text-danger"></label>
												</div>
											</div>
																					
											<div class="col-md-2">
												<div class="form-group">	
													<input type="text" class="form-control" id="Channel_Name" placeholder="Name">
													<label id="Channel_Name_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Business hours</label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">		
													<input type="time" class="form-control" id="From_Time" placeholder="">
													<label id="From_Time_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">		
													<input type="time" class="form-control" id="To_Time" placeholder="">
													<label id="To_Time_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
										
										<div class="row">	
										
					                		<div class="col-md-2">
												<div class="form-group">	
													<label for="">Authorization</label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">		
													<input type="text" class="form-control" id="user_id" placeholder="User Id">
													<label id="user_id_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">		
													<input type="password" class="form-control" id="Password" placeholder="Password">
													<label id="Password_error" class="text-danger"></label>
												</div>
											</div>	

											<div class="col-md-6">
												  <div class="row">												
													
														<div class="col-md-1">
															<div class="form-group">	
																<label for="">OAuth</label>
															</div>
														</div>
													
														<div class="col-md-1">											
															<div class="form-check" align="center">
																<label class="form-check-label">
																	<input id="oauth" class="form-check-input" type="checkbox" value="">
																	<span class="form-check-sign"></span>
																</label>
																<label id="oauth_error" class="text-danger"></label>
															</div>											
														</div>	
														
														<div class="col-md-2">
															<div class="form-group">	
																<label for="">Auto Recon</label>
															</div>
														</div>
													
														<div class="col-md-1">											
															<div class="form-check" align="center">
																<label class="form-check-label">
																	<input id="auto_recon" class="form-check-input" type="checkbox" value="">
																	<span class="form-check-sign"></span>
																</label>
																<label id="auto_recon_error" class="text-danger"></label>
															</div>											
														</div>	
														
														<div class="col-md-2">
															<div class="form-group">	
																<label for="">Amount Limit</label>
															</div>
														</div>
													
														<div class="col-md-1">												
															<div class="form-check" align="center">
																<label class="form-check-label">
																	<input id="Amount_limit" class="form-check-input" type="checkbox" value="">
																	<span class="form-check-sign"></span>
																</label>
																<label id="Amount_limit_error" class="text-danger"></label>
															</div>
														</div>	
														
														<div class="col-md-1">
															<div class="form-group">	
																<label for="">Status</label>
															</div>
														</div> 
														
														<div class="col-md-1">												
															<div class="form-check" align="center">
																<label class="switch small">
																  <input id="status" type="checkbox">
																  <span class="slider round small"></span>
																</label>
															</div>
														</div>
																									
													</div>										
												</div>
											</div>
											
											
										<div class="row">	
										
					                		<div class="col-md-2">
												<div class="form-group">	
													<label for="">Secret Key</label>
												</div>
											</div>
											
											<div class="col-md-4">
												<div class="form-group">		
													<input type="text" class="form-control" id="secret_key" placeholder="Secret Key" readonly>
													<label id="secret_key_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-2">
												<div class="form-group">	
													<button id="Generate_Secret" class="btn btn-secondary">Generate</button>	
												</div>
											</div>
											
										</div>	
											
										<div class="row mt--1">						
											<div class="col-md-12" align="center"><hr></div>					
											<div class="col-md-12" align="center">							
												<h6 class="text-secondary" style="text-align:center">Payment Service</h6>											
											</div>													
										</div>
										
										<div class="row">										
											
											<div class="col-md-3"></div>
											<div class="col-md-6">
												<table id="paygate" class="table table-head-bg-secondary table-bordered-bd-secondary">
												<thead>
													<tr>
														<th scope="col">S.No</th>
														<th scope="col">Payment Gateway</th>
														<th scope="col">Enabled / Disabled</th>														
													</tr>
												</thead>
												<tbody>													
													
												</tbody>
											</table>
										  </div>
										  <div class="col-md-3"></div>
										</div>
	
									<div id="custom_card_action" class="card-action">
										<div class="row mt--2">	
											<div class="col-md-12" align="center">
												<button id="channel_action" class="btn btn-secondary">Submit</button>	
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