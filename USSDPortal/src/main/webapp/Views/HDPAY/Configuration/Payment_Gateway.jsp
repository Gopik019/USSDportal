<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Configuration/CSS_&_JS3.jsp" %>   
 
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
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Payment Gateway</label>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">	
													<input type="text" class="form-control" id="Gateway_Id" placeholder="Id">
													<label id="Gateway_error" class="text-danger"></label>
												</div>
											</div>
																					
											<div class="col-md-3">
												<div class="form-group">	
													<input type="text" class="form-control" id="Gateway_Name" placeholder="Name">
													<label id="Gateway_Name_error" class="text-danger"></label>
												</div>
											</div>
											
										</div>
									
										<div class="row">	
					                		<div class="col-md-1"></div>
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
					                		<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Protocol</label>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">		
													<select id="Protocol" class="form-control">
														<option>Select</option>
														<option>REST</option>
														<option>SOAP</option>
														<option>TCP-IP</option>
														<option>HTTP</option>
														<option>HTTPS</option>
														<option>MQ</option>												
													</select>
													<label id="Protocol_error" class="text-danger"></label>
												</div>
											</div>	
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Format</label>
												</div>
											</div>
											<div class="col-md-2">
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
													<label for="">Transaction Fee Applicable</label>
												</div>
											</div>
											
											<div class="col-md-1">												
												<div class="form-check" align="center">
													<label class="form-check-label">
														<input id="Transaction_Fee" class="form-check-input" type="checkbox" value="">
														<span class="form-check-sign"></span>
													</label>
													<label id="Transaction_Fee_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-1">
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
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Amount limit</label>
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
											
											
																																						
										</div>
										
									<div class="row">	
										<div class="col-md-1"></div>											
										<div class="col-md-2">
											<div class="form-group">	
												<label for="">Reversal Principle Applicable</label>
											</div>
										</div>
										
										<div class="col-md-1">												
											<div class="form-check" align="center">
												<label class="form-check-label">
													<input id="Reversal" class="form-check-input" type="checkbox" value="">
													<span class="form-check-sign"></span>
												</label>
												<label id="Reversal_error" class="text-danger"></label>
											</div>
										</div>
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="">Reversal Charges Applicable</label>
											</div>
										</div>
										
										<div class="col-md-1">												
											<div class="form-check" align="center">
												<label class="form-check-label">
													<input id="Reversal_Fee" class="form-check-input" type="checkbox" value="">
													<span class="form-check-sign"></span>
												</label>
												<label id="Reversal_Fee_error" class="text-danger"></label>
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
									
										
									<div id="custom_card_action" class="card-action">
										<div class="row">	
											<div class="col-md-12" align="center">
												<button id="gateway_action" class="btn btn-secondary">Submit</button>	
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