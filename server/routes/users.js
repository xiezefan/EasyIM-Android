var express = require('express');
var router = express.Router();

/* GET users listing. */
router.post('/', function(req, res) {
  res.send('respond with a resource');
});

var users = {
    register : function(req, res, next) {

    }
};

module.exports = router;
