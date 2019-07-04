import React from 'react';
import logo from './logo.svg';
import './App.css';

import 'typeface-roboto';
import SignIn from './SignIn.js';
import SignUp from './SignUp.js';
import { BrowserRouter as Router, Link, Route, Switch } from 'react-router-dom';

function Home() {
  return (
    <div>
      Home
    </div>
  );
}

function ForgotPassword() {
  return (
    <div>
      Forgot
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
      </Router>
    </div>
  );
}

export default App;
