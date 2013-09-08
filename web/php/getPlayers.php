<?php
//Set correct content-type header for json
header("Content-Type: application/json");

require_once './MySQLConnect.php';
//Get all the player names
$stmt = $db->query('SELECT name FROM invscan.players ORDER BY name ASC');
//Empty array for the names that are going to be send
$toSend = array();
//Go through all the results and fix the nesting (from $results[$i]["name"] to $toSend[$i])
while ($result = $stmt->fetch())
{
	$toSend[count($toSend)] = $result["name"];
}
//Encode and send
echo json_encode($toSend);
?>