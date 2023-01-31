$(document).ready(function() {
	
	Render_Chart();
	
});

function Render_Chart()
{	
	var chart_object = new Chart( $('#sales-chart'), {
		    type: 'bar',
		    data: {
		      labels: ['Jan', 'Feb', 'March', 'Apr', 'May', 'Jun'],
		      datasets: [
		        {
		        	backgroundColor: '#fd7e14',  
		        	borderColor: '#007bff',
		           	data: [1000, 2000, 3000, 1500, 1000, 2800]
		        },
		        {
		        	backgroundColor: '#007bff', 
		        	borderColor: '#ced4da',
		        	data: [700, 1700, 2700, 3000, 2000, 1000]
		        },
				{
		        	backgroundColor: '#ced4da',
		        	borderColor: '#007bff',
		        	data: [1000, 2000, 3000, 1200, 900, 1900]
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
	
	Circles.create({
		id:'circles-1',
		radius:45,
		value:50,
		maxValue:100,
		width:7,
		text: 50,
		colors:['#f1f1f1', '#FF9E27'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});

	Circles.create({
		id:'circles-2',
		radius:45,
		value:60,
		maxValue:100,
		width:7,
		text: 60,
		colors:['#f1f1f1', '#2BB930'],
		duration:400,
		wrpClass:'circles-wrp',
		textClass:'circles-text',
		styleWrapper:true,
		styleText:true
	});
}