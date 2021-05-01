var express = require('express');
var router = express.Router();
router.use(express.urlencoded());
router.use(express.json());


const axios = require('axios');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/login', function(req, res, next) {
  var username = req.body.name;
  var password = req.body.password;
  console.log(username);

  axios.post('http://localhost:8080/login' + userToken).then(function (backendResponse) {
    console.log(username);
});
});

router.post('/register', function(req, res, next) {
  var username = req.body.name;
  var password = req.body.password;

  axios.post('http://localhost:8080/register' + userToken).then(function (backendResponse) {
      console.log(username);
  });
});



module.exports = router;
