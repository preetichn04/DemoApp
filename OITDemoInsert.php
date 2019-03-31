<?php
$con= mysqli_connect("localhost","root","","mydb");

// Check connection
if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
	
$firstname = $_POST['first_name'];
$lastname = $_POST['last_name'];
$username = $_POST['username'];

// prepare and bind
$stmt = $con->prepare("INSERT INTO OITDemo (first_name, last_name, username) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $firstname, $lastname, $username);


$stmt->execute();

echo "Submitted successfully";

$stmt->close();
$con->close();

?>