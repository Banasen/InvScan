<?php
require_once './MySQLConnect.php';

if(isset($_GET['name']))
{
	$stmt = $db->prepare('SELECT itemRawName, itemName, slot, quantity FROM invscan.items WHERE playerName = :name ORDER BY slot ASC');
	$stmt->execute($_GET);
	$result = $stmt->fetchAll();
	$toSend = array();
	foreach($result as $i => $stack)
	{
		if (!file_exists("../texture/".$stack["itemRawName"].".png"))
		{
			$stack["itemRawName"] = "unknown";
		}
		$toSend[$i] = $stack;
	}
	echo json_encode($toSend);
}
?>