<%@ include file="../../../Headers_&_Footers/Others/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Others/Dashboard/CSS_&_JS.jsp" %>   
 
<body>   
	<div class="content">
	
		<div class="panel-header bg-secondary-gradient">
			<div class="page-inner py-5">
				<div class="d-flex align-items-left align-items-md-center flex-column flex-md-row">
					<div>
						<h2 class="text-white pb-2 fw-bold">Main Dashboard</h2>
					</div>
					<div class="ml-md-auto py-2 py-md-0">
						<a href="#" class="btn btn-white btn-border btn-round mr-2">All Branch</a>
						<a href="#" class="btn btn-secondary btn-round">Branch 1</a>
						<a href="#" class="btn btn-secondary btn-round">Branch 2</a>
						<a href="#" class="btn btn-secondary btn-round">Branch 3</a>
						<a href="#" class="btn btn-secondary btn-round">Branch 4</a>
					</div>
				</div>
			</div>
		</div>
		
		<div class="page-inner mt--5">	
			
			<div class="row row-card-no-pd mt--2">
			
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body ">
							<div class="row">
								<div class="col-3">
									<div class="icon-big text-center">
										<i class="flaticon-users text-warning"></i>
									</div>
								</div>
								<div class="col-9 col-stats">
									<div class="numbers">
										<p class="card-category">Total Customer</p>
										<h4 class="card-title">1500</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body ">
							<div class="row">
								<div class="col-3">
									<div class="icon-big text-center">
										<i class="flaticon-web-1 text-success"></i>
									</div>
								</div>
								<div class="col-9 col-stats">
									<div class="numbers">
										<p class="card-category">Total Account</p>
										<h4 class="card-title">1000</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body">
							<div class="row">
								<div class="col-2">
									<div class="icon-big text-center">
										<i class="flaticon-diagram text-danger"></i>
									</div>
								</div>
								<div class="col-10 col-stats">
									<div class="numbers">
										<p class="card-category">Total Active Loan A/C</p>
										<h4 class="card-title">230</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body">
							<div class="row">
								<div class="col-2">
									<div class="icon-big text-center">
										<i class="flaticon-coins text-primary"></i>
									</div>
								</div>
								<div class="col-10 col-stats">
									<div class="numbers">
										<p class="card-category">Total Disburse Amount</p>
										<h4 class="card-title">25000</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body">
							<div class="row">
								<div class="col-3">
									<div class="icon-big text-center">
										<i class="flaticon-coins text-info"></i>
									</div>
								</div>
								<div class="col-9 col-stats">
									<div class="numbers">
										<p class="card-category">Total Repayment</p>
										<h4 class="card-title">25000</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 col-md-2">
					<div class="card card-stats card-round">
						<div class="card-body">
							<div class="row">
								<div class="col-3">
									<div class="icon-big text-center">
										<i class="flaticon-coins text-danger"></i>
									</div>
								</div>
								<div class="col-9 col-stats">
									<div class="numbers">
										<p class="card-category">Total Outstanding</p>
										<h4 class="card-title">25000</h4>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
			</div>
					
	        <div class="row">	
	        	
	        	<div class="col-md-5 col-sm-12">
					<div class="card full-height">
						
						<div class="card-header text-center">
							<div class="card-title">Statistics</div>
						</div>
								
						<div class="card-body">
																			
							<div class="d-flex flex-wrap justify-content-around pb-2 pt-4">
								<div class="px-2 pb-2 pb-md-0 text-center">
									<div id="circles-1"></div>
									<h6 class="fw-bold mt-3 mb-0">New Customers</h6>
								</div>
								<div class="px-2 pb-2 pb-md-0 text-center">
									<div id="circles-2"></div>
									<h6 class="fw-bold mt-3 mb-0">New Account</h6>
								</div>
							</div>		
							<div class="container">
								<div class="row mt-5">	
									<div class="col-4">
										<div class="float-right">Month : </div>
									</div>
									<div class="col-7">
										<div class="float-left"><input type="month" class="form-control" value="2021-06"></div>
									</div>
								</div>
							</div>											
						</div>
					</div>
				</div>
					
               	<div class="col-md-7 col-sm-12">
					<div class="card full-height">
						
						<div class="card-header text-center">
							<div class="card-title">Load Disbursement / Repayment</div>
						</div>
						
						<div class="card-body">		
							
							<div class="row">	
								<div class="col-12 m-auto">
				                  	<canvas id="sales-chart" height="200"></canvas>
				            	 </div>
							</div>
							
				
			                <div class="d-flex flex-row justify-content-center">
			                   <span class="mr-3"><i class="fas fa-square text-orange"></i> Disbursement  </span>						                   					
			                   <span class="mr-3">  <i class="fas fa-square text-primary"></i> Repayment   </span>				                  
			                   <span class="mr-3"> <i class="fas fa-square text-gray-light"></i> Outstanding </span>
			                </div>	
						</div>						
					</div>
				</div>
			</div>	
				
			</div>					
     	</div>	
      		
		 <input type="hidden" id="ContextPath" value="<%= request.getContextPath() %>" />
	</body>
</html>