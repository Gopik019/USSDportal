$(document).ready(function() {
	
	var myPieChart1 = new Chart( $("#GEPG_CHART_TZS") , {
	    type: 'pie',
		data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["#fd7e14","#237a26","#ffc107","#6c757d"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart2 = new Chart( $("#GEPG_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["teal",'indigo',"#fd7e14", "pink"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		var myPieChart3 = new Chart( $("#TRA_CHART_TZS") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["green","red","pink","indigo"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart4 = new Chart( $("#TRA_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["green","orange","indigo","brown"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		var myPieChart5 = new Chart( $("#MSC_CHART_TZS") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["indigo","grey","#ffc107","brown"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart6 = new Chart( $("#MSC_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["#1d7af3","#f3545d","#6c757d"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		
		/****** Inward & Outward *******/
		
		var myPieChart11 = new Chart( $("#TIPS_OUTWARD_CHART_TZS") , {
	    type: 'pie',
        height: 200,
		data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["#fd7e14","#237a26","#ffc107","#6c757d"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart12 = new Chart( $("#TIPS_OUTWARD_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["teal",'indigo',"#fd7e14", "pink"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		var myPieChart13 = new Chart( $("#TIPS_INWARD_CHART_TZS") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["green","red","pink","indigo"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart14 = new Chart( $("#TIPS_INWARD_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["green","orange","indigo","brown"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		/*********** TIGO Dashboard ************/
		
		var myPieChart21 = new Chart( $("#TIGO_OUTWARD_CHART_TZS") , {
	    type: 'pie',
        height: 200,
		data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["indigo","grey","#ffc107","brown"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart22 = new Chart( $("#TIGO_OUTWARD_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["green","red","pink","indigo"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
		
		var myPieChart23 = new Chart( $("#TIGO_INWARD_CHART_TZS") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [50000, 20000, 30000, 40000],
					backgroundColor :["#fd7e14","#237a26","#ffc107","#6c757d"],
					borderWidth: 0
				}],
				labels: ['IB', 'HH', 'MB', 'Others'] 
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
		
		var myPieChart24 = new Chart( $("#TIGO_INWARD_CHART_VOL") , {
			type: 'pie',
			data: {
				datasets: [{
					data: [567, 234, 345, 456],
					backgroundColor :["teal",'indigo',"#fd7e14", "pink"],
					borderWidth: 0
				}],
				labels: ['IB', 'MB', 'HH', 'MSC'] 
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
});