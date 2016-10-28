<?php
 
	$con=mysqli_connect("vacantable2.cluxbygujyfw.ap-northeast-2.rds.amazonaws.com","wlsdnghkd123","dkwkd326", "vacantable");  
	
	mysqli_set_charset($con,"utf8");
	
	// json response array
	$response = array("error" => FALSE);

	if (mysqli_connect_errno($con))  
	{  
   		echo "Failed to connect to MySQL: " . mysqli_connect_error();  
	}  
	
	
class DB_Functions {


    // constructor
    function __construct() {
       
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
   
    public function getUserByIdAndPassword($user_id, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE user_id = ?");

        $stmt->bind_param("s", $user_id);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }


    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

$db = new DB_Functions();



    // receiving the post params
    $user_id = 'slipout123';
    $password = 'dkwkd326';

    // get the user by email and password
    $user = $db->getUserByIdAndPassword($user_id, $password);

    if ($user != false) {
        // use is found
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["name"] = $user["name"];
        $response["user"]["user_id"] = $user["user_id"];
        $response["user"]["created_at"] = $user["created_at"];
        $response["user"]["updated_at"] = $user["updated_at"];
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }

?>


