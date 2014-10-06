var redis = require('redis');
var Config = require('../config/server-config');
var Util = require('../common/unit.js');


var client = redis.createClient(Config.redisPort, Config.redisHost);

var messages = {
    send : function(req, res, next) {
        var reqData = req.body;
        var user = req.user;
        console.log(req.params);
        res.json({code:3000});
        next();
    }
};

module.exports = messages;