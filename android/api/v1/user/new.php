<?php
if($_POST['username'] == "satrichard")
{
?>
{"success": false,
"message":["Username Already Here"]
}
<?php
}
else
{
?>
{
"success":true,
"message":["Welcome"]
}
<?php
}
?>
