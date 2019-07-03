import React from 'react';
import logo from './logo.svg';
import './App.css';

import 'typeface-roboto';
import SignIn from './SignIn.js';
import { BrowserRouter as Router, Link, Route, Switch } from 'react-router-dom';

function Home() {
  return (
    <div>
      Home
    </div>
  );
}

function App() {
  return (
    <div>
      <Router>
        <Route path="/" exact component={Home} />
        <Route path="/login" component={SignIn} />
      </Router>
    </div>
  );
}

export default App;
