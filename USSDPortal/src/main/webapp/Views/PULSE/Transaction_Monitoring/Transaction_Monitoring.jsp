<%@ include file="../../../Headers_&_Footers/PULSE/common/Header.jsp" %>

<%@ include file="../../../Headers_&_Footers/PULSE/Transaction_Monitoring/CSS_&_JS.jsp" %>   
 
 <style>
 
 .switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

/* Hide default HTML checkbox */
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #e43636;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #59d05d;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
.chartjs-render-monitor{
    height: 200px !important;	
}

.card-body table{
	width:100%;
}
 </style>
 
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
						<h4 class="page-title" align="center"><span class="datavision_head">Transaction Monitoring</span></h4>
					</div>
				</div>
				<div class="page-inner mt--5">
					<div class="row mt--2">
						<div class="col-md-6">
							<div class="card full-height">
									
								<div class="card-body">
									<div class="card-title" align="center">
										<h3><b>Transaction volume by Status</b></h3>
									</div>
									
									<div class="d-flex flex-wrap justify-content-around pb-4 pt-4 mt-5">
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-1"></div>
											<h6 id="circles-1-desc" class="fw-bold mt-3 mb-0"></h6>
										</div>
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-2"></div>
											<h6 id="circles-2-desc" class="fw-bold mt-3 mb-0"></h6>
										</div>
										<div class="px-2 pb-2 pb-md-0 text-center">
											<div id="circles-3"></div>
											<h6 id="circles-3-desc" class="fw-bold mt-3 mb-0"></h6>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="card">
								
								<div class="card-body">
								<div class="card-title" align="center"><h3><b>Transaction Volume by Month</b></h3></div>
									<div class="chart-container">
										<canvas id="multipleBarChart"></canvas>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-12">
						<div class="card">
								
								<div class="card-body">
									<table id="transfer_tb" class="table table table-bordered table-head-bg-primary mt-4">
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
	
   	data.append('MODULEID'  , "TRANMON");
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
				/*############# circle ############# */

				var circle = data.Sections_details[0].Charts;
				
				for(var i=0; i<circle.length;i++)
				{
					Circles.create(circle[i]);
					
					$("#"+circle[i].id+"-desc").html(circle[i].Description);				
				}	 
				
				/*############# Bar ############# */
				
				var bar = data.Sections_details[1].Charts[0];
				
				var myMultipleBarChart = new Chart(bar.id , bar ); 
				
				/*############# Table #############*/
				
				var table = data.Sections_details[2].Charts[0];
				
				var table_id = table.id;
				
				var headers = []; var content = [];
				
				for(var i=0; i<table.Headers.length;i++)
				{
					$("#"+table_id+" > thead tr").append("<th scope=\"col\">"+Space_Builder(table.Headers[i])+"</th>");
				}

				for(var i=0; i<table.Payment_Type.length;i++)
				{
					var Payment_Type = table.Payment_Type[i];
					var Initial_Process = table.Initiation_Process[i] === "1" ? "btn btn-icon btn-round btn-success" : "btn btn-icon btn-round btn-danger";		
					var Verification_Process = table.Verification_Process[i] === "1" ? "btn btn-icon btn-round btn-success" : "btn btn-icon btn-round btn-danger";
					var Authorization_Process = table.Authorization_Process[i] === "1" ? "btn btn-icon btn-round btn-success" : "btn btn-icon btn-round btn-danger";
					var Transit_to_payment_hub = table.Transit_to_Payment_Hub[i] === "1" ? "btn btn-icon btn-round btn-success" : "btn btn-icon btn-round btn-danger";
					var Response_from_payment_hub = table.Response_from_Payment_Hub[i] === "1" ? "btn btn-icon btn-round btn-success" : "btn btn-icon btn-round btn-danger";
					var Status = table.Status[i] === "1" ? "checked disabled" : "unchecked disabled";
					
					var info = "<tr>"+
									"<td>"+Payment_Type+"</td>"+
									"<td>"+
										"<div align=\"center\">"+
											"<button type=\"button\" class=\""+Initial_Process+"\">"+
												"<i class=\"fas fa-check\"></i>"+
											"</button>"+
										"</div>"+
									"</td>"+
									"<td>"+
										"<div align=\"center\">"+
											"<button type=\"button\" class=\""+Verification_Process+"\">"+
												"<i class=\"far fa-id-card\"></i>"+
											"</button>"+
										"</div>"+
									"</td>"+
									"<td>"+
										"<div align=\"center\">"+
											"<button type=\"button\" class=\""+Authorization_Process+"\">"+
												"<i class=\"fas fa-fingerprint\"></i>"+
											"</button>"+
										"</div>"+
									"</td>"+
									"<td>"+
										"<div align=\"center\">"+
											"<button type=\"button\" class=\""+Transit_to_payment_hub+"\">"+
												"<i class=\"flaticon-analytics\"></i>"+
											"</button>"+
										"</div>"+
									"</td>"+
									"<td>"+
										"<div align=\"center\">"+
											"<button type=\"button\" class=\""+Response_from_payment_hub+"\">"+
												"<i class=\"fas fa-reply\"></i>"+
											"</button>"+
										"</div>"+
									"</td>"+
									"<td>"+
										"<label class=\"switch\">"+
										   "<input type=\"checkbox\" "+Status+" >"+
										   "<span class=\"slider round\"></span>"+
										"</label>"+
									"</td>"+
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
	
/*Circles.create({
	id:'circles-1',
	radius:45,
	value:60,
	maxValue:100,
	width:7,
	text: 5,
	colors:['#f1f1f1', '#FF9E27'],
	duration:400,
	wrpClass:'circles-wrp',
	textClass:'circles-text',
	styleWrapper:true,
	styleText:true
})

Circles.create({
	id:'circles-2',
	radius:45,
	value:70,
	maxValue:100,
	width:7,
	text: 36,
	colors:['#f1f1f1', '#2BB930'],
	duration:400,
	wrpClass:'circles-wrp',
	textClass:'circles-text',
	styleWrapper:true,
	styleText:true
})

Circles.create({
	id:'circles-3',
	radius:45,
	value:40,
	maxValue:100,
	width:7,
	text: 12,
	colors:['#f1f1f1', '#F25961'],
	duration:400,
	wrpClass:'circles-wrp',
	textClass:'circles-text',
	styleWrapper:true,
	styleText:true
}) 

	var myMultipleBarChart = new Chart(multipleBarChart, {
		type: 'bar',
		data: {
			labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			datasets : [{
				label: "Credits",
				backgroundColor: '#59d05d',
				borderColor: '#59d05d',
				data: [95, 100, 112, 101, 144, 159, 178, 156, 188, 190, 210, 245],
			},
			{
				label: "Debits",
				backgroundColor: '#fdaf4b',
				borderColor: '#fdaf4b',
				data: [145, 256, 244, 233, 210, 279, 287, 253, 287, 299, 312,356],
			}, 
			{
				label: "Returned",
				backgroundColor: '#177dff',
				borderColor: '#177dff',
				data: [185, 279, 273, 287, 234, 312, 322, 286, 301, 320, 346, 399],
			}],
		},
		options: {
			responsive: true, 
			maintainAspectRatio: false,
			legend: {
				position : 'bottom'
			},
			title: {
				display: true,
				text: 'Traffic Stats'
			},
			tooltips: {
				mode: 'index',
				intersect: false
			},
			scales: {
				xAxes: [{
					stacked: true,
				}],
				yAxes: [{
					stacked: true
				}]
			}
		}
	});
*/

</script>
	
</body>
</html>