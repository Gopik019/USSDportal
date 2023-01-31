<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS2.jsp" %>
	
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
								
		<!-- <div style="width:20%">
			<label id="hello" class="form-control">Notification Dashboard</label>
		</div> -->
		<div class="row">
			<div class="col-md-6"></div>
			<div class="col-md-1"></div>
		<%--comment <div class="col-md-4">
				<div class="ml-md-auto py-3">
				<a href='<spring:url value="Whatsapp_Day_Notification"/>' class="btn btn-md btn-secondary">Today</a>
			 		<a href='<spring:url value="Whatsapp_Week_Notification"/>' class="btn btn-md btn-secondary">Week</a>
					<a href='<spring:url value="Whatsapp_Month_Notification"/>' class="btn btn-md btn-secondary">Month</a>
					<a href='<spring:url value="Whatsapp_Customer"/>' class="btn btn-md btn-secondary">Customer</a>
				</div> 
			</div>
		</div>
			<div class="ml-md-auto py-3">
				<button class="btn btn-md btn-secondary" id="all">All</button>
				<button class="btn btn-md btn-secondary" id="intersection">Interaction Session</button>
				<button class="btn btn-md btn-secondary" id="image">Image Notification</button>
				<button class="btn btn-md btn-secondary" id="text">Text Notification</button>
			</div>
		
		<div class="btn-group">
			<button class="btn btn-md btn-secondary" id="g1">Graph</button>
			<button type="button" class="btn btn-md btn-secondary" id="d1" >Data</button>
		</div>
		comment --%>
		<div class="container pb-5">
			<div class="row mt-5" id="graph">
				<h3 style="padding-left: 35%">Whatsapp Day Notification</h3><br>
					<div class="col-md-12">
						<canvas id="sales-chart6" height="300"></canvas>
					</div>
				</div>
	
	<!-- Modal  
		<div class="modal fade" id="myModal" role="dialog">
   		 	<div class="modal-dialog modal-lg">
    
	      <!-- Modal content
	      	<div class="modal-content">
	        	<div class="modal-header">
	        		<h4 class="modal-title">Week Notification Data</h4>
	          		<button type="button" class="close" data-dismiss="modal">&times;</button>
	       		 </div>
	        	<div class="modal-body">-->
	          		<!-- <div class="row" id="table">
								<div class="col-md-12">
									<div class="table-responsive sm-table">
				              
									<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										<thead>
											<tr>
												<th scope="col">S.No</th>
												<th scope="col">Channel</th>
												<th scope="col">Notification Type</th>
												<th scope="col">Service</th>
												<th scope="col">Mobile No</th>
												<th scope="col">Message</th>
												<th scope="col">Status</th>
												<th scope="col">Date</th>
												<th scope="col">Interaction Count</th>														
											</tr>
										</thead>
										<tbody>	
											<tr>												
												<td>1</td>
												<td>IB</td>
												<td>Text</td>
												<td>001</td>
												<td>22113478509</td>
												<td>Welcome</td>
												<td>OK</td>
												<td>15-07-2022</td>
												<td>378</td>
											</tr>
											<tr>												
												<td>2</td>
												<td>IB</td>
												<td>Text</td>
												<td>001</td>
												<td>22113478509</td>
												<td>Welcome</td>
												<td>OK</td>
												<td>15-07-2022</td>
												<td>174</td>
											</tr>
											<tr>												
												<td>3</td>
												<td>IB</td>
												<td>Text</td>
												<td>001</td>
												<td>22113478509</td>
												<td>Welocme</td>
												<td>OK</td>
												<td>15-07-2022</td>
												<td>233</td>
											</tr>
										</tbody>
									</table>
									</div>
								</div>
							</div> -->
	        	<!-- </div>
	        	<div class="modal-footer">
	          		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        	</div>
	      	</div>
	      
	    	</div>-->
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
		$(document).ready(function(){
			
			Channel_Monitoring();
			
			//Interaction_session_count();       //get the interaction count
	});
		
		
		function Channel_Monitoring()
			{	
			var data = new FormData();
			
			//var Res = values.split("|");
		    
			$.ajax({		 
				url  :  $("#ContextPath").val()+"/Whatsapp_Day_Notification",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{   
					//Swal.close();
					
					if(data.result == "Success")
					{
						//$("#Channel_Code").val(data.Interaction_Session);	
						
						//alert(data.Interaction_Session);
						
						realtime_data(data.Interaction_Session, 0, 0);
						
						//alert("hi! Interaction data found!!");

					}
					else
				    {
						//Sweetalert("warning", "", data.Message);
				    }	
				},
				beforeSend: function( xhr )
		 		{
					
		 			//Sweetalert("load", "", "Please Wait");
		        },
			    error: function (jqXHR, textStatus, errorThrown) 
				{ 
			    	//Sweetalert("warning", "", "errrrr");  
			    }
		   });
			
			}	
			
		function realtime_data(val1, val2, val3)
		{

			var chart_object = {
				type: 'bar',
				data: {
				  labels: ['Today'],
				  datasets: [
					{
					  label: "Interaction Session",
					  backgroundColor: '#fd7e14',  
					  borderColor: '#007bff',
					  data: [val1]
					},
					{
					  label: "Image Notification",
					  backgroundColor: '#007bff', 
					  borderColor: '#ced4da',
					  data: [val2]
					},
					{
					  label: "Text Notification",
					  backgroundColor: '#ced4da',
					  borderColor: '#007bff',
					  data: [val3]
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
		
			
			
			
			
			
		
		
		
		
		
		
		function Interaction_session_count()
		{
			var data = new FormData();
			
			//var Res = values.split("|");
		    
			$.ajax({		 
				url  :  $("#ContextPath").val()+"/HDPAY/Whatsapp_Day_Notification",
				type :  'POST',
				data :  data,
				cache : false,
				contentType : false,
				processData : false,
				success: function (data) 
				{   
					Swal.close();
					
					if(data.Result == "Success")
					{
						$("#Channel_Code").val(data.Interaction_Session);		
						
					}
					else
				    {
						//Sweetalert("warning", "", data.Message);
				    }	
				},
				beforeSend: function( xhr )
		 		{
					
		 			//Sweetalert("load", "", "Please Wait");
		        },
			    error: function (jqXHR, textStatus, errorThrown) 
				{ 
			    	//Sweetalert("warning", "", "errrrr");  
			    }
		   });
		}
		
		
		
		
		
	</script>
						
	</body>
	
</html>