<?php
error_reporting(0);
/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */


require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['shopnum']) && isset($_POST['password'])) {

    // receiving the post params
    $shopnum = $_POST['shopnum'];
    $password = $_POST['password'];

    // get the user by email and password
    $user = $db->getUserByShopNumAndPassword($shopnum, $password);

    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["shopnum"] = $user["shopnum"];
        $response["user"]["name"] =   $user["shopname"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] =  iconv("EUC-KR","UTF-8", "이름, 아이디, 휴대폰 번호, 패스워드를 모두 입력해주세요.");
    echo json_encode($response);
}
?>

