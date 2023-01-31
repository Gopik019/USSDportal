$(document).ready(function() {
	
	Request_Monotoring();
	
	 var $salesChart = $('#sales-chart')
	
	  var salesChart = new Chart($salesChart, {
	    type: 'bar',
	    data: {
	      labels: ['IB', 'MB', 'H2H', 'Others'],
	      datasets: [
	        {
	          backgroundColor: '#fd7e14',  
	          borderColor: '#007bff',
	          data: [1000, 2000, 3000, 2500]
	        },
	        {
	          backgroundColor: '#007bff', 
	          borderColor: '#ced4da',
	          data: [700, 1700, 2700, 2000]
	        },
			{
	          backgroundColor: '#ced4da',
	          borderColor: '#007bff',
	          data: [1000, 2000, 3000, 2500]
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
	  });

});

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
				Bill_Request(data.Bill);
				
				Payment_Request(data.PAYMENT);
				
				Account_Lookup_Request(data.ACCOUNT);
				
				Recon_Request(data.RECON);
				
				RECON_Channel(data.RECON_CHANNEL);
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

function Bill_Request(Bill)
{
	var Total_Bill = Bill.TOTAL;
	var GEPG_Bill = Bill.GEPG;
	var TRA_Bill = Bill.TRA;
	var TIPS_Bill = Bill.TIPS;
	
	Circles.create({
		id:'circles-1',
		radius:50,
		value:Total_Bill,
		maxValue:100,
		width:7,
		text: Total_Bill.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'Bill-GEPG',
		radius:50,
		value:GEPG_Bill,
		maxValue:100,
		width:7,
		text: GEPG_Bill.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'Bill-TRA',
		radius:50,
		value: TRA_Bill,
		maxValue:100,
		width:7,
		text: TRA_Bill.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'Bill-TIPS',
		radius:50,
		value:TIPS_Bill,
		maxValue:100,
		width:7,
		text: TIPS_Bill.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
}

function Payment_Request(Payment)
{
	var Total_Payment = Payment.TOTAL;
	var IN_GEPG_Payment = Payment.Inward_GEPG;
	var IN_TRA_Payment = Payment.Inward_TRA;
	var IN_TIPS_Payment = Payment.Inward_TIPS;
	
	var OUT_GEPG_Payment = Payment.Outward_GEPG;
	var OUT_TRA_Payment = Payment.Outward_TRA;
	var OUT_TIPS_Payment = Payment.Outward_TIPS;
	
	Circles.create({
		id:'circles-2',
		radius:50,
		value:Total_Payment,
		maxValue:100,
		width:7,
		text: Total_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'IN-PAYMENT-GEPG',
		radius:50,
		value:IN_GEPG_Payment,
		maxValue:100,
		width:7,
		text: IN_GEPG_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'IN-PAYMENT-TRA',
		radius:50,
		value: IN_TRA_Payment,
		maxValue:100,
		width:7,
		text: IN_TRA_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'IN-PAYMENT-TIPS',
		radius:50,
		value:IN_TIPS_Payment,
		maxValue:100,
		width:7,
		text: IN_TIPS_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'OUT-PAYMENT-GEPG',
		radius:50,
		value:OUT_GEPG_Payment,
		maxValue:100,
		width:7,
		text: OUT_GEPG_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'OUT-PAYMENT-TRA',
		radius:50,
		value: OUT_TRA_Payment,
		maxValue:100,
		width:7,
		text: OUT_TRA_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'OUT-PAYMENT-TIPS',
		radius:50,
		value:OUT_TIPS_Payment,
		maxValue:100,
		width:7,
		text: OUT_TIPS_Payment.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
}

function Account_Lookup_Request(Account)
{
	var Total_ACCOUNT = Account.ACCOUNT;
	var CURRENT = Account.CURRENT;
	var SAVINGS = Account.SAVINGS;
	
	Circles.create({
		id:'circles-3',
		radius:50,
		value:Total_ACCOUNT,
		maxValue:100,
		width:7,
		text: Total_ACCOUNT.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'ACCOUNT-CURRENT',
		radius:50,
		value:CURRENT,
		maxValue:100,
		width:7,
		text: CURRENT.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'ACCOUNT-SAVINGS',
		radius:50,
		value: SAVINGS,
		maxValue:100,
		width:7,
		text: SAVINGS.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
}


function Recon_Request(Recon)
{
	var Total_Recon = Recon.TOTAL;
	var GEPG_Recon = Recon.GEPG;
	var TRA_Recon = Recon.TRA;
	var TIPS_Recon = Recon.TIPS;
	
	Circles.create({
		id:'circles-4',
		radius:50,
		value:Total_Recon,
		maxValue:100,
		width:7,
		text: Total_Recon.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'RECON-GEPG',
		radius:50,
		value:GEPG_Recon,
		maxValue:100,
		width:7,
		text: GEPG_Recon.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'RECON-TRA',
		radius:50,
		value: TRA_Recon,
		maxValue:100,
		width:7,
		text: TRA_Recon.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
	
	Circles.create({
		id:'RECON-TIPS',
		radius:50,
		value:TIPS_Recon,
		maxValue:100,
		width:7,
		text: TIPS_Recon.toString(),
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
}


function RECON_Channel(Recon)
{
	 var IB_PENDING = Recon.IB_PENDING;
	 var IB_MATCHED = Recon.IB_MATCHED;
	 var IB_NOT_MATCHED = Recon.IB_NOT_MATCHED;
	 var H2H_PENDING = Recon.H2H_PENDING;
	 var H2H_MATCHED = Recon.H2H_MATCHED;
	 var H2H_NOT_MATCHED = Recon.H2H_NOT_MATCHED;
	
	 var myPieChart1 = new Chart( $("#IB") , {
	    type: 'pie',
		data: {
				datasets: [{
					data: [ IB_MATCHED, IB_NOT_MATCHED, IB_PENDING],
					backgroundColor :["#fd7e14","#237a26","#ffc107"],
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
	
	var myPieChart2 = new Chart( $("#H2H") , {
		type: 'pie',
		data: {
			datasets: [{
				data: [H2H_MATCHED, H2H_NOT_MATCHED, H2H_PENDING],
				backgroundColor :['#00a65a', '#f56954', '#3c8dbc'],   //'#f56954', '#00a65a', '#f39c12', '#00c0ef', '#3c8dbc', '#d2d6de'
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