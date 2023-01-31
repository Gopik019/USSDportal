<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Reports/CSS_&_JS.jsp" %>   
 
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
			
	        <div class="row fluid-container">
	        
				<div class="col-md-12 mt--1">
	
					<div id="colour_body" class="card">
	
						<div id="tab_card" class="card-body">
							
				              <div class="table-responsive sm-table">
				              
								<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
						
									<thead>
										<tr role="row">
											<th>S.No</th>
											<th>CHN</th>
											<th>PAYMENT SYSTEM</th>
											<th>DEBIT/CREDIT</th>
											<th>TRN DATE</th>
											<th>REF ID</th>
											<th>SRC A/C</th>
											<th>DST A/C</th>
											<th>TYPE</th>
											<th>TRN AMT</th>
											<th>TRN CUR</th>
											<th>BILL NO</th>
											<th>Flow</th>
											<th>HOLD STATUS</th>
											<th>Reversal STATUS</th>
										</tr>
									</thead>
									
									 <tbody>																																						
									</tbody>
								</table>
							</div>
									</div></div>
								</div>
							</div>
						</div>	
					</div>
				</div>	
			</div>   
			
			<div class="modal fade" id="Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">Hold Reason</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
		        	<div class="row">	
						<div class="col-md-12">
							<div class="form-group">		
								<input type="hidden" id="CHCODE"> 
								<input type="hidden" id="PAYTYPE"> 
								<input type="hidden" id="REQSL"> 
								<textarea class="form-control" id="holdReason" rows="5" cols="40" placeholder="Type here"></textarea>
								<label id="holdReason_error" class="text-danger"></label>
							</div>
						</div>	
					</div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" id="hold_submit" class="btn btn-primary">Submit</button>
			      </div>
			    </div>
			  </div>
			</div>
	</body>
</html>