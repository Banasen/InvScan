<?php
require_once("./MySQLConnect.php");

$query = mysql_query("SELECT * FROM `invscan`.`invs` WHERE `name` LIKE '".$_GET["name"]."';");
$result = mysql_fetch_assoc($query);
$toSend = array();
foreach ($result as $index => $filename)
{
	if ($index != "name" && $index != "timestamp")
	{
		if (!file_exists("../texture/".$filename.".png"))
		{
			$filename = "unknown";
		}
		$toSend[$index] = "./texture/".$filename.".png";
	}
}
echo json_encode($toSend);
mysql_close($mysql_link);
?>