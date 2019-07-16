import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import HomeIcon from '@material-ui/icons/Home';
import DvrIcon from '@material-ui/icons/Dvr';
import TuneIcon from '@material-ui/icons/Tune';
import VideoGameIcon from '@material-ui/icons/VideogameAsset';
import { withRouter } from 'react-router-dom';

import Hidden from '@material-ui/core/Hidden';

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  list: {
    width: drawerWidth,
  },
  fullList: {
    width: 'auto',
  },
  drawer: {
    flexShrink: 0,
    width: drawerWidth,
  },
  drawerPaper: {
    width: drawerWidth,
  },
  toolbar: theme.mixins.toolbar
}));

function MyDrawer(props) {
  const classes = useStyles();

  const toggleDrawer = (argOpen) => event => {
    if (event && event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    props.toggleDrawer(argOpen);
  };

  const sideList = () => (
    <div
      className={classes.list}
      role="presentation"
      onClick={toggleDrawer(false)}
      onKeyDown={toggleDrawer(false)}
    >
      <List>
        <ListItem button key={"Game"} onClick={() => {props.history.push("/")}}>
          <ListItemIcon><VideoGameIcon /></ListItemIcon>
          <ListItemText primary={"Game"} />
        </ListItem>
        <ListItem button key={"AI Model"} onClick={() => {props.history.push("/model")}}>
          <ListItemIcon><TuneIcon /></ListItemIcon>
          <ListItemText primary={"AI Model"} />
        </ListItem>
      </List>
    </div>
  );

  return (
    <div>
      <Hidden smUp>
        <SwipeableDrawer
          className={classes.drawer}
          open={props.open}
          onClose={toggleDrawer(false)}
          onOpen={toggleDrawer(true)}
        >
          <div className={classes.toolbar}/>
          {sideList()}
        </SwipeableDrawer>
      </Hidden>
      <Hidden xsDown>
        <Drawer
          className={classes.drawer}
          classes={{
            paper: classes.drawerPaper,
          }}
          open={true}
          variant="permanent"
        >
          <div className={classes.toolbar}/>
          {sideList('left')}
        </Drawer>
      </Hidden>
    </div>
  );
}

export default withRouter(MyDrawer);
