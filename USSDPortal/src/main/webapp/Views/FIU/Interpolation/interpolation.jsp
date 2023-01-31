<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Interpolation/CSS & JS4.JSP"%>  
 
<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %> 
	
    <div class="main-panel">
    
		<div class="content">
		
			<div class="page-inner">	
			
					<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   
			
	        <div class="row fluid-container">
	        
				<div class="col-md-12 mt--1">
					<div id="colour_body" class="card">
	
						<div id="tab_card" class="card-body">
						
							<div class="row">

										<div class="col-md-12">

											<div class="row">
												<div class="col-md-2"></div>
												<div class="col-md-4">
													<div class="row">
														<div class="col-md-4">
															<div class="form-group">
																<label for="sb">Sec ID</label>
															</div>
														</div>
														<div class="col-md-8">
															<div class="form-group">
																<input type="text" class="form-control" placeholder="" id="sec">
															</div>
														</div>
													</div>
												</div>
												<div class="col-md-4">
													<div class="row">
														<div class="col-md-4">
															<div class="form-group">
																<label for="sb">Date</label>
															</div>
														</div>
														<div class="col-md-8">
															<div class="form-group">
																<input type="date" class="form-control" placeholder="" id="date">
															</div>
														</div>
													</div>
												</div>
												
												<div class="col-md-2">
													<div class="form-group">
														<button id="get_value" class="btn btn-secondary">Get Details</button>	
													</div>												
												</div>
												
											</div>
	
										<hr>
							<div class="row" id="table">
								<div class="col-md-2"></div>
								<div class="col-md-8">
						
				             		 <div class="table-responsive sm-table">
				              
									<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
						
									<thead>
										<tr role="row">
											<th>BANK</th>
											<th>SEC ID</th>
											<th>TENOR</th>
											<th>BID</th>
											<th>ASK</th>
											<th>AVG</th>
										</tr>
									</thead>
									
									 <tbody>
									 	<tr role="row">
											
										</tr>																																						
									</tbody>
									
								</table>
							</div>
							</div>
							<div class="col-md-2">
								<button id="pdf" class="btn btn-secondary">PDF</button>
								<button id="excel" class="btn btn-secondary">EXCEL</button>
							</div>
						</div>
							
							<div id="custom_card_action" class="card-action">
													<div class="row">
														<div class="col-md-4"></div>
														<div class="col-md-4">
															<div class="row">
																<div class="col-md-8">
																	<button id="value_add" class="btn btn-secondary">Submit</button>
																</div>
																<div class="col-md-4">
																	<button id="dis" class="btn btn-secondary">Discard</button>
																</div>
															</div>
														</div>
														<div class="col-md-4"></div>
													</div></div>
									</div></div>
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