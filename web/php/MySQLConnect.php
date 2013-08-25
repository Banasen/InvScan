<?php
// MySQL-Data #EXAMPLE CONFIG
$mysql_data = array(
	"host" => "localhost",
	"user" => "user",
	"password" => "password",
	"database" => "invscan");

// Test mysql connection
$mysql_link = mysql_connect($mysql_data["host"], $mysql_data["user"], $mysql_data["password"], $mysql_data["database"]);
if (mysqli_connect_errno($mysql_link))
{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	exit();
}
if (!mysql_select_db($mysql_data["database"]))
{
	echo "Unable to select database: " . mysql_error();
	exit();
}
?>