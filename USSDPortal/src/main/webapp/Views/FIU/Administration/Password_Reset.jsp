<%@ include file="../../../Headers_&_Footers/FIU/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/FIU/Administration/CSS_&_JS2.jsp" %>   
 
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/global.js" />' ></script> 		
										  															  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/sha256.js" />' ></script> 

<%@ page import="com.hdsoft.utils.PasswordUtils" %>

 
 <%
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/Datavision/";
	
	String rsalt = PasswordUtils.getSalt();
	
	//session = request.getSession(true);
	 
	//session.setAttribute("RANDOM_SALT", rsalt);
	
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
										<div class="col-md-3">
											<div class="form-group">	
												<label for="email2">New Password</label>
											</div>
										</div>
										<div class="col-md-5">
											<div class="form-group">		
												<input type="password" class="form-control" id="topwd" placeholder="">
											</div>
										</div>
									</div>
								
									<div class="row">	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="email2">Re-Enter Password</label>
											</div>
										</div>
										<div class="col-md-5">
											<div class="form-group">		
												<input type="password" class="form-control" id="tpwd" placeholder="">
											</div>
										</div>
									</div>	
																	
									<input type="hidden" id="torgcd" value="<%= session.getAttribute("sesDomainID") %>" >
									<input type="hidden" id="tuserid" value="<%= session.getAttribute("sesUserId") %>">
									<input type="hidden" name="hashedPassword" id="hashedPassword" />
									<input type="hidden" name="randomSalt" id="randomSalt" value="<%=rsalt %>" />
										
								</div>
								<div class="card-action">
									<div class="row">									
										<div class="col-md-6">
											<button class="btn btn-indigo float-right" onclick="Validate()">Submit</button>
										</div>
										<div class="col-md-6">	
											<button class="btn btn-danger float-left">Clear</button>
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

var serbasePath="<%=basePath%>" ;

function Validate()
{
	if(document.getElementById('tpwd').value == "" || document.getElementById('topwd').value == "")
	{
		Sweetalert("warning", "", "Please fill all the Required Fields");
		
		return;
	}
	
	if(document.getElementById('tpwd').value != document.getElementById('topwd').value)
	{
		Sweetalert("warning", "", "Passwords Mismatch"); 
		
		return;
	}
	
	var ciphertext=des(document.getElementById('tuserid').value+document.getElementById("tpwd").value, 1, 0);    

	var EncStr=hex_sha256(ciphertext);
	
	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
	
	document.getElementById("hashedPassword").value=finalHash;
		
	document.getElementById("tpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	document.getElementById("topwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
	
	Reset_Password();
}

function Reset_Password()
{
	var data = new FormData();
	
   	data.append('torgcd' , document.getElementById('torgcd').value);
 	data.append('tuserid' , document.getElementById('tuserid').value);
 	data.append('randomSalt' , document.getElementById('randomSalt').value);
 	data.append('hashedPassword' , document.getElementById('hashedPassword').value);
	
   	$.ajax({		 
		url  :  serbasePath + "/Password_Reset",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{ 
			Swal.close();
			
			if(data.Result == "Success")
			{ 
				Sweetalert("success_load_Current", "", "Password Reset Succcesfully !!");
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
	    error: function (jqXHR, textStatus, errorThrown) { return false; }
   });
}


</script>
 
</body>
</html>