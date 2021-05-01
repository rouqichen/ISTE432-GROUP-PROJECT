var express = require('express');
var router = express.Router();
const axios = require('axios');


/* GET home page. */
router.get('/', function(req, res, next) {
    var userToken;
    if(req.session.userToken) {
        userToken = req.session.userToken;
    } else {
        userToken = 1;
        //res.redirect('../users/');
    }
    
    var libraryBody
    axios.get('http://localhost:8080/library?userToken=' + userToken).then(function (backendResponse) {
        libraryBody = backendResponse.data;
        console.log(libraryBody);
        res.render('library', { title: 'Library', library: libraryBody});
    });

    
});

module.exports = router;
