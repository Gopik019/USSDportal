<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Reversal/CSS_&_JS.jsp" %>   
 
<body data-background-color="${Menu.get('body_color').getAsString()}">   
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">	
			
		<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %>
			
		        <div class="row">
		        
					<div class="col-md-12 mt--1">
		
						<div id="colour_body" class="card">
		
							<div id="tab_card" class="card-body">
								
								<div class="row mt--3">	
	
				                	<div class="col-md-12">
										
										<div class="row">	
					                		
					                		<div class="col-md-1">
												<div class="form-group">	
													<label for="">Txn Date</label>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">		
													<input type="date" class="form-control" id="Tran_date" placeholder="">
													<label id="Tran_date_error" class="text-danger"></label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">		
													<select id="Paygate" class="form-control">
														<option value="Select">PAYTYPE</option>
														<option>TIPS</option>
													</select>
													<label id="Paygate_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-2">
												<div class="form-group">		
													<select id="Channel" class="form-control">
														<option value="Select">CHANNEL</option>
														<option value="USSD">USSD</option>
														<option value="IB">IB</option>
														<option value="MB">MB</option>
														<option value="TIPS">TIPS</option>
													</select>
													<label id="Channel_error" class="text-danger"></label>
												</div>
											</div>	
											
											<div class="col-md-3">
												<div class="form-group">		
													<input type="text" class="form-control" id="Txn_Id" placeholder="Txn Id">
													<label id="Txn_Id_error" class="text-danger"></label>
												</div>
											</div>		
											
											<div class="col-md-2 mt-2">
												<button id="get_details" class="btn btn-secondary">Get Details</button>	
											</div>
																	
										</div>
										
										<div class="row">
											<div class="col-md-12"><hr></div>
										</div>
	
										<div class="row">	
					                		<div class="table-responsive sm-table">
				              
												<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										
													<thead>
													
														<tr role="row">
															<th>S.No</th>
															<th>Amount Type</th>
															<th>Txn Date</th>														
															<th>Txn Id</th>
															<th>Amount</th>
															<th>Cur</th>
															<th>Source A/C</th>
															<th>Dest A/C</th>
															<th>Status</th>
															<th>Hold</th>	
															<th>Reversal</th>	
															<th>Channel</th>
															<th>Narration</th>
															<th>Req-Ref No</th>
															<th>Payer Id</th>
															<th>Payee Id</th>
														</tr>
														
													</thead>
													
													 <tbody>																																						
													</tbody>
													
												</table>
											</div>
																	
										</div>
										
										<div class="row">
											<div class="col-md-12"><hr></div>
										</div>
										
										<div class="row">
											
											<div class="col-md-3">
												<div class="form-group">	
													<label for="">Select Amounts you want to Hold / Reverse</label>
												</div>
											</div>
											<div class="col-md-5">
												<select id="payments" class="multidrop" multiple>
												</select>
												<label id="payments_error" class="text-danger"></label>
											</div>
											<div class="col-md-3">
												<input type="text" class="form-control" placeholder="Hold / Reversal Reason" id="Rev_reason">
												<label id="Rev_reason_error" class="text-danger"></label>
												<input type="hidden" id="REQSL" value="">
												<input type="hidden" id="FLOW" value="">
											</div>
										</div>
										
									<!--  	<div class="row">
											<div class="col-md-1"></div>
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Approval For hold</label>
												</div>
											</div>
											<div class="col-md-1">
												<div class="form-check" align="center">
													<label class="switch small">
													  <input id="status" type="checkbox">
													  <span class="slider round small"></span>
													</label>
												</div>
											</div>
										</div> -->
										
										<div class="row">
											<div class="col-md-12"><hr></div>
										</div>
											
										<div class="row hold_rev mt-2">
											<div class="col-md-1"></div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Approval For Hold</label>
												</div>
											</div>
											
											<div class="col-md-3">							
												  <select id="hold" class="form-control">
												  	<option value="Pending">Pending</option>
												  	<option value="Approved">Approve</option>
												  	<option value="Rejected">Reject</option>
												  </select>											 
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Approval For Reversal</label>
												</div>
											</div>
											
											<div class="col-md-3">							
												  <select id="rev" class="form-control">
												  	 <option value="Pending">Pending</option>
												  	 <option value="Approved">Approve</option>
												  	 <option value="Rejected">Reject</option>
												  </select>											 
											</div>
											
											<div class="col-md-1"></div>
											<div class="col-md-1"></div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Hold Reject Reason</label>
												</div>
											</div>
											
											<div class="col-md-3">							
												 <textarea id="hold_Rej_reason" class="form-control" rows="2"></textarea>										 
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Reversal Reject Reason</label>
												</div>
											</div>
											
											<div class="col-md-3">							
												  <textarea id="rev_rej_reason" class="form-control" rows="2"></textarea>										 
											</div>
											
										</div>
										
										<div class="row mt-2">
											<div class="col-md-1"></div>											
										</div>
										
										<div id="custom_card_action" class="card-action">
											<div class="row">	
												<div class="col-md-12" align="center">
												<!-- 	<button id="Rev_action" class="btn btn-secondary">Initiate</button>	  -->
													<button id="Rev_action2" class="btn btn-secondary">Hold Submit</button>	
													<button id="Rev_action3" class="btn btn-secondary">Rev Submit</button>	
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
	 </div>
	 </div>
	 </div>	
	
	</body>
	
</html>