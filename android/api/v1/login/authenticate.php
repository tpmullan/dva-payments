<?php

$username = $_POST['username'];
$password = $_POST['password'];

if($username == "satrichard" && $password== "fucker")
{
?>{
   "success":true,
   "message":["You have login" ],
   "sessionToken":"DEADBEEFDEADBEEF",
   "userId": "1"
}
<?php
}
else
{
?>
{
"success":false,
"message":["Invalid Username","Invalid Password"],
"sessionToken":"0",
"userId":"0"
}
<?php
}
