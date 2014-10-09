var Schema = require('jugglingdb').Schema;
var schema = new Schema('redis', {port: 6379});

var Group = schema.define('group', {
    name: String
});
module.exports = Group;






