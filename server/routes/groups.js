var redis = require('redis');
var Config = require('../config/server-config');
var Util = require('../common/unit.js');
var Group = require('../models/group.js');

var groups = {
    create : function(req, res, next) {
        var reqData = req.body;
        var user = req.user;

        var group = new Group();
        group.name = reqData.name;
        group.save(function(err, group) {
            group.addUsers(user);
        });
    },
    join : function(req, res, next) {
        var reqData =  req.body;
        var params = req.params;
        var user = req.user;
        Group.findOne({where:{id:params.id}}, function(err, group) {
            group.addUsers(user);
        });

    }

};

function createGroup(users, callback) {

}