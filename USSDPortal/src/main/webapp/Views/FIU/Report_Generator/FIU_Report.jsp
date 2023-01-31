<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Report_Generator/CSS_&_JS.jsp" %>   


<style>
	.text-danger
	{
		display:none;
		margin-bottom: 0px !important;
	}
	
	table.dataTable thead, tfoot {
		background-color: #6c757d;
		color:white;
	}
	
	.btn.btn-secondary {
		color: #fff !important;
	}
</style>

<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %>   
	
	<%@ include file="../Report_Generator/Report_Popup.jsp" %>    
	
	<div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
	  <div class="modal-dialog modal-dialog-centered" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        	<div class="col-md-12" align="center">
	        		<h5 class="modal-title" id="exampleModalLongTitle">FIU Report is Ready !!</h5>
	        	</div>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
	      </div>
	      <div class="modal-body">
	      	<div class="row" align="center">
	      		<div class="col-md-12">
	      			<a id="download" class="btn btn-download" href="#" role="button">
						<span class="btn-label"><i class="fas fa-file-download"></i></span>
						Download
					</a>
				</div>
	      	</div>
	      </div>
	    </div>
	  </div>
	</div>

    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
					
				<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   
						
					<div class="row">
						<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
							
								<div class="card-body">
								
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Report Code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<select id="i_paytype" class="form-control">
													<option>Select</option>
													<option value="CTRs">Currency (Cash) Transactions</option>
													<option value="IFT">International fund Transfer Transactions</option>				
													<option value="EFTs">Electronic Fund Transfer Transaction</option>					
												</select>
												<p id="i_paytype_msg" class="text-danger"></p>
											</div>
										</div>

										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Submission Code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<select id="i_sub_code" class="form-control">
													<option>Select</option>
													<option>Manual</option>
													<option>Electronic</option>	
												</select>
												<p id="i_subcode_msg" class="text-danger"></p>
											</div>
										</div>
									</div>
									
									
									
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">From Date</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="date" class="form-control" id="i_fromdate" placeholder="">
												<p id="i_fromdate_msg" class="text-danger"></p>
											</div>
										</div>										
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">To Date</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="date" class="form-control" id="i_todate" placeholder="">
												<p id="i_todate_msg" class="text-danger"></p>
											</div>
										</div>

									</div>
									
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2"> Amount limit</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="number" class="form-control" id="i_famount" placeholder="Equivalent or above (In USD)"> 
												<p id="i_famount_msg" class="text-danger"></p>
											</div>
										</div>										
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Debit / Credit</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<select id="i_dc" class="form-control">
													<option>Select</option>
													<option value="D">Debit</option>
													<option value="C">Credit</option>	
													<option value="B">Both</option>								
												</select>
												<p id="i_dc_msg" class="text-danger"></p>
											</div>
										</div>
	
									</div>
									
									<div class="row">	
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Reason</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<textarea rows="2" class="form-control" id="i_reason" placeholder=""></textarea>
												<p id="i_reason_msg" class="text-danger"></p>
											</div>
										</div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Action</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<textarea rows="2" class="form-control" id="i_action" placeholder=""></textarea>
												<p id="i_action_msg" class="text-danger"></p>
											</div>
										</div>
									</div>
									
								</div>
								
								<div class="card-action">
									<div class="row">										
										<div class="col-md-6">											
											<button id="view"class="btn btn-indigo float-right" data-toggle="modal" data-target="#Modal2">View Report</button>									
											<button id="generate" class="btn btn-indigo float-right mr-3">Generate</button>																								
										</div>
										
										<div class="col-md-6">	
											<button id="reset" class="btn btn-danger float-left">Reset</button>
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