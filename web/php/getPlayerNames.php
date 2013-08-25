<?php
require_once './MySQLConnect.php';

$result = $db->query('SELECT playerName FROM invscan.inventories ORDER BY playerName ASC')->fetchAll();
echo json_encode($result);
?>