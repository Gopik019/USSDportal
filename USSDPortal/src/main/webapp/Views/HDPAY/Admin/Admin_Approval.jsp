<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>
<script>
	
</script>


<%
	javax.servlet.http.HttpServletRequest _request = (javax.servlet.http.HttpServletRequest) request;

	String auth_error = (String) _request.getAttribute("AUTH_ERROR");
	
	_request.setAttribute("AUTH_ERROR", null);
	


	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS2.jsp" %>   

<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/scripts/xmlutil.js"/>' ></script>

<style>

table.dataTable thead, tfoot {
	background-color: #6c757d;
	color:white;
}

table.dataTable .sorting, .sorting_asc, .sorting_desc {
    background : none;
}

table.dataTable tbody tr.selected {
    color: green;
    background-color: #eeeeee;
    /*-webkit-box-shadow: 0 0 1em #5a5a5a;
    box-shadow: 0 0 1em #5a5a5a; */
}

.pagination  .paginate_button.page-item.active  a{
    background-color: cadetblue;
    color: white;
}

#Modal2 input {
    background-color: white !important;
    border: black 1px solid !important;
    color: black !important;
    opacity : .9 !important;
}

</style>

<body data-background-color="${Menu.get('body_color').getAsString()}">  
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
		     
		</div>
	
		<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   

	<div class="modal fade" id="Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">Rejection Remarks</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
        	<div class="row">	
				<div class="col-md-12">
					<div class="form-group">		
						<textarea class="form-control" id="txtReason" rows="5" cols="40" placeholder="Type here"></textarea>
					</div>
				</div>	
			</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-primary"onclick=" Rejectcol('Reject')">Submit</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" id="Modal2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog modal-xl" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	      	 <h5 class='col-12 modal-title text-center'>
	        		User Registration Details
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
	        </h5>
	      </div>
	      <div class="modal-body">
        		<div class="row">	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Organization Code</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="torgcd" placeholder="" maxlength="25" size="20" value="" readonly>
						</div>
					</div>
				
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">User ID</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="tuserid" placeholder="" maxlength="20" size="15" value="" readonly>
						</div>
					</div>
				</div>
			
				<div class="row">	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">User Name</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="tusernme" placeholder="" maxlength="20" size="15" value="" readonly>
						</div>
					</div>
					
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Date of Birth</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="date-picker form-control" id="tbirthdate" placeholder=""  maxlength="10" size="12" value="" readonly>
						</div>
					</div>	
				</div>
			
				<div class="row">	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Primary Mobile Number</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="tmobile" placeholder="" maxlength="10" size="20" value="" readonly>
						</div>
					</div>	
					
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Primary Email Id</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="email" class="form-control" id="temail" placeholder="" maxlength="100" size="30" value="" readonly>
						</div>
					</div>	
				</div>
				
				<div class="row">	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Password</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="password" class="form-control" id="tpwd" placeholder="" maxlength="20" size="15" value="" readonly>
						</div>
					</div>	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Re-Enter Password</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="password" class="form-control" id="tconfirmpwd" placeholder="" maxlength="20" size="15" value="" readonly>
						</div>
					</div>	
				</div>
				
				<div class="row">	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Role Code</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="trolecd" placeholder="" maxlength="20" size="15" value="" readonly>
						</div>
					</div>	
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Registration Date</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="tregdate" placeholder="" maxlength="10" size="12" value="" readonly>
						</div>
					</div>	
				</div>
				
				<div class="row">	
					
					<div class="col-md-2">
						<div class="form-group">	
							<label for="email2">Branch / Regional Code</label>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">		
							<input type="text" class="form-control" id="branchcd" placeholder="" maxlength="10" size="12" value="" readonly>
						</div>
					</div>	
				</div>
	      	</div>
	      <div class="modal-footer">
	       
	      </div>
	    </div>
	  </div>
	</div>
	  
    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
					
					<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %>   
					
					<div class="row">
							<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
							
								<div class="card-body">
								
									<div class="row">	
										<div class="col-md-5">
											<div class="row">	
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Program Name</label>
													</div>
												</div>
												<div class="col-md-8">																						
													<div class="form-group">		
														<select id="txtPgmId" class="form-control">
															<option>Select</option>
														</select>
													</div>
												</div>
											</div>
										</div>
										
										
										<div class="col-md-3">
											<div class="row">	
												<div class="col-md-6">
													<div class="form-group">	
														<label for="email2">Search Value</label>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">		
														<input type="text" class="form-control" id="searchkey" placeholder="" maxlength="20" size="15" value="">
													</div>
												</div>
											</div>
										</div>
										
										<div class="col-md-3">
											<div class="row">	
												<div class="col-md-6">
													<div class="form-group">	
														<label for="email2">Entered by</label>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">		
														<input type="text" class="form-control" id="enteredby" placeholder="" maxlength="20" size="15" value="">
													</div>
												</div>
											</div>
										</div>
										
										<div class="col-md-1">
											<div class="row">		
												<div class="col-md-12">
													<div class="form-group">		
														<button type="button" class="btn btn-icon btn-round btn-danger" onclick="Reset()"><i class="fas fa-redo"></i> </button>
													</div>
												</div>
											</div>
										</div>
										
									</div>
			
									<div class="row">	
										<div class="col-md-5">
											<div class="row">	
												<div class="col-md-4">
													<div class="form-group">
														<label>row per page</label>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">
														<select id="page_len" class="form-control float-left">
							                                <option>5</option>
							                                <option>10</option>
							                                <option>25</option>
							                                <option>50</option>
							                                <option>100</option>
						                            	</select>   
					                            	</div>
												</div>											
											</div>
										</div>
										
										<div class="col-md-3"></div>
										
									<div class="col-md-3">
										<div class="row">									
											<div class="col-md-6">
												<div class="form-group">
													<button class="btn btn-primary float-right" onclick="update('Auth')">Authorize</button>
												</div>
											</div>
										
											<div class="col-md-6">
												<div class="form-group ">
													<button class="btn btn-danger float-left" onclick="update('Reject')" >Reject</button>
												</div>
											</div>											
										</div>
									</div>
								</div>
										
									<div class="row">
                						<div class="col-md-12">
                						 	<div class="table-responsive"> 
											<table id="example" class="table table-bordered  dataTable" style="width: 100%;"  role="grid" aria-describedby="example_info">
							
												<thead>
													<tr role="row">
														<th>Select</th>
														<th>S.No</th>
														<th>Program Name</th>
														<th>Key Values</th>
														<th>User</th>
														<th>Date </th>
														<th>Tran /Entry SEQ </th>
														<th>Status</th>
														<th>Actions</th>
													</tr>
												</thead>
												
												<tbody>
													<tr class="odd">
														<td valign="top" colspan="9" class="dataTables_empty">No data available in table</td>
													</tr>
												</tbody>
												
											</table>
										</div>
									</div>
						          </div>
						          
								</div>
								
								<input type="hidden" name="CSRFTOKEN" value="${sessionScope['CSRFTOKEN']}">
								<input type="hidden" id="userIP" value="<%= session.getAttribute("sessionIP") %>" />
								<input type="hidden" id="userName" value="<%= session.getAttribute("sesUserName") %>" />
								
								<input type="hidden" id="domainId" value="<%= session.getAttribute("sesDomainID") %>" />
								<input type="hidden" id="userId" value="<%= session.getAttribute("sesUserId") %>" />
								<input type="hidden" id="calcurrbusDate" value="<%= session.getAttribute("calcurrbusDate") %>" />
								
								<input type="hidden" id="txtRejCode" />
										
								<input type="hidden" id="txtExtDomain" />
								
							<!--  	<div class="card-action">
									<div class="row">	
										<div class="col-md-4"></div>
										<div class="col-md-1">
											<button class="btn btn-success" onclick="revalidate()">Submit</button>
										</div>
										<div class="col-md-1">	
											<button class="btn btn-danger" onclick="">Cancel</button>
										</div>
									</div>	
								</div> -->
								
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	</body>
</html>