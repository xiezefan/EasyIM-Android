/**
 * Created by xiezefan on 2014/10/4.
 */

var Request = require('request');

/*
Request.post({
    url:"http://localhost:3000/messages/send",
    headers:{"content-type":"application/json"},
    body:JSON.stringify({key:"value"})
}, function(err, res) {
    if (err) {
        console.log("Error: " + JSON.stringify(err));
    } else {
        console.log("Success: " + JSON.stringify(res));
    }
});*/

var content = {from:"abc", to:"def", type:"text", extras: {date:new Date(), content:"I Love You"}};
console.log(JSON.stringify(content));
