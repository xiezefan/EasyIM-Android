/**
 * Created by xiezefan on 2014/10/4.
 */

var express = require('express');

var router = express.Router();



/* GET home page. */
router.post('/send', function(req, res) {
    console.log("header: " + JSON.stringify(req.headers));
    console.log("User: " + req.user);
    console.log(JSON.stringify(req.body));

    // validate


    res.status(200).json({ content: 'success' });
});

module.exports = router;
