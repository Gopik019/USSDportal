<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Login/CSS_&_JS.jsp" %>   
 
<body>
	<div class="container login_box">
		<div class="row">
			<div class="col-sm-6 loginimage">
				<img src="<spring:url value="/resources/PULSE/img/Welcome.gif" />" />
			</div>
			
			<div class="col-sm-6">
				<div class="form_align">
					<form id="login_form" action="<%= request.getContextPath() %>/PULSE/login" method="post">
					  
					  <div class="container">
					  
						<div class="center" ><h4>PULSE</h4></div>
					  
						<label for="uname"><b>Username</b></label>
						<input type="text" placeholder="Enter Username" name="uname" required>

						<label for="psw"><b>Password</b></label>
						<input type="password" placeholder="Enter Password" name="psw" required>
						
					  </div>

					  <div class="container foot">
						<div class="row">
							<div class="col-sm-3"></div>
							<div class="col-sm-2"><button type="submit" class="loginbtn">Login</button></div>
							<div class="col-sm-2"><button type="button" class="cancelbtn">Cancel</button></div>
						</div>
					  </div>
					</form>
				</div>	
			</div>
		</div>		 
	</div>
</body>
</html>