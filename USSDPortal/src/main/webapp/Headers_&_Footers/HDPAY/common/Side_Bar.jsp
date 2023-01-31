<div class="sidebar sidebar-style-2" data-background-color="${Menu.get('sidebar_color').getAsString()}">	 
			<div class="sidebar-wrapper scrollbar scrollbar-inner">
				<div class="sidebar-content">
					<div class="user">
						<div class="avatar-sm float-left mr-2">
							<img src="<%= session.getAttribute("sess_user_photo") %>" alt="..." class="avatar-img rounded-circle" id="user_img">
						</div>
						<div class="info">
							<a data-toggle="collapse" href="#collapseExample" aria-expanded="true">
								<span>
									<%=	session.getAttribute("sesUserName") %>
									<span class="user-level"><%=	session.getAttribute("sesRole") %></span>
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
					
					${Menu.get("Menu_Content").getAsString()}

				</div>
			</div>
		</div>
		
	<input type="hidden" id="ContextPath" value="<%= request.getContextPath() %>/HDPAY" />
	<input type="hidden" id="ContextPath2" value="<%= request.getContextPath() %>" />