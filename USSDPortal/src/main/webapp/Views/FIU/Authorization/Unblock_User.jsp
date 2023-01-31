<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Authorization/CSS_&_JS2.jsp" %>   

<!--  
<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/Login/HTTPValidator.js" />' ></script>  
 
<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/Login/globalnew.js" />' ></script> 
 
<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/Login/globalconatants.js" />' ></script>  -->

<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/Login/sha256.js" />' ></script> 

<script type="text/javascript" src='<spring:url value="/resources/FIU/Validations/Login/global.js" />' ></script>    
	
<%@ page import="com.hdsoft.utils.PasswordUtils" %>

<%@ page import="com.hdsoft.utils.WebContext"%>

<%
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/Datavision/";
	
	String rsalt = PasswordUtils.getSalt();
	
	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

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
										
										<div class="col-md-2"></div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">User ID</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control" id="tuserid" placeholder="" maxlength="20" size="15">
												<label id="tuserid_error" class="text-danger"></label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="button" class="btn btn-secondary" id="chek_sts" value="Check Status">
											</div>
										</div>
									</div>
									
									<div class="row" id="status_row" style="display:none">	
										
										<div class="col-md-2"></div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Current Status</label>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="form-group">		
												<label id="blocked" class="text-danger">Blocked</label>
												<label id="un_blocked" class="text-success">Not Blocked</label>
											</div>
										</div>
									</div>
								
									<div class="row" id="action_row" style="display:none">	
										<div class="col-md-2"></div>
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Block / Unblock</label>
											</div>
										</div>
										
									<!--  	<div class="col-md-3">
											<div class="form-check">
												<label class="form-check-label">
													<input class="form-check-input" id="tbublock" type="checkbox">
													<span id="tbublock_label" class="form-check-sign">Unblock</span>
												</label>
											</div>												
										</div>		 -->
										
										<div class="col-md-3">
											<div class="form-group">
												<select id="tbublock" class="form-control">
													<option>Select</option>
													<option value="1">Block</option>
													<option value="0">UnBlock</option>
												</select>
												<label id="tbublock_error" class="text-danger"></label>
											</div>
										</div>
																							
									</div>
									
								</div>
								
								<div class="card-action">
									<div class="row">										
										<div class="col-md-6">
											<button class="btn btn-indigo float-right" id="proceed">Submit</button>	
										</div>
										<div class="col-md-6">	
											<button class="btn btn-danger float-left" onclick="">Cancel</button>
											
											<input type="hidden" id="cmenuoption">
										    <input type="hidden" id="mode">
										    
										    <input type="hidden" name="CSRFTOKEN" value="${sessionScope['CSRFTOKEN']}">
											<input type="hidden" id="calcurrbusDate" value="<%= session.getAttribute("sesMcontDate") %>" />
											<input type="hidden" name="hashedPassword" id="hashedPassword" />
											<input type="hidden" name="randomSalt" id="randomSalt" value="<%=rsalt %>" />
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
	
	<script>
	
	var jspath="<%=path%>" ;
	var serbasePath="<%=basePath%>" ;
	
	$(document).ready(function() {
		
		 $("#tuserid").autocomplete({
			    source:  serbasePath + 	"/Suggestions/User_Id",			//"/Suggestions/Unblock/User_Id",
			    minLength: 1,			
			    change: function(event, ui) 
			    {
			    	$("#status_row, #action_row").hide();
			    	// if(ui.item) 
				    // {
				        //QUEUECHECKER();
				   //  } 
			    },		
			    select: function(event, ui) 
			    {
			    	$("#status_row, #action_row").hide();
			    	// if(ui.item) 
				    // {
				        //QUEUECHECKER();
				   //  } 
			    },	
		  }).autocomplete( "instance" )._renderItem = function(ul,item) 
		  	{
		  		 return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
			};
			
		/*	$("#tbublock").change(function() {
				
				if($("#tbublock").prop('checked') == true)
				{
					$("#tbublock_label").html('Block');
				}
				else
				{
					$("#tbublock_label").html('Unblock');
				}		
			});	
		*/
		
			$("#chek_sts").click(function() {
				QUEUECHECKER();
			});	
			
			$("#proceed").click(function() {
				proceed();
			});		
	});
		
	function proceed()
	{
		if($("#tuserid").val() == "")
		{
			$("#tuserid_error").html('Required');
			$("#tuserid_error").show();
			return;
		}
		else
		{
			$("#tuserid_error").hide();
		}	
		
		if($("#tbublock").val() == "Select")
		{
			$("#tbublock_error").html('Required');
			$("#tbublock_error").show();
			return;
		}
		else
		{
			$("#tbublock_error").hide();
		}	
	
	    var data = new FormData();
			
		data.append("tuserid" , $("#tuserid").val());
		data.append("tbublock" , $("#tbublock").val());
		data.append("mode", $("#mode").val());
			
	   $.ajax({		 
			url  :  serbasePath + "/Block_Unblock_User",
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
	
	function QUEUECHECKER()
	{
		if($("#tuserid").val() == "")
		{
			$("#tuserid_error").html('Required');
			$("#tuserid_error").show();
			return;
		}
		else
		{
			$("#tuserid_error").hide();
		}	
		
		var data = new FormData();
		
		data.append("tuserid" , $("#tuserid").val());
		data.append("torgcd" , "EXIM");
		data.append("pgmID" , "UNBLOCKUSERID");
		
	   $.ajax({		 
			url  :  serbasePath + "/QUEUECHECKER",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					if(data.SucFlg == "1" ) 
		            {
						 if(data.STATUS == "Blocked")
						 {
						 	document.getElementById("tbublock").checked = true;
						 	
						 	$("#un_blocked").hide();
						 	
						 	$("#status_row, #action_row, #blocked").show();
						 }
						 else
						 {
						 	document.getElementById("tbublock").checked = false;
						 	
						 	$("#blocked").hide();
						 	
						 	$("#status_row, #action_row, #un_blocked").show();
						 }
						 
						 document.getElementById('mode').value =  data.mode;
					 }
		             else
		             {
		            	 $("#status_row, #action_row").hide();           	
		             }
				}
			},
			beforeSend: function( xhr )
	 		{
	 			//Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
	}

	function validateEmail(email)
	{
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    
	    return re.test(String(email).toLowerCase());
	}
	
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
	
	function isvalidPassword(val) 
	{ 
        if (val.match(/[a-z]/g) && val.match(/[A-Z]/g) && val.match(/[0-9]/g) && val.match(/[^a-zA-Z\d]/g)) 
        {
        	return true;
        }   
        else 
        {
        	return false;
        }
    } 

</script>
	</body>
</html>