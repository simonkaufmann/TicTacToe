import React from 'react';
import ReactDOM from 'react-dom';
import { VictoryBar, VictoryChart } from 'victory';

const data = [
  {quarter: 1, earnings: 13000},
  {quarter: 2, earnings: 16500},
  {quarter: 3, earnings: 14250},
  {quarter: 4, earnings: 19000}
];

export default function Graph() {
    return (
        <VictoryChart>
            <VictoryBar/>
        </VictoryChart>
    );
}
