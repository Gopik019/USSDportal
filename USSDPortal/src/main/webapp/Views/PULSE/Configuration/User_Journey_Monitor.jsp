<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS6.jsp" %>   
 
<%
  	String Module_Id = request.getAttribute("Module_Id") !=null ? (String)request.getAttribute("Module_Id") : "";
%>

<style>

</style>

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
					<div class="col-md-12  mt--1">
					
						<div id="colour_body" class="card">
								
							<div id="tab_card" class="card-body">
								
								<div class="row" align="center">	
						           		<div class="col-md-2"></div>	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="">Module Id</label>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="form-group">		
												<select id="Module_Id" class="form-control">
													<option value="Select">Select</option>
													<option value="TRANMON" <%= Module_Id.equals("TRANMON")? "selected" : "" %>>Transactions Monitoring</option>
													<option value="APPMON"  <%= Module_Id.equals("APPMON") ? "selected" : "" %>>Application Server Monitoring</option>
													<option value="DBMON"   <%= Module_Id.equals("DBMON")  ? "selected" : "" %>>Database Monitoring</option>		
													<option value="FILEMON" <%= Module_Id.equals("FILEMON")? "selected" : "" %>>File Monitoring</option>		
													<option value="WEBMON"  <%= Module_Id.equals("WEBMON") ? "selected" : "" %>>Web service Monitoring</option>				
													<option value="EXEMON"  <%= Module_Id.equals("EXEMON") ? "selected" : "" %>>Exception and Error Scanning</option>				
													<option value="CONMON"  <%= Module_Id.equals("CONMON") ? "selected" : "" %>>Connectivity Monitoring</option>		
													<option value="INFMON"  <%= Module_Id.equals("INFMON") ? "selected" : "" %>>Infra Monitoring</option>						
												</select>
											</div>
										</div>
										
										<div class="col-md-1">
									  		<button id="Add_user_Journey" class="btn btn-secondary"><i class="fa fa-plus" aria-hidden="true"></i></button>	
									  	</div>
									  
									</div>
									
								<div id="msform" class="row">	
							 		<div class="col-md-12">
									
									<!--  	<div class="row heading">Account Monitoring</div> 
									
				                   		 <ul class="progressbar">
					                        <li class="fas fa-check start active"><h5>Account</h5></li>
					                        <li class="fas fa-check active"><h5>Personal</h5></li>
					                        <li class="fas fa-times failed"><h5>Image</h5></li> 
					                        <li class="fas fa-times failed"><h5>Finish</h5></li>
					                        <li class="fas fa-times failed"><h5>Finish</h5></li>
					                        <li class="fas fa-times failed"><h5>Finish</h5></li>
					                        <li class="fas fa-times end failed"><h5>Finish</h5></li>     
					                    </ul>	
									</div>   -->
										
								</div>
							</div>
						</div>
					</div>
				</div>	
			</div>   
		</div>	
    </div> 
 </div>
     </div>
     
     
     <!-- ############### Model 1 ################ -->
      	
      		<div class="modal fade bd-example-modal-xl" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog modal-xl">
		    <div class="modal-content">
		      <div class="modal-header text-center">
		        <h5 class="modal-title text-success w-100" id="exampleModalLongTitle">Event Details</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        	<div class="col-md-12">
		        		<div class="row">
				        	<div class="col-md-12">
				        		<div class="row">	                		
									<div class="col-md-2">
										<div class="form-group">	
											<label for="">Event Name</label>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group">		
											<input type="number" class="form-control" id="sql_id2" placeholder="">
										</div>
									</div>
									
								             		
									<div class="col-md-2">
										<div class="form-group">	
											<label for="">Event Status</label>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group">		
											<input type="text" class="form-control" id="sql_name" placeholder="">
										</div>
									</div>
								
								</div>
	
				        	</div>
	   	
                	</div>
                	
                	<div class="row">	                		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">SLA Count</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">		
								<input type="number" class="form-control" id="sql_id2" placeholder="">
							</div>
						</div>
						
					             		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">SLA Time</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">		
								<input type="text" class="form-control" id="sql_name" placeholder="">
							</div>
						</div>
					
					</div>

					<div class="row">	                		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">Email Sent</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<div class="row">		
									<div class="col-md-2"><input type="checkbox" class="form-control" id="sql_id2" placeholder=""></div> 
									<div class="col-md-10"><input type="text" class="form-control" id="sql_name" placeholder="To Whom"></div>  
								</div>
							</div>
						</div>
											             		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">SMS Sent</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<div class="row">		
									<div class="col-md-2"><input type="checkbox" class="form-control" id="sql_id2" placeholder=""></div> 
									<div class="col-md-10"><input type="text" class="form-control" id="sql_name" placeholder="To Whom"></div>  
								</div>
							</div>
						</div>				
					</div>
					
					<div class="row">	                		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">Directly Contacted</label>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<div class="row">		
									<div class="col-md-2"><input type="checkbox" class="form-control" id="sql_id2" placeholder=""></div> 
									<div class="col-md-10"><input type="text" class="form-control" id="sql_name" placeholder="To Whom"></div>  
								</div>
							</div>
						</div>		
							             		
						<div class="col-md-6">
							
						</div>
					
					</div>
					
					<div class="row">	                		
						<div class="col-md-2">
							<div class="form-group">	
								<label for="">Failure Reason</label>
							</div>
						</div>
						
						<div class="col-md-10">
							<div class="form-group">		
								<textarea rows="3" class="form-control" id="sql_query"></textarea>
							</div>
						</div>
					</div>											
				</div>	
		      </div>
		      
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
		
	</body>
</html>