<?php
//SEt correct content-type header for json
header("Content-Type: application/json");
require_once './MySQLConnect.php';

if(isset($_GET['name']) || (isset($_GET["x"]) && isset($_GET["y"]) && isset($_GET["z"])))
{
	//Prepare the query, execute it with the variables, and fetch it
	$result = $db->prepare('SELECT id, damage, rawName, name, quantity FROM invscan.chestItems WHERE (chestX = :chestX AND chestY = :chestY AND chestZ = :chestZ) OR chestName = :chestName')->execute(array('chestX' => $_GET["x"], 'chestY' => $_GET["y"], 'chestZ' => $_GET["z"], 'chestName' => $_GET["name"]))->fetchAll();
	//Empty array for the stacks that are going to be send
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