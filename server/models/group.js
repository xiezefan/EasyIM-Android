var Schema = require('jugglingdb').Schema;
var schema = new Schema('redis', {port: 6379});
var redis = require('redis');
var Config = require('../config/server-config');

var REDIS_KEY_GROUP_MEMBERS = "s:group.members:{group_id}";
var client = redis.createClient(Config.redisHost, Config.redisPort);

var Group = schema.define('group', {
    name: String
});

function addUsers(users, callback) {
    if (this.id) {
        if (users instanceof Array) {
            var length = users.length;
            while(length--) {
                var user = users[length];
                client.sadd(REDIS_KEY_GROUP_MEMBERS.replace('{group_id}', this.id), user.id);
            }
        } else {
            client.sadd(REDIS_KEY_GROUP_MEMBERS.replace('{group_id}', this.id), users.id);
        }
        if (callback) callback();
    }
}


Group.prototype.addUsers = addUsers;
module.exports = Group;






