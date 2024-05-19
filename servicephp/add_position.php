<?php
require_once 'config.php';
// array for JSON response
$response = array();

// Check if parameters are set
if(isset($_POST['pseudo']) && isset($_POST['longitude']) && isset($_POST['latitude'])) {
    
    // Assigning GET parameters to variables
    //$idposition = $_POST['idposition'];
    $pseudo = $_POST['pseudo'];
    $longitude = $_POST['longitude'];
    $latitude = $_POST['latitude'];

    // Establish connection
    $con= mysqli_connect($server,$user,$mp,$database,$port);
    
    // Insert into Position table
    $result = mysqli_query($con, "INSERT INTO Position (pseudo, longitude, latitude) 
                VALUES ('$pseudo', '$longitude', '$latitude')");

    // Check if query executed successfully
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Position added successfully.";
    } else {
        $response["success"] = 0;
        $response["message"] = "Error adding position.";
    }

} else {
    // Parameters not set
    $response["success"] = 0;
    $response["message"] = "Missing parameters. Please provide  pseudo, longitude, and latitude.";
}

// Echo JSON response
echo json_encode($response);
?>
