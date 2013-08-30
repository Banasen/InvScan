<?php
require_once './MySQLConnect.php';

$stmt = $db->prepare('SELECT itemRawName, itemName FROM invscan.items ORDER BY itemName ASC');
$stmt->execute();
$result = $stmt->fetchAll();
$toSend = array();
foreach($result as $i => $stack)
{
	if (!file_exists("../texture/".$stack["itemRawName"].".png"))
	{
		$toSend[$i] = $stack;
	}

}
echo json_encode($toSend);
?>
