<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS.jsp" %>   
 
<body>
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/PULSE/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">
					
					<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %> 
			
		        <form class="form-register" action="#" method="post">
		       		
				   	<div class="row">
						<div class="col-md-12 mt--1">
							<div id="colour_body"  class="card">
								<div class="card-header">
									<div class="row" align="center">	
						           		<div class="col-md-2"></div>	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="">Payment Type</label>
											</div>
										</div>
										
										<div class="col-md-3">
											<div class="form-group">		
												<select class="form-control">
													<option>Internal Transfer</option>
													<option>Domestic Transfer</option>
													<option>International Transfer</option>				
												</select>
											</div>
										</div>
									</div>
								</div>
								<div id="tab_card" class="card-body">
									<ul class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">
										<li>											
											<a class="main_tab" id="pills-home-tab-nobd" data-toggle="pill" href="#pills-initiate" role="tab" aria-controls="pills-home-nobd" aria-selected="true" onclick="change_active('pills-initiate')">
												<span id="pills-initiate_main_tab" class="main_tab_round mtr_active">
													
												</span>
												<span id="pills-initiate_main_tab_line" class="main_tab_line mtl_active" > </span>
												<span class="main_tab_text"><label><b>Initiate Transaction Request</b></label></span>
											</a>
										</li>
										<li>											
											<a class="main_tab" id="pills-profile-tab-nobd" data-toggle="pill" href="#pills-request" role="tab" aria-controls="pills-home-nobd" aria-selected="true" onclick="change_active('pills-request')">
												<span id="pills-request_main_tab" class="main_tab_round"></span>
												<span id="pills-request_main_tab_line" class="main_tab_line"> </span>
												<span class="main_tab_text"><label><b>Verifiation Request</b></label></span>
											</a>
										</li>
										<li>											
											<a class="main_tab" id="pills-home-tab-nobd3" data-toggle="pill" href="#pills-Statement" role="tab" aria-controls="pills-home-nobd" aria-selected="true" onclick="change_active('pills-Statement')">
												<span id="pills-Statement_main_tab" class="main_tab_round"> </span>
												<span id="pills-Statement_main_tab_line" class="main_tab_line"> </span>
												<span class="main_tab_text"><label><b>Statement Verification</b></label></span>
											</a>
										</li>
										<li>											
											<a class="main_tab" id="pills-home-tab-nobd4" data-toggle="pill" href="#pills-Destination" role="tab" aria-controls="pills-home-nobd" aria-selected="true" onclick="change_active('pills-Destination')">
												<span id="pills-Destination_main_tab" class="main_tab_round"> </span>
												<span id="pills-Destination_main_tab_line" class="main_tab_line"> </span>
												<span class="main_tab_text"><label><b>Destination Verification</b></label></span>
											</a>
										</li>
 
									</ul>
								
								<div id="step_card" class="card">	
									<div class="tab-content mt-2 mb-3" id="pills-without-border-tabContent">
										
			<!--  section 1-->			<div class="tab-pane fade active show" id="pills-initiate" role="tabpanel" aria-labelledby="pills-home-tab-nobd">
											<section>  

									            <ul class="nav nav-pills nav-secondary" id="pills-tab" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab" data-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">API Integration Configuration</a>	
													</li>
													<li class="nav-item submenu">
														<a class="nav-link" id="pills-profile-tab" data-toggle="pill" href="#pills-profile" role="tab" aria-controls="pills-profile" aria-selected="false">DB Configuration</a>
													</li>
												</ul>
							
						
												<div class="tab-content mt-2 mb-3" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">
														
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">URL / End Point</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>GET</option>
																			<option>POST</option>
																			<option>PUT</option>
																			<option>DELETE</option>
																		</select>
																	</div>
																</div>
																
																<div class="col-md-1">
																	<div class="form-group">	
																		<label for="">Format</label>
																	</div>
																</div>
																<div class="col-md-2">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>JSON</option>
																			<option>XML</option>
																		</select>
																	</div>
																</div>
																
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Authorization</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="User Id">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="Password">
																	</div>
																</div>
						
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t1_headers">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Headers</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Key">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t1_add_header" class="btn btn-primary btn-round">Add Header</button>
																		</div>
																	</div>											
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Payload</label>
																	</div>
																</div>
																
																<div class="col-md-6">
																	<div class="form-group">		
																		<textarea rows="5" class="form-control" id=""></textarea>
																	</div>
																</div>
								
															</div>

															</div>	
														</div>
														
									                	
													</div>
													
													<div class="tab-pane fade" id="pills-profile" role="tabpanel" aria-labelledby="pills-profile-tab">
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">SQL</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>Table</option>
																			<option>Procedure</option>
																			<option>Function</option>				
																		</select>
																	</div>
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Arguments</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="No of Inputs">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="No of Outputs">
																	</div>
																</div>
																
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t1_inputs">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Input Arguments</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Input">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t1_add_input" class="btn btn-primary btn-round">Add Input</button>
																		</div>
																	</div>											
																</div>
															</div>
															</div>	
														</div>
													</div>	
													
													<div class="row">
														<div class="col-md-5"></div>
														<div class="col-md-2">
															<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
																<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-request" onclick="change_active('pills-request')">
																	<i class="fas fa-angle-right"></i>
																</button>
															</div>	
														</div>
														<div class="col-md-5"></div>
													</div>	
															
												</div>	
			        						</section>
										</div>
										
			<!--  section 2-->			<div class="tab-pane fade" id="pills-request" role="tabpanel" aria-labelledby="pills-profile-tab-nobd">
											  <section>  

									            <ul class="nav nav-pills nav-secondary" id="pills-tab2" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab2" data-toggle="pill" href="#pills-home2" role="tab" aria-controls="pills-home" aria-selected="true">API Integration Configuration</a>	
													</li>
													<li class="nav-item submenu">
														<a class="nav-link" id="pills-profile-tab2" data-toggle="pill" href="#pills-profile2" role="tab" aria-controls="pills-profile" aria-selected="false">DB Configuration</a>
													</li>
												</ul>
							
						
												<div class="tab-content mt-2 mb-3" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home2" role="tabpanel" aria-labelledby="pills-home-tab">
														
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">URL / End Point</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>GET</option>
																			<option>POST</option>
																			<option>PUT</option>
																			<option>DELETE</option>
																		</select>
																	</div>
																</div>
																
																<div class="col-md-1">
																	<div class="form-group">	
																		<label for="">Format</label>
																	</div>
																</div>
																<div class="col-md-2">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>JSON</option>
																			<option>XML</option>
																		</select>
																	</div>
																</div>
																
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Authorization</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="User Id">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="Password">
																	</div>
																</div>
																
																
						
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t2_headers">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Headers</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Key">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t2_add_header" class="btn btn-primary btn-round">Add Header</button>
																		</div>
																	</div>											
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Payload</label>
																	</div>
																</div>
																
																<div class="col-md-6">
																	<div class="form-group">		
																		<textarea rows="5" class="form-control" id=""></textarea>
																	</div>
																</div>
	
															</div>
							
															</div>	
														</div>
														
									                	
													</div>
													
													<div class="tab-pane fade" id="pills-profile2" role="tabpanel" aria-labelledby="pills-profile-tab">
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">SQL</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>Table</option>
																			<option>Procedure</option>
																			<option>Function</option>				
																		</select>
																	</div>
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Arguments</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="No of Inputs">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="No of Outputs">
																	</div>
																</div>
																
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t2_inputs">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Input Arguments</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Input">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t2_add_input" class="btn btn-primary btn-round">Add Input</button>
																		</div>
																	</div>											
																</div>
															</div>
															</div>	
														</div>
													</div>	
													
													<div class="row">
														<div class="col-md-5"></div>
														<div class="col-md-1">
															<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
																<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-initiate" onclick="change_active('pills-initiate')">
																	<i class="fas fa-angle-left"></i>
																</button>
															
															</div>
														</div>
														<div class="col-md-1">
															<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
																<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-Statement" onclick="change_active('pills-Statement')">
																	<i class="fas fa-angle-right"></i>
																</button>
															</div>	
														</div>
														<div class="col-md-5"></div>
													</div>				
												</div>	
			        						</section>
										</div>
										
			<!--  section 3-->			<div class="tab-pane fade" id="pills-Statement" role="tabpanel" aria-labelledby="pills-contact-tab-nobd">
											     <section>  

									            <ul class="nav nav-pills nav-secondary" id="pills-tab3" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab3" data-toggle="pill" href="#pills-home3" role="tab" aria-controls="pills-home" aria-selected="true">API Integration Configuration</a>	
													</li>
													<li class="nav-item submenu">
														<a class="nav-link" id="pills-profile-tab3" data-toggle="pill" href="#pills-profile3" role="tab" aria-controls="pills-profile" aria-selected="false">DB Configuration</a>
													</li>
												</ul>
							
						
												<div class="tab-content mt-2 mb-3" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home3" role="tabpanel" aria-labelledby="pills-home-tab">
														
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">URL / End Point</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>GET</option>
																			<option>POST</option>
																			<option>PUT</option>
																			<option>DELETE</option>
																		</select>
																	</div>
																</div>
																
																<div class="col-md-1">
																	<div class="form-group">	
																		<label for="">Format</label>
																	</div>
																</div>
																<div class="col-md-2">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>JSON</option>
																			<option>XML</option>
																		</select>
																	</div>
																</div>
																
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Authorization</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="User Id">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="Password">
																	</div>
																</div>
																
																
						
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t3_headers">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Headers</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Key">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t3_add_header" class="btn btn-primary btn-round">Add Header</button>
																		</div>
																	</div>											
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Payload</label>
																	</div>
																</div>
																
																<div class="col-md-6">
																	<div class="form-group">		
																		<textarea rows="5" class="form-control" id=""></textarea>
																	</div>
																</div>
								
															</div>
							
															</div>	
														</div>
														
									                	
													</div>
													
													<div class="tab-pane fade" id="pills-profile3" role="tabpanel" aria-labelledby="pills-profile-tab">
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">SQL</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>Table</option>
																			<option>Procedure</option>
																			<option>Function</option>				
																		</select>
																	</div>
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Arguments</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="No of Inputs">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="No of Outputs">
																	</div>
																</div>
																
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t3_inputs">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Input Arguments</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Input">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t3_add_input" class="btn btn-primary btn-round">Add Input</button>
																		</div>
																	</div>											
																</div>
															</div>
															</div>	
														</div>
													</div>	
												</div>	
												
												<div class="row">
													<div class="col-md-5"></div>
													<div class="col-md-1">
														<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
															<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-request" onclick="change_active('pills-request')">
																<i class="fas fa-angle-left"></i>
															</button>
														
														</div>
													</div>
													<div class="col-md-1">
														<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
															<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-Destination" onclick="change_active('pills-Destination')">
																<i class="fas fa-angle-right"></i>
															</button>
														</div>	
													</div>
													<div class="col-md-5"></div>
												</div>	
													
			        						</section>
										</div>
										
			<!--  section 4-->			<div class="tab-pane fade" id="pills-Destination" role="tabpanel" aria-labelledby="pills-contact-tab-nobd">
											  <section>  

									            <ul class="nav nav-pills nav-secondary" id="pills-tab4" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab4" data-toggle="pill" href="#pills-home4" role="tab" aria-controls="pills-home" aria-selected="true">API Integration Configuration</a>	
													</li>
													<li class="nav-item submenu">
														<a class="nav-link" id="pills-profile-tab4" data-toggle="pill" href="#pills-profile4" role="tab" aria-controls="pills-profile" aria-selected="false">DB Configuration</a>
													</li>
												</ul>
							
						
												<div class="tab-content mt-2 mb-3" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home4" role="tabpanel" aria-labelledby="pills-home-tab">
														
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">URL / End Point</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>GET</option>
																			<option>POST</option>
																			<option>PUT</option>
																			<option>DELETE</option>
																		</select>
																	</div>
																</div>
																
																<div class="col-md-1">
																	<div class="form-group">	
																		<label for="">Format</label>
																	</div>
																</div>
																<div class="col-md-2">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>JSON</option>
																			<option>XML</option>
																		</select>
																	</div>
																</div>
																
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Authorization</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="User Id">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="Password">
																	</div>
																</div>
																
																
						
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t4_headers">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Headers</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Key">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t4_add_header" class="btn btn-primary btn-round">Add Header</button>
																		</div>
																	</div>											
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Payload</label>
																	</div>
																</div>
																
																<div class="col-md-6">
																	<div class="form-group">		
																		<textarea rows="5" class="form-control" id=""></textarea>
																	</div>
																</div>
								
															</div>
							
															</div>	
														</div>
														
									                	
													</div>
													
													<div class="tab-pane fade" id="pills-profile4" role="tabpanel" aria-labelledby="pills-profile-tab">
														<div class="row">
									                		<div class="col-md-12">
									                	
										                	<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">SQL</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="">
																	</div>
																</div>
																
															</div>
														
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Method</label>
																	</div>
																</div>
																<div class="col-md-6">
																	<div class="form-group">		
																		<select class="form-control">
																			<option>Table</option>
																			<option>Procedure</option>
																			<option>Function</option>				
																		</select>
																	</div>
																</div>
															</div>
															
															<div class="row">	
										                		<div class="col-md-1"></div>
																<div class="col-md-2">
																	<div class="form-group">	
																		<label for="">Arguments</label>
																	</div>
																</div>
																<div class="col-md-3">
																	<div class="form-group">		
																		<input type="text" class="form-control" id="" placeholder="No of Inputs">
																	</div>
																</div>
															
																
																<div class="col-md-3">
																	<div class="form-group">	
																		<input type="text" class="form-control" id="" placeholder="No of Outputs">
																	</div>
																</div>
																
																<div class="col-md-1"></div>	
															</div>
															
															<div id="t4_inputs">
																<div class="row">	
											                		<div class="col-md-1"></div>
																	<div class="col-md-2">
																		<div class="form-group">	
																			<label for="">Input Arguments</label>
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">		
																			<input type="text" class="form-control" id="" placeholder="Input">
																		</div>
																	</div>
																	
																	<div class="col-md-3">
																		<div class="form-group">	
																			<input type="text" class="form-control" id="" placeholder="Value">
																		</div>
																	</div>
																	
																	<div class="col-md-2">
																		<div class="form-group">
																			<button type="button" id="t4_add_input" class="btn btn-primary btn-round">Add Input</button>
																		</div>
																	</div>											
																</div>
															</div>
															</div>	
														</div>
													</div>	
												</div>	
												
												<div class="row">
													<div class="col-md-5"></div>
													<div class="col-md-2">
														<div class="nav nav-pills nav-secondary main_tab_nav" id="pills-tab-without-border" role="tablist">				
															<button type="button" class="btn btn-icon btn-round btn-secondary" data-toggle="pill" data-target="#pills-request" onclick="change_active('pills-Statement')">
																<i class="fas fa-angle-left"></i>
															</button>
														
														</div>
													</div>
													<div class="col-md-5"></div>
												</div>	
												
			        						</section>
										</div>
									</div>	
								</div>	
							</div>	
						</div>   
					</div>	
		       	</div> 
		     </form>
		</div>
	</div>
</div>
</div>

</body>
</html>