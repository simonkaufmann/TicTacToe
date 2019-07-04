// middleware.js
const jwt = require('jsonwebtoken');
const secret = 'debug';

const withAuth = function(req, res, next) {
  console.log('withAuth called');
  const token =
    req.body.token 
    req.query.token 
    req.headers['x-access-token'] 
    req.cookies.token;
  if (!token) {
    console.log('no token');
    res.status(401).send('Unauthorized: No token provided');
  } else {
    jwt.verify(token, secret, function(err, decoded) {
      if (err) {
        console.log('invalid token');
        res.status(401).send('Unauthorized: Invalid token');
      } else {
        console.log('valid token');
        req.email = decoded.email;
        next();
      }
    });
  }
}
module.exports = withAuth;
