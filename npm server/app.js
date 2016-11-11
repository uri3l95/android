var express = require('express');
var app = express();

app.get('/', function (req, res) {
  res.send('Hello World!');
});


app.post('/MyPersons',function(request,response){
	var id=request.query.userId;
	console.log('Trying to get my persons for user with id: '+id);
	var mysql = require('mysql');
    var connection = mysql.createConnection({
      host     : '127.0.0.1',
      user     : 'root',
      password : '',
      database : 'persons'
    });
    connection.connect();
    var query='SELECT * from person where UserID='+id;
    connection.query(query, function(err, row, fields) {
      console.log("Retrieving results...");
      response.json(row);
    });
    connection.end();

});


app.post('/Login', function(request, response) {
  var username=request.query.username;
  var password=request.query.password;
  console.log('Trying to log user: '+username);
  var mysql = require('mysql');
    var connection = mysql.createConnection({
      host     : '127.0.0.1',
      user     : 'root',
      password : '',
      database : 'persons'
    });

    connection.connect();
    var query='SELECT * from user where Username="'+username+'" and Password="'+password+'"';
    connection.query(query, function(err, row, fields) {
      console.log("Retrieving results...");
      if (!err){  
        if(row.length>0)
        {    console.log("User login with success...");
            response.json({data:row});
        }else{
            console.log("User login with failure");
            response.json({data:null});
        }
      }
    });
    connection.end();
});


app.post('/Register', function(request, response) {
  var firstname=request.query.firstname;
  var lastname=request.query.lastname;
  var username=request.query.username;
  var password=request.query.password;
  console.log('Trying to register user: '+username);
  var mysql      = require('mysql');
    var connection = mysql.createConnection({
      host     : '127.0.0.1',
      user     : 'root',
      password : '',
      database : 'persons'
    });

    connection.connect();
    var query='INSERT INTO user(FirstName,LastName,Username,Password)'+
                        'VALUES("'+firstname+'","'+lastname+'","'+username+'","'+password+'")';
    connection.query(query, function(err, row, fields) {
      console.log("Retrieving results...");
      if (!err){  
            console.log("Register user with success...");
            response.json({valid:1});
      }
      else{
        console.log("Username already exist..");
        response.json({valid:0});
      }
    });
    connection.end();
});


app.post('/AddPerson',function(request, response) {
  var firstname=request.query.firstname;
  var lastname=request.query.lastname;
  var email=request.query.email;
  var age=request.query.age;
  var phone=request.query.phone;
  var country=request.query.country;
  var userid=request.query.userid;
  console.log('Trying to add person: '+email);
  var mysql      = require('mysql');
    var connection = mysql.createConnection({
      host     : '127.0.0.1',
      user     : 'root',
      password : '',
      database : 'persons'
    });

    connection.connect();
    var query='INSERT INTO person(FirstName,LastName,Email,Age,Phone,Country,UserID)'+
                        'VALUES("'+firstname+'","'+lastname+'","'+email+'",'+age+',"'+phone+'","'+country+'",'+userid+')';
    connection.query(query, function(err, row, fields) {
      console.log("Retrieving results...");
      if (!err){  
            console.log("Person added with success...");
            response.json({valid:1});
      }
      else{
        console.log("Person already exist..");
        response.json({valid:0});
      }
    });
    connection.end();
});


app.listen(3000,'89.137.110.65', function () {
  console.log('App listening on port 3000!');
});