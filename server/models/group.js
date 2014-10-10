var Schema = require('jugglingdb').Schema;
var schema = new Schema('redis', {port: 6379});
var redis = require('redis');
var Config = require('../config/server-config');

var REDIS_KEY_GROUP_MEMBERS = "s:group.members:{group:id}";

var Group = schema.define('group', {
    name: String
});


function addUsers(users, callback) {
    var length = users.length;
    while(length--) {
        var user = users[length];
        
    }
}



module.exports = Group;






