<?php
require_once './MySQLConnect.php';

if((isset($_GET['name']) || (isset($_GET["x"]) && isset($_GET["y"]) && isset($_GET["z"]))) && !empty($_POST['item']))
{
	$var = "Stuff gets executed";
	$db->prepare('DELETE FROM invscan.chests WHERE (x = :chestX AND y = :chestY AND z = :chestZ) OR name = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]));
	$db->prepare('INSERT INTO invscan.chests SET x = :chestX, y = :chestY, z = :chestZ, playerName = :playerName, name = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'playerName' => $_GET["playerName"], 'chestName' => $_GET["name"]));
	
	$db->prepare('DELETE FROM invscan.chestItems WHERE (chestX = :chestX AND chestY = :chestY AND chestZ = :chestZ) OR chestName = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]));
	$itemStmt = $db->prepare('INSERT INTO invscan.chestItems SET chestX = :chestX, chestY = :chestY, chestZ = :chestZ, chestName = :chestName, id = :id, damage = :damage, rawName = :rawName, name = :name, quantity = :size');
	
	foreach($_POST['item'] as $item)
	{
		$itemStmt->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]) + $item);
	}
}
?>