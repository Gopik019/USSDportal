<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Dashboard/CSS_&_JS2.jsp" %>   
 
<body data-background-color="${Menu.get('body_color').getAsString()}">   
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
       <div class="main-panel">
    	<div class="content">
    	
    			<div class="panel-header bg-secondary-gradient">
					<div class="page-inner py-4">
						<div class="d-flex align-items-left align-items-md-center flex-column flex-md-row">
							<div>
								<h2 class="text-white pb-2 fw-bold">${Menu.get('Title').getAsString()}</h2>
								<h5 class="text-white op-7 mb-2"></h5>
							</div>
							<div class="ml-md-auto py-3">
									<a href="#" class="btn btn-sm btn-secondary btn-round mr-2">Today</a>
								<a href="#" class="btn btn-sm btn-secondary btn-round mr-2">Week</a>
								<a href="#" class="btn btn-sm btn-secondary btn-round mr-2">Month</a>
							</div>
						</div>
					</div>
				</div>
				
				<div class="page-inner mt--5">
				
		        <div class="row">

						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Outward Transaction Volume</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
					                </div>	
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Inward Transaction Volume</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart2" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
					                </div>	
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Outward Transaction Value in USD</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart3" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
					                </div>	
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Inward Transaction Value in USD</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart4" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
					                </div>	
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Outward Transaction Value in TZS</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart5" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
					                </div>	
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
									<div class="card-title" align="center">
										 <h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">Channel Inward Transaction Value in TZS</a></b></h5>
									</div>
									<div class="position-relative mb-4">
						                  <canvas id="sales-chart6" height="200"></canvas>
						             </div>
						
					                <div class="d-flex flex-row justify-content-center">
					                   <span class="mr-3"><i class="fas fa-square text-orange"></i> TIPS  </span>						                   					
					                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> TRA   </span>				                  
					                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> GEPG </span>
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