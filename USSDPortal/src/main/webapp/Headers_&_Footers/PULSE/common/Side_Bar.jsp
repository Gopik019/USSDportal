<div class="sidebar sidebar-style-2" data-background-color="${sidebar_color}">	
			<div class="sidebar-wrapper scrollbar scrollbar-inner">
				<div class="sidebar-content">
					<div class="user">
						<div class="avatar-sm float-left mr-2">
							<img src="<spring:url value="/resources/PULSE/img/admin.png" />" alt="..." class="avatar-img rounded-circle">
						</div>
						<div class="info">
							<a data-toggle="collapse" href="#collapseExample" aria-expanded="true">
								<span>
									Benjamin
									<span class="user-level">Administrator</span>
									<span class="caret"></span>
								</span>
							</a>
							<div class="clearfix"></div>

							<div class="collapse in" id="collapseExample">
								<ul class="nav">
									<li>
										<a href="#profile">
											<span class="link-collapse">My Profile</span>
										</a>
									</li>
									<li>
										<a href="#edit">
											<span class="link-collapse">Edit Profile</span>
										</a>
									</li>
									<li>
										<a href="#settings">
											<span class="link-collapse">Settings</span>
										</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
					
					<%
					
					String Active_Main = (String) request.getAttribute("active_main_menu");
					String Active_Sub  = (String) request.getAttribute("active_sub_menu");
					
					%>
					
					<ul class="nav nav-primary">
						<li class="nav-item <%= Active_Main.equals("Dashboard") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#dashboard" class="collapsed" aria-expanded="false">
								<i class="fas fa-home"></i>
								<p>Dashboard</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Dashboard") ? "show" : "" %>" id="dashboard">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Dashboard_light") || Active_Sub.equals("Dashboard_dark")? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Dashboard_light">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("Configuration") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#tables8">
								<i class="fas fa-wrench"></i>
								<p>Configuration</p>
								<span class="caret"></span>
							</a>
						  	<div class="collapse <%= Active_Main.equals("Configuration") ? "show" : "" %>" id="tables8">
								<ul class="nav nav-collapse">
							<!--		<li class="<%= Active_Sub.equals("Configuration") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Configuration">
											<span class="sub-item">Configuration</span>
										</a>
									</li> 
									<li class="<%= Active_Sub.equals("Threads") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Threads">
											<span class="sub-item">Threads</span>
										</a>
									</li> -->
									<li class="<%= Active_Sub.equals("Event_Creation") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Event_Creation">
											<span class="sub-item">Event Creation</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_Creation") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Creation">
											<span class="sub-item">User Journey Creation</span>
										</a>
									</li>									
									<li class="<%= Active_Sub.equals("Alert Distribution Creation") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Alert-Distribution-Creation">
											<span class="sub-item">Alert Distribution Creation</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
							
						<li class="nav-item <%= Active_Main.equals("DBMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#base">
								<i class="fas fa-layer-group"></i>
								<p>Database Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("DBMON") ? "show" : "" %>" id="base">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Database_Monitoring") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Database_Monitoring">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_DBMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/DBMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_DBMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/DBMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("TRANMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#sidebarLayouts">
								<i class="fas fa-th-list"></i>
								<p>Transaction Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("TRANMON") ? "show" : "" %>" id="sidebarLayouts">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Transaction_Monitoring") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Transaction_Monitoring">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_TRANMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/TRANMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_TRANMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/TRANMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("INFMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#tables">
								<i class="fas fa-pen-square"></i>
								<p>Infra Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("INFMON") ? "show" : "" %>" id="tables">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Infra_Monitoring") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/PULSE/Infra_Monitoring">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_INFMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/INFMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_INFMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/INFMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
		   <!-- 		<li class="nav-item <%= Active_Main.equals("APPMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#asm">
								<i class="fas fa-layer-group"></i>
								<p>App Server Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("APPMON") ? "show" : "" %>" id="asm">
								<ul class="nav nav-collapse">
									<li class="#">   
										<a href="#">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_APPMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/APPMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_APPMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/APPMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("FILEMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#sidebarLayouts2">
								<i class="fas fa-th-list"></i>
								<p>File Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("FILEMON") ? "show" : "" %>" id="sidebarLayouts2">
								<ul class="nav nav-collapse">
									<li class="">   
										<a href="#">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_FILEMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/FILEMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_FILEMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/FILEMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("WEBMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#tables2">
								<i class="fas fa-pen-square"></i>
								<p>Web Service Monitoring</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("WEBMON") ? "show" : "" %>" id="tables2">
								<ul class="nav nav-collapse">
									<li class="#">   
										<a href="#">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_WEBMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/WEBMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_WEBMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/WEBMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("EXEMON") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#tables3">
								<i class="fas fa-pen-square"></i>
								<p>Exception & Error <br> Scanning</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("EXEMON") ? "show" : "" %>" id="tables3">
								<ul class="nav nav-collapse">
									<li class="#">   
										<a href="#">
											<span class="sub-item">Dashboard</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Event_Monitoring_EXEMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/Event_Monitoring/EXEMON">
											<span class="sub-item">Event Monitoring</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("User_Journey_EXEMON") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/PULSE/User_Journey_Monitoring/EXEMON">
											<span class="sub-item">User Journey  Monitoring</span>
										</a>
									</li>
								</ul>
							</div>
						</li>  -->
													
					</ul>
				</div>
			</div>
		</div>
		
	<input type="hidden" id="ContextPath" value="<%= request.getContextPath() %>/PULSE" />