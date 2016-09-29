<?php  
	error_reporting(0); 
	$con=mysqli_connect("localhost","root","dkwkd326","vacantable");  
 
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	$waitingnum = $_POST['waitingnum'];
  	$table = ccaretable;
  	
  	$whether = mysqli_query($con,"select whetherthecall from $table where waitingnum = $waitingnum");
  	$row = mysqli_fetch_assoc($whether);
  	echo 'whetherthecall : '.$row['whetherthecall'].'<br>';
  	
  	
  	if($row['whetherthecall'] == 1){
		$result = mysqli_query($con,"update $table set whetherthecall = 0 where waitingnum = $waitingnum");  
  
 	 	if($result){  
  	  		echo "success waitingnum :  '$waitingnum'  whetherthecall :  0 ";  
  		}
  		else{  
   	 		echo 'failure whetherthcall이 1일 때';
	 	 } 
	}else{
		$result = mysqli_query($con,"update $table set whetherthecall = 1 where waitingnum = $waitingnum");  
  
 	 	if($result){  
  	  		echo "success waitingnum :  '$waitingnum'  whetherthecall :  1 ";  
  		}  
  		else{  
   	 		echo 'failure whetherthecall이 0일 때';
	 	 } 
	 }	 
  
  
	mysqli_close($con);  
?> 
