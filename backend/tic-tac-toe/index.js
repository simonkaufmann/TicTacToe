const express = require('express')
const jwt = require('jsonwebtoken');
const cookieParser = require('cookie-parser');

// Import our User schema
const User = require('./models/User.js');
const withAuth = require('./middleware');

const app = express()
const port = 3001

var cors = require('cors'); // for Allow Origin Error

app.use(cors());
app.use(cookieParser());

const bodyParser = require('body-parser');

app.use(bodyParser.json());

const mongoose = require('mongoose');

const mongo_uri = 'mongodb://localhost/react-auth';

mongoose.set('useCreateIndex', true);

mongoose.connect(mongo_uri, { useNewUrlParser: true }, function(err) {
  if (err) {
    throw err;
  } else {
    console.log("Connecting to mongodb at " + mongo_uri + " successful");
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

app.post('/api/authenticate', function(req, res) {
  const { email, password } = req.body;
  User.findOne({ email }, function(err, user) {
    if (err) {
      console.error(err);
      res.status(500)
        .json({
        error: 'Internal error please try again'
      });
    } else if (!user) {
      res.status(401)
        .json({
          error: 'Incorrect email or password'
        });
    } else {
      user.isCorrectPassword(password, function(err, same) {
        if (err) {
          res.status(500)
            .json({
              error: 'Internal error please try again'
          });
        } else if (!same) {
          res.status(401)
            .json({
              error: 'Incorrect email or password'
          });
        } else {
          // Issue token
          const payload = { email };
          const token = jwt.sign(payload, secret, {
            expiresIn: '1h'
          });
          res.cookie('token', token, { httpOnly: true })
            .sendStatus(200);
        }
      });
    }
  });
});

app.get('/api/secret', withAuth, function(req, res) {
  res.send('The password is potato');
});

app.get('/checkToken', withAuth, function(req, res) {
  res.sendStatus(200);
});

app.listen(port, () => console.log(`Backend listening on port ${port}`));

