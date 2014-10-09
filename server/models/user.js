var Schema = require('jugglingdb').Schema;
var schema = new Schema('redis', {port: 6379});

var User = schema.define('user', {
    username: { type: String, default: false, index: true },
    password: String
});
User.validatesUniquenessOf('username', {message: 'username is not unique'});


module.exports = User;



