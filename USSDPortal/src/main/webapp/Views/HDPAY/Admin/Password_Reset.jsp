<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS4.jsp" %> 

<%@ page import="com.hdsoft.utils.PasswordUtils" %> 
<%
String rsalt = PasswordUtils.getSalt();
%>

<style>
	
	input::-ms-reveal,
      input::-ms-clear {
        display: none;
      }
		
	.toggle-password {
   
    float: right;
    margin-top: -25px;
    padding-right: 26px;
    position: relative;
  
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
		</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">	
			
			<%@ include file="../../../Headers_&_Footers/HDPAY/common/Form_header.jsp" %> 
			
		        <div class="row"  oncopy="return false" onpaste="return false" oncut="return false">
					<div class="col-md-12 mt--1">
		
						<div id="colour_body" class="card">
		
							<div id="tab_card" class="card-body">
								<div class="row mt--3">
									<div class ="col-md-2"></div>
									<div class="col-md-2">
										<div class="form-group">
											<label>New Password:</label>
										</div>
									</div>
									
									<div class="col-md-4">
										<div class="form-group">
										 
										 	<input type="password" id="topwd" class="form-control"> 
											<div class="fas fa-eye-slash  toggle-password" id="toggle"></div>
											<label id="topwd_error" class="text-danger" for="topwd" data-error="wrong" data-success="right" ></label>
											
											
										</div>
									</div>
									<div class="col-md-4">
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
								<div class="row">
									<div class="col-md-2"></div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Re-Enter Password:</label>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group">
											<input type="password" id="tpwd" class="form-control">
											<div class="fas fa-eye-slash  toggle-password" id="toggle_eye"></div>
											<label id="tpwd_error" class="text-danger"></label>
											
										</div>
									</div>
									<div class="col-md-4"></div>
								</div>
								<input type="hidden" id="torgcd" value="<%= session.getAttribute("sesDomainID") %>" >
								<input type="hidden" id="tuserid" value="<%= session.getAttribute("sesUserId") %>">
								<input type="hidden" id="randomSalt" value="<%= rsalt %>">
								<input type="hidden" id="hashedPassword">
								<div id="custom_card_action" class="card-action">
									<div class="row">
										<div class="col-md-4"></div>
										<div class="col-md-2">
											<input type="submit"  id ="submit" name="submit" onclick="Validate()" class="btn btn-secondary float-right" value="Submit">
										</div>
										<div class="col-md-2">
											<button class="btn btn-danger float-left" id="reset">Reset</button>
										</div>
										<div class="col-md-4"></div>
									</div>
								</div>
							</div>
						</div>
							
					</div>
				</div>	
			</div>   
		</div>	
    </div> 	</div>
    
    
	</body>
</html>