var express = require('express');
var router = express.Router();
const axios = require('axios');


/* GET home page. */
router.get('/', function(req, res, next) {
    //redirect if not logged in
    var userToken;
    if(req.session.userToken) {
        userToken = req.session.userToken;
        console.log("User token from session: " + userToken);
    } else {
        console.log("No user token stored in session: " + req.session.userToken);
        res.redirect('../');
    }

    //if set, run search
    if(req.query.search) {
        
        axios.get('http://localhost:8080/library/search/title?userToken=' + userToken + '&title=' + req.query.search).then(function (backendResponse) {
            var libraryBody = backendResponse.data;
            console.log(libraryBody);
            res.render('library', { title: 'Library', library: libraryBody});
        });

    //otherwise return full library
    } else {
        axios.get('http://localhost:8080/library?userToken=' + userToken).then(function (backendResponse) {
            var libraryBody = backendResponse.data;
            console.log(libraryBody);
            res.render('library', { title: 'Library', library: libraryBody});
        });
    }
    
});

router.get('/save', function(req, res, next) {
    var userToken;
    if(req.session.userToken) {
        userToken = req.session.userToken;
    } else {
        res.redirect('../');
    }

    var type = 'movie'; //default type to movie

    if(req.query.type) {
        type = req.query.type;
    }
    
    res.render('saveMedia', {title: 'Save Media', type: type});
});

router.post('/saveMovie', function(req, res, next) {

    var data = {
        "title": req.body.title,
        "location": req.body.location,
        "userToken": req.session.userToken
    };

    console.log("Trying to save " + data.title);

    if(req.body.genres && req.body.genres != "") {
        data.genres = req.body.genres.split(",");
    }

    if(req.body.release && req.body.release != "") {
        data.release = req.body.release;
    }

    if(req.body.image && req.body.image != "") {
        data.image = req.body.image;
    }

    if(req.body.description && req.body.description != "") {
        data.description = req.body.description;
    }

    if(req.body.mpaaRating && req.body.mpaaRating != "") {
        data.mpaaRating = req.body.mpaaRating;
    }

    if(req.body.runTime && req.body.runTime != "") {
        data.runtime = req.body.runTime;
    }

    axios.post("http://localhost:8080/library/movie", data).then(function (backendResponse) {
        if(backendResponse.data.success) {
            console.log("Success: " + backendResponse.data.success);
            res.redirect('../library/');
        } else {
            console.log("Error: " + backendResponse.data.error);
            console.log(JSON.stringify(backendResponse.data));
        }
    });
});

router.post('/saveShow', function(req, res, next) {

    var data = {
        "title": req.body.title,
        "location": req.body.location,
        "userToken": req.session.userToken
    };

    console.log("Trying to save " + data.title);

    if(req.body.genres && req.body.genres != "") {
        data.genres = req.body.genres.split(",");
    }

    if(req.body.release && req.body.release != "") {
        data.release = req.body.release;
    }

    if(req.body.image && req.body.image != "") {
        data.image = req.body.image;
    }

    if(req.body.description && req.body.description != "") {
        data.description = req.body.description;
    }

    if(req.body.tvRating && req.body.tvRating != "") {
        data.tvRating = req.body.tvRating;
    }

    if(req.body.episodeLength && req.body.episodeLength != "") {
        data.episodeLength = req.body.episodeLength;
    }

    axios.post("http://localhost:8080/library/show", data).then(function (backendResponse) {
        if(backendResponse.data.success) {
            console.log("Success: " + backendResponse.data.success);
            res.redirect('../library/');
        } else {
            console.log("Error: " + backendResponse.data.error);
            console.log(JSON.stringify(backendResponse.data));
        }
    });
});

module.exports = router;
