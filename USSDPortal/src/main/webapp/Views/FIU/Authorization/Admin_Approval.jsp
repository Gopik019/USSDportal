<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Authorization/CSS_&_JS.jsp" %>   

<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/scripts/xmlutil.js"/>' ></script>

<%
	javax.servlet.http.HttpServletRequest _request = (javax.servlet.http.HttpServletRequest) request;

	String auth_error = (String) _request.getAttribute("AUTH_ERROR");
	
	_request.setAttribute("AUTH_ERROR", null);
	
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/Datavision/";

	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

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

<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
		     
		</div>
	
		<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %>   

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
	        <button type="button" class="btn btn-primary" onclick="update('Reject')">Submit</button>
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
							<input type="date" class="date-picker form-control" id="tbirthdate" placeholder=""  maxlength="10" size="12" value="" readonly>
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
							<input type="date" class="form-control" id="tregdate" placeholder="" maxlength="10" size="12" value="" readonly>
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
					
					<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   
					
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
														<label>Rows per page</label>
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
													<button class="btn btn-indigo float-right" onclick="update('Auth')">Authorize</button>
												</div>
											</div>
										
											<div class="col-md-6">
												<div class="form-group ">
													<button class="btn btn-danger float-left"  data-toggle="modal" data-target="#Modal">Reject</button>
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
	
	<script>
	
	var table;
	
	var serbasePath="<%=basePath%>" ;
	
	$(document).ready(function() {

		LoadPgmId();

		$('#page_len').on('change', function () {
		    table.page.len( this.value ).draw();
		});
		
		$('#txtPgmId').on('change', function () {   
			
			 Load_Result(this.value);
			 
			 table.rows({ selected: true}).deselect();
		});
		
		$('#searchkey').on('keyup', function () {   
			
			 table.rows({ selected: true}).deselect();
			 
			 table.columns(3).search(this.value).draw();
		});
		
		$('#enteredby').on('keyup', function () {   
			
			 table.rows({ selected: true}).deselect();
			 
			 table.columns(4).search(this.value).draw();	 
		});
	});
	
	function LoadPgmId()
	{
		var domainid=document.getElementById('domainId').value;			
		var userid=document.getElementById('userId').value;
		
		var auguments=domainid+"|"+userid;
		
		var data = new FormData();
		
	    data.append('auguments' , auguments);
		
	    $.ajax({		 
			url  :  serbasePath + "/validateAdmPgmId",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					var category = data.CATEGORY;
					
					document.getElementById("txtExtDomain").value=domainid;
					document.getElementById("txtExtDomain").readOnly=true;
					
					var rtnVal = data.PGMID;
					
			     	var xmlObj = loadXML(rtnVal);
			      	var rowCnt = xmlObj.getRowCount(); 
					
					
			     	for(var i=0;i<rowCnt;i++)
					{
						var newOpt = document.createElement("OPTION");
			            newOpt.value = xmlObj.getValue(i,1);
			            var module = xmlObj.getValue(i,1);
			 			var pgmname = xmlObj.getValue(i,2);
			            newOpt.text = pgmname;
			
						//alert(newOpt);
			
			            try 
						{
			                 document.getElementById("txtPgmId").add(newOpt);
			            }
						catch(e)
						{ 
			                 document.getElementById("txtPgmId").appendChild(newOpt);
			            } 
				   }	
				}
				else
				{
					Sweetalert("warning", "", data.Message);
				}
			},
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}
	
	function Load_Result(val)
	{
		var data = new FormData();
		
		data.append("pgmid", val)
		
	    $.ajax({		 
			url  :  serbasePath + "/Info/Get_auth001_Info",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
				
					table = $('#example').DataTable( {
						 "sDom":"ltipr",
						  "aaData":  data.Info,
						  "aoColumns": [
						   // { "mData": "Checkbox", "bSortable": false },
						    { "mData": "Checkbox" },
						    { "mData": "S_NO" },
						    { "mData": "AUTHQ_PGM_ID"},
						    { "mData": "AUTHQ_MAIN_PK"},
						    { "mData": "AUTHQ_DONE_BY"},
						    { "mData": "AUTHQ_ENTRY_DATE", "render": function ( data, type, row ) { return formatDate(data); } },	    
						    { "mData": "AUTHQ_DISPLAY_DTLS"},
						    { "mData": "STATUS" },
						    { "mData": "View" }
						  ],	
						  //"order": [[1, 'asc']],
						  "paging":true,
						  "ordering":false,
						  "destroy": true,
						  "deferRender": true,
						  "searching": true,
				  		  "info": true,   
						  "pageLength":5,
						  "lengthChange": false,
						  "columnDefs": [ {
					            "orderable": false,
					            "className": 'select-checkbox',
					            "targets":   0
					        } ],
					        "select": {
					            "style":    'os',
					            "selector": 'td:first-child'
					        },
					        "order": [[ 1, 'asc' ]]
					});	
				}
				else
				{
					//Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			//Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}
	
	function show_details(pk)
	{
		var data = new FormData();
		
		data.append("pk", pk);
		
	    $.ajax({		 
			url  :  serbasePath + "/Info/pk/Get_auth003_Info",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					data = data.Info;
					
					$("#torgcd").val(data[0].element_1);
					$("#tuserid").val(data[0].element_2);
					$("#tusernme").val(data[0].element_3);
					$("#tbirthdate").val(formatDate2(data[0].element_4));
					$("#tmobile").val(data[0].element_5);
					$("#temail").val(data[0].element_6);
					$("#tpwd").val(data[0].element_7);
					$("#tconfirmpwd").val(data[0].element_8);
					$("#trolecd").val(data[0].element_9);
					$("#tregdate").val(formatDate2(data[0].element_10));
					//$("#tcomaddr").val('');
					$("#branchcd").val(data[0].element_17);
					
					$('#Modal2').modal('show');
				}
				else
				{
					Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function() 
			{
				$('#torgcd, #tuserid, #tusernme, #tbirthdate, #tmobile, #temail, #tpwd, #tconfirmpwd, #trolecd, #tregdate, #branchcd').val('');
				
				//$('#torgcd').css({'background-color':'#f6f6f6 !important'});
		    },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}
	
	function do_Auth_or_Reject(AuthRejectFlag, txtArgs)
	{
		var data = new FormData();
		
		data.append("txtArgs", txtArgs);
		
	    $.ajax({		 
			url  :  serbasePath + "/Admin_Approval",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				Swal.close();
				
				var txtPgmId = document.getElementById('txtPgmId').value;
				
				Load_Result(txtPgmId);
				
				if(data.Result == 'Success')
				{
					if(AuthRejectFlag=="Auth")
					{
						Sweetalert("success", "", "Record Authorized Successfully !!");
					}	
					else 
					{
						$("#Modal").modal('hide');
						
						Sweetalert("success", "", "Record Rejected Successfully !!");
					}
				}
				else
				{
					Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}
	
	function update(AuthRejectFlag)
	{			
		var flag = 0;  var index;
		  
	    table.rows({ selected: true} ).every(function(){
		 
		    index = this.index();
		  
		    flag = 1 ;
		});
		  
		if(flag == 1)
		{ 
			var domainid = document.getElementById('domainId').value;
			var txtPgmId = document.getElementById('txtPgmId').value;
			var userId = document.getElementById('userId').value;
			var calcurrbusDate = get_current_date(); //document.getElementById('calcurrbusDate').value;
			
			var txtRejCode = document.getElementById('txtRejCode').value;

			var txtReason = document.getElementById('txtReason').value;

			var pk =  table.cell( index, 3 ).data();
			var entdby = table.cell( index, 4 ).data();

			if(AuthRejectFlag == "Auth")
			{
				Swal.fire({
					  icon: 'warning',
					  title: '' ,
					  text:  'Do You Want To Authorize This Record ?' ,		 
					  showCancelButton: true,
					  confirmButtonColor: '#3085d6',
					  cancelButtonColor: '#d33',
					  confirmButtonText: 'Yes',	
					  closeOnConfirm: true
					}).then(function (res) {
		              if(res.value)
		              { 
		            	   document.getElementById('txtRejCode').value = " ";
							
							document.getElementById('txtReason').value = " ";
							
							txtRejCode = " ";
							
							txtReason = " ";
								
							if(document.getElementById('txtReason').value=="") { document.getElementById('txtReason').value=" "; }
								
							var txtArgs = txtPgmId +"$"+pk+"$"+ userId +"$"+calcurrbusDate +"$"+AuthRejectFlag+"$"+txtRejCode +"$"+txtReason +"$"+entdby+"$"+domainid;		
									
							do_Auth_or_Reject(AuthRejectFlag, txtArgs);
		              }
		              else if(res.dismiss == 'cancel')
		              { 
		                  return false;
		              }
		              else if(res.dismiss == 'esc')
		              { 
		              	 return false;
		              }
		          });
			}
			else
			{
		      	if(document.getElementById('txtReason').value=="")
		      	{
		      		Sweetalert("warning", "", "Please enter remarks");
		      		
		      		return false;
		      	}
		      	
		      	Swal.fire({
				  icon: 'warning',
				  title: '' ,
				  text:  'Do You Want To Reject This Record ?' ,		 
				  showCancelButton: true,
				  confirmButtonColor: '#3085d6',
				  cancelButtonColor: '#d33',
				  confirmButtonText: 'Yes',	
				  closeOnConfirm: true
				}).then(function (res) {
	              if(res.value)
	              { 
	            	  	txtRejCode = "Reject";
   						
			      		var txtArgs = txtPgmId +"$"+pk+"$"+ userId +"$"+calcurrbusDate +"$"+AuthRejectFlag+"$"+txtRejCode +"$"+txtReason +"$"+entdby+"$"+domainid;		
							
			      		do_Auth_or_Reject(AuthRejectFlag, txtArgs);			
	              }
	              else if(res.dismiss == 'cancel')
	              { 
	                  return false;
	              }
	              else if(res.dismiss == 'esc')
	              { 
	              	 return false;
	              }
	          });
			}			
		}
		else
		{	
			if(AuthRejectFlag=="Auth")
			{
				Sweetalert("warning", "", "Select atleast one row to Authorize");
			}	
			else 
			{
				Sweetalert("warning", "", "Select atleast one row to Reject");
			}
				
			return false;
		}
	}

	function Reset()
	{
		$("#txtPgmId").val('Select');
	}
	
	function formatDate(date) 
	{
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();

	    if (month.length < 2) 
	        month = '0' + month;
	    if (day.length < 2) 
	        day = '0' + day;
 
	    return [day, month, year].join('-');
	}
	
	function formatDate2(date) 
	{
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();

	    if (month.length < 2) 
	        month = '0' + month;
	    if (day.length < 2) 
	        day = '0' + day;
 
	    return [year, month, day].join('-');
	}
	
	function get_current_date()
	{
		var today = new Date();
		var dd = today.getDate();

		var mm = today.getMonth()+1; 
		var yyyy = today.getFullYear();
		
		if(dd<10) 
		{
		    dd='0'+dd;
		} 

		if(mm<10) 
		{
		    mm='0'+mm;
		} 
		
		today = dd+'-'+mm+'-'+yyyy; 
		
		return today;
	}
	
	</script>
	
	</body>
</html>