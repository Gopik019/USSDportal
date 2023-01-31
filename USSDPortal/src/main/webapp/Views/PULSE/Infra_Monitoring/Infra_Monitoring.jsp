<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Infra_Monitoring/CSS_&_JS.jsp" %>  
 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/PULSE/";
	
	response.setHeader("Pragma", "no-cache"); 
	response.setHeader("Cache-Control", "no-store"); 
	response.setDateHeader("Expires", 0);
%>

<body data-background-color="${body_color}">
	<div class="wrapper sidebar_minimize">
	
		<div class="main-header">
		
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Logo_Header.jsp" %>       
		     
		     <%@ include file="../../../Headers_&_Footers/PULSE/common/Navigation_Bar.jsp" %>     
	</div>
	
	<%@ include file="../../../Headers_&_Footers/PULSE/common/Side_Bar.jsp" %>   
	
    <div class="main-panel">
    	<div class="content">
				<div class="panel-header">
					<div class="page-inner py-4">
						<h4 class="page-title" align="center"><span class="datavision_head">Infra Monitoring</span></h4>
					</div>
				</div>
				<div class="page-inner mt--5">
					<div class="row mt--2">
						
						<div class="col-md-4">
							<div class="card full-height">
								<div class="card-header">
									<div class="card-title" align="center">Heat Map</div>
								</div>
								<div class="card-body">
									<div class="chart-container"><div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;"><div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
										<canvas id="radarChart" width="740" height="300" class="chartjs-render-monitor" style="display: block; width: 740px; height: 300px;"></canvas>
									</div>
								</div>
							</div>
						</div>
												
						<div class="col-md-4">
							<div class="card full-height">
								<div class="card-header">
									<div class="card-title" align="center">Real-time CPU Utilization</div>
								</div>
								<div class="card-body">
									<div class="chart-container"><div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;"><div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
										<canvas id="lineChart" width="740" height="300" class="chartjs-render-monitor" style="display: block; width: 740px; height: 300px;"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-4">
							<div class="card full-height">
								<div class="card-header">
									<div class="card-title" align="center">Real-time Memory Utilization</div>
								</div>
								<div class="card-body">
									<div class="chart-container"><div class="chartjs-size-monitor" style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;"><div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
										<canvas id="lineChart2" width="740" height="300" class="chartjs-render-monitor" style="display: block; width: 740px; height: 300px;"></canvas>
									</div>
								</div>
							</div>
						</div>
						
					</div>	
						
					<div class="row">

						<div class="col-md-4">
							<div class="card">
									<div class="card-header">
										<div class="card-title" align="center">Devices by Memory Utilization</div>
									</div>
									
								<div class="card-body">
									<div class="table-responsive">
										<table id="memory_util" class="table">
										<thead>
											<tr>
											</tr>
										</thead>
											<tbody>
												
												
											</tbody>
											</table>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-4">
							<div class="card">
									<div class="card-header">
										<div class="card-title" align="center">Devices by CPU Utilization</div>
									</div>
									
								<div class="card-body">
									<div class="table-responsive">
									<table id="cpu_util" class="table">
									<thead>
										<tr>
											
										</tr>
									</thead>
										<tbody>
											
										</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-4">
						
						<div class="card">
									<div class="card-header">
										<div class="card-title" align="center">Devices by Memory Utilization</div>
									</div>
									
								<div class="card-body">
									<div class="table-responsive">
									<table id="memory_util2" class="table">
									<thead>
										<tr>
												
										</tr>
									</thead>
										<tbody>
											
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

	<script>
	
	var serbasePath="<%=basePath%>" ;

	$(document).ready(function() {
		
		get_Dashboard();
	});

	function get_Dashboard()
	{	
		var data = new FormData();
		
	   	data.append('MODULEID'  , "INFMON");
	  	data.append('PROGRAMID' , "Dashboard");
	  	data.append('SYSCODE'   , "PULSE");
	  	
	   	$.ajax({		 
			url  :  serbasePath + "/Dashboard",
			type :  'POST',
			data :  data,
			cache : false,
			contentType: false,
			processData : false,
			success: function (data) 
			{ 
				if(data.Result == "Success" )
				{
					/*############# Radar chart ############# */ 
					
					var radar_details = data.Sections_details[0].Charts[0];
					
					var Radar_Chart = new Chart(radar_details.id , radar_details );	
			
					/*############# Line chart 1 ############# */
					
					var line_details = data.Sections_details[1].Charts[0];
					
					var LineChart = new Chart(line_details.id , line_details );	
									
					/*############# Line chart 2 ############# */
					
					var line_details_2 = data.Sections_details[2].Charts[0];
					
					var LineChart_2 = new Chart(line_details_2.id , line_details_2 );	
					
					/*############# Tables for Memory & CPU util #############*/
					
					for(var tb=3; tb < 6; tb++) 
					{
						var table = data.Sections_details[tb].Charts[0];
						
						var table_id = table.id;
						
						var headers = []; var content = [];
						
						for(var i=0; i<table.Headers.length;i++)  
						{
							var header = table.Headers[i] == "_DOTTED_COLUMN_" ? "..." : Space_Builder(table.Headers[i]);
							
							$("#"+table_id+" > thead tr").append("<th scope=\"col\">"+header+"</th>");
						}
						
						if(tb == 5)
						{
							for(var i=0; i<table.Device_Name.length;i++)
							{
								var info = "<tr>"+
												"<td>"+table.Device_Name[i]+"</td>"+
												"<td>"+
													"<div class=\"progress progress-sm\">"+
														"<div class=\"progress-bar bg-success w-"+Number(table.Availability[i])+"\" role=\"progressbar\" aria-valuenow=\""+Number(table.Availability[i])+"\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div>"+
													"</div>"+
													
												"</td>"+
												"<td>"+
													"<div class=\"d-flex justify-content-between mt-2\">"+
														"<p class=\"text-muted mb-0\"></p>"+
														"<p class=\"text-muted mb-0\">"+Number(table.Availability[i])+"%</p>"+
													"</div>"+
												"</td>"+
											"</tr>"; 
		
								content.push(info);					 
							}
						}
						else
						{
							for(var i=0; i<table.Device_Name.length;i++)
							{
								var info = "<tr>"+
												"<td>"+table.Device_Name[i]+"</td>"+
												"<td>"+table.Min[i]+"</td>"+
												"<td>"+table._DOTTED_COLUMN_[i]+"</td>"+
												"<td>"+
													"<div class=\"progress progress-sm\">"+
														"<div class=\"progress-success bg-info w-"+Number(table.Avg[i])+"\" role=\"progressbar\" aria-valuenow=\""+Number(table.Avg[i])+"\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div>"+
													"</div>"+
												"</td>"+
												"<td>"+table.Avg[i]+"</td>"+												
											"</tr>"; 
													
								content.push(info);					 
							}
						}
						
						$("#"+table_id+" tbody").empty(); 
						
						$("#"+table_id+" tbody").append(content);	
					}
														
					$(".card").show();
				}
			},
			beforeSend: function( xhr )
			{
				$(".card").hide();
	        },
		    error: function (jqXHR, textStatus, errorThrown) 
		    { 
		    	return false; 
		    }
	   }); 
	}
	
	function Space_Builder(val)
	{
		var val = val.replace(/_/g, ' ');
		
		return val;
	}
	
	/*var myRadarChart = new Chart(radarChart, {
		type: 'radar',
		data: {
			labels: ['', '', '', '', ''],
			datasets: [{
				data: [20, 10, 30, 2, 30],
				borderColor: '#1d7af3',
				backgroundColor : 'rgba(29, 122, 243, 0.25)',
				pointBackgroundColor: "#1d7af3",
				pointHoverRadius: 4,
				pointRadius: 3,
				label: 'Clear'
			}, 
			{
				data: [10, 20, 15, 30, 22],
				borderColor: '#716aca',
				backgroundColor: 'rgba(113, 106, 202, 0.25)',
				pointBackgroundColor: "#716aca",
				pointHoverRadius: 4,
				pointRadius: 3,
				label: 'Critical'
			},
		    {
				data: [10, 20, 15, 30, 22],
				borderColor: '#716aca',
				backgroundColor: 'rgba(113, 106, 202, 0.25)',
				pointBackgroundColor: "#716aca",
				pointHoverRadius: 4,
				pointRadius: 3,
				label: 'Trouble'
			}
			]
		},
		options : {
			responsive: true, 
			maintainAspectRatio: false,
			legend : {
				position: 'bottom'
			}
		}
	});
	
	var myLineChart = new Chart(lineChart, {
		type: 'line',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets: [{
				label: "CPU Min:40 Max:90 Avg:51",
				borderColor: "#17a2b8",
				pointBorderColor: "#FFF",
				pointBackgroundColor: "#1d7af3",
				pointBorderWidth: 2,
				pointHoverRadius: 4,
				pointHoverBorderWidth: 1,
				pointRadius: 4,
				backgroundColor: 'transparent',
				fill: true,
				borderWidth: 2,
				data: [542, 480, 430, 550, 530, 453, 380, 434, 568, 610, 700, 900]
			}]
		},
		options : {
			responsive: true, 
			maintainAspectRatio: false,
			legend: {
				position: 'bottom',
				labels : {
					padding: 10,
					fontColor: "#17a2b8",
				}
			},
			tooltips: {
				bodySpacing: 4,
				mode:"nearest",
				intersect: 0,
				position:"nearest",
				xPadding:10,
				yPadding:10,
				caretPadding:10
			},
			layout:{
				padding:{left:15,right:15,top:15,bottom:15}
			}
		}
	});
	
	var myLineChart = new Chart(lineChart2, {
		type: 'line',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets: [{
				label: "Memory Min:40 Max:90 Avg:51",
				borderColor: "#17a2b8",
				pointBorderColor: "#FFF",
				pointBackgroundColor: "#1d7af3",
				pointBorderWidth: 2,
				pointHoverRadius: 4,
				pointHoverBorderWidth: 1,
				pointRadius: 4,
				backgroundColor: 'transparent',
				fill: true,
				borderWidth: 2,
				data: [700, 200, 620, 200, 300, 100, 380, 434, 300, 200, 800, 500]
			}]
		},
		options : {
			responsive: true, 
			maintainAspectRatio: false,
			legend: {
				position: 'bottom',
				labels : {
					padding: 10,
					fontColor: "#17a2b8",
				}
			},
			tooltips: {
				bodySpacing: 4,
				mode:"nearest",
				intersect: 0,
				position:"nearest",
				xPadding:10,
				yPadding:10,
				caretPadding:10
			},
			layout:{
				padding:{left:15,right:15,top:15,bottom:15}
			}
		}
	});
	*/
	</script>
</body>
</html>