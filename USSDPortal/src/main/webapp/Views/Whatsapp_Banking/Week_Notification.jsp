<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS3.jsp" %>	
	<style>
		
		.btn-group button{
			background-color: #6861ce;
			border: 1px solid;
			border-color: #6861ce;
			color: white;
			padding: 10px 15px;
			cursor: pointer;
			float:left;
			size: 100px;
		}
		.btn-group:after{
			clear:both;
		}
		.btn-group button:not(:last-child){
			border-right: none;
		}
		.btn-group button:hover{
			background-color:white;
			color:black;
		}
		
		#hello{
			background-color:#6861ce;
			text-align:center;
			color:white;
		}
		#hello1{
			background-color:#6861ce;
			text-align:left;
			color:white;
		}
		table, th,td{
			border: 2px solid white;
			border-collapse: collapse;
			padding: 10px 15px;
		}
	</style>   
 
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
								
		<div class="container">
		<!-- <div style="width:20%">
			<label id="hello" class="form-control">Notification Dashboard</label>
		</div> -->
		<div class="row">
			<div class="col-md-6"></div>
			<div class="col-md-1"></div>
			<div class="col-md-4">
				<%-- <div class="ml-md-auto py-3">
					<a href='<spring:url value="Whatsapp_Day_Notification" />' class="btn btn-md btn-secondary">Today</a>
					<a href='<spring:url value="Whatsapp_Week_Notification" />' class="btn btn-md btn-secondary">Week</a>
					<a href='<spring:url value="Whatsapp_Month_Notification" />' class="btn btn-md btn-secondary">Month</a>
					<a href='<spring:url value="Whatsapp_Customer" />' class="btn btn-md btn-secondary">Customer</a>
				</div> --%>
			</div>
		</div>
		<br>
			<!-- <div class="ml-md-auto py-3">
				<button class="btn btn-md btn-secondary" id="all">All</button>
				<button class="btn btn-md btn-secondary" id="intersection">Interaction Session</button>
				<button class="btn btn-md btn-secondary" id="image">Image Notification</button>
				<button class="btn btn-md btn-secondary" id="text">Text Notification</button>
			</div> -->
		<!-- <div class="btn-group">
			<button class="btn btn-md btn-secondary" id="all1">Graph</button>
			<button type="button" class="btn btn-md btn-secondary" data-toggle="modal" data-target="#myModal">Data</button>
		</div> -->
		<div class="container pb-5">
			<div class="row mt-5">
				<h3 style="padding-left: 35%">Whatsapp week Notification</h3><br>
				<div class="col-12">
					<div class="col-md-12">
						<canvas id="sales-chart6" height="300"></canvas>
					</div>
				</div>
			</div>
		</div>
		
		<!-- Modal  -->
		<div class="modal fade" id="myModal" role="dialog">
   		 	<div class="modal-dialog modal-lg">
    
	      <!-- Modal content-->
	      	<div class="modal-content">
	        	<div class="modal-header">
	        		<h4 class="modal-title">Week Notification Data</h4>
	          		<button type="button" class="close" data-dismiss="modal">&times;</button>
	       		 </div>
	        	<div class="modal-body">
	          		<div class="row">
								<div class="col-md-2"></div>
								<div class="col-md-8">
									<div class="table-responsive sm-table">
				              
									<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										<thead>
											<tr>
												<th scope="col">Day</th>
												<th scope="col">Interaction Session</th>
												<th scope="col">Image Notification</th>
												<th scope="col">Text Notification</th>														
											</tr>
										</thead>
										<tbody>	
											<tr>												
												<td>Sunday</td>
												<td>100</td>
												<td>200</td>
												<td>300</td>
											</tr>
											<tr>												
												<td>Monday</td>
												<td>200</td>
												<td>600</td>
												<td>500</td>
											</tr>
											<tr>												
												<td>Tuesday</td>
												<td>300</td>
												<td>250</td>
												<td>700</td>
											</tr>
											<tr>												
												<td>Wednesday</td>
												<td>300</td>
												<td>150</td>
												<td>400</td>
											</tr>
											<tr>												
												<td>Thursday</td>
												<td>400</td>
												<td>300</td>
												<td>600</td>
											</tr>
											<tr>												
												<td>Friday</td>
												<td>500</td>
												<td>400</td>
												<td>700</td>
											</tr>
											<tr>												
												<td>Saturday</td>
												<td>600</td>
												<td>350</td>
												<td>400</td>
											</tr>
										</tbody>
									</table>
									</div>
								</div>
								<div class="col-md-2"></div>
							</div>
	        	</div>
	        	<div class="modal-footer">
	          		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        	</div>
	      	</div>
	      
	    	</div>
	  </div>
	<script>
			$(document).ready(function(){ 

					all_Monitoring();
					
				$("#all ,#all1").click(function() { 
						
					all_Monitoring();
				});
				
				$("#intersection").click(function() { 
										
					Intersection_Monitoring();
				});
									
				$("#image").click(function() { 
						
					Image_Monitoring();
				});
					
				$("#text").click(function() { 
						
					Text_Monitoring();
				});
			});
			function all_Monitoring()
				{	
					var chart_object = {
							type: 'bar',
							data: {
							  labels: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
							  datasets: [
								{
								  label: "Interaction Session",
								  backgroundColor: '#fd7e14',  
								  borderColor: '#007bff',
								  data: [100, 200, 300, 300, 400, 500, 600]
								},
								{
								  label: "Image Notification",
								  backgroundColor: '#007bff', 
								  borderColor: '#ced4da',
								  data: [200, 600, 250, 150, 300, 400, 350]
								},
								{
								  label: "Text Notification",
								  backgroundColor: '#ced4da',
								  borderColor: '#007bff',
								  data: [300, 500, 700, 400, 600, 700, 400]
								},
							  ]
							},
							options: {
							  maintainAspectRatio: false,
							  tooltips: {
								mode: 'index',
								intersect: true
							  },
							  hover: {
								mode: 'index',
								intersect: true
							  },
							  legend: {
								display: false
							  },
							  scales: {
								yAxes: [{
								  display: true,
								  gridLines: {
									display: true,
									lineWidth: '4px',
									color: 'rgba(0, 0, 0, .2)',
									zeroLineColor: 'transparent'
								  }
								}],
								xAxes: [{
								  display: true,
								  gridLines: {
									display: false
								  },
								}]
							  }
							}
						  };
					var salesChart5 = new Chart( $('#sales-chart6'), chart_object);
				}
			function Intersection_Monitoring()
			{	
				var chart_object = {
						type: 'bar',
						data: {
							  labels: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
						  datasets: [
							  {
								  label: "Interaction Session",
								  backgroundColor: '#fd7e14',  
								  borderColor: '#007bff',
								  data: [100, 200, 300, 300, 400, 500, 600]
								},
						  ]
						},
						options: {
						  maintainAspectRatio: false,
						  tooltips: {
							mode: 'index',
							intersect: true
						  },
						  hover: {
							mode: 'index',
							intersect: true
						  },
						  legend: {
							display: false
						  },
						  scales: {
							yAxes: [{
							  display: true,
							  gridLines: {
								display: true,
								lineWidth: '4px',
								color: 'rgba(0, 0, 0, .2)',
								zeroLineColor: 'transparent'
							  }
							}],
							xAxes: [{
							  display: true,
							  gridLines: {
								display: false
							  },
							}]
						  }
						}
					  };
				var salesChart5 = new Chart( $('#sales-chart6'), chart_object);
			}
			function Image_Monitoring()
			{	
				var chart_object = {
						type: 'bar',
						data: {
							  labels: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
						  datasets: [
							  {
								  label: "Image Notification",
								  backgroundColor: '#007bff', 
								  borderColor: '#ced4da',
								  data: [200, 600, 250, 150, 300, 400, 350]
								},
						  ]
						},
						options: {
						  maintainAspectRatio: false,
						  tooltips: {
							mode: 'index',
							intersect: true
						  },
						  hover: {
							mode: 'index',
							intersect: true
						  },
						  legend: {
							display: false
						  },
						  scales: {
							yAxes: [{
							  display: true,
							  gridLines: {
								display: true,
								lineWidth: '4px',
								color: 'rgba(0, 0, 0, .2)',
								zeroLineColor: 'transparent'
							  }
							}],
							xAxes: [{
							  display: true,
							  gridLines: {
								display: false
							  },
							}]
						  }
						}
					  };
				var salesChart5 = new Chart( $('#sales-chart6'), chart_object);
			}
			function Text_Monitoring()
			{	
				var chart_object = {
						type: 'bar',
						data: {
							  labels: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
						  datasets: [
							  {
								  label: "Text Notification",
								  backgroundColor: '#ced4da',
								  borderColor: '#007bff',
								  data: [300, 500, 700, 400, 600, 700, 400]
								},
						  ]
						},
						options: {
						  maintainAspectRatio: false,
						  tooltips: {
							mode: 'index',
							intersect: true
						  },
						  hover: {
							mode: 'index',
							intersect: true
						  },
						  legend: {
							display: false
						  },
						  scales: {
							yAxes: [{
							  display: true,
							  gridLines: {
								display: true,
								lineWidth: '4px',
								color: 'rgba(0, 0, 0, .2)',
								zeroLineColor: 'transparent'
							  }
							}],
							xAxes: [{
							  display: true,
							  gridLines: {
								display: false
							  },
							}]
						  }
						}
					  };
				var salesChart5 = new Chart( $('#sales-chart6'), chart_object);
			}
						</script>
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