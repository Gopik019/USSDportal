<%@ include file="../../../Headers_&_Footers/HDPAY/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/HDPAY/Dashboard/CSS_&_JS2.jsp" %>   
 
<body data-background-color="${body_color}">
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/HDPAY/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/HDPAY/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
    	<div class="content">
				
				<div class="panel-header mt--3">
					<div class="page-inner py-4">
						<h4 class="page-title" align="center"><span class="datavision_head">Channels Monitoring</span></h4>
					</div>
				</div>
				
				<div class="page-inner mt--5">
					<div class="row mt--3">
					
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">		
								
												
									<table id="source" style="display:none">
									      <caption> Channel Outward Transaction Volume </caption>
									      <thead>
									    		<tr>	
									    			<th></th>
										         	<th>IB</th>
										          	<th>MB</th>
										         	<th>H-H</th>
										         	<th>Others</th>										         	
									        	</tr>
									  		</thead>
									        <tbody>
										    	<tr>
										          <th>TIPS</th>
										          <td>250</td>
										          <td>350</td>
										          <td>120</td>
										          <td>10</td>											          
										        </tr>
										   	 <tr>
										          <th>TRA</th>
										          <td>350</td>
										          <td>250</td>
										          <td>200</td>
										          <td>25</td>
										        </tr>
										   	 <tr>
										          <th>GEPG</th>
										          <td>110</td>
										          <td>150</td>
										          <td>250</td>
										          <td>5</td>											          
										      </tr>
									  	</tbody>
									 </table>
																		
									<div class="fluid-container">	
										<div class="row">		
											<div class="col-md-12">																		
												<div class="bartb-target" id="target"></div>	
											</div>	
										</div>																									
									</div>							
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 col-sm-12">
							<div class="card">
								<div class="card-body">								
									<table id="source2" style="display:none">
									      <caption> Channel Inward Transaction Volume </caption>
									      <thead>
									    		<tr>	
									    			<th></th>
										         	<th>IB</th>
										          	<th>MB</th>
										         	<th>H-H</th>
										         	<th>Others</th>										         	
									        	</tr>
									  		</thead>
									        <tbody>
										    	<tr>
										          <th>TIPS</th>
										          <td>250</td>
										          <td>350</td>
										          <td>120</td>
										          <td>10</td>											          
										        </tr>
										   	 <tr>
										          <th>TRA</th>
										          <td>350</td>
										          <td>250</td>
										          <td>200</td>
										          <td>25</td>
										        </tr>
										   	 <tr>
										          <th>GEPG</th>
										          <td>110</td>
										          <td>150</td>
										          <td>250</td>
										          <td>5</td>											          
										      </tr>
									  	</tbody>
									 </table>
									
									<div class="fluid-container">																					
										<div class="bartb-target" id="target2"></div>																										
									</div>
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 mt--3">
							<div class="card">
								<div class="card-body">								
									<table id="source3" style="display:none">
									      <caption> Channel Outward Transaction Value in TZS </caption>
									      <thead>
									    		<tr>	
									    			<th></th>
										         	<th>IB</th>
										          	<th>MB</th>
										         	<th>H-H</th>
										         	<th>Others</th>										         	
									        	</tr>
									  		</thead>
									        <tbody>
										    	<tr>
										          <th>TIPS</th>
										          <td>150000</td>
										          <td>25000</td>
										          <td>240000</td>
										          <td>12000</td>											          
										        </tr>
										   	 <tr>
										          <th>TRA</th>
										          <td>234500</td>
										          <td>234506</td>
										          <td>23434</td>
										          <td>2343</td>
										        </tr>
										   	 <tr>
										          <th>GEPG</th>
										          <td>8989</td>
										          <td>9898</td>
										          <td>19099</td>
										          <td>3434</td>											          
										      </tr>
									  	</tbody>
									 </table>
									
									<div class="fluid-container">																					
										<div class="bartb-target" id="target3"></div>																										
									</div>
								</div>						
							</div>
						</div>
						
						<div class="col-md-6 mt--3">
							<div class="card">
								<div class="card-body">								
									<table id="source4" style="display:none">
									      <caption> Channel Inward Transaction Value in TZS </caption>
									      <thead>
									    		<tr>	
									    			<th></th>
										         	<th>IB</th>
										          	<th>MB</th>
										         	<th>H-H</th>
										         	<th>Others</th>										         	
									        	</tr>
									  		</thead>
									        <tbody>
										    	<tr>
										          <th>TIPS</th>
										          <td>150000</td>
										          <td>25000</td>
										          <td>240000</td>
										          <td>12000</td>											          
										        </tr>
										   	 <tr>
										          <th>TRA</th>
										          <td>234500</td>
										          <td>234506</td>
										          <td>23434</td>
										          <td>2343</td>
										        </tr>
										   	 <tr>
										          <th>GEPG</th>
										          <td>8989</td>
										          <td>9898</td>
										          <td>19099</td>
										          <td>3434</td>											          
										      </tr>
									  	</tbody>
									 </table>
									
									<div class="fluid-container">																					
										<div class="bartb-target" id="target4"></div>																										
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
	
	$('#source').tableBarChart('#target', '', false);
	
	$('#source2').tableBarChart('#target2', '', false);
	
	$('#source3').tableBarChart('#target3', '', false);
	
	$('#source4').tableBarChart('#target4', '', false);
	
});

</script>
 
</body>
</html>