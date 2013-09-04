<?php
require_once './MySQLConnect.php';

$stmt = $db->query('SELECT playerName FROM invscan.inventories ORDER BY playerName ASC');
$toSend = array();
while ($result = $stmt->fetch())
{
	$toSend[count($toSend)] = $result["playerName"];
}
echo json_encode($toSend);
?>