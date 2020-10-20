<?php

error_reporting(0);

$conn	= mysql_connect("localhost", "193651", "Mnb89bnm", "193651");

if(!$conn) {
	die("Error al intentar conectarse al servidor" . $mysqli_connect_error());
}

$response = array();

$sql_query = "select * from WINES";

$result = mysqli_query($conn, $sql_query);

if (mysqli_num_rows($result) > 0 ) {
	
	while ($row = mysqli_fetch_assoc($result)){
		array_push($response, $row);
	}
} else {
	$response['success'] = 0;
	$response['message'] = 'No data';
}

echo json_encode($response);
mysql_close();

?>


