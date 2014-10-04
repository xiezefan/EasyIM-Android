/**
 * Created by xiezefan on 2014/10/4.
 */

var JPush = require('jpush-sdk');
var Config = require('../config/jpush-config');

var client = JPush.buildClient(Config.appkey, Config.mastersectet);

client.push().setPlatform(JPush.ALL)
    .setAudience(JPush.ALL)
    .setMessage('content', 'title', 'type', {key:"value"})
    .send(function(err, res) {
        if (err) {
            if (err instanceof JPush.APIConnectionError) {
                console.log(err.message);
            } else if (err instanceof  JPush.APIRequestError) {
                console.log(err.message);
            }
        } else {
            console.log('Sendno: ' + res.sendno);
            console.log('Msg_id: ' + res.msg_id);
        }
    });

function pushByTag(tag, message, callback) {
    client.push().setPlatform(JPush.ALL)
        .setAudience(JPush.tag(tag))
        .setMessage(message.from, message.to, message.type, message.extras)
        .send(callback);
}

function pushByAlias(alias, message, callback) {
    client.push().setPlatform(JPush.ALL)
        .setAudience(JPush.alias(alias))
        .setMessage(message.from, message.to, message.type, message.extras)
        .send(callback);
}



exports.pushByTag = pushByTag;
exports.pushByAlias = pushByAlias;