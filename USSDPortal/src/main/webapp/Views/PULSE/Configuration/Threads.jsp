<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS2.jsp" %>   
 
<body>
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/PULSE/common/Side_Bar.jsp" %>   
	
	<%
		boolean Dedicated_Thread = request.getAttribute("Dedicated_Thread").toString().equals("true") ? true : false;
		boolean Event_Thread = request.getAttribute("Event_Based_Thread").toString().equals("true") ? true : false;
		boolean Notify_Thread = request.getAttribute("Notify_Thread").toString().equals("true") ? true : false;
	%>
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">

			<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %> 
			
				   	<div class="row">
						<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
								
								<div id="tab_card" class="card-body">
								
									<div class="row">	
						           		<div class="col-2"></div>	
										<div class="col-4">
											<div class="form-group">	
												<h5>Dedicated Thread</h5>
											</div>
										</div>
										
										<div class="col-4">
											<div class="form-group">		
												<label class="switch">
												  <input id="Dedicated_Thread" type="checkbox" <%= Dedicated_Thread ? "checked" : "" %> >
												  <span class="slider round"></span>
												</label>
											</div>
										</div>
										<div class="col-2"></div>	
									</div>
									
									<div class="row">	
						           		<div class="col-2"></div>	
										<div class="col-4">
											<div class="form-group">	
												<h5>Event Based Thread</h5>
											</div>
										</div>
										
										<div class="col-4">
											<div class="form-group">		
												<label class="switch">
												  <input id="Event_Thread" type="checkbox" <%= Event_Thread ? "checked" : "" %> >
												  <span class="slider round"></span>
												</label>
											</div>
										</div>
										<div class="col-2"></div>	
									</div>
									
									<div class="row">	
						           		<div class="col-2"></div>	
										<div class="col-4">
											<div class="form-group">	
												<h5>Notification Thread</h5>
											</div>
										</div>
										
										<div class="col-4">
											<div class="form-group">		
												<label class="switch">
												  <input id="Notify_Thread" type="checkbox" <%= Notify_Thread ? "checked" : "" %> >
												  <span class="slider round"></span>
												</label>
											</div>
										</div>
										<div class="col-2"></div>	
									</div>
									
									<div class="row">	
										<div class="col-6"></div>	
										<div class="col-6">
											<div class="form-group">	
												<button type="button" id="Submit" class="btn btn-info">Submit</button>
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
</div>

</body>
</html>