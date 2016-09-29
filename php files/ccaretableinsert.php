<?php  
	error_reporting(0); 
	$con=mysqli_connect("localhost","root","dkwkd326", "vacantable");  
	
	
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
	
	$shopnum = $_POST['shopnum'];  
	$userid = $_POST['userid'];  
	$persons = $_POST['persons'];  
	
	$sql = "select * from ccaretable where shopnum='$shopnum'";
    $rows = mysqli_query($con, $sql);
	$waitingnum = mysqli_num_rows($rows)+1; 
  	$table = ccaretable;
  	
	$result = mysqli_query($con,"insert into $table (shopnum, userid, waitingnum, persons, issuingtime) values ('$shopnum', '$userid', '$waitingnum', '$persons', NOW())");  
  
 	 if($result){  
  	  echo "success shopnum :  '$shopnum'  userid :  '$userid'  waitingnum :  '$waitingnum'  persons :  '$persons'";  
  	 
  	}  
  	else{  
   	 echo 'failure';  
	  }  
  
  
	mysqli_close($con);  
?> 
