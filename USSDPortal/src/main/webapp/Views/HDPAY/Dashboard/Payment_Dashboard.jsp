<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Dashboard/CSS_&_JS.jsp" %>   
 
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
					<div class="row mt--2">
						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">GEPG Transaction Value in TZS</a></b></h5>
									</div>
									
									<div class="chart-container">
										<canvas id="GEPG_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">GEPG Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="GEPG_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TRA Transaction Value In TZS</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TRA_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TRA Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TRA_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
							<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">MSC Transaction Value In TZS</a></b></h5></div>
									<div class="chart-container">
										<canvas id="MSC_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">MSC Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="MSC_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-6"></div>
						
						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS Outward Transaction Value in TZS</a></b></h5>
									</div>
									
									<div class="chart-container">
										<canvas id="TIPS_OUTWARD_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS Outward Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIPS_OUTWARD_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS Inward Transaction Value in TZS</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIPS_INWARD_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIPS Inward Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIPS_INWARD_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIGO Outward Transaction Value in TZS</a></b></h5>
									</div>
									
									<div class="chart-container">
										<canvas id="TIGO_OUTWARD_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIGO Outward Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIGO_OUTWARD_CHART_VOL"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIGO Inward Transaction Value in TZS</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIGO_INWARD_CHART_TZS"></canvas>
									</div>
								</div>
							</div>
						</div>	
						
						<div class="col-md-3">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h5><b><a href="<%= request.getContextPath() %>/HDPAY/Transaction_Reports">TIGO Inward Transaction Volume</a></b></h5></div>
									<div class="chart-container">
										<canvas id="TIGO_INWARD_CHART_VOL"></canvas>
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