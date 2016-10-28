<?php  
	error_reporting(0); 
	$con=mysqli_connect("vacantable2.cluxbygujyfw.ap-northeast-2.rds.amazonaws.com","wlsdnghkd123","dkwkd326","vacantable") or
	die("SQL server에 연결할 수 없습니다.");  
 
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
	if(isset($_POST['tablenum']) && isset($_POST['empty'])){
	
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
	}
  
	mysqli_close($con);  
?> 

