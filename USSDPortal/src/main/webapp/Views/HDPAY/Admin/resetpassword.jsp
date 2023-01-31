<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Administration/CSS_&_JS4.jsp" %> 

<%@ page import="com.hdsoft.utils.PasswordUtils" %> 
<%
String rsalt = PasswordUtils.getSalt();
%>
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
		
							<div id="tab_card" class="card-body">
								<div class="row mt--3">
									<div class="col-md-4">
										<div class="form-group">
											<label>New Password:</label>
										</div>
									</div>
									
									<div class="col-md-8">
										<div class="form-group">
											<input type="text" id="topwd" class="form-control col-sm-4">
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-4">
										<div class="form-group">
											<label>Re-Enter Password:</label>
										</div>
									</div>
									<div class="col-md-8">
										<div class="form-group">
											<input type="text" id="tpwd" class="form-control col-sm-4">
										</div>
									</div>
								</div>
								<input type="hidden" id="torgcd" value="<%= session.getAttribute("sesDomainID") %>" >
								<input type="hidden" id="tuserid" value="<%= session.getAttribute("sesUserId") %>">
								<input type="hidden" id="randomSalt" value="<%= rsalt %>">
								<input type="hidden" id="hashedPassword">
								<div id="custom_card_action" class="card-action">
									<div class="row">
										<div class="col-md-3"></div>
											<div class ="col-md-6">
												<input type="submit" name="submit" onclick="Validate()" class="btn btn-secondary float-right mr-1" value="Submit">
												<input type="reset" name="clear" class="btn btn-danger float-left" value="Clear">
											</div>
										<div class="col-md-3"></div>
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