import React from 'react';
import './App.css';

import Container from '@material-ui/core/Container';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import { useState, useEffect } from 'react';

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
    performance: [],
    timer: null,
  });

  useEffect(() => {
    let tim = setInterval(updatePerformance, 1000);
    setState({...state, timer: tim});
  }, []);

  function updatePerformance() {
    fetch('/api/model/get-performance')
      .then((response) => response.json())
      .then(json => {
        setState({ ...state, performance: json});
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
          <Graph data={state.performance}/>
        </div>
      </Container>
    </div>
  );
} 

