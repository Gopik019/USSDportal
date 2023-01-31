<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>
<%@ page import="com.hdsoft.utils.PasswordUtils" %>

<%@ page import="com.hdsoft.utils.WebContext"%>
<%
	javax.servlet.http.HttpServletRequest _request = (javax.servlet.http.HttpServletRequest) request;

	String auth_error = (String) _request.getAttribute("AUTH_ERROR");
	
	_request.setAttribute("AUTH_ERROR", null);
	
	String rsalt = PasswordUtils.getSalt();
	
	//session = request.getSession(true);
	 
	session.setAttribute("RANDOM_SALT", rsalt);
	
	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%> 

<style>

.passval input{
	top : 0;
	left : 0;
	background : transparent;
	width : 100%;
	padding: 12px 20px;
	margin: 8px 0;
	border: 1px solid #ccc;
	box-sizing: border-box;
}

#toggle{
	position : absolute;
	top : 60%;
	right : 10px;
	transform : translateY(-50%);
	right : 20px;
	width : 30px;
	height : 30px;
	cursor : pointer;
}

#toggle1{
	position : absolute;
	top : 60%;
	right : 10px;
	transform : translateY(-50%);
	right : 20px;
	width : 30px;
	height : 30px;
	cursor : pointer;
}

#toggle2{
	position : absolute;
	top : 60%;
	right : 10px;
	transform : translateY(-50%);
	right : 20px;
	width : 30px;
	height : 30px;
	cursor : pointer;
}

</style>

<%@ include file="../../../Headers_&_Footers/HDPAY/Login/CSS_&_JS2.jsp" %> 
   
	
 
 
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<div class="form_align">
					  
					  <div class="container m-5 p-5">
					  
						<div class="row mt-3 mb-3">
							<div class="col col-sm-2"></div>
							<div class="col col-sm-8 shadow-lg p-4 rounded-md">
								<div class="row">
									<div class="col col-sm-2"></div>
									<div class="col col-sm-4" ><img src="<spring:url value="/resources/HDPAY/img/Hdpay_Logo.jpeg"  />"  style="width:100px;height:50px;float:right;"/></div>
								</div> 
								<div class="row mt-1">
									<div class="col-sm-6"><label for="txtUserId"><b>Enter Username</b></label></div>
									<div class="col-sm-6"><input type="text" id="txtUserId" placeholder="Enter Username" class="form-control" name="uname" required></div>
								</div>
								<div class="row mt-2">
									<div class="col-sm-6"><label for="sthrow" id="sthrow1"><b>Send OTP Through</b></label></div>
									<div class="col-sm-6">
										<input type="text" id="sthrow" class="form-control" value="EMAIL" readonly>
									</div>
								</div>
								<div class="row mt-2" id="otpvalrow" style="display:none;" class="form-group">
									<div class="col-sm-6"><label for='otp' id='otp1'><b>Enter OTP</b></label></div>
									<div class="col-sm-3"><input type='text' id='otp' placeholder='Enter OTP' name='otp' class="form-control" required></div>
									
	  								<div class="col-sm-3"><input type='submit' id='valid' value='Validate OTP' onclick='proceed();' class='btn btn-primary float-right'></div> 
								</div>
								<div class="row mt-2 pasvalrow" style="display:none;">
									<div class="col-sm-6"><label for='topwd'><b>Enter Password</b></label></div>
									<div class="col-sm-6">
										<div class='passval'>
											<input type='password' id='topwd' placeholder='Enter Password' class="form-control" name='topwd' required>
											<div class='fas fa-eye' id='toggle' onclick='eyeiconclick("topwd","toggle")'></div>
										</div>
									</div>
								</div>
								<div class="row mt-2 pasvalrow" style="display:none;">
									<div class="col-sm-6"><label for='tpwd'><b>Re-Enter Password</b></label></div>
									<div class="col-sm-6">
										<div class='passval'>
		  									<input type='password' id='tpwd' placeholder='Enter Password' class="form-control" name='tpwd' required>
											<div class='fas fa-eye' id='toggle1' onclick='eyeiconclick("tpwd","toggle1")'></div>
										</div>
									</div>
								</div>
								<!--  <div class="row mt-2 pasvalrow" style="display:none;">
									<div class="col-sm-2"></div>
									<div class="col-sm-8"><input type='submit' id='passubmit' value='submit' onclick='Validate();' class='btn btn-primary'></div>
									<div class="col-sm-2"></div>
								</div> --> 
								<div class="row mt-2">
									<div class="col-sm-2"></div>
									<div class="col-sm-8">
										<div class="row">
											<div class="col-sm-6"><button type="button" id="gtotp" class="btn btn-primary float-right" onclick="revalidate()">Get OTP</button></div>
										 	<!-- <div class="col-sm-3 pasvalrow"><input type='submit' id='valid' value='Validate OTP' onclick='proceed();' class='btn btn-primary'></div> -->
											<div class="col-sm-3 pasvalrow" style="display:none;"><input type='submit' id='passubmit' value='submit' onclick='Validate();' class='btn btn-primary'></div>
											<div class="col-sm-3"><button type="button" class="btn btn-danger float-left" onclick="clearFields()">Cancel</button></div>
										</div>
									</div>
									<div class="col-sm-2"></div>
								</div>
							</div>
							<div class="col col-sm-2"></div>
						</div>
					  
						
				<!--  		<div class="row" ><div class="col-md-12 text-danger" align="center"><h5>Session Timeout</h5></div></div> -->
						
					  </div>
					  
					  <div class="container foot">
						<div class="row">
							<div class="col-sm-3">
								<input type="hidden" id="CSRFTOKEN" name="CSRFTOKEN" value="${sessionScope['CSRFTOKEN']}">
								<input type="hidden" name="txtDomainId" id="txtDomainId">
								<input type="hidden" name="hidColor" id="hidColor">
								<input type="hidden" name="timeCheck1" id="timeCheck1">
								<input type="hidden" name="timeCheck2" id="timeCheck2">
								<input type="hidden" name="hashedPassword" id="hashedPassword" />
								<input type="hidden" name="randomSalt" id="randomSalt" value="<%=rsalt %>" />
								<input type="hidden" id="ContextPath" value="<%= request.getContextPath() %>" />
							 </div>
							
							
							
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						</div>
					  </div>
				</div>	
			</div>
		</div>
		</div>
	
	<script type="text/javascript">
	
		var result = "<%=request.getAttribute("errMsg")%>";
		var autherror="<%=auth_error == null ? "" : auth_error%>";
		var randsalt="<%=rsalt%>";
		
		var characters = "ABCDEFGHIJKLMNOPQRSTUVWXTZ"; 
		var lenString = 15;  
	    var randomstring = ''; 
		for (var i=0; i<lenString; i++) { 
			var a = Math.floor(Math.random() * characters.length);  
	        randomstring += characters.substring(a, a+1);
		}
		  
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
		
		/*function LOADER()
	    {
			document.getElementById('loginForm').setAttribute("autocomplete", "off");
			document.getElementById('txtDomainId').setAttribute("autocomplete","off");
			document.getElementById('txtUserId').setAttribute("autocomplete","off");
			
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
	  }*/
		
	  function revalidate()
	  { 
				     
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

	  		if(errorCount == 0)
	  		{	
	  			var uname = document.getElementById("txtUserId").value;
	  			var sthrow = document.getElementById("sthrow").value;
	  			
	  			var data = new FormData();
	  			
	  			data.append('token' , randomstring);
	  			data.append('type' , 'EXIM');
	  			data.append('username' , uname);
	  		   	data.append('unique' , uname);
	  		    data.append('sendthrough' , sthrow);
	  		   	$.ajax({		 
	  				url  :  $("#ContextPath").val() + "/getotp",
	  				type :  'POST',
	  				data :  data,
	  				cache : false,
	  				contentType : false,
	  				processData : false,
	  				success: function (data) 
	  				{ 
	  					if(data.Result == "Success")
	  					{
	  						$("#sthrow,#sthrow1,#gtotp").hide();
	  						$("#otpvalrow").show();
	  						$("#txtUserId").attr('disabled','disabled');
	  						
	  						Sweetalert("success", "", data.Message);
	  					}else{
	  						$("#sthrow,#sthrow1,#gtotp").show();
	  						$("#otpvalrow").hide();
	  					}
	  				},
	  			    error: function (jqXHR, textStatus, errorThrown) { return false; }
	  		   });
	       	}
	  		else
	  		{
	       	 	Sweetalert("warning", "", "User Id or Password Should Not Be Blank");
	       		return false;
	 		}
	  }
	  
	  function proceed()
	  {
		  var uname = document.getElementById("txtUserId").value;
		  var otp = document.getElementById("otp").value;
		  var sthrow = document.getElementById("sthrow").value;
			
			var data = new FormData();
			data.append('token' , randomstring);
			data.append('type' , 'EXIM');
			data.append('otp' , otp);
		   	data.append('unique' , uname);
		    data.append('sendthrough' , sthrow);
			
		   $.ajax({		 
				url  :  $("#ContextPath").val() + "/validateotp",
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
						$(".pasvalrow").show();
						
						$("#otp,#otp1,#valid").hide();
						
						Sweetalert("success", "", data.Message);
					}
					else
					{
						$(".pasvalrow").hide();
						
						$("#otp,#otp1,#valid").show();
						
						Sweetalert("warning", "", data.Message);
					}
				},
				beforeSend: function( xhr )
		 		{ },
			    error: function (jqXHR, textStatus, errorThrown) { }
		   });
	  }
	  
	  function eyeiconclick(e,f){
	  	var eyeicon = document.getElementById(f);
		var passinp = document.getElementById(e);
		
			if(passinp.type === 'password'){
				passinp.setAttribute('type', 'text');
				eyeicon.classList.remove('fa-eye');
				eyeicon.classList.add('fa-eye-slash');
			}else{
				passinp.setAttribute('type', 'password');
				eyeicon.classList.add('fa-eye');
				eyeicon.classList.remove('fa-eye-slash');
			}
		var isIE = /*@cc_on!@*/false || !!document.documentMode;
		var isEdge = !isIE && !!window.StyleMedia;
		var showButton = !(isIE || isEdge)
		if (!showButton) {
		    document.getElementById("toggle").style.visibility = "hidden";
		}
	  }
	  
	  function Validate()
	  {
	  	var tpwd = document.getElementById("tpwd").value;
	  	var topwd = document.getElementById("topwd").value;
	  	document.getElementById('txtDomainId').value="EXIM";
	  	if(document.getElementById("tpwd").value == "" || document.getElementById("topwd").value == "")
	  	{
	  		Sweetalert("warning", "", "Please fill all the Required Fields");
	  		
	  		return;
	  	}
	  	
	  	if(document.getElementById('tpwd').value != document.getElementById('topwd').value)
	  	{
	  		Sweetalert("warning", "", "Passwords Mismatch"); 
	  		
	  		return;
	  	}
	  	
	  	var ciphertext = des(document.getElementById("txtUserId").value+document.getElementById("tpwd").value, 1, 0); 
		
   		var EncStr = hex_sha256(ciphertext);
   		
   	 	var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
   	 	
		document.getElementById("hashedPassword").value = finalHash;
		
		document.getElementById("tpwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
		document.getElementById("topwd").value="xxxxxxxyyyyyyyyyyyyyzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
			
		Reset_Password();
	  }

	  function Reset_Password()
	  {
	  	var data = new FormData();
	  	
	     data.append('txtDomainId' , document.getElementById('txtDomainId').value);
	   	data.append('tuserid' , document.getElementById('txtUserId').value);
	   	data.append('hashedPassword' , document.getElementById('hashedPassword').value);
	   	data.append('randomSalt' , randsalt);
	  	
	     	$.ajax({		 
	  		url  :  $("#ContextPath").val() + "/Forget_Password_Reset",
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
	  				
	  				location.href = '<%= request.getContextPath() %>/HDPAY/login';
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
	  
	  function getCurrentTime(id)
	  {
		   var data = new FormData();
			
		   data.append('CSRFTOKEN' , $("#CSRFTOKEN").val());
			
		   $.ajax({		 
				url  :  $("#ContextPath").val() + "/getcurrenttime",
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
		  w_HostAddr = "0";
		  w_HostName = "0";
		  w_ErrMsg = "";
		  w_Ok = false;
		  document.all.txtUserId.focus();
		  w_SaveFlg = 1;	
		  window.location = $("#ContextPath").val()+"/HDPAY/login";
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
			if(val == undefined || val == null || trim(val) == "")	
			{ 
				return true;
			}
			return false;
		}
	</script>

</body>
</html>