<?php
/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();


// json response array
$response = array("error" => FALSE);

if (isset($_POST['password'])) {

    // receiving the post params
    $password = $_POST['password'];

        // create a new user
        $user = $db->storeUser($password);
        if(!$user){
            $response["error"] = TRUE;
            $response["error_msg"] = iconv("EUC-KR","UTF-8", "���� �� �� �� ���� ������ �߻��Ͽ����ϴ�.");
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] =  iconv("EUC-KR","UTF-8", "���������ȣ Ȥ�� �н����尡 �������ϴ�.");
    echo json_encode($response);
}
?>

