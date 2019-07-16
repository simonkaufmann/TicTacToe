import React from 'react';
import './App.css';

import Container from '@material-ui/core/Container';
import { withStyles, makeStyles } from '@material-ui/core/styles';

import Skeleton from './Skeleton.js';

const useStyles = makeStyles(theme => ({
  toolbar: theme.mixins.toolbar,
  myContainer: {
    textAlign: "center",
    [theme.breakpoints.up('sm')]: {
      marginLeft: "120px",
    },
    [theme.breakpoints.up('md')]: {
      marginLeft: "70px",
    },
  },
}));

export default function AccountSettings() {
  const classes = useStyles();

  return (
    <div>
      <Skeleton />
      <Container className={classes.myContainer}>
        <div className={classes.toolbar}/>
        <div>
          Account Settings
        </div>
      </Container>
    </div>
  );
}


