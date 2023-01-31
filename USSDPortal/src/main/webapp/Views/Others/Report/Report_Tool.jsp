<%@ include file="../../../Headers_&_Footers/Others/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Others/Report/CSS_&_JS.jsp" %>   
 
<body>   
	<div class="content">
		<div class="page-inner">	
			
		<%@ include file="../../../Headers_&_Footers/Others/common/Form_header.jsp" %>
			
			<%@ include file="Report_Popup.jsp" %>
			
		        <div class="row">		        
					<div class="col-md-12 mt--1">		
						<div id="colour_body" class="card">		
							<div id="tab_card" class="card-body">								
								<div class="row mt--3">		
				                	<div class="col-md-12" align="center">
										
										<div class="row justify-content-center">					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Report Type</label>
												</div>
											</div>										
											<div class="col-md-3">
												<div class="form-group">	
													<select class="form-control" id="Report_type">
														<option>Select</option>
													</select>
													<label id="Report_type_error" class="text-danger"></label>
												</div>
											</div>																																						
										</div>
										
										<div id="dyna_inputs"></div>	
									
										<div id="custom_card_action" class="card-action">
											<div class="row mt--2">	
												<div class="col-md-12" align="center">
													<button id="report_action" class="btn btn-secondary">Submit</button>	
												</div>
											</div>	
										</div>
									</div>
								</div>	
							</div>
						</div>
						</div>	
					</div>   
				
				
				<div class="row">		        
					<div class="col-md-12 mt--1">		
						<div id="colour_body" class="card">														   
								<div id="tab_card" class="card-body">								
									<div class="row fluid-container">		
					                	<div class="col-md-12" align="center">									
							                <div class="table-responsive data_report sm-table sm_row justify-content-center">
							                									
											</div>
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