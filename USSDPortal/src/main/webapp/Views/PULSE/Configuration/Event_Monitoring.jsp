<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Configuration/CSS_&_JS7.jsp" %>   
 
<style>

.events_monitor {
    box-shadow: 0 6px 10px rgba(0,0,0,.08), 0 0 6px rgba(0,0,0,.05);
    transition: .3s transform cubic-bezier(.155,1.105,.295,1.12),.3s box-shadow,.3s -webkit-transform cubic-bezier(.155,1.105,.295,1.12);
    cursor: pointer;
}

.events_monitor:hover {
     transform: scale(1.05);
  box-shadow: 0 10px 20px rgba(0,0,0,.12), 0 4px 8px rgba(0,0,0,.06);
}

</style>

<%
  	String Module_Id = request.getAttribute("Module_Id") !=null ? (String)request.getAttribute("Module_Id") : "";
%>

<body>
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/PULSE/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
		<div class="content">
			<div class="page-inner">
					
					<%@ include file="../../../Headers_&_Footers/FIU/common/Form_header.jsp" %> 
			
				   	<div class="row">
						<div class="col-md-12 mt--1">
							<div id="colour_body" class="card">
								
								<div id="tab_card" class="card-body">
								
								<div class="row" align="center">	
						           		<div class="col-md-2"></div>	
										<div class="col-md-3">
											<div class="form-group">	
												<label for="">Module</label>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="form-group">		
												<select id="Module_Id" class="form-control">
													<option value="Select">Select</option>
													<option value="TRANMON" <%= Module_Id.equals("TRANMON")? "selected" : "" %>>Transactions Monitoring</option>
													<option value="APPMON"  <%= Module_Id.equals("APPMON") ? "selected" : "" %>>Application Server Monitoring</option>
													<option value="DBMON"   <%= Module_Id.equals("DBMON")  ? "selected" : "" %>>Database Monitoring</option>		
													<option value="FILEMON" <%= Module_Id.equals("FILEMON")? "selected" : "" %>>File Monitoring</option>		
													<option value="WEBMON"  <%= Module_Id.equals("WEBMON") ? "selected" : "" %>>Web service Monitoring</option>				
													<option value="EXEMON"  <%= Module_Id.equals("EXEMON") ? "selected" : "" %>>Exception and Error Scanning</option>				
													<option value="CONMON"  <%= Module_Id.equals("CONMON") ? "selected" : "" %>>Connectivity Monitoring</option>		
													<option value="INFMON"  <%= Module_Id.equals("INFMON") ? "selected" : "" %>>Infra Monitoring</option>						
												</select>
											</div>
										</div>
										
										<div class="col-md-1">
									  		<button id="Add_event_Monitoring" class="btn btn-secondary"><i class="fa fa-plus" aria-hidden="true"></i></button>	
									  	</div>
									  	
									</div>
																		
								<div id="fixed_events_card" class="card mt-5">	
									<div class="tab-content mt-2" id="pills-without-border-tabContent">
										
			<!--  section 1-->			<div class="tab-pane fade active show" id="pills-initiate" role="tabpanel" aria-labelledby="pills-home-tab-nobd">
											<section>  

									            <ul class="nav nav-pills nav-secondary justify-content-center" id="pills-tab" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab" data-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">Time Based Event</a>	
													</li>	
												</ul>
							
												<div class="tab-content" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">													
														<div class="fluid_row mt-3">
								                			<div  class="col-md-12">
								                				<div id="fixed_events" class="row"></div>
															</div>
														</div>
													</div>
												</div>	
			        						</section>
										</div>
									</div>	
								</div>	
								
								<div id="nonstop_events_card" class="card mt-3">	
									<div class="tab-content mt-2 mb-3" id="pills-without-border-tabContent">
										
			<!--  section 2-->			<div class="tab-pane fade active show" id="pills-initiate" role="tabpanel" aria-labelledby="pills-home-tab-nobd">
											<section>  

									            <ul class="nav nav-pills nav-secondary justify-content-center" id="pills-tab" role="tablist">
													<li class="nav-item submenu">
														<a class="nav-link active show" id="pills-home-tab" data-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">Non Stop Event</a>	
													</li>	
												</ul>
							
												<div class="tab-content" id="pills-tabContent">
													<div class="tab-pane fade active show" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">													
														<div class="fluid_row mt-3">
									                		<div class="col-md-12">
									                			<div id="nonstop_events" class="row"></div>								
															</div>	
														</div>
													</div>
												</div>	
		        						</section>
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