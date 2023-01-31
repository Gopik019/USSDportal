<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Dashboard/CSS_&_JS3.jsp" %>   
 
 
<script src="https://code.highcharts.com/maps/highmaps.js"></script>
<script src="https://code.highcharts.com/mapdata/custom/africa.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>

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
						<div class="col-sm-6 col-md-3 nav-item"> 
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="icon-big text-center">
												<i class="flaticon-interface-6 text-primary"></i>
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<a href="#all-bill-block" class="card-category" data-toggle="collapse" aria-expanded="false" aria-controls="test-block">Bill Request</a>
												<h4 id="total_bill" class="card-title">1,515</h4>
											</div>
										</div>
									</div>
								</div>
							</div>								
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="icon-big text-center">
												<i class="flaticon-coins text-success"></i>
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
											<a href="#all-payment-block" class="card-category" data-toggle="collapse" aria-expanded="false" aria-controls="test-block">Payment Request</a>
												<h4 id="total_payment" class="card-title">350</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="icon-big text-center">
												<i class="flaticon-users text-primary"></i>
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<a href="#all-ac-lookup-block" class="card-category" data-toggle="collapse" aria-expanded="false" aria-controls="test-block">Account lookup Request</a>
												<h4 id="total_ac" class="card-title">4500</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="icon-big text-center">
												<i class="flaticon-repeat text-warning"></i>
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<a href="#all-recon-block" class="card-category" data-toggle="collapse" aria-expanded="false" aria-controls="test-block">Recon Request</a>
												<h4 id="total_recon" class="card-title">450</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
				<div class="collapse" id="all-bill-block">
					<div class="row row-card-no-pd">				
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/gepg.png" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">GEPG - Bill</p>
												<h4 id="gepg_bill" class="card-title">1000</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/msc.jpg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">MSC - Bill</p>
												<h4 id="msc_bill" class="card-title">100</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/tigo.png" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">TIGO - Bill</p>
												<h4 id="tigo_bill" class="card-title">415</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
					
				<div class="collapse" id="all-payment-block">			
					<div class="row row-card-no-pd " >										
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/gepg.png" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">GEPG</p>
												<h4 id="out_gepg" class="card-title">50</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/msc.jpg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">MSC</p>
												<h4 id="out_msc" class="card-title">100</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/tips.jpeg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">TIPS - Inward</p>
												<h4 id="in_tips" class="card-title">200</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/tips.jpeg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">TIPS - Outward</p>
												<h4 id="out_tips" class="card-title">200</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="collapse" id="all-ac-lookup-block">	
					<div class="row row-card-no-pd">				
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/gepg.png" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">GEPG - AC Lookup</p>
												<h4 id="ac_gepg" class="card-title">1000</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/msc.jpg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">MSC - AC Lookup</p>
												<h4 id="ac_msc" class="card-title">100</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/tips.jpeg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">TIPS - AC Lookup</p>
												<h4 id="ac_tips" class="card-title">415</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="collapse" id="all-recon-block">		
					<div class="row row-card-no-pd " >										
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body ">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/gepg.png" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">GEPG - Recon</p>
												<h4 id="recon_geog" class="card-title">50</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/msc.jpg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">MSC- Recon</p>
												<h4 id="recon_msc" class="card-title">100</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-3">
							<div class="card card-stats card-round">
								<div class="card-body">
									<div class="row">
										<div class="col-5">
											<div class="avatar">
												<img src='<spring:url value="/resources/HDPAY/img/bill/tips.jpeg" />' alt="..." class="avatar-img rounded-circle">
											</div>
										</div>
										<div class="col-7 col-stats">
											<div class="numbers">
												<p class="card-category">TIPS - Recon</p>
												<h4 id="recon_tips" class="card-title">200</h4>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
			</div>
			
			<div class="row">

						<div class="col-md-6">
							<div class="card">
								<div class="card-body">
									<div class="card-title" align="center">
										<h3><b>H2H_Reconciliation</b></h3>
									</div>
									
									<div class="chart-container">
										<div id="container_one"></div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="card full-height">
								<div class="card-body">
									<div class="card-title" align="center">
										<h3><b>Internet Banking Reconciliation</b></h3>
									</div>
									
									<div class="chart-container">
										<canvas id="IB"></canvas>
									</div>
								</div>
							</div>
						</div>
										
				<!--  <div class="col-md-6">
							<div class="card">						
								<div class="card-body">
								<div class="card-title" align="center"><h3><b>Source Map : Africa</b></h3></div>
									<div class="chart-container">
										<div id="container_two"></div>
									</div>
								</div>
							</div>
						</div>		 -->
				</div>
				
	<!--  		<div class="row">

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
				  </div> -->
						
			   </div>																				
			</div>
		</div>
	</div>	

</body>
</html>