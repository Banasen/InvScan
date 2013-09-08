<?php
// MySQL-Data EXAMPLE
$mysql_data = array(
	"host" => "localhost",
	"user" => "invscan",
	"password" => "scaninvmc18",
	"database" => "invscan");

$db = new PDO('mysql:'.$mysql_data["host"].';dbname='.$mysql_data["user"], $mysql_data["database"], $mysql_data["password"], array(PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC));
?>