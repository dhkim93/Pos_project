<?
	error_reporting(0); 
    // �����ͺ��̽� ���� ���ڿ�. (db��ġ, ���� �̸�, ��й�ȣ)

    $connect=mysqli_connect( "localhost", "root", "dkwkd326", "postable");

	mysqli_set_charset($connect,"utf8");
	
	if (mysqli_connect_errno($connect))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
   session_start();

 
   // ������ ����

   $sql = "select * from ccaretable";


   // ���� ���� ����� $result�� ����

   $result = mysqli_query($connect, $sql);

   // ��ȯ�� ��ü ���ڵ� �� ����.

   $total_record = mysqli_num_rows($result);

 

   // JSONArray �������� ����� ���ؼ�...

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

?>