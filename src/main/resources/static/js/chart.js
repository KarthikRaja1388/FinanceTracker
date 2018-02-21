$(document).ready(function(){
		var ctx = $('#myChart');
		var income = $("#income").text().slice(1);
		var expense = $("#expense").text().slice(1);
		var savings = $("#savings").text().slice(1);
		
		console.log(income + " "+ expense + " "+ savings);
		var myChart = new Chart(ctx,{
			
			type: 'pie',
			data : {
				    datasets: [{
				        data: [income, expense, savings],
				        backgroundColor:[
				        	'rgba(12, 96, 36,1)',
				        	'rgba(196, 9, 9,1)',
				        	'rgba(191, 156, 15,1)'
				        ]
				    }],
				    labels: [
				        'Income',
				        'Expense',
				        'Savings'
				    ]
				},
			options:{
				legend:{
					labels:{
						fontColor: 'white'
					}
				}
			}
		});
});
