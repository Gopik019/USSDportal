<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Dashboard/CSS_&_JS3.jsp" %>   
 
<body data-background-color="${Menu.get('body_color').getAsString()}"> 
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
    	<div class="content">
    	
    			<div class="panel-header mt--3">
					<div class="page-inner py-4">
						<h4 class="page-title" align="center"><span class="datavision_head">${Menu.get('Title').getAsString()}</span></h4>
					</div>
				</div>
				
				<div class="page-inner mt--5">
					<div class="row">
					
						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<!--  <h5><b>Request Monitoring</b></h5> -->
									</div>
									
									<div class="d-flex flex-wrap justify-content-around">
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-1"></div>
											<h6 id="circles-1-desc" class="fw-bold mt-3 mb-0">
												<a data-toggle="collapse" href="#bill-block" aria-expanded="true" aria-controls="test-block">Bill Request</a>
											</h6>
										</div>
										</div>
										</div>
									</div>
									
								</div>
							
						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<!--  <h5><b>Request Monitoring</b></h5> -->
									</div>
									
									<div class="d-flex flex-wrap justify-content-around">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-2"></div>
											<h6 id="circles-2-desc" class="fw-bold mt-3 mb-0">
												<a data-toggle="collapse" href=".payment-block" aria-expanded="true" aria-controls="test-block">Payment Request</a>
											</h6>
										</div>
										
									</div>
									
								</div>
							</div>
						</div>

						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<!--  <h5><b>Request Monitoring</b></h5> -->
									</div>
									
									<div class="d-flex flex-wrap justify-content-around">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-3"></div>
											<h6 id="circles-3-desc" class="fw-bold mt-3 mb-0">
												<a data-toggle="collapse" href="#ac-block" aria-expanded="true" aria-controls="test-block">Account Lookup</a>
											</h6>
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						

						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<!--  <h5><b>Request Monitoring</b></h5> -->
									</div>
									
									<div class="d-flex flex-wrap justify-content-around">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-4"></div>
											<h6 id="circles-4-desc" class="fw-bold mt-3 mb-0">
												<a data-toggle="collapse" href="#recon-block" aria-expanded="true" aria-controls="recon-block">Recon Request</a>
											</h6>
										</div>
									</div>
									
								</div>
							</div>
						</div>
						
						
						<div class="col-md-12 collapse" id="bill-block">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										  <h5><b>Bill Monitoring</b></h5> 
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-5 pt-5 mt-2">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="Bill-GEPG"></div>
											<h6 id="Bill-GEPG-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Bill_Request_Reports">GEPG</a></h6>			
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="Bill-TRA"></div>
											<h6 id="Bill-TRA-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Bill_Request_Reports">TRA</a></h6>
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="Bill-TIPS"></div>
											<h6 id="Bill-TIPS-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Bill_Request_Reports">TIPS</a></h6>						    
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						
						<div class="col-md-6 collapse payment-block">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h5><b>Outward Payments</b></h5> 
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-5 pt-5 mt-2">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="OUT-PAYMENT-GEPG"></div>
											<h6 id="OUT-PAYMENT-GEPG-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">GEPG</a></h6>			
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="OUT-PAYMENT-TRA"></div>
											<h6 id="OUT-PAYMENT-TRA-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TRA</a></h6>
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="OUT-PAYMENT-TIPS"></div>
											<h6 id="OUT-PAYMENT-TIPS-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS</a></h6>						    
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						
						<div class="col-md-6 collapse payment-block">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										  <h5><b>Inward Payments</b></h5> 
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-5 pt-5 mt-2">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="IN-PAYMENT-GEPG"></div>
											<h6 id="IN-PAYMENT-GEPG-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">GEPG</a></h6>			
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="IN-PAYMENT-TRA"></div>
											<h6 id="IN-PAYMENT-TRA-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TRA</a></h6>
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="IN-PAYMENT-TIPS"></div>
											<h6 id="IN-PAYMENT-TIPS-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS</a></h6>						    
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						
						
						<div class="col-md-12 collapse" id="ac-block">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										  <h5><b>Account Monitoring</b></h5>
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-5 pt-5 mt-2">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="ACCOUNT-CURRENT"></div>
											<h6 id="ACCOUNT-CURRENT-desc" class="fw-bold mt-3 mb-0">CURRENT ACCOUNTS</h6>
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="ACCOUNT-SAVINGS"></div>
											<h6 id="ACCOUNT-SAVINGS-desc" class="fw-bold mt-3 mb-0">SAVINGS ACCOUNTS</h6>						    
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						
						<div class="col-md-12 collapse" id="recon-block">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										  <h5><b>Recon Monitoring</b></h5> 
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-5 pt-5 mt-2">
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="RECON-GEPG"></div>
											<h6 id="RECON-GEPG-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Reconcilation_Reports">GEPG</a></h6>			
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="RECON-TRA"></div>
											<h6 id="RECON-TRA-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Reconcilation_Reports">TRA</a></h6>
										</div>
										
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="RECON-TIPS"></div>
											<h6 id="RECON-TIPS-desc" class="fw-bold mt-3 mb-0"><a href="<%= request.getContextPath() %>/HDPAY/Reconcilation_Reports">TIPS</a></h6>						    
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
						
						
						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h3><b>Internet Banking Reconcilation</b></h3>
									</div>
									
									<div class="chart-container">
										<canvas id="IB"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h3><b>H2H Reconcilation</b></h3></div>
									<div class="chart-container">
										<canvas id="H2H"></canvas>
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