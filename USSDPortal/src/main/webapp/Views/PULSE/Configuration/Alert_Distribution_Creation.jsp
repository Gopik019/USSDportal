<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS5.jsp" %>   
 

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
												
									<div class="card-action">
										<div class="row">	
											<div class="col-md-12" align="center">
												<button id="distribution_creation" class="btn btn-secondary">Submit</button>	
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