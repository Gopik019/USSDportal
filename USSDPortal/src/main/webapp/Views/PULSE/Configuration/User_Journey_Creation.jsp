<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS3.jsp" %>   
 
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
												<label for="email2">User Journey Type</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<select id="user_jouery_type" class="form-control">
													<option>Select</option>
													<option value="TRANMON">Transactions</option>
													<option value="APPMON">Application</option>
													<option value="DBMON">Database</option>
													<option value="FILEMON">File monitoring</option>
													<option value="WEBMON">Web service</option>
												</select>
												<label id="user_jouery_type_error" class="text-danger"></label>
											</div>
										</div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">User Journey Code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control" id="user_jouery_code" placeholder="" maxlength="10" size="15">
												<label id="user_jouery_code_error" class="text-danger"></label>
											</div>
										</div>
									</div>
										
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">User Journey Name</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">										 
												<input type="text" class="form-control" id="user_jouery_name" placeholder="" maxlength="20" size="15">
												<label id="user_jouery_name_error" class="text-danger"></label>
											</div>
										</div>
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Frequency in Seconds</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control" id="freq_in_sec" placeholder="" maxlength="20" size="15">
												<label id="freq_in_sec_error" class="text-danger"></label>
											</div>
										</div>
									</div>
									
									<div class="row">	
										<div class="col-md-12">		
											<div class="form-group">						
												<h5 class="text-secondary">Sequence of Executions</h5>
											</div>
										</div>
										<div class="col-md-11 mt--2 scroll">
											<table id="example" class="table table-hover table-bordered table-head-bg-secondary table-bordered-bd-secondary">
											<thead>
												<tr>
													<th class="w-10"><div align="center">S.No</div></th>
													<th class="w-30"><div align="center">Event Code</div></th>
													<th class="w-50"><div align="center">Event Name</div></th>
													<th class="w-10"><div align="center">Action</div></th>
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
												
									<div id="custom_card_action" class="card-action">
										<div class="row">	
											<div class="col-md-12" align="center">
												<button id="event_creation" class="btn btn-secondary">Submit</button>	
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