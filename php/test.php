<?php  
	error_reporting(0); 
	$con=mysqli_connect("localhost","root","dkwkd326","postable");  
 
	mysqli_set_charset($con,"utf8");
  
	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
  	$table = ccaretable;
  	
  	
  	
  	$shopnum = $_POST['shopnum'];
	$whetherthecall =  $_POST['whetherthecall'];
	
	$result = mysqli_query($con,"select * from $table where shopnum = $shopnum and whetherthecall = $whetherthecall");  
	$total_record = mysqli_num_rows($result);	
  
 	if($result){  
  	  	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

 

   		// 반환된 각 레코드별로 JSONArray 형식으로 만들기.

  		 for ($i=0; $i < $total_record; $i++)                    
		
  		 {

     		 // 가져올 레코드로 위치(포인터) 이동  

     		 mysqli_data_seek($result, $i);       

        

      		$row = mysqli_fetch_array($result);

   		echo "{\"shopnum\":\"$row[shopnum]\",\"userid\":\"$row[userid]\",\"waitingnum\":\"$row[waitingnum]\",\"persons\":\"$row[persons]\",\"whetherthecall\":\"$row[whetherthecall]\",\"issuingtime\":\"$row[issuingtime]\"}";

 

  		 // 마지막 레코드 이전엔 ,를 붙인다. 그래야 데이터 구분이 되니깐.  

  		 if($i<$total_record-1){

    		  echo ",";

   		}
	
    

   		}

   		// JSONArray의 마지막 닫기

  		 echo "]}";
  	}else{  
   	 		echo 'failure';
	 	 } 
	
?> 
