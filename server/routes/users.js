var Util = require('../common/unit.js');
var User = require('../models/user');

var users = {
    register : function(req, res, next) {
        var reqData = req.body;
        var username = reqData.username;

        User.findOne({where:{username:username}}, function(err, user) {
            if (err) {
                res.status(500);
                res.json({code:1001, content:'Bad Server'});
                return next();
            } else if (user) {
                res.json({code:3001, content:{password:user.password}});
                return next();
            } else {
                user = new User();
                user.username = reqData.username;
                user.password = Util.randomString(24);
                user.save(function(err, result) {
                    if (err) {
                        res.status(500);
                        res.json({code:1001, content:'Bad Server'});
                        return next();
                    }
                    res.json({code:3001, content:{password:user.password}});
                    return next();
                });
            }
        });
    },
    login:function(req, res, next) {
        var reqData = req.body;
        var username = reqData.username;
        var password = reqData.password;
        User.findOne({where:{username:username}}, function(err, user) {
            if (err || !user || user.password !== password) {
                res.status(403);
                res.json({code:1001, content: 'Password Error'});
                return next();
            } else {
                res.json({code:3000, content:'success'});
                return next();
            }
        });
    }
};


module.exports = users;
