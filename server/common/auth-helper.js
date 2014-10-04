/**
 * Created by xiezefan on 2014/10/4.
 */
var BasicStrategy = require('passport-http').BasicStrategy;

var UserAuthorization = new BasicStrategy(
    function(username, password, done) {
        console.log("username: " + username)
        console.log("password: " + password);
        return done(null, "Authorization " + username);
    }
);

exports.UserAuthorization = UserAuthorization;