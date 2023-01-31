<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/Whatsapp_Banking/CSS & JS11.jsp" %>	
	<style>
	
		#hello{
			background-color:#6861ce;
			text-align:center;
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
								
							<!-- <div style="width:50%">
								<label id="hello" class="form-control">Configuration - Service Mapping Account Level</label>
							</div> -->
							<br><br>
								<div class ="row">
									<div class="col-md-2"></div>
									<div class="col-md-2">
										<div class="form-group">	
											<label for="">Account No</label>
										</div>
									</div>
																
									<div class="col-md-4">		
										<input type="text" class="form-control" id="ac_no" placeholder="">
									</div>
									<div class="col-md-4">
										<button id="" class="btn btn-secondary">Get Details</button>	
									</div>
								</div>
							<!-- <div style="width:30%">
								<label id="hello" class="form-control">Services Enable / Disable List</label>
							</div> -->
							<hr>
							<div class="row">
								<div class="col-md-2"></div>
								<div class="col-md-8">
									<div class="table-responsive sm-table">
				              
									<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
										<thead>
											<tr>
												<th scope="col">S.No</th>
												<th scope="col">Service Code</th>
												<th scope="col">Service Name</th>
												<th scope="col">Enabled / Disabled</th>														
											</tr>
										</thead>
										<tbody>	
											<tr>												
												<td>1</td>
												<td>001</td>
												<td>Debit Transaction Notification</td>
												<td><input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"></td>
											</tr>
											<tr>												
												<td>2</td>
												<td>002</td>
												<td>Credit Transaction Notification</td>
												<td><input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"></td>
											</tr>
											<tr>												
												<td>3</td>
												<td>002</td>
												<td>Credit Transaction Notification</td>
												<td><input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"></td>
											</tr>
											<tr>												
												<td>4</td>
												<td>002</td>
												<td>Credit Transaction Notification</td>
												<td><input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"></td>
											</tr>
											<tr>												
												<td>5</td>
												<td>002</td>
												<td>Credit Transaction Notification</td>
												<td><input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"></td>
											</tr>
										</tbody>
									</table>
									</div>
								</div>
								<div class="col-md-2"></div>
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