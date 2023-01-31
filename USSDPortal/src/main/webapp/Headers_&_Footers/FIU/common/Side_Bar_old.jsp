<div class="sidebar sidebar-style-2" data-background-color="${sidebar_color}">	
			<div class="sidebar-wrapper scrollbar scrollbar-inner">
				<div class="sidebar-content">
								
	<%				String Active_Main = (String) request.getAttribute("active_main_menu");
					String Active_Sub  = (String) request.getAttribute("active_sub_menu");  %>
					
					<ul class="nav nav-primary">
						<li class="nav-item <%= Active_Main.equals("Dashboard") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#dashboard" class="collapsed" aria-expanded="false">
								<i class="fas fa-home"></i>
								<p>Dashboard</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Dashboard") ? "show" : "" %>" id="dashboard">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Dashboard_light") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Dashboard">
											<span class="sub-item">View Dashboard</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
							
						<li class="nav-item <%= Active_Main.equals("Administration") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#base">
								<i class="fas fa-layer-group"></i>
								<p>Administration</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Administration") ? "show" : "" %>" id="base">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("User_Registration") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/User_Registration">
											<span class="sub-item">User Registration</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Password_Reset") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Password_Reset">
											<span class="sub-item">Password Reset</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Profile_Image_Upload") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Profile_Image_Upload">
											<span class="sub-item">Profile Image</span>
										</a>
									</li>	
								</ul>
							</div>
						</li>
						<li class="nav-item <%= Active_Main.equals("Report Generator") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#sidebarLayouts">
								<i class="fas fa-th-list"></i>
								<p>Report Generator</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Report Generator") ? "show" : "" %>" id="sidebarLayouts">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("FIU_Report") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Report_Generator/FIU_Report">
											<span class="sub-item">FIU Report</span>
										</a>
									</li>
									<li>
										<a href="#">
											<span class="sub-item">Graph Report</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("Report Setup") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#tables">
								<i class="fas fa-pen-square"></i>
								<p>Report Setup</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Report Setup") ? "show" : "" %>" id="tables">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("FIU_Reporting_Person") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Report_Setup/FIU_Reporting_Person">
											<span class="sub-item">Reporting Person Details</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("FIU_Employer") ? "active" : "" %>">   
										<a href="<%= request.getContextPath() %>/Report_Setup/FIU_Employer">
											<span class="sub-item">FIU Employer Details</span>
										</a>
									</li>
									<li>
										<a href="#">
											<span class="sub-item">Automation Settings</span>
										</a>
									</li>
									<li>
										<a href="#">
											<span class="sub-item">Graph Report Setup</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
						<li class="nav-item <%= Active_Main.equals("Authorization") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#maps">
								<i class="fas fa-fingerprint"></i>
								<p>Authorization</p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Authorization") ? "show" : "" %>" id="maps">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Admin_Approval") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Admin_Approval">
											<span class="sub-item">Approval</span>
										</a>
									</li>	
									<li>
										<a href="#">
											<span class="sub-item">Master Authorization</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
					</ul>
				</div>
				<input type="hidden" id="Context_Path" value="<%= request.getContextPath() %>" />
			</div>
		</div>