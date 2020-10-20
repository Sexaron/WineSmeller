<?php

include ('connection.php');

error_reporting(0);

$name					= $_POST["name"];
$alcohol				= $_POST["alcohol"];
$year					= $_POST["year"];
$winery					= $_POST["winery"];
$bar_code				= $_POST["bar_code"];
$denomination_of_origin	= $_POST["denomination_of_origin"];
$ecologic				= $_POST["ecologic"];
$aging					= $_POST["aging"];
$country				= $_POST["country"];
$type_of_grape			= $_POST["type_of_grape"];
$type_of_wine			= $_POST["type_of_wine"];
$rating					= $_POST["rating"];
$type_of_wine			= $_POST["NUMBER_OF_VOTES"];
$type_of_wine			= $_POST["description"];

$rq	= "insert into WINES(	NAME,ALCOHOL,YEAR,WINERY,BAR_CODE,DENOMINATION_OF_ORIGIN,
							ECOLOGIC,AGING,COUNTRY,TYPE_OF_GRAPE,TYPE_OF_WINE,RATING,
							NUMBER_OF_VOTES,DESCRIPTION ) values (
							'PRUEBA','13.4','2222','PROBANDO BODEGA',
							'00000223','ESPAÑA','0','CRIANZA','ESPAÑA',
							'muchas','tinto','9','3','DESCRIPCION MOLA MAZO')";

//('$name','$alcohol','$year','$winery','$bar_code','$denomination_of_origin','$ecologic','$aging','$country','$type_of_grape','$type_of_wine')";
mysqli_query($conn, $rq);

$rq	= "insert into WINES(	NAME,ALCOHOL,YEAR,WINERY,BAR_CODE,DENOMINATION_OF_ORIGIN,
							ECOLOGIC,AGING,COUNTRY,TYPE_OF_GRAPE,TYPE_OF_WINE,RATING,
							NUMBER_OF_VOTES,DESCRIPTION ) values (
							'PRUEBA','13.4','2222','PROBANDO BODEGA',
							'00000223','ESPAÑA','0','CRIANZA','ESPAÑA',
							'muchas','tinto','9','3','DESCRIPCION MOLA MAZO')";

mysqli_query($conn, $rq);

$rq	= "insert into WINES(	NAME,ALCOHOL,YEAR,WINERY,BAR_CODE,DENOMINATION_OF_ORIGIN,
							ECOLOGIC,AGING,COUNTRY,TYPE_OF_GRAPE,TYPE_OF_WINE,RATING,
							NUMBER_OF_VOTES,DESCRIPTION ) values (
							'PRUEBA','13.4','2222','PROBANDO BODEGA',
							'00000223','ESPAÑA','0','CRIANZA','ESPAÑA',
							'muchas','tinto','9','3','DESCRIPCION MOLA MAZO')";

mysqli_query($conn, $rq);

mysql_close($conn);
?>