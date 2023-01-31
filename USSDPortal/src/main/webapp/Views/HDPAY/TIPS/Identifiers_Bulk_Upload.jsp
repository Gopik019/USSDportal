<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/TIPS/CSS_&_JS3.jsp" %>    
  
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
											<th>Account No</th>
											<th>Account Type</th>
											<th>Full Name</th>
											<th>Currency</th>
											<th>Mobile No</th>
											<th>Customer No</th>
											<th>Email</th>
											<th>NIDA</th>
											<th>Valid</th>	
											<th>Failure Reason</th>	
											<th>Approval</th>		
										</tr>
										
									</thead>
									
									 <tbody>																																						
									</tbody>
									
								</table>
							</div>

						<div id="custom_card_action" class="card-action mt-3 no_show">
							<div class="row">	
								<div class="col-md-12" align="center">
									<button id="identifier_action" class="btn btn-secondary">Submit</button>	
								</div>
							</div>	
						</div>
						
					</div>
										
				</div>	
			</div>   
		</div>	
			
		<!-- ############### Model 1 for Image ################ -->
			 
		<div class="modal fade" id="modal1" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
	  		<div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header text-center">
			         <div class="avatar-sm">
			         	<img id="attach" src='<spring:url value="/resources/HDPAY/img/attach.png" />' class="avatar-img rounded-circle" alt="your image" title="">
			         </div>
			         <div class="float-left"><h4 class="text">Attachment</h4></div>	
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      
			      <div class="modal-body">	
			      	
			      		<div class="row">					                		
							<div class="col-md-2 ml-auto">
								<div class="form-group">	
									<label for="">Payment Gateway</label>
								</div>
							</div>
							
							<div class="col-md-3 mr-auto">
								<div class="form-group">		
									<select id="Paygate" class="form-control">
										<option>Select</option>
										<option>TIPS</option>
									</select>
									<label id="Paygate_error" class="text-danger"></label>
								</div>
							</div>													
						</div>
						
						<div class="row">
							<div class="col-md-12"><hr></div>
						</div>
										
						<div id="file_uploader" class="row">	
							<div class="col-md-10 col-sm-10 mr-auto ml-auto">
								<div class="file-upload">
									<input type="file" id="inputGroupFile012" class="imgInp custom-file-input" accept=".xlsx" data-original-title="" title=""> 
									<label id="File_name" class="custom-file-label" for="inputGroupFile01">Choose file</label> 
								</div>	 	
							</div>  
						</div>
					
						<div class="row">
							<input type="hidden" class="form-control" id="unique_Id" placeholder="">
							<div id="selected" class="col-md-10 col-sm-10 mr-auto ml-auto"></div> 	 
						 </div>			
				</div>		 		  	
			      	 	
			     <div class="modal-footer">
			       
					<div class="col-md-12" align="center">	
						<button id="identifier_Update" class="btn btn-secondary">Submit</button>	
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