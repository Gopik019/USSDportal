<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Report_Setup/CSS_&_JS.jsp" %>   

<%
	String path = request.getContextPath();
	
	String sesRole = session.getAttribute("sesRole") !=null ? (String)session.getAttribute("sesRole") : "";
	
	String sesUserId = session.getAttribute("sesUserId") !=null ? (String)session.getAttribute("sesUserId") : "";
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/Datavision/";
	
	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

<style>
	.text-danger{
		display:none;
		margin-bottom: 0px !important;
	}
</style>

<body>
	<div class="wrapper">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/FIU/common/Navigation_Bar.jsp" %>     
		</div>
	
	<%@ include file="../../../Headers_&_Footers/FIU/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
					
						<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %>   
						
					<div class="row">
						<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
								
								<div class="card-body">
								
									<div class="row">	
									
										<div class="col-md-6">
											<div class="row">	
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">User Id</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
														<% if(sesRole.equals("ADMIN")) 
														{ %>
															<input type="text" class="form-control" id="User_Id" maxlength="25" value="<%= sesUserId %>" placeholder="">
													<%	}
														else
														{ %>
															<input type="text" class="form-control white" id="User_Id" maxlength="25" value="<%= sesUserId %>" placeholder="" readonly>
													<%	} %>	

														<label id="User_Id_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Title</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
														
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<select id="Title" class="form-control">
																	<option value="">Select</option>
																	<option value="MR">MR</option>
																	<option value="MS">MS</option>	
																	<option value="MRS">MRS</option>	
																</select>
													<%	}
														else
														{ %>
															<input type="text" class="form-control white" id="Title" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="Title_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">First Name</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="First_Name" placeholder="">
													<%	}
														else
														{ %>
															<input type="text" class="form-control white" id="First_Name" placeholder="" readonly>
													<%	} %>	
													
													
														
														<label id="First_Name_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Middle Name</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Middle_Name" placeholder="">
													<%	}
														else
														{ %>
															<input type="text" class="form-control white" id="Middle_Name" placeholder="" readonly>
													<%	} %>	
													
													
														
														<label id="Middle_Name_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Last Name</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Last_Name" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Last_Name" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="Last_Name_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-sm-4">
													<div class="form-group">		
													<label for="email2">Date of birth</label>
													</div>
												</div>

												<div class="col-md-7">
													<div class="form-group">		
													
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="date" class="form-control" id="Date_of_birth" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Date_of_birth" placeholder="" readonly>
													<%	} %>	
													
														<label id="Date_of_birth_error" class="text-danger"></label>
													</div>
												</div>	
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Gender</label>
													</div>
												</div>
												
												<div class="col-md-7">
													<div class="form-group">
													
													 <% if(sesRole.equals("ADMIN")) 
														{ %>
																<select id="Gender" class="form-control">
																	<option value="">Select</option>
																	<option value="M">Male</option>
																	<option value="F">Female</option>	
																</select>
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Gender" placeholder="" readonly>
													<%	} %>	
															
														<label id="Gender_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-sm-4">
													<div class="form-group">		
													<label for="email2">Birth Place</label>
													</div>
												</div>
												
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Birth_Place" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Birth_Place" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="Birth_Place_error" class="text-danger"></label>
													</div>
												</div>	
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Nationality</label>
													</div>
												</div>
												
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Nationality" maxlength="10" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Nationality" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="Nationality_error" class="text-danger"></label>
													</div>
												</div>
												
											</div>
										</div>
																			
										<div class="col-md-6">
											<div class="row">	
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">National Id</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="National_Id" placeholder="SSN">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="National_Id" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="National_Id_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Id Number</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Id_No" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Id_No" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="Id_No_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Phone No</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Phone_No" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Phone_No" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Phone_No_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Email</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Email" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Email" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Email_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Occupation</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Occupation" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Occupation" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Occupation_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Address</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<textarea rows="1" id="Address" class="form-control"></textarea>
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Address" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Address_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">City</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">		
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="City" maxlength="30" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="City" placeholder="" readonly>
													<%	} %>	
													
														
														<label id="City_error" class="text-danger"></label>
													</div>
												</div>
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Country</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Country_Code" maxlength="10" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Country_Code" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Country_Code_error" class="text-danger"></label>
													</div>
												</div>
												
												<div class="col-md-4">
													<div class="form-group">	
														<label for="email2">Residence</label>
													</div>
												</div>
												<div class="col-md-7">
													<div class="form-group">	
													
													<% if(sesRole.equals("ADMIN")) 
														{ %>
																<input type="text" class="form-control" id="Residence" maxlength="30" placeholder="">
													<%	}
														else
														{ %>
																<input type="text" class="form-control white" id="Residence" placeholder="" readonly>
													<%	} %>	
														
														
														<label id="Residence_error" class="text-danger"></label>
													</div>
												</div>
												
											</div>
										</div>
									</div>								
								</div>
								
								 <% if(sesRole.equals("ADMIN")) 
									{ %>
											<div class="card-action">
												<div class="row">					
													<div class="col-md-6">
														<button class="btn btn-indigo float-right" id="submit">Submit</button>
													</div>
													<div class="col-md-6">	
														<button id="Reset" class="btn btn-danger float-left">Reset</button>
													</div>
												</div>	
											</div>
								<%	} %>	
													
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
<script>
	
	var jspath="<%=path%>" ;
	var serbasePath="<%=basePath%>" ;
	var session_role; 
	
	$(document).ready(function() {
		
		 Get_Reporting_Person_Details("sesUserId");
		
		 $("#User_Id").autocomplete({
			    source:  serbasePath + "/Suggestions/User_Id",
			    minLength: 1,			  
			    select: function(event, ui) 
			    {
			    	Get_Reporting_Person_Details(ui.item.label);
			    }
		  }).autocomplete( "instance" )._renderItem = function(ul,item) 
		  	{
			 	 if(session_role == "ADMIN")
			 	 {
		  		 	return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
		  		 }
			};
		 
			$("#User_Id").blur(function() {
				validate_User_Id();
			});
			
			$("#Gender").blur(function() {
				if(session_role == 'ADMIN') { validate_Gender(); }
			});
			
			$("#Title").blur(function() {
				if(session_role == 'ADMIN') { validate_Title(); }
			});
			
			$("#First_Name").blur(function() {
				if(session_role == 'ADMIN') { validate_First_Name(); }
			});
			
		/*	$("#Middle_Name").blur(function() {
				validate_Middle_Name();
			});
			
			$("#Last_Name").blur(function() {
				validate_Last_Name();
			}); */
			
			$("#Date_of_birth").keyup(function() {
				if(session_role == 'ADMIN') { validate_Date_of_birth(); }
			});
			
			$("#Birth_Place").blur(function() {
				if(session_role == 'ADMIN') { validate_Birth_Place(); }
			});
			
			$("#National_Id").blur(function() {
				if(session_role == 'ADMIN') { validate_National_Id(); }
			});
			
			$("#Id_No").blur(function() {
				if(session_role == 'ADMIN') { validate_Id_No(); }
			});
			
			$("#Nationality").blur(function() {
				if(session_role == 'ADMIN') { validate_Nationality(); }
			});
			
			$("#Phone_No").blur(function() {
				if(session_role == 'ADMIN') { validate_Phone_No(); }
			});
			
			$("#Email").blur(function() {
				if(session_role == 'ADMIN') { validate_Email(); }
			});
			
			$("#Occupation").blur(function() {
				if(session_role == 'ADMIN') { validate_Occupation(); }
			});
			
			$("#Address").blur(function() {
				if(session_role == 'ADMIN') { validate_Address(); }
			});
			
			$("#City").blur(function() {
				if(session_role == 'ADMIN') { validate_City(); }
			});
			
			$("#Country_Code").blur(function() {
				if(session_role == 'ADMIN') { validate_Country_Code(); }
			});
			
			$("#Residence").blur(function() {
				if(session_role == 'ADMIN') { validate_Residence(); }
			});
			
			$("#submit").click(function() {
				 revalidate(); 
			});
			
			$("#Reset").click(function() {
				Reset();
			});	
	});		
	
	function validate_User_Id()
	{
		if($("#User_Id").val() == "")
		{
			$("#User_Id_error").html('Required');
			$("#User_Id_error").show();
			return false;
		}
		else
		{
			$("#User_Id_error").hide();
			return true;
		}	
	}
	
	function validate_Gender()
	{
		if($("#Gender").val() == "")
		{
			$("#Gender_error").html('Required');
			$("#Gender_error").show();
			return false;
		}
		else
		{
			$("#Gender_error").hide();
			return true;
		}	
	}
	
	function validate_Title()
	{
		if($("#Title").val() == "")
		{
			$("#Title_error").html('Required');
			$("#Title_error").show();
			return false;
		}
		else
		{
			$("#Title_error").hide();
			return true;
		}	
	}
	
	function validate_First_Name()
	{
		if($("#First_Name").val() == "")
		{
			$("#First_Name_error").html('Required');
			$("#First_Name_error").show();
			return false;
		}
		else
		{
			$("#First_Name_error").hide();
			return true;
		}	
	}
	
	function validate_Date_of_birth()
	{
		if($("#Date_of_birth").val() == "")
		{
			$("#Date_of_birth_error").html('Required');
			$("#Date_of_birth_error").show();
			return false;
		}
		else
		{
			$("#Date_of_birth_error").hide();
			return true;
		}	
	}
	
	function validate_Birth_Place()
	{
		if($("#Birth_Place").val() == "")
		{
			$("#Birth_Place_error").html('Required');
			$("#Birth_Place_error").show();
			return false;
		}
		else
		{
			$("#Birth_Place_error").hide();
			return true;
		}	
	}
	
	function validate_National_Id()
	{
		if($("#National_Id").val() == "")
		{
			$("#National_Id_error").html('Required');
			$("#National_Id_error").show();
			return false;
		}
		else
		{
			$("#National_Id_error").hide();
			return true;
		}	
	}
	
	function validate_Id_No()
	{
		if($("#Id_No").val() == "")
		{
			$("#Id_No_error").html('Required');
			$("#Id_No_error").show();
			return false;
		}
		else
		{
			$("#Id_No_error").hide();
			return true;
		}	
	}
	
	function validate_Nationality()
	{
		if($("#Nationality").val() == "")
		{
			$("#Nationality_error").html('Required');
			$("#Nationality_error").show();
			return false;
		}
		else
		{
			$("#Nationality_error").hide();
			return true;
		}	
	}
	
	function validate_Phone_No()
	{
		if($("#Phone_No").val() == "")
		{
			$("#Phone_No_error").html('Required');
			$("#Phone_No_error").show();
			return false;
		}
		else
		{
			$("#Phone_No_error").hide();
			return true;
		}	
	}
	
	function validate_Email()
	{
		if($("#Email").val() == "")
		{
			$("#Email_error").html('Required');
			$("#Email_error").show();
			
			return false;
		}
		
		if(!validateEmail($("#Email").val()))
		{
			$("#Email_error").html('Invalid Email Address');
			$("#Email_error").show();
			
			return false;
		}
		
		$("#Email_error").hide();
		
		return true;
	}
	
	function validate_Occupation()
	{
		if($("#Occupation").val() == "")
		{
			$("#Occupation_error").html('Required');
			$("#Occupation_error").show();
			return false;
		}
		else
		{
			$("#Occupation_error").hide();
			return true;
		}		
	}
	
	function validate_Address()
	{
		if($("#Address").val() == "")
		{
			$("#Address_error").html('Required');
			$("#Address_error").show();
			return false;
		}
		else
		{
			$("#Address_error").hide();
			return true;
		}		
	}
	
	function validate_City()
	{
		if($("#City").val() == "")
		{
			$("#City_error").html('Required');
			$("#City_error").show();
			return false;
		}
		else
		{
			$("#City_error").hide();
			return true;
		}		
	}
	
	function validate_Country_Code()
	{
		if($("#Country_Code").val() == "")
		{
			$("#Country_Code_error").html('Required');
			$("#Country_Code_error").show();
			return false;
		}
		else
		{
			$("#Country_Code_error").hide();
			return true;
		}		
	}
	
	function validate_Residence()
	{
		if($("#Residence").val() == "")
		{
			$("#Residence_error").html('Required');
			$("#Residence_error").show();
			return false;
		}
		else
		{
			$("#Residence_error").hide();
			return true;
		}	
	}
	
	function revalidate()
	{	     
       	var errorCount = 0;
       	
       	if(!validate_User_Id())      { errorCount++; }
    	if(!validate_Gender())     { errorCount++; }
    	if(!validate_Title())    { errorCount++; }
    	if(!validate_First_Name())  { errorCount++; }
    	if(!validate_Date_of_birth())     { errorCount++; }
    	if(!validate_Birth_Place())      { errorCount++; }  	
    	if(!validate_National_Id())        { errorCount++; }
    	if(!validate_Id_No())    { errorCount++; }
    	if(!validate_Nationality()) { errorCount++; }
    	if(!validate_Phone_No())     { errorCount++; }
    	if(!validate_Email())    { errorCount++; }
    	if(!validate_Occupation())    { errorCount++; }
    	if(!validate_Address())    { errorCount++; }
    	if(!validate_City())    { errorCount++; }
    	if(!validate_Country_Code())    { errorCount++; }
    	if(!validate_Residence())    { errorCount++; }
    	
  		if(errorCount == 0)
  		{	
  			proceed();
       	}
  		else
  		{
       		return false;
 		}
	}
	
	function proceed()
	{
	    var data = new FormData();
		
		data.append('User_Id' , $("#User_Id").val());
		data.append('Gender' , $("#Gender").val());
		data.append('Title' , $("#Title").val());
		data.append('First_Name' , $("#First_Name").val());
		data.append('Middle_Name' , $("#Middle_Name").val());
		data.append('Last_Name' , $("#Last_Name").val());
		data.append('Date_of_birth' , date_format($("#Date_of_birth").val()));
		data.append('Birth_Place' , $("#Birth_Place").val());
		data.append('National_Id' , $("#National_Id").val());
		data.append('Id_No' , $("#Id_No").val());
		data.append('Nationality' , $("#Nationality").val());
		data.append('Phone_No' , $("#Phone_No").val());		
		data.append('Email', $("#Email").val());
		data.append('Occupation', $("#Occupation").val());
		data.append('Address', $("#Address").val());
		data.append('City', $("#City").val());
		data.append('Country_Code', $("#Country_Code").val());
		data.append('Residence' , $("#Residence").val());
		
	    $.ajax({		 
			url  :  serbasePath + "/Report_Setup/FIU_Reporting_Person/Update",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					Sweetalert("success_load_Current", "", data.Message);
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
	
	function Get_Reporting_Person_Details(User_Id)
	{
		 var data = new FormData();
			
		 data.append('User_Id' , User_Id);
		 
		 $.ajax({		 
				url  :  serbasePath + "/Report_Setup/FIU_Reporting_Person/Details",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{ 
					Swal.close();
					
					if(data.Result == 'Success')
					{
						$("#User_Id").val(data.User_Id);
						$("#Title").val(data.Title);
						$("#Gender").val(data.Gender);
						$("#First_Name").val(data.First_Name);
						$("#Middle_Name").val(data.Middle_Name);
						$("#Last_Name").val(data.Last_Name);
						$("#Date_of_birth").val(formatDate2(data.Date_of_birth));
						$("#Birth_Place").val(data.Birth_Place);
						$("#National_Id").val(data.National_Id);
						$("#Id_No").val(data.Id_No);
						$("#Nationality").val(data.Nationality);
						$("#Phone_No").val(data.Phone_No);
						$("#Email").val(data.Email);
						$("#Occupation").val(data.Occupation);
						$("#Address").val(data.Address);
						$("#City").val(data.City);
						$("#Country_Code").val(data.Country_Code);
						$("#Residence").val(data.Residence);
						
						 session_role = data.sesRole;
					}	
					else
					{
						Reset(false);
					}
				},
				beforeSend: function( xhr )
		 		{
		 			Sweetalert("load", "", "Please Wait");
		        },
			    error: function (jqXHR, textStatus, errorThrown) { }
		  });
	}
	
	function Reset(User_Id)
	{
		if(User_Id != false)
		{
			$("#User_Id").val('');
		}
		
		$("#Title").val('');
		$("#Gender").val('');
		$("#First_Name").val('');
		$("#Middle_Name").val('');
		$("#Last_Name").val('');
		$("#Date_of_birth").val('');
		$("#Birth_Place").val('');
		$("#National_Id").val('');
		$("#Id_No").val('');
		$("#Nationality").val('');
		$("#Phone_No").val('');
		$("#Email").val('');
		$("#Occupation").val('');
		$("#Address").val('');
		$("#City").val('');
		$("#Country_Code").val('');
		$("#Residence").val('');
	}
	
	//function freeze()
	//{
		//$("#Title").prop('disabled', true);
		//$("#Title, #First_Name").css("background-color", "white");
		
		//$("#First_Name").prop("readonly", true);
		//$("#First_Name").addClass("white");
	//}
	
	function date_format(val)
	{
        var dt = new Date(val); 

        var dd = dt.getDate(); 
        var mm = dt.getMonth() + 1; 
  
        var yyyy = dt.getFullYear(); 
        if (dd < 10) { 
            dd = '0' + dd; 
        } 
        if (mm < 10) { 
            mm = '0' + mm; 
        } 
        
        var condate = dd + '-' + mm + '-' + yyyy; 
        
        return condate;
  
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
	
	function validateEmail(email)
	{
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    
	    return re.test(String(email).toLowerCase());
	}
	
	
</script>
	
 </body>
</html>