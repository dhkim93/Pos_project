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

 

   		// ��ȯ�� �� ���ڵ庰�� JSONArray �������� �����.

  		 for ($i=0; $i < $total_record; $i++)                    
		
  		 {

     		 // ������ ���ڵ�� ��ġ(������) �̵�  

     		 mysqli_data_seek($result, $i);       

        

      		$row = mysqli_fetch_array($result);

   		echo "{\"shopnum\":\"$row[shopnum]\",\"userid\":\"$row[userid]\",\"waitingnum\":\"$row[waitingnum]\",\"persons\":\"$row[persons]\",\"whetherthecall\":\"$row[whetherthecall]\",\"issuingtime\":\"$row[issuingtime]\"}";

 

  		 // ������ ���ڵ� ������ ,�� ���δ�. �׷��� ������ ������ �Ǵϱ�.  

  		 if($i<$total_record-1){

    		  echo ",";

   		}
	
    

   		}

   		// JSONArray�� ������ �ݱ�

  		 echo "]}";
  	}else{  
   	 		echo 'failure';
	 	 } 
	
?> 
