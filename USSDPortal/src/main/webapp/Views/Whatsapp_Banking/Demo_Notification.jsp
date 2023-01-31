<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>
<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS12.jsp" %>	


<title>Demo Notification</title>

	<link href='<spring:url value="/resources/HDPAY/css/atlantis.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/HDPAY/css/bootstrap.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/HDPAY/css/fonts.min.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/HDPAY/css/autocomplete.css" />' rel="stylesheet">
	<link href='<spring:url value="/resources/HDPAY/img/Hdpay_Logo.jpeg" />' rel="shortcut icon">
	<link href='<spring:url value="/resources/HDPAY/css/styles.css" />' rel="stylesheet"> 

	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/sweetalert/dist/sweetalert.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/ajax/libs/jquery/3.2.1/jquery.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/plugin/webfont/webfont.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/atlantis.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/autocomplete.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/autocomplete_ui.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/core/popper.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"/>' ></script>	
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/js/core/bootstrap.min.js"/>' ></script>
	<script type="text/javascript" src='<spring:url value="/resources/HDPAY/Validations/Login/Time_Out.js" />' ></script>	
 	
</head>
 
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
	
				                	<div class="col-md-12">
	
										<div class="row">	
					                		
					                		<div class="col-md-2"></div>
					                		
											<div class="col-md-2">
												<div class="form-group">	
													<label for="">Mobile No</label>
												</div>
											</div>
											
											<div class="col-md-2">
												<div class="form-group">	
													<select class="form-control" id="pin">
														<option>+91</option>
														<option>+253</option>
													</select>
													<label id="pin_error" class="text-danger"></label>
												</div>
											</div>
																					
											<div class="col-md-3">
												<div class="form-group">	
													<input type="number" class="form-control" id="mob" placeholder="Mobile Number">
													<label id="mob_error" class="text-danger"></label>
												</div>
											</div>
																					
										</div>
										
										<div class="row">	
											
											<div class="col-md-2"></div>
											
					                		<div class="col-md-2">
												<div class="form-group">	
													<label for="">Message</label>
												</div>
											</div>
											
											<div class="col-md-5">
												<div class="form-group">		
													<textarea rows="4" class="form-control" id="msg">
													</textarea>
													<label id="msg_error" class="text-danger"></label>
												</div>
											</div>
																													
									</div>
											
									<div id="custom_card_action" class="card-action">
										<div class="row mt--2">	
											<div class="col-md-12" align="center">
												<button id="channel_action" class="btn btn-secondary">Submit</button>	
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
	 </div>
	 </div>
	 
	 <script>
	 $(document).ready(function() {  
						
			$("#channel_action").click(function() { 
				
				Create_Event();
			});

		});

		function Create_Event()
		{
			var err = 0;
			
			if(!validation("pin", "txt"))      { err++; }
			if(!validation("mob", "txt"))       { err++; }
			if(!validation("msg", "txt"))         { err++; }
			
			if(err !=0 )
			{
				return;
			}

			var data = new FormData();
			
			data.append("pin", $("#pin").val());
			data.append("mob", $("#mob").val());
			data.append("msg", $("#msg").val());
		
			$.ajax({		 
				url  :  $("#ContextPath").val()+"/Whatsapp-Banking",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{   
					if(data.Result == "Success")
					{
						Sweetalert("success", "", data.Message);
					}
					else
				    {
						Sweetalert("warning", "", data.Message);
				    }	
				},
				beforeSend: function( xhr )
		 		{
		 			Sweetalert("load", "", "Please Wait");
		        },
			    error: function (jqXHR, textStatus, errorThrown) 
				{ 
			    	//Sweetalert("warning", "", "errrrr");  
			    }
		   });
		}
		
		function validation(Id, Type)
		{
			var valid = false;
			
			if(Type == "dd")
			{
				if($("#"+Id).val() != "Select" && $("#"+Id).val() != "") 
				{  
					valid = true;
				}
			}
			
			if(Type == "txt" && $("#"+Id).val() != '')
			{
				valid = true;
			}
			
			if(!valid)    
			{  
				$("#"+Id+"_error").html('Required');  
				$("#"+Id+"_error").show();	
				return false;
			}
			else
			{ 
				$("#"+Id+"_error").hide();	
				return true;
			}
		}

		function Sweetalert(Type, Title, Info)
		{
			if(Type == "success")
			{
				Swal.fire({
					  icon: 'success', 
					  title: Title ,
					  text: Info ,			 
					  timer: 200000
				});
			}
			else if(Type == "success_load_Current")
			{
				Swal.fire({
					  icon: 'success',
					  title: Title ,
					  text:  Info,
					  timer: 2000
				}).then(function (result) {
					  if (true)
					  {							  
						  location.reload();
					  }
				});		
			}
			else if(Type == "error")
			{
				Swal.fire({
					  icon: 'error',
					  title: Title ,
					  text:  Info ,		 
					  timer: 200000
				});
			}
			else if(Type == "warning")
			{
				Swal.fire({
					  icon: 'warning',
					  title: Title ,
					  text:  Info ,		 
					  timer: 200000
				});
			}
			else if(Type == "validation")
			{
				Swal.fire({
					  icon: 'warning',
					  title: Title ,
					  html: Info
				});
			}
			else if(Type == "load")
			{
				Swal.fire({
					  title: Title,
					  html:  Info,
					  timerProgressBar: true,
					  allowOutsideClick: false,
					  onBeforeOpen: () => {
					    Swal.showLoading()
					  },
					  onClose: () => {
					  }
				});	
			}	
		}
		
	 </script>
	 
	 	
	</body>
</html>