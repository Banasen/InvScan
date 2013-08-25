<?php
require_once("./MySQLConnect.php");

$query = mysql_query("SELECT `name` FROM `invscan`.`invs` ORDER BY `name` ASC;");
$toSend = array();
while ($result = mysql_fetch_assoc($query))
{
	$toSend[count($toSend)] = $result["name"];
}
echo json_encode($toSend);
mysql_close($mysql_link);
?>