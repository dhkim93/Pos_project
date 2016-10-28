<?php  
	$con=mysqli_connect("localhost","root","dkwkd326","postable");  
 
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	$tables = $_POST['tables'];  
	$empty = $_POST['empty'];  
  	$table = emtable;
  	
	$result = mysqli_query($con,"insert into $table (tables,empty) values ('$tables','$empty')");  
  
 	 if($result){  
  	  echo "success tables :  '$tables'  empty :  '$empty'";  
  	 
  	}  
  	else{  
   	 echo 'failure';  
	  }  
  
  
	mysqli_close($con);  
?> 
