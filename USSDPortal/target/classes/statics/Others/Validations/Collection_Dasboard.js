$(document).ready(function() {
	
	Render_Chart();
	
});

function Render_Chart()
{	
	var barChart = document.getElementById('sales-chart').getContext('2d');
	
	var myBarChart = new Chart(barChart, {
		type: 'horizontalBar',
		data: {
			labels: ["Pincode 1", "Pincode 2", "Pincode 3", "Pincode 4", "Pincode 5", "Pincode 6", "Pincode 7", "Pincode 8", "Pincode 9", "Pincode 10", "Pincode 11", "Pincode 12"],
			datasets : [{
				label: "Postal Code wise Defaulter",
				data: [3, 2, 9, 5, 4, 6, 4, 6, 7, 8, 7, 4],
				axis: 'y',
				backgroundColor : [
				      'rgb(255, 99, 132)',
				      'rgb(255, 159, 64)',
				      'rgb(255, 205, 86)',
				      'rgb(75, 192, 192)',
				      'rgb(54, 162, 235)',
				      'rgb(153, 102, 255)',
				      'rgb(201, 203, 207)',
				      'rgb(255, 99, 132)',
				      'rgb(255, 159, 64)',
				      'rgb(255, 205, 86)',
				      'rgb(75, 192, 192)',
				      'rgb(54, 162, 235)',
				  ],
			}],
		},
		options: {
			responsive: true,
			//maintainAspectRatio: false,
			scales: {
				xAxes: [{
				    ticks: {
				        beginAtZero: true
				    }
				}],
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