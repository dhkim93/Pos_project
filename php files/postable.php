<?
	error_reporting(0); 
    // �����ͺ��̽� ���� ���ڿ�. (db��ġ, ���� �̸�, ��й�ȣ)

    $connect=mysql_connect( "localhost", "root", "dkwkd326") or  

        die( "SQL server�� ������ �� �����ϴ�.");

 

    

    mysql_query("SET NAMES UTF8");

   // �����ͺ��̽� ����

   $db=mysql_select_db("vacantable",$connect);
	
 
   // ���� ����

   session_start();

 

   // ������ ����

   $sql = "select * from ptable";

 

   // ���� ���� ����� $result�� ����

   $result = mysql_query($sql, $connect);

   // ��ȯ�� ��ü ���ڵ� �� ����.

   $total_record = mysql_num_rows($result);

 

   // JSONArray �������� ����� ���ؼ�...

   echo "{\"status\":\"OK\",\"num_results\":{\"rows\":\"$total_record\"},\"results\":[";

 

   // ��ȯ�� �� ���ڵ庰�� JSONArray �������� �����.

   for ($i=0; $i < $total_record; $i++)                    

   {

      // ������ ���ڵ�� ��ġ(������) �̵�  

      mysql_data_seek($result, $i);       

        
      $row = mysql_fetch_array($result);

   echo "{\"shopnum\":\"$row[shopnum]\",\"tablenum\":\"$row[tablenum]\",\"empty\":\"$row[empty]\"}";

 

   // ������ ���ڵ� ������ ,�� ���δ�. �׷��� ������ ������ �Ǵϱ�.  

   if($i<$total_record-1){

      echo ",";

   }

    

   }

   // JSONArray�� ������ �ݱ�

   echo "]}";

?>