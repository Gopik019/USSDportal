<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/TIPS/CSS_&_JS2.jsp" %>   
 
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
					                		
											<div class="col-md-2 ml-auto">
												<div class="form-group">	
													<label for="">Payment Gateway</label>
												</div>
											</div>
											
											<div class="col-md-3 mr-auto">
												<div class="form-group">		
													<select id="Paygate" class="form-control">
														<option>Select</option>
														<option>TIPS</option>
													</select>
													<label id="Paygate_error" class="text-danger"></label>
												</div>
											</div>	
																
										</div>
										
										<div class="row">
											<div class="col-md-12"><hr></div>
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Account No</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="number" class="form-control" id="Ac_No" placeholder="">
													<label id="Ac_No_error" class="text-danger"></label>
												</div>
											</div>	
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Mobile No</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="number" class="form-control" id="Mobile_No" placeholder="">
													<label id="Mobile_No_error" class="text-danger"></label>
												</div>
											</div>		
											
											<div class="col-md-3 mt-2">
												<button id="get_details" class="btn btn-secondary">Get Details</button>	
											</div>
																	
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Account Type</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<select id="Ac_type" class="form-control">
														<option>Select</option>
														<option value="P">Personal Account</option>
														<option value="B">Business Account</option>
														<option value="M">Merchant Account</option>
													</select>
													<label id="Ac_type_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Customer No</label>
												</div>
											</div>
											
											<div class="col-md-3">
												<div class="form-group">		
													<input type="number" class="form-control" id="Customer_No" placeholder="">
													<label id="Customer_No_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-3 mt-2">
													
											</div>
																				
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Full Name</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="text" class="form-control" id="Full_Name" placeholder="">
													<label id="Full_Name_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">NIDA ID</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="number" class="form-control" id="NIDA_Id" placeholder="">
													<label id="NIDA_Id_error" class="text-danger"></label>
												</div>
											</div>		
											
											<div class="col-md-3 mt-2">
													
											</div>
																				
										</div>
										
										<div class="row">	
					                		<div class="col-md-1"></div>
					                		
					                		<div class="col-md-1">
												<div class="form-group">	
													<label for="">Email Id</label>
												</div>
											</div>
											<div class="col-md-3">
												<div class="form-group">		
													<input type="number" class="form-control" id="Email_Id" placeholder="">
													<label id="Email_Id_error" class="text-danger"></label>
												</div>
											</div>		
											
											<div class="col-md-1">
												<div class="form-group">	
													<label for="">Identifier</label>
												</div>
											</div>
											
											<div class="col-md-3">
												<div class="form-group">	
													<input type="text" class="form-control" id="Identifier" placeholder="Enter Identifier">
													<label id="Identifier_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-1 no_show mt-3">												
												<button type="button" id="available" class="btn btn-sm2 btn-icon btn-round btn-success"><i class="fa fa-check"></i></button>	
												<button type="button" id="not_available" class="btn btn-sm2 btn-icon btn-round btn-danger"><i class="fa fa-times"></i></button>											
											</div>
																						
											<div class="col-md-2 mt-2">
												<button id="Check_Availability" class="btn btn-secondary">Check Availability</button>	
											</div>
											
										</div>
										
										
										<div id="custom_card_action" class="card-action">
											<div class="row">	
												<div class="col-md-12" align="center">
													<button id="identifier_action" class="btn btn-secondary">Submit</button>	
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