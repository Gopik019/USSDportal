<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS4.jsp" %>   
 
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
			
		        <div class="row">
					<div class="col-md-12 mt--1">
		
						<div id="colour_body" class="card">
		
							<div id="tab_card" class="card-body">
								
									<div class="row mt--3">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Module</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<select id="module" class="form-control">
														<option value="">Select</option>
														<option value="TRANMON">Transactions Monitoring</option>
														<option value="APPMON">Application Server Monitoring</option>
														<option value="DBMON">Database Monitoring</option>		
														<option value="FMON">File Monitoring</option>		
														<option value="WEBMON">Web service Monitoring</option>				
														<option value="EXEMON">Exception and Error Scanning</option>				
														<option value="CONMON">Connectivity Monitoring</option>		
														<option value="INFMON">Infra Monitoring</option>						
													</select>
														<label id="module_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Event Code</label>
													</div>
												</div>
											<div class="col-md-8">
												<div class="form-group">		
													<input type="text" class="form-control" id="event_code" placeholder="" maxlength="25" size="20">
													<label id="event_code_error" class="text-danger"></label>
												</div>
											</div>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Event Name</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<input type="text" class="form-control" id="event_name" placeholder="" maxlength="50" size="55">
														<label id="event_name_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Event Type</label>
													</div>
												</div> 
										
												<div class="col-md-8">
													<div class="form-group">		
														<select id="event_type" class="form-control">
														    <option value="Select">Select</option>
															<option>Monitor</option> 
															<option>File Generation</option>
															<option>File Processing</option>
															<option>Reconciliation</option>	
															<option>Others</option>		
														</select>  
														<label id="event_type_error" class="text-danger"></label>
													</div>
												</div>
												
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Event Sub Type</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<select id="event_sub_type" class="form-control">
															<option value="Select">Select</option>
															<option value="F">Fixed Time</option> 
															<option value="N">Non-Stop</option>
															<option value="U">User Journey</option>
														</select>  
														<label id="event_sub_type_error" class="text-danger"></label>
													</div>
												</div>
											
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Event Method</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="form-group">	
														
														<select id="event_method" class="form-control">
															<option value="Select">Select</option>
															<option value="DB">Database</option> 
															<option value="WS">Web Service</option>
														</select>  
														<label id="event_method_error" class="text-danger"></label>
													</div>
												</div>
												
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Frequency Type</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<select id="freq_type" class="form-control">    
															<option value="Select">Select</option>
															<option value="D">Daily</option> 
															<option value="W">Weekly</option>
															<option value="M">Monthly</option>
															<option value="Q">Quarterly</option>
															<option value="H">Half yearly</option>
															<option value="Y">Yearly</option>
															<option value="T">Time</option>
															<option value="R">Time Range</option>
														</select>  
														<label id="freq_type_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Freq Sub Type</label>
													</div>
												</div> 
										
												<div class="col-md-8">
													<div class="form-group">	
														<select id="freq_sub_type" class="form-control">
															<option value="Select">Select</option>
															<option value="T">Time Based</option> 
														<!--  	<option value="C">Customer Level</option>
															<option value="A">Account Level</option>
															<option value="S">System Based </option> -->
														</select>  
														<label id="freq_sub_type_error" class="text-danger"></label>
													</div>
												</div>
											
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Frequency in Time</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<input id="freq_time" type="text" class="form-control" placeholder="For Example (H01, M15)">    	
														<label id="freq_time_error" class="text-danger"></label>
													</div>
												</div>												
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Start Time</label>
													</div>
												</div> 
										
												<div class="col-md-8">
													<div class="form-group">	
														<input id="start_time" type="time" class="form-control">    
														<label id="start_time_error" class="text-danger"></label>
													</div>
												</div>
												
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">End Time</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<input id="end_time" type="time" class="form-control"> 	
														<label id="end_time_error" class="text-danger"></label>
													</div>
												</div>										
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">SLA Time </label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">		
														<input id="sla_time" type="time" class="form-control"> 	
														<label id="sla_time_error" class="text-danger"></label>
													</div>
												</div>	
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Holiday Execution</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="holiday_exec" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="holiday_exec_error" class="text-danger"></label>
													</div>
												</div>											
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Weekend Execution</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="weekend_exec" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="weekend_exec_error" class="text-danger"></label>
													</div>
												</div>									
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">SLA Count Limit</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="form-group">	
														<input id="sla_cnt_time" type="number" class="form-control" maxlength="5" size="15">    
														<label id="sla_cnt_time_error" class="text-danger"></label>
													</div>
												</div>		
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Enabled/Disabled </label>
													</div>
												</div>
												<div class="col-md-8">												
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="enabled_disabled" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="enabled_disabled_error" class="text-danger"></label>
													</div>
												</div>					
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Duplicate Check </label>
													</div>
												</div>
												<div class="col-md-8">													
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="dup_check" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="dup_check_error" class="text-danger"></label>
													</div>
												</div>	
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Duplicate Skip Time</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="form-group">	
														<input id="dup_check_time" type="time" class="form-control">    
														<label id="dup_check_time_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">System Status Check</label>
													</div>
												</div> 
										
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="sys_stats_check" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="sys_stats_check_error" class="text-danger"></label>
													</div>
												</div>				
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Pre Check</label>
													</div>
												</div> 
										
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="pre_check" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="pre_check_error" class="text-danger"></label>
													</div>
												</div>		
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Parent Event</label>
													</div>
												</div>
												<div class="col-md-8">												
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="par_event" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="par_event_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
									</div>
		
									<div class="row">	
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">SMS Required</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="sms_req" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="sms_req_error" class="text-danger"></label>
													</div>
												</div>					
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Email Required</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="email_req" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="email_req_error" class="text-danger"></label>
													</div>
												</div>		
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Call Required</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-check" align="center">
														<label class="form-check-label">
															<input id="call_req" class="form-check-input" type="checkbox" value="">
															<span class="form-check-sign"></span>
														</label>
														<label id="call_req_error" class="text-danger"></label>
													</div>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-6">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Notification Team ID</label>
													</div>
												</div> 
												
												<div class="col-md-8">
													<div class="row">	
														<div class="col-md-6">
															<div class="form-group">	
																<input id="notify_team_id" type="text" class="form-control" maxlength="10" size="15">    
																<label id="notify_team_id_error" class="text-danger"></label>
															</div>
														</div>
														<div class="col-md-6">
															<div class="form-group">	
																<button class="btn btn-secondary" data-toggle="modal" data-target="#modal3">Configure</button>	
															</div>
														</div>
													</div>
												</div>
														
											</div>
										</div>
										
										<div class="col-md-6">
											<div id="sql_api" class="row">
												<div id="sql_config" class="col-md-4">
													<div class="form-group">	
														<label for="email2">SQL ID</label>
													</div>
												</div> 
									
												<div id="sql_config_" class="col-md-8">
													<div class="row">	
														<div class="col-md-6">
															<div class="form-group">	
																<input id="sql_id" type="number" class="form-control">    
																<label id="sql_id_error" class="text-danger"></label>
															</div>
														</div>
														<div class="col-md-6">
															<div class="form-group">	
																<button class="btn btn-secondary" data-toggle="modal" data-target="#modal1">Configure</button>	
															</div>
														</div>
													</div>
												</div>
										
												<div id="api_config" class="col-md-4">
													<div class="form-group">	
														<label for="email2">API ID</label>
													</div>
												</div>
										
												<div id="api_config_" class="col-md-8">
													<div class="row">	
														<div class="col-md-6">
															<div class="form-group">	
																<input id="api_id" type="number" class="form-control">    
																<label id="api_id_error" class="text-danger"></label>
															</div>
														</div>
														<div class="col-md-6">
															<div class="form-group">	
																<button class="btn btn-secondary" data-toggle="modal" data-target="#modal2">Configure</button>	
															</div>
														</div>
													</div>
												</div>	
											</div>
										</div>
									</div>
		
									<div id="custom_card_action" class="card-action">
										<div class="row">	
											<div class="col-md-12" align="center">
												<button id="event_creation" class="btn btn-secondary">Create Event</button>	
											</div>
										</div>	
									</div>
								</div>
							</div>
							
						</div>
					</div>	
				</div>   
			</div>	
      	</div> 	</div>
      	
      	<!-- ############### Model 1 ################ -->
      	
      		<div class="modal fade bd-example-modal-xl" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header text-center">
		        <h5 class="modal-title text-success w-100" id="exampleModalLongTitle">DB Configuration</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        	<div class="col-md-12">
		        		<div class="row">
				        	<div class="col-md-12">
				        		<div class="row">	                		
									<div class="col-md-1">
										<div class="form-group">	
											<label for="">SQL ID</label>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group">		
											<input type="number" class="form-control" id="sql_id2" placeholder="">
										</div>
									</div>
									
								             		
									<div class="col-md-1">
										<div class="form-group">	
											<label for="">SQL Name</label>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">		
											<input type="text" class="form-control" id="sql_name" placeholder="">
										</div>
									</div>
								
								</div>
	
				        	</div>
	   	
                	</div>
                	
                	<div class="row">
                		<div class="col-md-4">
	                		<div class="row">	
								<div class="col-md-3">
									<div class="form-group">	
										<label for="">Seq No</label>
									</div>
								</div>
								<div class="col-md-8">
									<div class="form-group">		
										<input type="number" class="form-control" id="sql_seq" placeholder="">
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="row">		                		
								<div class="col-md-4">
									<div class="form-group">	
										<label for="">Method</label>
									</div>
								</div>
								<div class="col-md-8">
									<div class="form-group">		
										<select id="sql_method" class="form-control">
											<option>Table</option>
											<option>Procedure</option>
											<option>Function</option>
										</select>
									</div>
								</div>		
							</div>
						</div>
						<div class="col-md-4">
							<div class="row">			                		
								<div class="col-md-4">
									<div class="form-group">	
										<label for="">Sub Method</label>
									</div>
								</div>
								
								<div class="col-md-8">
									<div class="form-group">		
										<select id="sql_sub_method" class="form-control">
											<option>Count</option>
											<option>Status</option>
											<option>Error Check</option>
										</select>
									</div>
								</div>
							</div>
						</div>
                	</div>

					<div class="row">	                		
						<div class="col-md-1">
							<div class="form-group">	
								<label for="">SQL</label>
							</div>
						</div>
						
						<div class="col-md-11">
							<div class="form-group">		
								<textarea rows="3" class="form-control" id="sql_query"></textarea>
							</div>
						</div>
					</div>
					
					<div class="row">	     
						<div class="col-md-12"><hr></div>  
					</div>
						
					<div class="row">	                		
						
						<div class="col-md-1">
							<div class="form-group mb-3">		
								<h6 class="text-success">INPUTS</h6>
							</div>
						</div>
						
						<div class="col-md-2">
							<div class="form-group">		
								<input type="date" class="form-control" id="cbd" placeholder="Current Business Date">
							</div>
						</div>
						
						<div class="col-md-3">
							<div class="form-group">	
								<input type="number" class="form-control" id="ac_no" placeholder="Account Number">
							</div>
						</div>	
					
						<div class="col-md-3">
							<div class="form-group">	
								<input type="number" class="form-control" id="cus_no" placeholder="Customer Number">
							</div>
						</div>	
					
						<div class="col-md-3">
							<div class="form-group">	
								<input type="text" class="form-control" id="tran_ref" placeholder="Transaction Reference">
							</div>
						</div>	
													
					</div>
					
					<div class="row">	     
						<div class="col-md-12"><hr></div>  
					</div>
					
					<div class="row">	     
						<div class="col-md-2">
							<div class="form-group">	
								<h6 class="text-success">OUTPUT</h6>
							</div>
						
						</div>           		
						<div class="col-md-4" align="center">
							<div class="form-group">		
								<input type="number" class="form-control" id="tran_amnt" placeholder="Transaction Amount">
							</div>
						</div>
						<div class="col-md-4"></div> 
					</div>
					
					<div class="row">	     
						<div class="col-md-12"><hr></div>  
					</div>
					
				</div>	
		      </div>
		      
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Proceed</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<!-- ############### Model 2 ################ -->
      	
      	<div class="modal fade bd-example-modal-lg" id="modal2" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">API Integration Configuration</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
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
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-primary">Save changes</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<!-- ############### Model 3 ################ -->
      	
      	<div class="modal fade bd-example-modal-xl" id="modal3" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLongTitle">Alert Distribution Creation</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      
		      <div class="modal-body">
		        	<div class="row">
                		<div class="col-md-12">
                	
	                		<div class="row">	
								<div class="col-md-2">
									<div class="form-group">	
										<label for="email2">Distribution ID</label>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">		
										<input type="text" class="form-control" id="distribution_id" placeholder="" maxlength="10" size="15">
										<label id="distribution_id_error" class="text-danger"></label>
									</div>
								</div>
									
								<div class="col-md-2">
									<div class="form-group">	
										<label for="email2">Distribution Name</label>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">		
										<input type="text" class="form-control" id="distribution_name" placeholder="" maxlength="10" size="15">
										<label id="distribution_name_error" class="text-danger"></label>
									</div>
								</div>
							</div>
										
							<div class="row">	
								<div class="col-md-12" align="center">		
									<div class="form-group">						
										<h5 class="text-secondary">Distribution List</h5>
									</div>
								</div>
								<div class="col-md-11 mt--2 scroll">
									<table id="example" class="table table-hover table-bordered table-head-bg-secondary table-bordered-bd-secondary">
									<thead>
										<tr>
											<th width="8%">S.No</th>
											<th width="12%">User Id</th>
											<th width="18%">User Name</th>
											<th width="18%">Mobile No</th>
											<th width="20%">Email Id</th>
											<th width="14%">Status</th>
											<th width="10%">Action</th>
										</tr>
									</thead>
									<tbody id="tbodyid">
										
									</tbody>
								</table>
							  </div>
							  <div class="col-md-1 mt--2">
							  	<button id="Add_Row" class="btn btn-secondary"><i class="fa fa-plus" aria-hidden="true"></i></button>	
							  </div>
							</div>
			
						</div>	
					</div>
		      </div>
		      <div class="modal-footer">
		        
		        <div class="row">	
					<div class="col-md-12" align="center">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Proceed</button>
					</div>
				</div>	
		      </div>
		    </div>
		  </div>
		</div>
      
	</body>
</html>