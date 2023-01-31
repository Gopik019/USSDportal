<div class="sidebar sidebar-style-2" data-background-color="${sidebar_color}">	
			<div class="sidebar-wrapper scrollbar scrollbar-inner">
				<div class="sidebar-content">
								
	<%				String Active_Main = (String) request.getAttribute("active_main_menu");
					String Active_Sub  = (String) request.getAttribute("active_sub_menu"); 
					
					String Role = "";
					
					if(session.getAttribute("sesRole") !=null)
					{
						Role = (String) session.getAttribute("sesRole");
					}
					
	%>				
					<ul class="nav nav-primary">
							
						<li class="nav-item <%= Active_Main.equals("Administration") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#base">
								<i class="fas fa-layer-group"></i>
								<p><%= Role.equals("ADMIN") ? "Administration" : "Account" %></p>
								<span class="caret"></span>
							</a>
							<div class="collapse <%= Active_Main.equals("Administration") ? "show" : "" %>" id="base">
								<ul class="nav nav-collapse">
								
						<%		if(Role.equals("ADMIN"))
								{  %>
									<li class="<%= Active_Sub.equals("User_Registration") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/User_Registration">
											<span class="sub-item">User Registration</span>
										</a>
									</li>
						<%		} %>
										
									<li class="<%= Active_Sub.equals("Password_Reset") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Password_Reset">
											<span class="sub-item">Password Reset</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("Profile_Image_Upload") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Profile_Image_Upload">
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
										<a href="<%= request.getContextPath() %>/Datavision/Report_Generator/FIU_Report">
											<span class="sub-item">FIU Report</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("INTER_Report") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Interpolation-Report">
											<span class="sub-item">Interpolation Report</span>
										</a>
									</li>
									<li class="<%= Active_Sub.equals("INTER_Report2") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Interpolation-Report2">
											<span class="sub-item">Interpolation Report2</span>
										</a>
									</li>
								</ul>
							</div>
						</li>
						
				<%		if(Role.equals("ADMIN"))
						{  %>
							<li class="nav-item <%= Active_Main.equals("Authorization") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#maps">
								<i class="fas fa-fingerprint"></i>
								<p>Authorization</p>
								<span class="caret"></span>
							</a>
							
							<div class="collapse <%= Active_Main.equals("Authorization") ? "show" : "" %>" id="maps">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("Admin_Approval") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Admin_Approval">
											<span class="sub-item">Approval</span>
										</a>
									</li>	
									<li class="<%= Active_Sub.equals("Block-Unblock_User") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Block-Unblock_User">
											<span class="sub-item">Block / Unblock User</span>
										</a>
									</li>	
								</ul>
							</div>
						</li>
						
				<%		} %>
				
							<li class="nav-item <%= Active_Main.equals("Report Setup") ? "active submenu" : "" %>">
							<a data-toggle="collapse" href="#maps2">
								<i class="fas fa-cog"></i>
								<p>Report Setup</p>
								<span class="caret"></span>
							</a>
							
							<div class="collapse <%= Active_Main.equals("Report Setup") ? "show" : "" %>" id="maps2">
								<ul class="nav nav-collapse">
									<li class="<%= Active_Sub.equals("FIU_Reporting_Person") ? "active" : "" %>">
										<a href="<%= request.getContextPath() %>/Datavision/Report_Setup/FIU_Reporting_Person">
											<span class="sub-item">Reporting Person <br> Details</span>
										</a>
									</li>								
								</ul>
							</div>
						</li>
	
					</ul>
				</div>
				<input type="hidden" id="Context_Path" value="<%= request.getContextPath() %>/Datavision" />
				<input type="hidden" id="Context_Path2" value="<%= request.getContextPath() %>" />
			</div>
		</div>