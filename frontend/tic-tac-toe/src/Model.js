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
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import Divider from '@material-ui/core/Divider';

import Skeleton from './Skeleton.js';
import Graph from './Graph.js';

const useStyles = makeStyles(theme => ({
  toolbar: theme.mixins.toolbar,
  myContainer: {
    paddingTop: theme.spacing(3),
    textAlign: "center",
    [theme.breakpoints.up('sm')]: {
      paddingLeft: (theme.spacing(1) + 240) + "px",
    },
    [theme.breakpoints.up('md')]: {
      paddingLeft: (theme.spacing(1) + 240) + "px",
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
  paper: {
    padding: theme.spacing(1),
  },
  paperBox: {
    marginTop: theme.spacing(1),
    marginBottom: theme.spacing(1),
    width: "100%",
    display: "inline-block",
    maxWidth: "600px",
    textAlign: "left",
  },
  listItem: {
  }
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
    trainingActive: false,
    chanceRandomMove: 0,
  });

  useEffect(() => {
    let tim = setInterval(updatePerformance, 1000);
    setState({...state, timer: tim});
    
    updateSettings();
  }, []);
  
  function updateSettings() {
    fetch('api/model/get-model-settings')
      .then((response) => response.json())
      .then(json => {
        setState(state => ({...state, alpha: json.alpha, chanceRandomMove: json.chanceRandomMove, trainingActive: json.trainingActive}));
      })
      .catch(() => {
        console.log("Error retrieving model settings");
      });
  }

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

  function saveAlpha() {
    fetch('/api/model/set-alpha', {
      method: "post",
      body: JSON.stringify({ alpha: state.alpha }),
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .catch(e => console.log(e));
  }

  function saveChanceRandomMove() {
    fetch('/api/model/set-chance-random-move', {
      mode: 'post',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ chanceRandomMove: state.chanceRandomMove }),
    })
    .catch(e => console.log(e));
  }

  function saveTrainingActive() {
    if (state.trainingActive === true) {
      fetch('api/model/start-training');
    } else {
      fetch('api/model/stop-training');
    }
  }

  function saveModelValues() {
    saveAlpha();
    saveChanceRandomMove();
    saveTrainingActive();
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

  const handleChangeSwitch = name => event => {
    setState({...state, [name]: event.target.checked});
  }

  return (
    <div>
      <Skeleton loggedIn={true}/>
      <div className={classes.toolbar}/> {/* place holder for app bar */}
      <CenteredTabs callBack={setTabValue} value={state.tabIndex}/>
      {
        (state.tabIndex === 0) && /* inline if */
        <Container className={classes.myContainer}>
          <Box className={classes.paperBox}>
            <Typography variant="subtitle1">Settings</Typography>
            <Paper className={classes.paper}>
              <List>
                <ListItem className={classes.listItem}>
                  <ListItemText primary="Alpha"/>
                  <ListItemSecondaryAction>
                    <TextField
                      value={state.alpha} className={classes.textField}
                      onChange={handleChange('alpha')}
                      margin="normal"
                    />
                  </ListItemSecondaryAction>
                </ListItem>
                <Divider variant="middle"/>
                <ListItem className={classes.listItem}>
                  <ListItemText primary="Probability Random Move"/>
                  <ListItemSecondaryAction>
                    <TextField
                      value={state.chanceRandomMove}
                      className={classes.textField}
                      onChange={handleChange('chanceRandomMove')}
                      margin="normal"
                    />
                  </ListItemSecondaryAction>
                </ListItem>
                <Divider variant="middle"/>
                <ListItem className={classes.listItem}>
                  <ListItemText primary="Training Active"/>
                  <ListItemSecondaryAction>
                    <Switch
                      checked={state.trainingActive}
                      onChange={handleChangeSwitch('trainingActive')}
                      value="bla"
                      color="primary"
                    />
                  </ListItemSecondaryAction>
                </ListItem>
              </List>
              <Button variant="contained" color="secondary" className={classes.button} onClick={saveModelValues}>
                Save Settings
              </Button>
            </Paper>
          </Box>
        </Container>
      }
      {
        (state.tabIndex === 1) && /* inline if */
        <Container className={classes.myContainer}>
          <div style={{width:"100%"}}>
          <Box className={classes.paperBox}>
            <Typography variant="subtitle1">Performance Player X</Typography>
            <Paper className={classes.paper}>
              <Graph data={state.performanceX}/>
            </Paper>
          </Box>
          <Box className={classes.paperBox}>
            <Typography variant="subtitle1">Performance Player O</Typography>
            <Paper className={classes.paper}>
              <Graph data={state.performanceO}/>
            </Paper>
          </Box>
          </div>
        </Container>
      }
    </div>
  );
} 

