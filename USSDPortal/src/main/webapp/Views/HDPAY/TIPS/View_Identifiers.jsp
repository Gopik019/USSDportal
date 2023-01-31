<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/TIPS/CSS_&_JS.jsp" %>    
  
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
								
								<div class="table-responsive sm-table">
				              
								<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
						
									<thead>
									
										<tr role="row">
											<th>S.No</th>
											<th>Identifier</th>
											<th>Full Name</th>
											<th>Account No</th>
											<th>Mobile No</th>
											<th>Customer No</th>
											<th>Status</th>
											<th>Action</th>	
										</tr>
										
									</thead>
									
									 <tbody>																																						
									</tbody>
									
								</table>
							</div>
							
						</div>
					</div>	
				</div>   
			</div>	
			
	<!-- ############### Model 1 for Image ################ -->
      	
      	<div class="modal" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header text-center">
		        <h5 class="modal-title w-100" id="exampleModalLongTitle">Identifier details</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      
		      <div class="modal-body">	
		      	
		      	<div class="row">
		      		<div class="col-sm-12 col-md-12" align="center">
		      		
		      			<div class="row">
							<div class="col-sm-12" align="center">
								<img id="cand_qr" src="<spring:url value="/resources/HDPAY/img/qr_sample.png" />" style="width:120px;height:120px;">  
							</div>
		      			</div>
		      			
		      			<div class="col-sm-12"><hr></div>
	
	      				<div class="row mb-2" align="center">
	      					<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Name</h6></div>
					   	 	<div class="col-md-6"><h6>SILAS DIAZ MATOI</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Identifier</h6></div>
					   	 	<div class="col-md-6"><h6>0011052802</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Account No</h6></div>
					   	 	<div class="col-md-6"><h6>0011052802</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Account Type</h6></div>
					   	 	<div class="col-md-6"><h6>Personal</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Mobile No</h6></div>
						    <div class="col-md-6"><h6>0767211637</h6></div>
						</div>		
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Customer No</h6></div>
						    <div class="col-md-6"><h6>90189728</h6></div>
						</div>	
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Email Id</h6></div>
						    <div class="col-md-6"><h6>sdmatoi@gmail.com</h6></div>
						</div>	
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>NIDA ID</h6></div>
						    <div class="col-md-6"><h6>23334366423242</h6></div>
						</div>	
						
						<div class="row">
							<div class="col-md-2"></div>
						    <div class="col-md-4 mb-1"><h6>Status</h6></div>
						    <div class="col-md-2">
								<label class="switch small">
								  <input id="status" type="checkbox" class="text-success">
								  <span class="slider round small"></span>
								</label>
						    </div>
						</div>	
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Registered at</h6></div>
						    <div class="col-md-6"><h6>08-09-2021</h6></div>
						</div>										

		      		</div>
		      	</div>		     	
		     </div>
		      
		     <div class="modal-footer">
		        
		     </div>
		    </div>
		  </div>
		</div>
		
		<!-- ############### Model2 for Status Update ################ -->
      	
      	<div class="modal" id="modal2" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
  			<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header text-center">
		        <h5 class="modal-title w-100" id="exampleModalLongTitle">Identifier details</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      
		      <div class="modal-body">	
		      	
		      	<div class="row">
		      		<div class="col-sm-12 col-md-12" align="center">
		      		
	      				<div class="row mb-2" align="center">
	      					<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Name</h6></div>
					   	 	<div class="col-md-6"><h6>SILAS DIAZ MATOI</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Identifier</h6></div>
					   	 	<div class="col-md-6"><h6>0011052802</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Account No</h6></div>
					   	 	<div class="col-md-6"><h6>0011052802</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
					    	<div class="col-md-4"><h6>Account Type</h6></div>
					   	 	<div class="col-md-6"><h6>Personal</h6> </div>
						</div>
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Mobile No</h6></div>
						    <div class="col-md-6"><h6>0767211637</h6></div>
						</div>		
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Customer No</h6></div>
						    <div class="col-md-6"><h6>90189728</h6></div>
						</div>	
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>Email Id</h6></div>
						    <div class="col-md-6"><h6>sdmatoi@gmail.com</h6></div>
						</div>	
						
						<div class="row mb-2">
							<div class="col-md-2"></div>
						    <div class="col-md-4"><h6>NIDA ID</h6></div>
						    <div class="col-md-6"><h6>23334366423242</h6></div>
						</div>	
						
						<div class="row">
							<div class="col-md-2"></div>
						    <div class="col-md-4 mb-1"><h6>Status</h6></div>
						    <div class="col-md-2">
								<label class="switch small">
								  <input id="status" type="checkbox">
								  <span class="slider round small"></span>
								</label>
						    </div>
						</div>										

		      		</div>
		      	</div>		     	
		     </div>
		      
		     <div class="modal-footer">
		        
		        <div class="col-md-12" align="center">
					<button id="assignment_update" class="btn btn-secondary">Update</button>		
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