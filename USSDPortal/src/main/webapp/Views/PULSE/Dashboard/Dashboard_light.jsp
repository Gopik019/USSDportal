<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Home/CSS_&_JS.jsp" %>   
 
<body data-background-color="${body_color}">
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/PULSE/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
    	<div class="content">
				<div class="panel-header">
					<div class="page-inner py-4">
						
					</div>
				</div>
				<div class="page-inner mt--5">
					<div class="row mt--2">
						
						<div class="col-md-12">
							<div class="card full-height">
								<div class="card-body">
									<div class="card-title">Total credit &amp; debit transactions</div>
									<div class="row py-3">
										<div class="col-md-4 d-flex flex-column justify-content-around">
											<div>
												<h6 class="fw-bold text-uppercase text-success op-8">Total Credit</h6>
												<h3 class="fw-bold">9.782</h3>
											</div>
											<div>
												<h6 class="fw-bold text-uppercase text-danger op-8">Total Debit</h6>
												<h3 class="fw-bold">1,248</h3>
											</div>
										</div>
										<div class="col-md-6">
											<div id="chart-container">
												<canvas id="totalIncomeChart"></canvas>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
			
						
					</div>
					
					<div class="row">
						<div class="col-md-12">
							<div class="card">
								<div class="card-header">
									<div class="card-head-row">
										<div class="card-title">User Statistics</div>
										<div class="card-tools">
											<a href="#" class="btn btn-info btn-border btn-round btn-sm mr-2">
												<span class="btn-label">
													<i class="fa fa-pencil"></i>
												</span>
												Export
											</a>
											<a href="#" class="btn btn-info btn-border btn-round btn-sm">
												<span class="btn-label">
													<i class="fa fa-print"></i>
												</span>
												Print
											</a>
										</div>
									</div>
								</div>
								<div class="card-body">
									<div class="chart-container" style="min-height: 375px">
										<canvas id="statisticsChart"></canvas>
									</div>
									<div id="myChartLegend"></div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-6">
							<div class="card card-primary bg-primary-gradient">
								<div class="card-header">
									<div class="card-title">Daily Transactions</div>
									<div class="card-category">March 25 - April 02</div>
								</div>
								<div class="card-body">
									<div class="mb-4 mt-2">
										<h1>4,578.58</h1>
									</div>
									<div class="pull-in">
										<canvas id="dailySalesChart"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="card card-primary bg-primary-gradient">
								<div class="card-body">
									<h4 class="mt-3 b-b1 pb-2 mb-4 fw-bold">Active user right now</h4>
									<h1 class="mb-4 fw-bold">17</h1>
									<h4 class="mt-3 b-b1 pb-2 mb-5 fw-bold">Page view per minutes</h4>
									<div id="activeUsersChart"></div>			
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>	
	</div>
	
	  <%@ include file="../../../Headers_&_Footers/PULSE/common/Custom_Settings.jsp" %>   
</div>
 
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/setting-demo.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/PULSE/js/demo.js"/>' ></script>
	
</body>
</html>