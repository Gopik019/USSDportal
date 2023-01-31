$(document).ready(function() {
	
	Channel_Monitoring();
	
});

function Channel_Monitoring()
{	
	var chart_object = {
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
		  };
	
	var salesChart = new Chart( $('#sales-chart'), chart_object);
	var salesChart2 = new Chart( $('#sales-chart2'), chart_object);
	var salesChart3 = new Chart( $('#sales-chart3'), chart_object);
	var salesChart4 = new Chart( $('#sales-chart4'), chart_object);
	var salesChart5 = new Chart( $('#sales-chart5'), chart_object);
	var salesChart5 = new Chart( $('#sales-chart6'), chart_object);
}