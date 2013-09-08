<?php
require_once './MySQLConnect.php';
//Check that necessary informations exist
if((isset($_GET['name']) || (isset($_GET["x"]) && isset($_GET["y"]) && isset($_GET["z"]))) && !empty($_POST['item']))
{
	//Delete the current entry for the chest
	$db->prepare('DELETE FROM invscan.chests WHERE (x = :chestX AND y = :chestY AND z = :chestZ) OR name = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]));
	//Insert new entry for the chest
	$db->prepare('INSERT INTO invscan.chests SET x = :chestX, y = :chestY, z = :chestZ, playerName = :playerName, name = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'playerName' => $_GET["playerName"], 'chestName' => $_GET["name"]));
	
	//Delete all entries for the items of the chest
	$db->prepare('DELETE FROM invscan.chestItems WHERE (chestX = :chestX AND chestY = :chestY AND chestZ = :chestZ) OR chestName = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]));
	//Prepare the query for inserting the new items
	$itemStmt = $db->prepare('INSERT INTO invscan.chestItems SET chestX = :chestX, chestY = :chestY, chestZ = :chestZ, chestName = :chestName, id = :id, damage = :damage, rawName = :rawName, name = :name, quantity = :quantity');
	
	//Go through all the items and execute the prepared statement for them
	foreach($_POST['item'] as $item)
	{
		$itemStmt->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"], 'id' => $item["id"], 'damage' => $item["damage"], 'rawName' => $item["rawName"], 'name' => $item["name"], 'quantity' => $item["size"]));
	}
}
?>