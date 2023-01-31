<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS.jsp" %>	
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
		<br><br>
			
		<div class="row">
			<div class="col-md-6"></div>
			<div class="col-md-1"></div>
			<div class="col-md-4">
				<div class="ml-md-auto py-3">
					<a href='<spring:url value="Whatsapp_Notification" />' class="btn btn-md btn-secondary">Today</a>
					<a href='<spring:url value="Whatsapp_Week_Notification" />' class="btn btn-md btn-secondary">Week</a>
					<a href='<spring:url value="Whatsapp_Month_Notification" />' class="btn btn-md btn-secondary">Month</a>
					<a href='<spring:url value="Whatsapp_Customer" />' class="btn btn-md btn-secondary">Customer</a>
				</div>
			</div>
		</div>
		<br>
			<div class="ml-md-auto py-3">
				<a href="#" class="btn btn-md btn-secondary">All</a>
				<a href="#" class="btn btn-md btn-secondary">Intersection Session</a>
				<a href="#" class="btn btn-md btn-secondary">Image Notification</a>
			</div>
		<br>
		<br>
		
		<div class="btn-group">
			<button>Graph</button>
			<button>Data</button>
		</div>
		<br><br>
		
		<div class="row">
		<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
			<thead>
				<tr>
					<th scope="col">Sl.No</th>
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
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
					<td> </td>
				</tr>
			</tbody>
		</table>
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
	</body>
</html>