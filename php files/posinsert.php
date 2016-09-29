<?php  
	error_reporting(0); 
	$con=mysqli_connect("localhost","root","dkwkd326","vacantable");  
 
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	$tablenum = $_POST['tablenum'];  
	$empty = $_POST['empty'];  
  	$table = ptable;
  	
	$result = mysqli_query($con,"update $table set empty = $empty where tablenum = $tablenum");  
  
 	 if($result){  
  	  echo "success tablenum :  '$tablenum'  empty :  '$empty'";  
  	 
  	}  
  	else{  
   	 echo 'failure';  
	  }  
  
  
	mysqli_close($con);  
?> 
