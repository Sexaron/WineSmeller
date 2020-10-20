<?php

$hostname	= 'localhost';
$username	= '193651';
$password	= 'Mnb89bnm';
$database	= '193651';

$conn		= mysqli_connect($hostname, $username, $password, $database);
if(!$conn){
	die("No se puede conectar a la base de datos: " . mysql_error());
}

?>