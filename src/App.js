import React from 'react';
import './App.css';

import 'typeface-roboto';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import withAuth from './withAuth.js';
import Home from './Home.js';
import Model from './Model.js';
import PasswordForgotten from './PasswordForgotten.js';
import AccountSettings from './AccountSettings.js';

function Secret() {
  return (
    <div>
      I'm the secret
    </div>
  );
}

function App() {
  return (
    <div>
      <Router>
        <Route path="/" exact component={Home} />
        <Route path="/model" component={Model} />
        <Route path="/login" component={SignIn} />
        <Route path="/signup" component={SignUp} />
        <Route path="/reset-password" component={PasswordForgotten} />
        <Route path="/secret" component={withAuth(Secret)} />
        <Route path="/account-settings" component={AccountSettings} />
      </Router>
    </div>
  );
}

export default App;
