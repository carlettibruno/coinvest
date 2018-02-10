<html>

<head>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js" type="text/javascript"></script>
</head>

<body>
<h2>Hello World!</h2>

<canvas id="myChart" width="400" height="400"></canvas>

</body>

<script>
var utils = Samples.utils;
function generateData(config) {
	return utils.numbers(Chart.helpers.merge(inputs, config || {}));
}
function generateLabels(config) {
	return utils.months(Chart.helpers.merge({
		count: inputs.count,
		section: 3
	}, config || {}));
}


var options = {
		maintainAspectRatio: false,
		spanGaps: false,
		elements: {
			line: {
				tension: 0.000001
			}
		},
		plugins: {
			filler: {
				propagate: false
			}
		},
		scales: {
			xAxes: [{
				ticks: {
					autoSkip: false,
					maxRotation: 0
				}
			}]
		}
	};
	
var ctx = document.getElementById("myChart").getContext('2d');
new Chart(ctx, {
	type: 'line',
	data: {
		labels: generateLabels(),
		datasets: [{
			backgroundColor: utils.transparentize(presets.red),
			borderColor: presets.red,
			data: generateData(),
			label: 'Dataset',
			fill: boundary
		}]
	},
	options: Chart.helpers.merge(options, {
		title: {
			text: 'fill: ' + boundary,
			display: true
		}
	})
});


function toggleSmooth(btn) {
var value = btn.classList.toggle('btn-on');
Chart.helpers.each(Chart.instances, function(chart) {
	chart.options.elements.line.tension = value? 0.4 : 0.000001;
	chart.update();
});
}
</script>

</html>
