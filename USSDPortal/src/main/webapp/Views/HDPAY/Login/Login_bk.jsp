<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Login/CSS_&_JS.jsp" %>   
 
<!--  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/HTTPValidator.js" />' ></script>  
																  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/globalnew.js" />' ></script> 
																  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/globalconatants.js" />' ></script> 
																  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/sha256.js" />' ></script> 
																  
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/global.js" />' ></script>    

 -->
	
<%@ page import="com.hdsoft.utils.PasswordUtils" %>

<%@ page import="com.hdsoft.utils.WebContext"%>


<%
	javax.servlet.http.HttpServletRequest _request = (javax.servlet.http.HttpServletRequest) request;

	String auth_error = (String) _request.getAttribute("AUTH_ERROR");
	
	_request.setAttribute("AUTH_ERROR", null);
	
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/HDPAY/";
	
	String rsalt = PasswordUtils.getSalt();
	
	//session = request.getSession(true);
	 
	//session.setAttribute("RANDOM_SALT", rsalt);
	
	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>


<body>
	<div class="container login_box">
		<div class="row">
			<div class="col-sm-6 loginimage">
				<img src="<spring:url value="/resources/HDPAY/img/Welcome.gif" />" />
			</div>
			
			<div class="col-sm-6">
				<div class="form_align">
					<form id="loginForm" action="<%= request.getContextPath() %>/HDPAY/login" method="post">
					  
					  <div class="container">
					  
						<div id="c_logo" class="row mt--3 mb-3">
							<div class="col col-sm-4"></div>
							<div class="col col-sm-4"><img src="<spring:url value="/resources/HDPAY/img/Hdpay_Logo.jpeg" />" /></div>
							<div class="col col-sm-4"></div>
						</div> 
					  
						<label for="uname"><b>Username</b></label>
						<input type="text" id="txtUserId" placeholder="Enter Username" name="uname" required>

						<label for="psw"><b>Password</b></label>
						<input type="password" id="txtPwd" placeholder="Enter Password" name="psw" required>
						
				<!--  		<div class="row" ><div class="col-md-12 text-danger" align="center"><h5>Session Timeout</h5></div></div> -->
						
					  </div>
					  
					  <div class="container foot">
						<div class="row">
							<div class="col-sm-3">
								<input type="hidden" id="CSRFTOKEN" name="CSRFTOKEN" value="">
								<input type="hidden" name="txtDomainId" id="txtDomainId">
								<input type="hidden" name="hidColor" id="hidColor">
								<input type="hidden" name="timeCheck1" id="timeCheck1">
								<input type="hidden" name="timeCheck2" id="timeCheck2">
								<input type="hidden" name="hashedPassword" id="hashedPassword" />
								<input type="hidden" name="EncStr" id="EncStr" />
								<input type="hidden" name="randomSalt" id="randomSalt" value="<%=rsalt %>" />
							 </div>
							
							<div class="col-sm-2"><button type="button" class="loginbtn" onclick="revalidate()">Login</button></div>
							<div class="col-sm-2"><button type="button" class="cancelbtn" onclick="clearFields()">Cancel</button></div>
							
							<input type="hidden" name="" value=""/>
							
						</div>
					  </div>
					</form>
				</div>	
			</div>
		</div>		 
	</div>
	
<script>
  
$(document).ready(function() {

	$('#txtPwd').keypress(function (e) {
		 var key = e.which;
		 if(key == 13)  
		 {
			 revalidate(); 
		 }
	});   
	
	LOADER3();
});

	var jspath="<%=path%>" ;
	var serbasePath="<%=basePath%>" ;
	var result = "<%=request.getAttribute("errMsg")%>";
	var autherror="<%=auth_error == null ? "" : auth_error%>";
	var randsalt="<%=rsalt%>";
	  
	var w_LastEntdFld;
	var w_Token;
	var w_RtnVal;
	var w_SaveFlg;
	var w_Ok;
	var w_Addr;
	var w_HostAddr;
	var w_HostName;
	var w_ErrMsg;
	var revalidationFlag = false;
	
	function LOADER3()
    {
		document.getElementById('loginForm').setAttribute("autocomplete", "off");
		document.getElementById('txtDomainId').setAttribute("autocomplete","off");
		document.getElementById('txtUserId').setAttribute("autocomplete","off");
		document.getElementById('txtPwd').setAttribute("autocomplete","off");
		
		getCurrentTime('timeCheck1');  
  		viewResult();
  		clearFields();
  		document.getElementById('hidColor').value=0;
  		
	  	if(autherror!="")
	  	{
	  	 	alert("Invalid 2FA Authentication Password");
	  	}  
	  	 
  		document.getElementById('txtDomainId').value="EXIM";
  		document.getElementById("chkDissolvedate").checked = false;
  		document.getElementById('randomSalt').value=randsalt;
  }
	
  function revalidate()
  {
	  	getCurrentTime('timeCheck2');  
			     
  	 	document.getElementById('txtDomainId').value="EXIM";
  	 	
       	var errorCount = 0;
       	
       	if(!isvc_txtDomainId())
       	{
       		errorCount++;
       	} 
       	
       	if(!isvc_txtUserId())	
       	{
       		errorCount++;
       	}
       	
       	if(!isvc_txtPwd())	
       	{
			errorCount++;
  		}
       
  		if(errorCount == 0)
  		{	
  			alert('0');
  			
			var ciphertext = des(document.getElementById("txtUserId").value+document.getElementById("txtPwd").value, 1, 0); 
			
			alert('1');
			
			//var ciphertext = des(document.getElementById("txtUserId").value, document.getElementById("txtPwd").value, 1, 0); 
			
       		var EncStr = hex_sha256(ciphertext);
       		
       		alert('2');
       		
       	 	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
       	 	
       		 alert('3');
       	 
       	    //finalHash = finalHash.replace(/\s/g, "");
		 
   			document.getElementById("hashedPassword").value = finalHash;
   		
   			document.getElementById("txtPwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
   			
   		
   			proceed_login();
       		
  			/*
  			
  			hashedpwd = PasswordUtils.getEncrypted(pwd, userid, PasswordUtils.getSalt(userid));
  			
  			var ciphertext = des(document.getElementById("txtUserId").value+document.getElementById("txtPwd").value, 1, 0);    
  			
       		var EncStr = hex_sha256(ciphertext);
       	
  			var data = new FormData();
  			
  		   	data.append('ciphertext', document.getElementById('randomSalt').value);
  		  	data.append('EncStr' , EncStr);
  		  	
  		   	$.ajax({		 
  				url  :  serbasePath + "/Encrypt",
  				type :  'POST',
  				data :  data,
  				cache : false,
  				contentType : false,
  				processData : false,
  				success: function (data) 
  				{ 
  					if(data.Result == "Success" )
  					{
						var finalHash = data.final_String;
						
						alert(finalHash);
						
						 finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
						 
						 alert(finalHash);
						
				   		document.getElementById("hashedPassword").value = finalHash;
				   		
				   		document.getElementById("txtPwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
				   		
				   		proceed_login();
  					}
  				},
  				beforeSend: function( xhr )
  		 		{
  		 			Sweetalert("load", "", "Please Wait");
  		        },
  			    error: function (jqXHR, textStatus, errorThrown) 
  			    { 
  			    	alert(errorThrown); 
  			    	
  			    	return false; 
  			    }
  		   }); */
       	}
  		else
  		{
       	 	Sweetalert("warning", "", "User Id or Password Should Not Be Blank");
       	 	
       		return false;
 		}
  }
  
  function proceed_login()
  {
	    var data = new FormData();
		
	    //data.append('INTFLAG' , $("#CSRFTOKEN").val());
		data.append('txtDomainId' , $("#txtDomainId").val());
		data.append('txtUserId' , $("#txtUserId").val());
		data.append('hashedPassword' , $("#hashedPassword").val());  
		data.append('randomSalt' , $("#randomSalt").val());
		
	   $.ajax({		 
			url  :  serbasePath + "/login/validate",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : false,
			processData : false,
			success: function (data) 
			{ 
				//Swal.close();
				
				if(data.Result == 'Success')
				{
					window.location = data.Action;
				}
				else
				{
					$("#txtPwd").val('');
					
					Sweetalert("warning", "", data.Message);
				}
			},
			beforeSend: function( xhr )
	 		{
	 			Sweetalert("load", "", "Please Wait");
	        },
		    error: function (jqXHR, textStatus, errorThrown) { 
		    	alert(errorThrown);
		    }
	   });
  }
  
  function getCurrentTime(id)
  {
	   var data = new FormData();
		
	   data.append('CSRFTOKEN' , $("#CSRFTOKEN").val());
		
	   $.ajax({		 
			url  :  serbasePath + "/getcurrenttime",
			type :  'POST',
			data :  data,
			cache : false,
			contentType : "application/x-www-form-urlencoded",
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == 'Success')
				{
					if(data.currentTime != "null")
					{ 
						document.getElementById(id).value = data.currentTime;	
						
						//alert(data.currentTime);
						
						if(id == 'timeCheck2')
						{
							timeDiffCheck();
						}
					}
				}
			},
		    error: function (jqXHR, textStatus, errorThrown) { }
	   });
  }
  
  function timeDiffCheck()
  {
		var h1 = Number((document.getElementById('timeCheck1').value).substring(0,2));  
		var m1 = Number((document.getElementById('timeCheck1').value).substring(3,5));
		var s1 = Number((document.getElementById('timeCheck1').value).substring(6,8));
		
		var h2 = Number((document.getElementById('timeCheck2').value).substring(0,2));  
		var m2 = Number((document.getElementById('timeCheck2').value).substring(3,5));
		var s2 = Number((document.getElementById('timeCheck2').value).substring(6,8));
		
		var hs = (h2-h1)*3600;
		var ms = (m2-m1)*60;
		var ss = (s2-s1); 
		
		if(hs+ms+ss>180)
		{
			Sweetalert("warning", "", "Please Re-Login for Security Concerns");
			
			//window.close();	
			//window.opener.close();
			return false;
		}
  }
  
  function viewResult()
  {
	  if(result == "null" || result == "")
	  {
		 result = "";
	  }
	  else
	  {
		 alert(result);
	  }
  } 

  function clearFields()
  {
	  document.all.txtUserId.value="";
	  document.all.txtPwd.value="";
	  w_HostAddr = "0";
	  w_HostName = "0";
	  w_ErrMsg = "";
	  w_Ok = false;
	  document.all.txtUserId.focus();
	  w_SaveFlg = 1;	
  }
  
  function validateDomainId(obj)
  {
		isvc_txtDomainId();	
  }
  
  function isvc_txtDomainId()
  {
		if(isBlank(document.getElementById('txtDomainId').value))
		{
			return false;
		}
		else
		{
			return true;
		}
  }
  
  function validateUserId()
  {
		isvc_txtUserId();
  } 
  
   function isvc_txtUserId()	
   {
		if(isBlank(document.getElementById('txtUserId').value))
		{
			return false;
		}
		else
		{
			return true;
		}
   }
   
	function validatePwd(obj)
	{	
		isvc_txtPwd();
	} 
	 
	function isvc_txtPwd()	
	{
		if(isBlank(document.getElementById('txtPwd').value))
		{
			return false;
		}
		else
		{
			return true;
		}
	 } 
	
	function isBlank(val)	
	{
		if(val == undefined || val == null || val.trim() == "")	
		{ 
			return true;
		}
		
		return false;
	}
	
</script>

</body>
</html>