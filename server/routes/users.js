var redis = require('redis');
var Config = require('../config/server-config');
var Util = require('../common/unit.js');

var client = redis.createClient(Config.redisPort, Config.redisHost);

var REDIS_USER_PREFIX = "user:{user_id}";
var REDIS_USER_USERNAME_PREFIX = "user.username:{username}";


var users = {
    register : function(req, res, next) {
        var reqData = req.body;
        var username = reqData.username;
        client.exists(REDIS_USER_USERNAME_PREFIX.replace('{username}', username), function(err, exist) {
            if (err) return;

            if (exist) {
                client.get(REDIS_USER_USERNAME_PREFIX.replace('{username}', username), function(err, userId) {
                    if (err) return;
                    client.hget(REDIS_USER_PREFIX.replace("{user_id}", userId), 'password', function(err, password) {
                        res.json({code:3001, content:{password:password}});
                    });
                });
            } else {
                var id = Util.randomString(36);
                var password = Util.randomString(24);

                var user = {id:id, username:username, password:password};
                var multi = client.multi();
                multi.hmset(REDIS_USER_PREFIX.replace("{user_id}", id), user);
                multi.set(REDIS_USER_USERNAME_PREFIX.replace('{username}', username), id);
                multi.exec(function(err, result) {
                    console.log(result);
                    res.json({code:3001, content:{password:password}});
                    next();
                });
            }
        });
    },
    login : function(req, res, next) {
        var reqData = req.body;
        var username = reqData.username;
        var password = reqData.password;

        client.get(REDIS_USER_USERNAME_PREFIX.replace("{username}", username), function(err, userId) {
            if (err) return;
            console.log(userId);
            if (userId) {
                client.hgetall(REDIS_USER_PREFIX.replace("{user_id}", userId), function(err, user) {
                    if (err) return;
                    console.log(user);
                    console.log(password);
                    if (user && user.password === password) {
                        res.json({code:3002, content:{id:user.id, username:user.username}});
                    } else {
                        res.status(403);
                        res.json({code:1001, content: 'Password Error'});
                    }
                });
            } else {
                res.status(403);
                res.json({code:1001, content: 'Password Error'});
            }
        });



    }
};

module.exports = users;
