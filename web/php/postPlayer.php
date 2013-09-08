<?php
require_once './MySQLConnect.php';
//Check that necessary informations exist
if(isset($_GET['name']) && !empty($_POST['item']))
{
	//Delete current entry for the player
	$db->prepare('DELETE FROM invscan.players WHERE name = :playerName')->execute(array('playerName' => $_GET["name"]));
	//Insert new entry for the player
	$db->prepare('INSERT INTO invscan.players SET name = :playerName, x = :x, y = :y, z = :z')->execute(array('playerName' => $_GET["name"], 'x' => $_GET["x"], 'y' => $_GET["y"], 'z' => $_GET["z"]));
	
	//Delete all entries for the items of the player
	$db->prepare('DELETE FROM invscan.playerItems WHERE playerName = :playerName')->execute(array('playerName' => $_GET["name"]));
	//Prepare the query for inserting the new items
	$itemStmt = $db->prepare('INSERT INTO invscan.playerItems SET playerName = :playerName, slot = :slot, id = :id, damage = :damage, rawName = :rawName, name = :name, quantity = :quantity');
	
	//Go through all the items and execute the prepared statement for them
	foreach($_POST['item'] as $slot => $item)
	{
		$itemStmt->execute(array('playerName' => $_GET["name"], 'slot' => $slot, 'id' => $item["id"], 'damage' => $item["damage"], 'rawName' => $item["rawName"], 'name' => $item["name"], 'quantity' => $item["size"]));
	}
}
?>