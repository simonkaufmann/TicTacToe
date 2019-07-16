import React from 'react';
import './App.css';

import ButtonAppBar from './ButtonAppBar.js';
import MyDrawer from './Drawer.js';

export default function Skeleton(props) {

  const [state, setState] = React.useState({
    drawerOpen: false,
  });

  function toggleDrawer(argOpen) {
    setState({...state, drawerOpen: argOpen});
  }

  return (
    <div>
      <ButtonAppBar open={state.drawerOpen} drawerOpen={state.drawerOpen} 
        toggleDrawer={toggleDrawer} drawerEnabled={props.drawerEnabled}
        loggedIn={props.loggedIn}/>
      <MyDrawer open={state.drawerOpen} toggleDrawer={toggleDrawer}
        drawerEnabled={props.drawerEnabled} loggedIn={props.loggedIn} />
    </div>
  );
}
