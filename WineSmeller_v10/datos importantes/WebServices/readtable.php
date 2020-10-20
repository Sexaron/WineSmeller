<?php

include ('connection.php');

$rs = mysqli_query($conn, "select * from WINES");

while ( $row = mysqli_fetch_array($rs,MYSQL_ASSOC) ) {
	echo $row['NAME'];
}

mysqli_close($conn);

?>


