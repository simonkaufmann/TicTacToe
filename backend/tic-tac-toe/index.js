const express = require('express')

// Import our User schema
const User = require('./models/User.js');

const app = express()
const port = 3001

const mongoose = require('mongoose');

const mongo_uri = 'mongodb://localhost/react-auth';

mongoose.connect(mongo_uri, function(err) {
  if (err) {
    throw err;
  } else {
    console.log('Successfully connected to ${mongo_uri}');
  }
});

app.get('/api/home', function(req, res) {
  res.send('Welcome!');
});

app.get('/api/secret', function(req, res) {
  res.send('The password is potato');
});


// POST route to register a user
app.post('/api/register', function(req, res) {
  console.log(req.body);
  const { email, password } = req.body;
  console.log("email: " + email);
  console.log("password: " + password);
  const user = new User({ email, password });
  user.save(function(err) {
    if (err) {
      res.status(500)
        .send("Error registering new user please try again.");
    } else {
      res.status(200).send("Welcome to the club!");
    }
  });
});

app.listen(port, () => console.log(`Example app listening on port ${port}!`));

