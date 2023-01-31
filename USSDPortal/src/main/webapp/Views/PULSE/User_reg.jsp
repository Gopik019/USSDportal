<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS.jsp" %>   
<% 
	String rsalt = PasswordUtils.getSalt();
%>
<style type="text/css">
	.text-danger{
		display:none;
		margin-bottom: 0px !important;
		
	}
	.toggle-password {
    float: right;
    cursor: pointer;
    margin-right: 10px;
    margin-top: -25px;
    }
    
label
{
    width: 100px;
}

.alert
{
    display: none;
}

.requirements
{
    list-style-type: none;
}

.wrong .fa-check
{
    display: none;
}

.good .fa-times
{
    display: none;
}


</style>
<body data-background-color="${Menu.get('body_color').getAsString()}">

	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>  
		     <%@ page import="com.hdsoft.utils.PasswordUtils" %>

			<%@ page import="com.hdsoft.utils.WebContext"%>   
		     
		</div>
	
		<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
		
    <div class="main-panel">
			<div class="content">
				<div class="page-inner">
					
				<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %>   
					
					<div class="row">
					
					<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
						
								<div class="card-body">
								
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Organization Code</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control white" id="torgcd" value="EXIM" placeholder="" maxlength="25" size="20" readonly>
												<label id="torgcd_error" class="text-danger"></label>
											</div>
										</div>
									
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">User ID</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control" id="tuserid" placeholder="" maxlength="20" size="15">
												<label id="tuserid_span" class="text-danger"></label>
											<br>
											<!-- 	<label id="tuserid_error" class="text-danger"></label>
												<br> -->
													<label id="tuserid_error_value" class="text-danger"></label>
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
												<input type="text" class="form-control" id="tusernme" placeholder="" maxlength="20" size="15">
												
												<label id="tusernme_error" class="text-danger"></label>
											</div>
										</div>
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Date of Birth</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="date" class=" form-control" id="tbirthdate" placeholder=""  maxlength="10" size="12">
												<label id="tbirthdate_error" class="text-danger"></label>
											</div>
										</div>	
									</div>
								
									<div class="row">	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Primary Mobile No</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="text" class="form-control" id="tmobile" placeholder="" maxlength="10" size="20">
												<label id="tmobile_span" class="text-danger"></label>
												<label id="tmobile_error" class="text-danger"></label>
											</div>
										</div>	
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Primary Email Id</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="email" class="form-control" id="temail" placeholder="" maxlength="100" size="30">
												<label id="temail_error" class="text-danger"></label>
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
												<input type="password" class="form-control" id="tpwd" placeholder="" maxlength="20" size="15">
												<label id="tpwd_error" class="text-danger"></label>
												<div class="fas fa-eye-slash  toggle-password" id="toggle"></div>
												<!-- <label  id= "tpwd_leng" class="text-danger"></label> -->
											</div>
										</div>	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Re-Enter Password</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
												<input type="password" class="form-control" id="tconfirmpwd" placeholder="" maxlength="20" size="15">
												<label id="tconfirmpwd_error" class="text-danger"></label>
												<label  class="text-danger" id="tconfirmpwd_leng"></label>
												<div class="fas fa-eye-slash toggle-password" id="toggle_eye"></div>
											</div>
										</div>	
									</div>
									
											<div class="row">
												<div class="col-md-6">
												<div class="form-group">

										        <div class="alert alert-warning password-alert" role="alert">
										          <ul>
											            <li class="requirements leng"><i class="fas fa-check green-text"></i><i class="fas fa-times red-text"></i> Your password must have at least 8 character.</li>
											            <li class="requirements big-letter"><i class="fas fa-check green-text"></i><i class="fas fa-times red-text"></i> Your password must have at least 1 Capital letter.</li>
											            <li class="requirements num"><i class="fas fa-check green-text"></i><i class="fas fa-times red-text"></i> Your password must have at least 1 number.</li>
											             <li class="requirements small-letter"><i class="fas fa-check green-text"></i><i class="fas fa-times red-text"></i> Your password must have at least 1 small letter.</li>
											            <li class="requirements special-char"><i class="fas fa-check green-text"></i><i class="fas fa-times red-text"></i> Your password must have at least 1 special character.</li>
											        </ul>
										        </div>
										
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
												<select class="form-control" id="trolecd">
												    <option value="">Select</option>
													<option value="ADMIN">ADMIN - Admin User</option>
													<option value="CHKR">CHKR - Authoriser</option>
													<option value="MAKR">MAKR - Peer Role</option>
												</select>
												<label id="trolecd_error" class="text-danger"></label>
											</div>
										</div>	
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Registration Date</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">		
														<input type="text" class="form-control " readonly id="tregdate"  placeholder="" maxlength="10" size="12">
												<label id="tregdate_error" class="text-danger"></label>
											</div>
										</div>	
									</div>
									
									<div class="row">										
											
										<div class="col-6 col-md-2">
											<div class="form-group">	
												<label for="email2">Branch Code</label>
											</div>
										</div>
										<div class="col-6 col-md-4">
											<div class="form-group">														
												<select class="form-control" id="branchcd">
												    <option value="">Select</option>											
												</select>
												<label id="branchcd_error" class="text-danger"></label>
											</div>
										</div>	

									</div>
								</div>
								
								<div class="card-action">
									<div class="row">	
										
										<div class="col-md-6">
											<button class="btn btn-secondary float-right" id= "submit" onclick="revalidate()">Submit</button>	
										</div>
										
										<div class="col-md-6">	
											<button class="btn btn-danger float-left" onclick="reset();">Reset</button>
											
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
	<script type="text/javascript">
			var date = document.querySelectorAll("input[type='date']");
			
			var dtToday = new Date();

			var month = dtToday.getMonth() + 1;
			var day = dtToday.getDate();
			var year = dtToday.getFullYear();

			if(month < 10)
				month = '0' + month.toString();
			if(day < 10)
				day = '0' + day.toString();

			var maxDate = year + '-' + month + '-' + day;
			var max  = document.createAttribute("max");
			max.value = maxDate;
			for (i = 0; i < date.length; i++) {
				//alert(date.item);
				date[i].setAttribute("max",maxDate);
			}
			
		
			var serbasePath = $("#ContextPath").val() ; 
			
			$(document).ready(function() {
				
					 
				 var today = new Date();
				  var dd = String(today.getDate()).padStart(2, '0');
				  var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
				  var yyyy = today.getFullYear();

				  today = mm + '-' + dd + '-' + yyyy;

				  $('#tregdate').val(today);
				
				
				$('body').bind("cut copy paste",function(e) {
					     e.preventDefault();
					 });

				
				 $("#tuserid").autocomplete({
					    source:  serbasePath + "/Suggestions/User_Id_Check",
					    minLength: 1,			  
					    change: function(event, ui)
						{
					        if(ui.item) 
					        {
					        	$("#tuserid_error_value").html('This User Id is already taken');
					        	$("#tuserid_error_value").show();
								$("#submit").attr('disabled','disabled');
					        } 
					        else
					        {
					        	$("#tuserid_error").hide();
					        }
						 }
				  }).autocomplete( "instance" )._renderItem = function(ul,item) 
				  	{
				  		 return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
					};
					
				 /*$("#torgcd").autocomplete({
					    source:  serbasePath + "/Suggestions/ORGCODE",
					    minLength: 1,
					    change: function(event, ui)
					    {
					       
					    }
				  }).autocomplete( "instance" )._renderItem = function(ul,item) 
				  	{
					     return $( "<li><div>"+item.label+"</div></li>" ).appendTo(ul);
					}; */
					
					/* $.ajax({		 
						url  :  serbasePath + "/Suggestions/BRANCHCODE_Value",
						type :  'GET',
						data :  '',
						cache : false,
						contentType : false,
						processData : false,
						success: function (data) 
						{   
							if(data.Result == "Success")
							{ 
								for(var i=0;i<data.Branch_Codes.length;i++)
								{
									$("#branchcd").append($('<option></option>').val(data.Branch_Codes[i].label).html(data.Branch_Codes[i].label+" -  "+data.Branch_Codes[i].id)); 
								}
							}	
						},
						beforeSend: function( xhr )
						{
							 $("#branchcd").empty();
							 $("#branchcd").append($('<option></option>').val("").html("Select"));   
				        }			   
				   });	 */
					
				/* $("#branchcd").autocomplete({
					    source:  serbasePath + "/Suggestions/BRANCHCODE",
					    minLength: 1,
					    select: function(event, ui) 
					    {
					    	$("#branchcd").val(ui.item.label);
					    },
					    change: function(event, ui)
					    {
					        if(!ui.item) 
					        {
					        	$("#branchcd_error").html('Invalid Branch Code');
								$("#branchcd_error").show();
					        } 
					        else
					        {
					        	$("#branchcd_error").hide();
					        }
					    }
				  }).autocomplete( "instance" )._renderItem = function(ul,item) 
				  	{
					 	return $( "<li><div>"+item.label+" -  "+item.id+"</div></li>" ).appendTo(ul);
					}; */
					
				/*$("#trolecd").autocomplete({
				    source:  serbasePath + "/Suggestions/ROLECODE",
				    minLength: 1,
				    autoFocus: true,
				    select: function(event, ui) 
				    {
				    	$("#trolecd").val(ui.item.label);
				    }
				    change: function(event, ui)
				    {
				        if(!ui.item) 
				        {
				        	$("#branchcd_error").html('Invalid Role Code');
							$("#branchcd_error").show();
				        } 
				        else
				        {
				        	$("#branchcd_error").hide();
				        }
				    }
				  }).autocomplete( "instance" )._renderItem = function(ul,item) 
				  	{
					     return $( "<li><div>"+item.label+" -  "+item.id+"</div></li>" ).appendTo(ul);
					}; */
					
					$("#torgcd").blur(function() {
						validate_org_Id();
					});
					
					$("#tuserid").blur(function() {
						//validate_User_Id();
					});
					
					$("#tusernme").blur(function() {
						validate_User_Name();
					});
					
					$("#tbirthdate").blur(function() {
						validate_DOB();
					});
					
					$("#tmobile").blur(function() {
						validate_MOBILE();
					});
					
					$("#temail").blur(function() {
						validate_EMAIL();
					});
					
					$("#tpwd, #tconfirmpwd").keyup(function() {
						validate_passwords();
					});
					
					$("#tpwd, #tconfirmpwd").blur(function() {
						validate_passwords();
					});
					
					$("#trolecd").blur(function() {
						validate_Role();
					});
					
					$("#tregdate").blur(function() {
						validate_REG_Date();
					});
					
					$("#branchcd").blur(function() {
						validate_branch_Code();
					});
			
			
					var eyeicon = document.getElementById("toggle");
					var passinp = document.getElementById("tpwd");
					
					eyeicon.addEventListener("click",function(){
						if(passinp.type === 'password'){
							passinp.setAttribute('type', 'text');
							eyeicon.classList.remove('fa-eye-slash');
							eyeicon.classList.add('fa-eye');
						}
						else{
							passinp.setAttribute('type', 'password');
							eyeicon.classList.add('fa-eye-slash');
							eyeicon.classList.remove('fa-eye');
						}
					});
					
					var eyeicon1 = document.getElementById("toggle_eye");
					var passinp1 = document.getElementById("tconfirmpwd");
					
					eyeicon1.addEventListener("click",function(){
						if(passinp1.type === 'password'){
							passinp1.setAttribute('type', 'text');
							eyeicon1.classList.remove('fa-eye');
							eyeicon1.classList.add('fa-eye-slash');
						}
						else{
							passinp1.setAttribute('type', 'password');
							eyeicon1.classList.add('fa-eye');
							eyeicon1.classList.remove('fa-eye-slash');
						}
					});
					
			var minLengths = 5;
					
					$("#tuserid").on("keydown keyup change", function(){
					    var value = $(this).val();
					    if (value.length < minLengths)
					    	{
					    	
					    	$("#tuserid_span").html('It must be more than 5 character');
					    	$("#tuserid_span").show();
					    	$("#submit").attr("disabled","disabled");
					   
					    	}
					      
					  
					    else
					    	$("#tuserid_span").hide();
						
					});
					
					var pnumber = 10;
					$("#tmobile").on("keydown keyup change", function(){
					    var value = $(this).val();
					    if (value.length < pnumber)
					    	{
					    	
					    	$("#tmobile_span").html('Number should be 10 digits only');
					    	$("#tmobile_span").show();
					    	$("#submit").attr("disabled","disabled");
					   
					    	}
					      
					  
					    else
					    	$("#tmobile_span").hide();
						
					});
										$(function () {
					    var $password = $(".form-control[type='password']");
					    var $passwordAlert = $(".password-alert");
					    var $requirements = $(".requirements");
					    var leng, bigLetter, smallLetter, num, specialChar;
					    var $leng = $(".leng");
					    var $bigLetter = $(".big-letter");
					    var $smallLetter = $(".small-letter");
					    var $num = $(".num");
					    var $specialChar = $(".special-char");
					    var specialChars = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?`~";
					    var numbers = "0123456789";

					    $requirements.addClass("wrong");
					    $password.on("focus", function(){$passwordAlert.show();});

					    $password.on("input blur", function (e) {
					        var el = $(this);
					        var val = el.val();
					        $passwordAlert.show();

					        if (val.length < 8) {
					            leng = false;
					        }
					        else if (val.length > 7) {
					            leng=true;
					        }
					        
					        if(val.toUpperCase()==val)
					         {
					            smallLetter = false;
					        }
					        else
					       {
					 	       smallLetter=true;
						      }

					        if(val.toLowerCase()==val)
					         {
					            bigLetter = false;
					        }
					        else
					       {
					 	       bigLetter=true;
						      }
					        
					        num = false;
					        for(var i=0; i<val.length;i++){
					            for(var j=0; j<numbers.length; j++){
					                if(val[i]==numbers[j]){
					                    num = true;
					                }
					            }
					        }
					        
					        specialChar=false;
					        for(var i=0; i<val.length;i++){
					            for(var j=0; j<specialChars.length; j++){
					                if(val[i]==specialChars.charAt(j)){
					                    specialChar = true;
					                }
					            }
					        }

					        console.log(leng, bigLetter, num, specialChar);
					        
					        if(leng==true&&bigLetter==true&&num==true&&specialChar==true&&smallLetter==true){
					            $(this).addClass("valid").removeClass("invalid");
					            $requirements.removeClass("wrong").addClass("good");
					            $passwordAlert.removeClass("alert-warning").addClass("alert-success");
								$("#submit").attr("disabled",false);
								
						    	/* $("#tpwd_leng").hide();
						    	$("#submit").attr("disabled",false); */
					        }
					        else
					        {
					             $(this).addClass("invalid").removeClass("valid");
					            $passwordAlert.removeClass("alert-success").addClass("alert-warning");

					            if(leng==false){$leng.addClass("wrong").removeClass("good");}
					            else{$leng.addClass("good").removeClass("wrong");}

					            if(bigLetter==false){$bigLetter.addClass("wrong").removeClass("good");}
					            else{$bigLetter.addClass("good").removeClass("wrong");}

					            if(num==false){$num.addClass("wrong").removeClass("good");}
					            else{$num.addClass("good").removeClass("wrong");}

					            if(smallLetter==false){$smallLetter.addClass("wrong").removeClass("good");}
					            else{$smallLetter.addClass("good").removeClass("wrong");}

					            if(specialChar==false){$specialChar.addClass("wrong").removeClass("good");}
					            else{$specialChar.addClass("good").removeClass("wrong");}
								$("#submit").attr("disabled","disabled"); 
								
					        	/* $("#tpwd_leng").html('Password Must contain 8 Characters with a  Uppercase, Lowercase, Number, Special characters');
						    	$("#tpwd_leng").show();
						    	$("#submit").attr("disabled","disabled"); */
					        }
					        
					        
					        if(e.type == "blur"){
					                $passwordAlert.hide();
					            }
					    });
					});	
			

			});
			
			function validate_org_Id()
			{
				$("#torgcd").val('EXIM');
				
				return true;
			}
			
			function validate_User_Id()
			{
				if($("#tuserid").val() == "")
				{
					$("#tuserid_error").html('Required');
					$("#tuserid_error").show();
					return false;
				}
				else
				{
					$("#tuserid_error").hide();
					return true;
				}	
			}
			
			function validate_User_Name()
			{
				if($("#tusernme").val() == "")
				{
					$("#tusernme_error").html('Required');
					$("#tusernme_error").show();
					return false;
				}
				else
				{
					$("#tusernme_error").hide();
					return true;
				}	
			}
			
			function validate_DOB()
			{
				if($("#tbirthdate").val() == "")
				{
					$("#tbirthdate_error").html('Required');
					$("#tbirthdate_error").show();
					return false;
				}
				else
				{
					$("#tbirthdate_error").hide();
					return true;
				}	
			}
			
			function validate_EMAIL()
			{
				if($("#temail").val() == "")
				{
					$("#temail_error").html('Required');
					$("#temail_error").show();
					
					return false;
				}
				
				if(!validateEmail($("#temail").val()))
				{
					$("#temail_error").html('Invalid Email Address');
					$("#temail_error").show();
					
					return false;
				}
				
				$("#temail_error").hide();
				
				return true;
			}
			
			function validate_MOBILE()
			{
				if($("#tmobile").val() == "")
				{
					$("#tmobile_error").html('Required');
					$("#tmobile_error").show();
					return false;
				}
				else
				{
					$("#tmobile_error").hide();
					return true;
				}	
			}
			
			function validate_passwords()
			{
				if($("#tpwd").val() == "" || $("#tconfirmpwd").val() == "")
				{
					if($("#tpwd").val() == "")
					{
						$("#tpwd_error").html('Required');
						$("#tpwd_error").show();
					}
					
					if($("#tconfirmpwd").val() == "")
					{
						$("#tconfirmpwd_error").html('Required');
						$("#tconfirmpwd_error").show();
					}
					
					return false;
				}
			
			/*	if(isvalidPassword($("#tpwd").val()) != true)
				{
					$("#tpwd_error").html('Passwords must contains Upper case, lower case and alphanumberic characters');
					
					return false;
				}
				
				if(isvalidPassword($("#tconfirmpwd").val()) != true)
				{
					$("#tconfirmpwd_error").html('Passwords must contains Upper case, lower case and alphanumberic characters');
					
					return false;
				} */
				
				if($("#tpwd").val() != $("#tconfirmpwd").val())
				{
					$("#tconfirmpwd_error, #tpwd_error").html('Passwords does not match');
					$("#tpwd_error, #tconfirmpwd_error").show();
					return false;
				}
				
				$("#tpwd_error, #tconfirmpwd_error").hide();
				
				return true;
			}
			
			function validate_Role()
			{
				if($("#trolecd").val() == "")
				{
					$("#trolecd_error").html('Required');
					$("#trolecd_error").show();
					return false;
				}
				else
				{
					$("#trolecd_error").hide();
					return true;
				}	
			}
			
			function validate_REG_Date()
			{
				if($("#tregdate").val() == "")
				{
					$("#tregdate_error").html('Required');
					$("#tregdate_error").show();
					return false;
				}
				else
				{
					$("#tregdate_error").hide();
					return true;
				}	
			}
			
			function validate_branch_Code()
			{
				if($("#branchcd").val() == "")
				{
					$("#branchcd_error").html('Required');
					$("#branchcd_error").show();
					return false;
				}
				else
				{
					$("#branchcd_error").hide();
					return true;
				}	
			}

			function revalidate()
			{	     
		       	var errorCount = 0;
		       	
		       	if(!validate_org_Id())      { errorCount++; }
		    	if(!validate_User_Id())     { errorCount++; }
		    	if(!validate_User_Name())    { errorCount++; }
		    	if(!validate_DOB())  { errorCount++; }
		    	if(!validate_MOBILE())     { errorCount++; }
		    	if(!validate_EMAIL())      { errorCount++; }  	
		    	if(!validate_passwords())        { errorCount++; }
		    	if(!validate_Role()) { errorCount++; }
		    	if(!validate_REG_Date())     { errorCount++; }
		    	if(!validate_branch_Code())    { errorCount++; }

		  		if(errorCount == 0)
		  		{	
		  		
		  			
		  			User_test();
		       	}
		  		else
		  		{
		       		return false;
		 		}
			}
			
			function isvalid(Id)
			{
				if(isBlank(document.getElementById(Id).value))
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
			
			function proceed()
			{
			    var data = new FormData();
			  
				
				data.append('torgcd' , $("#torgcd").val());
				data.append('tuserid' , $("#tuserid").val());
				data.append('tusernme' , $("#tusernme").val());
				data.append('tbirthdate' , date_format($("#tbirthdate").val())); 
				data.append('tmobile' , $("#tmobile").val());
				data.append('temail' , $("#temail").val());
				data.append('tpwd' , $("#tpwd").val());
				data.append('tconfirmpwd' , $("#tconfirmpwd").val());
				data.append('trolecd' , $("#trolecd").val());
				data.append('tregdate' , date_format($("#tregdate").val())); 
				data.append('branchcd' , $("#branchcd").val());		
				data.append('hashedPassword', $("#hashedPassword").val());  
				data.append('randomSalt', $("#randomSalt").val());   
				
			   $.ajax({	
		
			
					url  :  serbasePath + "/User_Registration",  
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
			
			function get_hashpassword()
			{	
				 
				var ciphertext = des(document.getElementById("tuserid").value+document.getElementById("tpwd").value, 1, 0);    
				
		   		var EncStr=hex_sha256(ciphertext);
		   		
		   		var finalHash = doEncrypt(EncStr,document.getElementById('randomSalt').value);
		   		
		   		document.getElementById("hashedPassword").value=finalHash;
		   		
		   		
		 
		   		proceed();

				/*var data = new FormData();
				
			   	data.append('ciphertext' , document.getElementById('randomSalt').value);
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
								
						   		document.getElementById("hashedPassword").value=finalHash;
						   		
						   		proceed();
							}
						},
					    error: function (jqXHR, textStatus, errorThrown) { return false; }
				   }); */
		   	}
			
			function User_test()
			{

			    var data = new FormData();
			    
			    var user  = $("#tuserid").val();
			    
			    data.append("user",user);
	
			   $.ajax({	
		
					url  :  serbasePath + "/Suggestions/User_Id",  
					type :  'POST',
					data :  data,
					cache : false,
					contentType : false,
					processData : false,
					success: function (data) 
					{ 
						if(data.Result == 'Success')
						{
							
							$("#tuserid_error").html('This User Id is already taken');
							$("#tuserid_error").show();
							Sweetalert("warning","","This User Id is already taken");
							
						}
						else
						{
							get_hashpassword();
							$("#tuserid_error").html("");
							$("#tuserid_error").hide();
						}
					},
					beforeSend: function( xhr )
			 		{
			 			//Sweetalert("load", "", "Please Wait");
			        },
				    error: function (jqXHR, textStatus, errorThrown) { }
			   });
			}

			function reset()
			{
				location.reload();
				//$('form :input').val('');
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
			
			function Sweetalert(Type, Title, Info)
			{
				if(Type == "success")
				{
					Swal.fire({
						  icon: 'success',
						  title: Title ,
						  text:  Info,		 		  
						  timer: 2000,
						  showConfirmButton: false 
					});
				}
				else if(Type == "success_load_Current")
				{
					Swal.fire({
						  icon: 'success',
						  title: Title ,
						  text:  Info,
						  timer: 2000
					}).then(function (result) {
						  if (true)
						  {							  
							  location.reload();
						  }
					});		
				}
				else if(Type == "error")
				{
					Swal.fire({
						  icon: 'error',
						  title: Title ,
						  text:  Info ,		 
						  timer: 2000,
						  showConfirmButton: false 
					});
				}
				else if(Type == "warning")
				{
					Swal.fire({
						  icon: 'warning',
						  title: Title ,
						  text:  Info ,		 
						  timer: 2000,
						  showConfirmButton: false 
					});
				}
				else if(Type == "load")
				{
					Swal.fire({
						  title: Title,
						  html:  Info,
						  timerProgressBar: true,
						  allowOutsideClick: false,
						  onBeforeOpen: () => {
						    Swal.showLoading()
						  },
						  onClose: () => {
						  }
					});	
				}	
			}

		</script>
	</body>
</html>