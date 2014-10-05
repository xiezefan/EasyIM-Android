/**
 * Created by xiezefan on 2014/10/4.
 */
var restify = require('restify');
var passport = require('passport');
var Config = require('./config/server-config');
var Auth = require('./common/auth-helper');

var User = require('./routes/users')

var server = restify.createServer({
    name: Config.name,
    versions: Config.versions
});

server.use(restify.pre.userAgentConnection());
server.use(restify.acceptParser(server.acceptable));
server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(passport.initialize());
passport.use(Auth.UserAuthorization);

// static files
server.get(/^\/((.*)(\.)(.+))*$/, restify.serveStatic({ directory: 'public', default: "index.html" }));

server.post('/test', passport.authenticate('basic', { session: false }), function (req, res, next) {
    console.log("header: " + JSON.stringify(req.headers));
    console.log("User: " + req.user);
    console.log(JSON.stringify(req.body));

    // validate
    res.json({ content: 'success' });
    next();
});

server.post("/user/register", User.register);
server.post("/user/login", User.login);

server.listen(Config.port, function () {
    console.log('%s listening at %s', server.name, server.url);
});