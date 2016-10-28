<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
        
        mysqli_set_charset($this->conn,"utf8");
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->stmt_init();
        $stmt->prepare("INSERT INTO posmanager(unique_id, encrypted_password, salt, created_at) VALUES(?, ?, ?, NOW())");
        $stmt->bind_param("sss", $uuid, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
        return $user; //아래 if문 사용하려면 이거 빼야함

        // check for successful store
      //  if ($result) {
      //      $stmt = $this->conn->stmt_init();
      //      $stmt->prepare("SELECT * FROM users WHERE  = ?");
      //      $stmt->bind_param("s", $email);
      //      $stmt->execute();
      //      $user = $stmt->get_result()->fetch_assoc();
      //      $stmt->close();

      //      return $user;
       // } else {
       //     return false;
       // }
    }

    /**
     * Get user by email and password
     */
    public function getUserByShopNumAndPassword($shopnum, $password) {

        $stmt = $this->conn->stmt_init();
        $stmt->prepare("SELECT * FROM posmanager WHERE shopnum = ?");
        $stmt->bind_param("s", $shopnum);

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
     * Check user is existed or not
     */
    public function isUserExisted($shopnum) {
        $stmt = $this->conn->prepare("SELECT * from users WHERE shopnum = ?");

        $stmt->bind_param("s", $shopnum);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
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

?>
