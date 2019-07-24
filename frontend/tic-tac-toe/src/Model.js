import React from 'react';
import './App.css';

import Container from '@material-ui/core/Container';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import { useState, useEffect } from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Paper from '@material-ui/core/Paper';
import Hidden from '@material-ui/core/Hidden';
import TextField from '@material-ui/core/TextField';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';

import Skeleton from './Skeleton.js';
import Graph from './Graph.js';

const useStyles = makeStyles(theme => ({
  toolbar: theme.mixins.toolbar,
  myContainer: {
    paddingTop: "24px",
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
  button: {
    margin: theme.spacing(1),
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200,
  },
}));

function CenteredTabs(props) {
  const classes = useStyles();

  function handleChange(event, newValue) {
    props.callBack(newValue);
  }

  return (
    <Paper className={classes.root} square={true}>
      <Hidden smDown>
        <Tabs
          value={props.value}
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
          value={props.value}
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
    tabIndex: 0,
    alpha: 0,
    chanceRandomMove: 0,
  });

  useEffect(() => {
    let tim = setInterval(updatePerformance, 1000);
    setState({...state, timer: tim});

    fetch('api/model/get-model-settings')
      .then((response) => response.json())
      .then(json => {
        setState(state => ({...state, alpha: json.alpha, chanceRandomMove: json.chanceRandomMove}));
      })
      .catch(() => {
        console.log("Error retrieving model settings");
      });
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

  function setTabValue(newValue) {
    setState({...state, tabIndex: newValue});
  }

  function startTraining() {
    fetch('/api/model/start-training')
      .catch(() => {
        console.log("Error start training");
      });
  }

  function stopTraining() {
    fetch('/api/model/stop-training')
      .catch(() => {
        console.log("Error stop training");
      });
  }

  const handleChange = name => event => {
    setState({...state, [name]: event.target.value});
  }

  return (
    <div>
      <Skeleton loggedIn={true}/>
      <div className={classes.toolbar}/> {/* place holder for app bar */}
      <CenteredTabs callBack={setTabValue} value={state.tabIndex}/>
      {
        (state.tabIndex === 0) && /* inline if */
        <Container className={classes.myContainer}>
          <Box>
            <TextField
              label="Alpha"
              value={state.alpha}
              className={classes.textField}
              onChange={handleChange('alpha')}
              margin="normal"
            />
            <TextField
              label="Probability Random Move"
              value={state.alpha}
              className={classes.textField}
              onChange={handleChange('chanceRandomMove')}
              margin="normal"
            />
          </Box>
          <Box>
            <Button variant="contained" color="primary" className={classes.button} onClick={startTraining}>
              Stop Training
            </Button>
            <Button variant="contained" color="secondary" className={classes.button} onClick={stopTraining}>
              Start Training 
            </Button>
          </Box>
        </Container>
      }
      {
        (state.tabIndex === 1) && /* inline if */
        <Container className={classes.myContainer}>
          <div class={classes.divGraph}>
            <Typography variant="h4" component="h4">Performance Player X</Typography>
            <Graph data={state.performanceX}/>
            <Typography variant="h4" component="h4">Performance Player O</Typography>
            <Graph data={state.performanceO}/>
          </div>
        </Container>
      }
    </div>
  );
} 

