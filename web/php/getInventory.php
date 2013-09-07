<?php
header("Content-Type: application/json");
require_once './MySQLConnect.php';

if(isset($_GET['name']))
{
	//Prepare the query
	$stmt = $db->prepare('SELECT rawName, name, slot, quantity FROM invscan.playerItems WHERE playerName = ? ORDER BY slot ASC');
	//Execute the query with the player name
	$stmt->execute(array($_GET["name"]));
	//Fetch the result
	$result = $stmt->fetchAll();
	$toSend = array();
	//Check through all the returned stacks
	foreach($result as $i => $stack)
	{
		//Replace not existing images by the unknown-image
		if (!file_exists("../texture/".$stack["rawName"].".png"))
		{
			$stack["rawName"] = "unknown";
		}
		//Add the animation to the stack if there's a .txt file, empty array if not
		if (!$stack["animation"] = file("../texture/".$stack["rawName"].".txt"))
		{
			$stack["animation"] = array();
		}
		$toSend[$i] = $stack;
	}
	//Encode and send
	echo json_encode($toSend);
}
?>