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
            $response["error_msg"] = iconv("EUC-KR","UTF-8", "가입 중 알 수 없는 에러가 발생하였습니다.");
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] =  iconv("EUC-KR","UTF-8", "매장고유번호 혹은 패스워드가 빠졌습니다.");
    echo json_encode($response);
}
?>


