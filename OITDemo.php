<?php
$con= mysqli_connect("localhost","root","","mydb");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
	
   $username = $_POST['username'];
$sql = "SELECT * FROM OITDemo where username='$username'";
   $result = mysqli_query($con,$sql);
   $row = mysqli_fetch_array($result);

   $data = $row[0] . " " . $row[1];

   if($data){
      echo $data;
   }
	
   mysqli_close($con);
   
?>