<nav class="navbar navbar-header navbar-expand-lg" data-background-color="${nav_color}">				
	<div class="container-fluid">

		<ul class="navbar-nav topbar-nav ml-md-auto align-items-center">
			<li class="nav-item toggle-nav-search hidden-caret">
				<a class="nav-link" data-toggle="collapse" href="#search-nav" role="button" aria-expanded="false" aria-controls="search-nav">
					<i class="fa fa-search"></i>
				</a>
			</li>

			<li class="nav-item dropdown hidden-caret">
				<a class="dropdown-toggle profile-pic" data-toggle="dropdown" href="#" aria-expanded="false">
					<div class="avatar-sm">
						<img src="<%= session.getAttribute("sess_user_photo") %>" alt="..." class="avatar-img rounded-circle">
					</div>
				</a>
				<ul class="dropdown-menu dropdown-user animated fadeIn">
					<div class="dropdown-user-scroll scrollbar-outer">
						<li>
							<div class="user-box">
								<div class="avatar-lg"><img src="<%= session.getAttribute("sess_user_photo") %>" alt="image profile" class="avatar-img rounded"></div>
								<div class="u-text">
									<h4><%=	session.getAttribute("sesUserName") %></h4>
									<p class="text-muted"><%= session.getAttribute("sesEmail") %></p>
								</div>
							</div>
						</li>
						<li>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="<%= request.getContextPath() %>/Datavision/logout">Logout</a>
						</li>
					</div>
				</ul>
			</li>
		</ul>
	</div>
</nav>

   <input type="hidden" id="ContextPath" value="${pageContext.request.contextPath}/Datavision" />
   
   