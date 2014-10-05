var redis = require('redis');
var Config = require('../config/server-config');

exports.redis = redis.createClient(Config.redisPort, Config.redisHost);