<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>
<%
	javax.servlet.http.HttpServletRequest _request = (javax.servlet.http.HttpServletRequest) request;
	
	String auth_error = (String) _request.getAttribute("AUTH_ERROR");
	
	String ses = session.getAttribute("sesUserId").toString();
	String val = ses;
	_request.setAttribute("AUTH_ERROR", null);
	
	String rsalt = PasswordUtils.getSalt();


	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>
<script type="text/javascript">
		var ses = '<%=val %>';
</script>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS3.jsp" %>   

<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/HTTPValidator.js" />' ></script>  
 
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/globalnew.js" />' ></script> 
 
<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/globalconatants.js" />' ></script> 

<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/sha256.js" />' ></script> 

<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/global.js" />' ></script>    
	
<%@ page import="com.hdsoft.utils.PasswordUtils" %>

<%@ page import="com.hdsoft.utils.WebContext"%>



<style>
	.text-danger{
		display:none;
		margin-bottom: 0px !important;
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
									</div>
								
									<div class="row">	
										<div class="col-md-2"></div>
										
										<div class="col-md-2">
											<div class="form-group">	
												<label for="email2">Block / Unblock</label>
											</div>
										</div>
										
										<div class="col-md-4">											
											<div class="form-check">
												<label class="form-check-label">
													<input class="form-check-input" id="tbublock" type="checkbox">
													<span id="tbublock_label" class="form-check-sign">Unblock</span>
												</label>
											</div>										
										</div>											
									</div>
									
								</div>
								
								<div class="card-action">
									<div class="row">										
										<div class="col-md-6">
											<button class="btn btn-primary float-right" id="proceed">Submit</button>	
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
	
	</body>
</html>