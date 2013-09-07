<?php
header("Content-Type: application/json");
require_once './MySQLConnect.php';

$stmt = $db->query('SELECT name FROM invscan.players ORDER BY name ASC');
$toSend = array();
while ($result = $stmt->fetch())
{
	$toSend[count($toSend)] = $result["name"];
}
echo json_encode($toSend);
?>