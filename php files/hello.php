<?php
function customError($errno, $errstr)
 { 
 echo "<p><strong>Error:</strong> [$errno] $errstr</p>";
 }

//에러 핸들러 세팅
set_error_handler("customError");
	$con=mysqli_connect("vacantable2.cluxbygujyfw.ap-northeast-2.rds.amazonaws.com","wlsdnghkd123","dkwkd326", "vacantable");  
	
	mysqli_set_charset($con,"utf8");
	
	if($con){
		echo "기무치!";
	}

	// json response array
	$response = array("error" => FALSE);
  
	echo "hello"
?>
