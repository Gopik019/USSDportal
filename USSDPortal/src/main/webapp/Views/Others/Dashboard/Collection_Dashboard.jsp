<%@ include file="../../../Headers_&_Footers/Others/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Others/Dashboard/CSS_&_JS2.jsp" %>   
 
<body>   
	<div class="content">
	
		<div class="panel-header bg-secondary-gradient">
			<div class="page-inner py-5">
				<div class="d-flex align-items-left align-items-md-center flex-column flex-md-row">
					<div>
						<h2 class="text-white pb-2 fw-bold">Collection Dashboard</h2>
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
		
		<div class="page-inner mt-3">	
			
	        <div class="row">	
	        	
	        	<div class="col-md-6 col-sm-12">
					<div class="card full-height">
						
						<div class="card-header text-center">
							<div class="card-title">Statistics</div>
						</div>
								
						<div class="card-body">
																			
							<div class="d-flex flex-wrap justify-content-around pb-2 pt-4">
								<div class="px-2 pb-2 pb-md-0 text-center">
									<div id="circles-1"></div>
									<h6 class="fw-bold mt-3 mb-0">Defaulter Account</h6>
								</div>
								<div class="px-2 pb-2 pb-md-0 text-center">
									<div id="circles-2"></div>
									<h6 class="fw-bold mt-3 mb-0">Pending Payment</h6>
								</div>
							</div>									
						</div>
					</div>
				</div>
					
               	<div class="col-md-6 col-sm-12">
					<div class="card full-height">
						
						<div class="card-header text-center">
							<div class="card-title">Load Disbursement / Repayment</div>
						</div>
						
						<div class="card-body">		
							
							<div class="row">	
								<div class="col-10 m-auto">
				                  	<canvas id="sales-chart" height="200"></canvas>
				            	 </div>
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