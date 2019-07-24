import React from 'react';
import ReactDOM from 'react-dom';
import {Scatter} from 'react-chartjs-2';

const dispAverage = 5;

export default function Graph(props) {
  var options = {
    scales: {
      xAxes: [
        {
          scaleLabel: [{
            display:true,
            labelString: "Training Iterations",
          }],
          type: "linear",
        }
      ],
    },
  };
  
  var data = {
    labels: ['Scatter'],
    datasets: [
      {
        label: 'Win',
        fill: false,
        backgroundColor: 'green',
        pointBorderColor: 'green',
        pointBackgroundColor: 'green',
        pointBorderWidth: 0,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: 'green',
        pointHoverBorderColor: 'green',
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        pointHitRadius: 10,
        showLine: true,
        data: [
        ]
      },
      {
        label: 'Lose',
        fill: false,
        backgroundColor: 'blue',
        pointBorderColor: 'blue',
        pointBackgroundColor: 'blue',
        pointBorderWidth: 0,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: 'blue',
        pointHoverBorderColor: 'blue',
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        pointHitRadius: 10,
        showLine: true,
        data: [
        ]
      },
      {
        label: 'Draw',
        fill: false,
        backgroundColor: '#ffbf00',
        pointBorderColor: '#ffbf00',
        pointBackgroundColor: '#ffbf00',
        pointBorderWidth: 0,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: '#ffbf00',
        pointHoverBorderColor: '#ffbf00',
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        pointHitRadius: 10,
        showLine: true,
        data: [
        ]
      },
  
    ]
  };

  let win = [];
  let draw = [];
  let lose = []; 
  if ("undefined" != typeof(props.data["performance"])) {
    let w = 0;
    let l = 0;
    let d = 0;
    for (var i = 0; i < props.data.performance.length; i++) {
      let r = props.data.performance[i];
      let x = r.trainingIterations;
      w += r.win;
      l += r.lose;
      d += r.draw;
      if (i % dispAverage === 0) {
        win.push({ x: x, y: w / dispAverage });
        lose.push({ x: x, y: l / dispAverage });
        draw.push({ x: x, y: d / dispAverage });
        w = 0;
        l = 0;
        d = 0;
      }
    }
  }

  data.datasets[0].data = win;
  data.datasets[1].data = lose;
  data.datasets[2].data = draw;

  return (
    <div>
      <Scatter data={data} options={options}/>
    </div>
  );
}
