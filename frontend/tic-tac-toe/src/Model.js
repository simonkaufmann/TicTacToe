import React from 'react';
import './App.css';

import Container from '@material-ui/core/Container';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import { useState, useEffect } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Paper from '@material-ui/core/Paper';
import Hidden from '@material-ui/core/Hidden';

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
  root: {
    [theme.breakpoints.up('sm')]: {
      paddingLeft: "240px",
    },
    flexGrow: 1,
  },
  divGraph: {
    display: "inline-block",
    width: "98%",
    [theme.breakpoints.up('md')]: {
      width: "85%",
    },
  },
}));

function CenteredTabs() {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  function handleChange(event, newValue) {
    setValue(newValue);
  }

  return (
    <Paper className={classes.root} square={true}>
      <Hidden smDown>
        <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor="primary"
          textColor="primary"
          centered
        >
          <Tab label="Settings" />
          <Tab label="Graphs" />
        </Tabs>
      </Hidden>
      <Hidden mdUp>
         <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor="primary"
          textColor="primary"
          variant="fullWidth"
        >
          <Tab label="Settings" />
          <Tab label="Graphs" />
        </Tabs>
      </Hidden>     
    </Paper>
  );
}

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
      <div className={classes.toolbar}/> {/* place holder for app bar */}
      <CenteredTabs/>
      <Container className={classes.myContainer}>
        <div class={classes.divGraph}>
          <h2>Performance Player X</h2>
          <Graph data={state.performanceX}/>
          <h2>Performance Player O</h2>
          <Graph data={state.performanceO}/>
        </div>
      </Container>
    </div>
  );
} 

