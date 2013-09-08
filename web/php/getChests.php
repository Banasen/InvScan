<?php
//Set correct content-type header for json
header("Content-Type: application/json");
require_once './MySQLConnect.php';
//Get all chests
$result = $db->query('SELECT x, y, z, name FROM invscan.chests ORDER BY name ASC')->fetchAll();
//No fixing of nesting necessary because we need arrays ($result[$i] = array($x, $y, $z, $name))
//Encode and send
echo json_encode($result);
?>