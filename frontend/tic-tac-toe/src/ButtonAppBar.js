import React from 'react';

import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import { withRouter, Link } from 'react-router-dom';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
    [theme.breakpoints.up('sm')]: {
      display: 'none',
    },
  },
  title: {
    flexGrow: 1,
  },
  drawer: {
    flexShrink: 0
  },
  appBar: {
    zIndex: 70000
  },
}));

function ButtonAppBar(props) {
  const classes = useStyles();

  function onClickLogin() {
    props.history.push('/login');
  }
  
  function onClickDrawer() {
    props.toggleDrawer(!props.drawerOpen);
  }

  return (
    <div className={classes.root}>
      <AppBar position="fixed" className={classes.appBar}>
        <Toolbar>
          <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="Menu" onClick={onClickDrawer}>
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            <Link to={"/"} style={{textDecoration: "none", color: "white",}}>Tic Tac Toe</Link>
          </Typography>
          <Button color="inherit" onClick={onClickLogin}>Login</Button>
        </Toolbar>
      </AppBar>
    </div>
  );
}

export default withRouter(ButtonAppBar);
