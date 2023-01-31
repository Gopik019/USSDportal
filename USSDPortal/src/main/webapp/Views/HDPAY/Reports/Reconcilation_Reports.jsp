<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Reports/CSS_&_JS3.jsp" %>   
 
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
			
	        <div class="row fluid-container">
	        
				<div class="col-md-12 mt--1">
	
					<div id="colour_body" class="card">
	
						<div id="tab_card" class="card-body">
							
				              <div class="table-responsive sm-table">
				              
								<table id="example" class="table table-striped table-hover table-bordered dt-responsive nowrap" style="width:100%">
						
									<thead>
										<tr role="row">
											<th>S.No</th>
											<th>CHN CODE</th>
											<th>PAY SYSTEM</th>
											<th>TRAN TYPE</th>
											<th>RECON DATE</th>
											<th>TRAN DATE</th>
											<th>STATUS</th>
										</tr>
									</thead>
									
									 <tbody>																																						
									</tbody>
									
								</table>
							</div>
									</div></div>
								</div>
							</div>
						</div>	
					</div>
				</div>	
			</div>   
	</body>
</html>