<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Databse_Monitoring/CSS_&_JS.jsp" %>   
 
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
						<h4 class="page-title" align="center"><span class="datavision_head">Database Monitoring</span></h4>
					</div>
				</div>
				<div class="page-inner mt--5">
				
					<div class="row mt--2">
						
						<div class="col-md-7">
							<div class="card full-height">
								
								<div class="card-body">
									<div class="chart-container">
										<canvas id="multipleLineChart"></canvas>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-5">
							<div class="card full-height">
								<div class="card-body">
								<div class="card-title">SQL Wait States</div>
									<div class="chart-container">
										<canvas id="doughnutChart" style="width: 50%; height: 50%"></canvas>
									</div>
								</div>
							</div>
						</div>		
					</div>
					
					<div class="row mt--2">
						<div class="col-md-7">
							<div class="card full-height">
								<div class="card-body">
									<div class="chart-container">
										<canvas id="lineChart"></canvas>
									</div>
								</div>
							</div>
						</div>									
					</div>
					
					<div class="row">
						<div class="col-md-12">
						
						<div class="card">
						
						<div class="card-body">
						<div class="card-title mb-2">SQL Executed By </div>
							<table id="sql_table" class="table table table-bordered table-head-bg-primary mt-4">
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
 
	<script>

	var serbasePath="<%=basePath%>" ;

	$(document).ready(function() {
		
		get_Dashboard();
		
	});

	function get_Dashboard()
	{	
		var data = new FormData();
		
	   	data.append('MODULEID'  , "DBMON");
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
					/*############# multiLine chart ############# */

					var multipleLine = data.Sections_details[0].Charts[0];
					
					var myMultipleBarChart = new Chart(multipleLine.id , multipleLine);
					
					/*############# Doughnut chart ############# */
					
					 var doughnut_details = data.Sections_details[1].Charts[0];
					
					var myMultipleBarChart = new Chart(doughnut_details.id , doughnut_details);  
					
					/*############# Line chart ############# */
					
					 var line_details = data.Sections_details[2].Charts[0];
					
					 var LineChart = new Chart(line_details.id, line_details);		 
					
					/*############# Table #############*/
					
					var table = data.Sections_details[3].Charts[0];
					
					var table_id = table.id;
					
					var headers = []; var content = [];
					
					for(var i=0; i<table.Headers.length;i++)  
					{
						$("#"+table_id+" > thead tr").append("<th scope=\"col\">"+Space_Builder(table.Headers[i])+"</th>");
					}

					for(var i=0; i<table.Machine.length;i++)
					{
						var info = "<tr>"+
										"<td>"+table.Machine[i]+"</td>"+
										"<td>"+table.Execution[i]+"</td>"+
										"<td>"+table.Elapsed_Time[i]+"</td>"+
										"<td>"+table.Weighted[i]+"</td>"+	
									"</tr>"; 
									
						content.push(info);					 
					}
					
					$("#"+table_id+" tbody").empty(); 
					
					$("#"+table_id+" tbody").append(content);	 
					
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
	
/*	var myMultipleLineChart = new Chart(multipleLineChart, {
		type: 'line',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets: [{
				label: "ElapsedTime",       //SECATTR2   ElapsedTime|Executions|CPU Time
				borderColor: "#1d7af3",		//SECATTR4	 #1d7af3|#59d05d|#f3545d
				pointBorderColor: "#FFF",	//SECATTR5		#FFF|#FFF|#FFF
				pointBackgroundColor: "#1d7af3",	//SECATTR6		#1d7af3|#59d05d|#f3545d
				pointBorderWidth: 2,			//SECATTR7		2|2|2
				pointHoverRadius: 4,			//SECATTR8		4|4|4
				pointHoverBorderWidth: 1,		//SECATTR9		1|1|1
				pointRadius: 4,					//SECATTR10		4|4|4
				backgroundColor: 'transparent',	//SECATTR11		transparent|transparent|transparent
				fill: true,						//SECATTR12		true|true|true
				borderWidth: 2,					//SECATTR13		2|2|2
				data: [30, 45, 45, 68, 69, 90, 100, 158, 177, 200, 245, 256]	//SECATTR14
			},{
				label: "Executions",
				borderColor: "#59d05d",
				pointBorderColor: "#FFF",
				pointBackgroundColor: "#59d05d",
				pointBorderWidth: 2,
				pointHoverRadius: 4,
				pointHoverBorderWidth: 1,
				pointRadius: 4,
				backgroundColor: 'transparent',
				fill: true,
				borderWidth: 2,
				data: [10, 20, 55, 75, 80, 48, 59, 55, 23, 107, 60, 87]
			}, {
				label: "CPU Time",
				borderColor: "#f3545d",
				pointBorderColor: "#FFF",
				pointBackgroundColor: "#f3545d",
				pointBorderWidth: 2,
				pointHoverRadius: 4,
				pointHoverBorderWidth: 1,
				pointRadius: 4,
				backgroundColor: 'transparent',
				fill: true,
				borderWidth: 2,
				data: [10, 30, 58, 79, 90, 105, 117, 160, 185, 210, 185, 194]
			}]
		},
		options : {
			responsive: true, 					//SECATTR15
			maintainAspectRatio: false,			//SECATTR16
			legend: {
				position: 'top',				//SECATTR17
			},
			tooltips: {
				bodySpacing: 4,					//SECATTR20
				mode:"nearest",					//SECATTR21
				intersect: 0,					//SECATTR22
				position:"nearest",				//SECATTR23
				xPadding:10,					//SECATTR24
				yPadding:10,					//SECATTR25
				caretPadding:10					//SECATTR26
			},
			layout:{
				padding:{left:15,right:15,top:15,bottom:15} //SECATTR27-30
			}
		}
	});
	
	

	var myDoughnutChart = new Chart(doughnutChart, {
		type: 'doughnut',
		data: {
			labels: ['CPU Utlization','CPU Idle','CPU Running'],
			datasets: [{
				data: [10, 20, 30],
				backgroundColor: ['#f3545d','#fdaf4b','#1d7af3']
			}]	
		},
		options: {
			responsive: true, 
			maintainAspectRatio: false,
			legend : {
				position: 'bottom'
			},
			layout: {
				padding: {
					left: 20,
					right: 20,
					top: 20,
					bottom: 20
				}
			}
		}
	});
	
	var myLineChart = new Chart(lineChart, {
		type: 'line',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets: [{
				label: "Buffer Gets",
				borderColor: "#1d7af3",
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
			},
			{
				label: "Physical Read",
				borderColor: "#ffc107",
				pointBorderColor: "#FFF",
				pointBackgroundColor: "#59d05d",
				pointBorderWidth: 2,
				pointHoverRadius: 4,
				pointHoverBorderWidth: 1,
				pointRadius: 4,
				backgroundColor: 'transparent',
				fill: true,
				borderWidth: 2,
				data: [10, 20, 55, 75, 80, 48, 59, 55, 23, 107, 60, 87]
			} ]
		},
		options : {
			responsive: true, 
			maintainAspectRatio: false,
			legend: {
				position: 'bottom',
				labels : {
					padding: 10,
					fontColor: '#1d7af3',
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
	}); */
	
	</script>
</body>
</html>