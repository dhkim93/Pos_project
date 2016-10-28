<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */


require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['name']) && isset($_POST['user_id']) && isset($_POST['password'])) {

	
    // receiving the post params
    $name = $_POST['name'];
    $user_id = $_POST['user_id'];
    //$phone = $_POST['phone'];
    $password = $_POST['password'];

    // check if user is already existed with the same email
    if ($db->isUserExisted($user_id)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $user_id;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $user_id, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["user_id"] = $user["user_id"];
            //$response["user"]["phone"] = $user["phone"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "가입도중 알 수 없는 에러가 발생했습니다!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "이름, 휴대폰 번호 또는 패스워드가 빠졌습니다.";
    echo json_encode($response);
}
?>


