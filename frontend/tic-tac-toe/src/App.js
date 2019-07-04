import React from 'react';
import './App.css';

import 'typeface-roboto';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import withAuth from './withAuth.js';
import Home from './Home.js';

function ForgotPassword() {
  return (
    <div>
      Forgot
    </div>
  );
}

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
        <Route path="/login" component={SignIn} />
        <Route path="/signup" component={SignUp} />
        <Route path="/reset-password" component={ForgotPassword} />
        <Route path="/secret" component={withAuth(Secret)} />
      </Router>
    </div>
  );
}

export default App;
