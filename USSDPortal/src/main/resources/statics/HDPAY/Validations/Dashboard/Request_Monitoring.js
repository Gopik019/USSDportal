$(document).ready(function() {
	
	Request_Monotoring();
	
	New_Dashboard();
	
	New_Dashboard2();

});


/*
//MAP
function New_Dashboard2()
{
	var data = [
	    ['ug', 0],
	    ['ng', 1],
	    ['st', 2],
	    ['tz', 3],
	    ['sl', 4],
	    ['gw', 5],
	    ['cv', 6],
	    ['sc', 7],
	    ['tn', 8],
	    ['mg', 9],
	    ['ke', 10],
	    ['cd', 11],
	    ['fr', 12],
	    ['mr', 13],
	    ['dz', 14],
	    ['er', 15],
	    ['gq', 16],
	    ['mu', 17],
	    ['sn', 18],
	    ['km', 19],
	    ['et', 20],
	    ['ci', 21],
	    ['gh', 22],
	    ['zm', 23],
	    ['na', 24],
	    ['rw', 25],
	    ['sx', 26],
	    ['so', 27],
	    ['cm', 28],
	    ['cg', 29],
	    ['eh', 30],
	    ['bj', 31],
	    ['bf', 32],
	    ['tg', 33],
	    ['ne', 34],
	    ['ly', 35],
	    ['lr', 36],
	    ['mw', 37],
	    ['gm', 38],
	    ['td', 39],
	    ['ga', 40],
	    ['dj', 41],
	    ['bi', 42],
	    ['ao', 43],
	    ['gn', 44],
	    ['zw', 45],
	    ['za', 46],
	    ['mz', 47],
	    ['sz', 48],
	    ['ml', 49],
	    ['bw', 50],
	    ['sd', 51],
	    ['ma', 52],
	    ['eg', 53],
	    ['ls', 54],
	    ['ss', 55],
	    ['cf', 56]
	];

	// Create the chart
	Highcharts.mapChart('container_two', {
	    chart: {
	        map: 'custom/africa'
	    },

	    title: {
	        text: ''
	    },

	    subtitle: {
	        text: ' '
	    },

	    mapNavigation: {
	        enabled: true,
	        buttonOptions: {
	            verticalAlign: 'bottom'
	        }
	    },

	    colorAxis: {
	        min: 0
	    },

	    series: [{
	        data: data,
	        name: 'Random data',
	        states: {
	            hover: {
	                color: '#BADA55'
	            }
	        },
	        dataLabels: {
	            enabled: true,
	            format: '{point.name}'
	        }
	    }]
	});
}
*/

function Request_Monotoring()
{
	var data = new FormData();
	
	$.ajax({		 
		url  :  $("#ContextPath").val()+"/Dashboard/Request_Monitoring",
		type :  'POST',
		data :  data,
		cache : false,
		contentType : false,
		processData : false,
		success: function (data) 
		{   
			if(data.Result === 'Success')
			{
				var Bill = data.Bill;
				
				$("#total_bill").html(Bill.TOTAL);
				$("#gepg_bill").html(Bill.GEPG);
				$("#msc_bill").html(Bill.MSC);
				$("#tigo_bill").html(Bill.TIGO);
				
				var PAYMENT = data.PAYMENT;
				
				$("#total_payment").html(PAYMENT.TOTAL);
				$("#out_gepg").html(PAYMENT.Outward_GEPG);
				$("#out_msc").html(PAYMENT.Outward_MSC);
				$("#in_tips").html(PAYMENT.Inward_TIPS);
				$("#out_tips").html(PAYMENT.Outward_TIPS);
				
				var ACCOUNT_Lookup = data.ACCOUNT;
				
				$("#total_ac").html(ACCOUNT_Lookup.TOTAL);
				$("#ac_gepg").html(ACCOUNT_Lookup.GEPG);
				$("#ac_msc").html(ACCOUNT_Lookup.MSC);
				$("#ac_tips").html(ACCOUNT_Lookup.TIPS);
				
				var Recon = data.RECON;
				
				$("#total_recon").html(Recon.TOTAL);
				$("#recon_geog").html(Recon.GEPG);
				$("#recon_msc").html(Recon.MSC);
				$("#recon_tips").html(Recon.TIPS);
				
				RECON_Channel(data.RECON_CHANNEL);
				
				RECON_Week(data.RECON_WEEK);	
			}
		},
		beforeSend: function( xhr )
		{
			
        },
	    error: function (jqXHR, textStatus, errorThrown) 
	    { 
	    	
	    }
   });
}

function RECON_Week(Info)
{
	var Days = Info.Total_Days;

	Highcharts.chart('container_one', {
	    chart: {
	        type: 'areaspline'
	    },
	    title: {
	        text: ''
	    },
	    legend: {
	       // layout: 'vertical',
	       /// align: 'top',
	       // verticalAlign: 'top',
	       // x: 250,
	       // y: 50,
	      //  floating: true,
	        borderWidth: 1,
	        backgroundColor:
	            Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF'
	    },
	    xAxis: {
	    	categories : Days,
	        plotBands: [{ // visualize the weekend
	            from: 4.5,
	            to: 6.5,
	            color: 'rgba(68, 170, 213, .2)'
	        }]
	    },
	    yAxis: {
	        title: {
	            text: 'Total No of Request'
	        }
	    },
	    tooltip: {
	        shared: true,
	        valueSuffix: ' Request'
	    },
	    credits: {
	        enabled: false
	    },
	    plotOptions: {
	        areaspline: {
	            fillOpacity: 0.5
	        }
	    },
	    series: [{
	        name: 'Matched',
	        data: Info.Matched
	    }, 
	    {
	        name: 'Not Matched',
	        data: Info.Not_Matched
	    },
	    {
	        name: 'Pending',
	        data: Info.Pending
	    }]
	});
}

function RECON_Channel(Recon)
{
	 var IB_PENDING = Recon.IB_PENDING;
	 var IB_MATCHED = Recon.IB_MATCHED;
	 var IB_NOT_MATCHED = Recon.IB_NOT_MATCHED;
	
	 var myPieChart1 = new Chart( $("#IB") , {
	    type: 'doughnut',
		data: {
				datasets: [{
					data: [ IB_MATCHED, IB_NOT_MATCHED, IB_PENDING],
					backgroundColor : ['#f3545d', '#fdaf4b', '#1d7af3'],
					borderWidth: 0
				}],
				labels: ['Matched', 'Not Matched', 'Pending'] 
			},
			options : {
				responsive: true, 
				maintainAspectRatio: false,
				legend: {
					position : 'bottom',
					labels : {
						fontColor: 'rgb(154, 154, 154)',
						fontSize: 10,
						usePointStyle : true,
						padding: 20
					}
				},
				pieceLabel: {
					render: 'value',
					fontColor: 'white',
					fontSize: 10,
				},
				tooltips: false,
				layout: {
					padding: {
						left: 20,
						right: 20,
						top: 0,
						bottom: 0
					}
				}
			}
		});
	
	 /*
	var myPieChart2 = new Chart( $("#H2H") , {
		type: 'doughnut',
		data: {
			datasets: [{
				data: [H2H_MATCHED, H2H_NOT_MATCHED, H2H_PENDING],
				backgroundColor :['#f3545d', '#fdaf4b', '#1d7af3'],   
				borderWidth: 0
			}],
			labels: ['Matched', 'Not Matched', 'Pending'] 
		},
		options : {
			responsive: true, 
			maintainAspectRatio: false,
			legend: {
				position : 'bottom',
				labels : {
					fontColor: 'rgb(154, 154, 154)',
					fontSize: 11,
					usePointStyle : true,
					padding: 20
				}
			},
			pieceLabel: {
				render: 'value',
				fontColor: 'white',
				fontSize: 14,
			},
			tooltips: false,
			layout: {
				padding: {
					left: 20,
					right: 20,
					top: 0,
					bottom: 0
				}
			}
		}
	});
	*/
}

function Sweetalert(Type, Title, Info)
{
	if(Type == "success")
	{
		Swal.fire({
			  icon: 'success',
			  title: Title ,
			  text: Info ,			 
			  timer: 2000
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
			  timer: 2000
		});
	}
	else if(Type == "warning")
	{
		Swal.fire({
			  icon: 'warning',
			  title: Title ,
			  text:  Info ,		 
			  timer: 2000
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

/*
$(document).ready(function() {

	Circles.create({
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
	
	var totalIncomeChart = document.getElementById('totalIncomeChart').getContext('2d');
	
	var mytotalIncomeChart = new Chart(totalIncomeChart, {
		type: 'bar',
		data: {
			labels: ["S", "M", "T", "W", "T", "F", "S", "S", "M", "T"],
			datasets : [{
				label: "Total Income",
				backgroundColor: '#ff9e27',
				borderColor: 'rgb(23, 125, 255)',
				data: [6, 4, 9, 5, 4, 6, 4, 3, 8, 10],
			}],
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			legend: {
				display: false,
			},
			scales: {
				yAxes: [{
					ticks: {
						display: false //this will remove only the label
					},
					gridLines : {
						drawBorder: false,
						display : false
					}
				}],
				xAxes : [ {
					gridLines : {
						drawBorder: false,
						display : false
					}
				}]
			},
		}
	});
	
	$('#lineChart').sparkline([105,103,123,100,95,105,115], {
		type: 'line',
		height: '70',
		width: '100%',
		lineWidth: '2',
		lineColor: '#ffa534',
		fillColor: 'rgba(255, 165, 52, .14)'
	});
	
}); */