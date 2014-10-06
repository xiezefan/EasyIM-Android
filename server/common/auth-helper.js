var BasicStrategy = require('passport-http').BasicStrategy;
var redis = require('redis');
var Config = require('../config/server-config');
var User = require("../routes/users");

var client = redis.createClient(Config.redisPort, Config.redisHost);

var UserAuthorization = new BasicStrategy(
    function(username, password, done) {
        console.log(username);
        console.log(password);
        client.get(User.REDIS_USER_USERNAME_PREFIX.replace("{username}", username), function(err, userId) {
            if (userId) {
                client.hgetall(User.REDIS_USER_PREFIX.replace("{user_id}", userId), function(err, user) {
                    console.log(user);
                    if (user && user.password === password) {
                        return done(null, user);
                    } else {
                        return done(null, false, {code:1002, message:"Authorization Fail"});
                    }
                });
            } else {
                return done(null, false, {code:1002, message:"Authorization Fail"});
            }
        });
    }
);

exports.UserAuthorization = UserAuthorization;