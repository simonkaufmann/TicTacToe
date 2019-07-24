import React from 'react';
import './App.css';

import Container from '@material-ui/core/Container';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import { useState, useEffect } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';

import Skeleton from './Skeleton.js';
import Graph from './Graph.js';

const useStyles = makeStyles(theme => ({
  toolbar: theme.mixins.toolbar,
  myContainer: {
    textAlign: "center",
    [theme.breakpoints.up('sm')]: {
      paddingLeft: "240px",
    },
    [theme.breakpoints.up('md')]: {
      paddingLeft: "240px",
    },
  },
}));

export default function Model() {
  const classes = useStyles();

  const [state, setState] = useState({
    performanceX: [],
    performanceO: [],
    timer: null,
  });

  useEffect(() => {
    let tim = setInterval(updatePerformance, 1000);
    setState({...state, timer: tim});
  }, []);

  function updatePerformance() {
    fetch('/api/model/get-performanceX')
      .then((response) => response.json())
      .then(json => {
        setState(state => ({ ...state, performanceX: json}));
      })
      .catch(() => {
        console.log("Error updating performance");
      });
    fetch('/api/model/get-performanceO')
      .then((response) => response.json())
      .then(json => {
        setState(state => ({ ...state, performanceO: json}));
      })
      .catch(() => {
        console.log("Error updating performance");
      });
  }

  return (
    <div>
      <Skeleton loggedIn={true}/>
      <Container className={classes.myContainer}>
        <div className={classes.toolbar}/>
        <div style={{width:"70%", display: "inline-block"}}>
          <h2>Performance Player X</h2>
          <Graph data={state.performanceX}/>
          <h2>Performance Player O</h2>
          <Graph data={state.performanceO}/>
        </div>
      </Container>
    </div>
  );
} 

