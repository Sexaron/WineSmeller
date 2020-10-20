<?php
	include ('connection.php')
	
	error_reporting(0);
	
	$email			= $_POST["email"]
	$password		= $_POST["password"]
	
	$rq				= "INSERT INTO USER_REGISTRATION (ID,EMAIL,PASSWORD) VALUES (NULL,'$email',MD5('$password'))";
	
	mysqli_query($conn, $rq);
	
	mysql_close($conn);	
	
?>