var express = require('express');
var router = express.Router();
const axios = require('axios');


/* GET home page. */
router.get('/', function(req, res, next) {
    var userToken;
    if(req.session.userToken) {
        userToken = req.session.userToken;
        console.log("User token from session: " + userToken);
    } else {
        console.log("No user token stored in session: " + req.session.userToken);
        res.redirect('../');
    }
    
    var libraryBody
    axios.get('http://localhost:8080/library?userToken=' + userToken).then(function (backendResponse) {
        libraryBody = backendResponse.data;
        console.log(libraryBody);
        res.render('library', { title: 'Library', library: libraryBody});
    });

    
});

module.exports = router;
