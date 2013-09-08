<?php
require_once './MySQLConnect.php';

if(isset($_GET['name']) && !empty($_POST['item']))
{
	$db->prepare('DELETE FROM invscan.players WHERE name = :playerName')->execute(array('playerName' => $_GET["name"]));
	$db->prepare('INSERT INTO invscan.players SET name = :playerName, x = :x, y = :y, z = :z')->execute(array('playerName' => $_GET["name"], 'x' => $_GET["x"], 'y' => $_GET["y"], 'z' => $_GET["z"]));
	
	$db->prepare('DELETE FROM invscan.playerItems WHERE playerName = :playerName')->execute(array('playerName' => $_GET["name"]));
	$itemStmt = $db->prepare('INSERT INTO invscan.playerItems SET playerName = :playerName, slot = :slot, id = :id, damage = :damage, rawName = :rawName, name = :name, quantity = :quantity');
	
	foreach($_POST['item'] as $slot => $item)
	{
		$itemStmt->execute(array('playerName' => $_GET["name"], 'slot' => $slot, 'id' => $item["id"], 'damage' => $item["damage"], 'rawName' => $item["rawName"], 'name' => $item["name"], 'quantity' => $item["size"]));
	}
}
?>