<?php
// Mostrar errores para depuración (solo en desarrollo)
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header("Content-Type: application/json; charset=UTF-8");

// Configuración de la base de datos
$host = "localhost";
$db_name = "pasteleria_db";
$username = "root";
$password = "1234"; 

try {
    $conn = new PDO("mysql:host=$host;dbname=$db_name", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    http_response_code(500);
    echo json_encode(["message" => "Error de conexión DB: " . $e->getMessage()]);
    exit();
}

// Obtener la ruta de la solicitud
$request_uri = $_SERVER['REQUEST_URI'];
$method = $_SERVER['REQUEST_METHOD'];

// Verificar que los datos JSON sean válidos
$json_input = file_get_contents("php://input");
$data = json_decode($json_input);

try {
    // Rutas básicas
    // Usamos strpos para ser flexibles si la ruta incluye 'index.php' o carpetas
    if (strpos($request_uri, '/api/usuarios') !== false && $method == 'POST') {
        // Registrar usuario
        if ($data && !empty($data->nombre) && !empty($data->correo) && !empty($data->password)) {
            // Verificar duplicados
            $checkQuery = "SELECT id FROM usuarios WHERE correo = :correo";
            $checkStmt = $conn->prepare($checkQuery);
            $checkStmt->bindParam(":correo", $data->correo);
            $checkStmt->execute();
            
            if ($checkStmt->rowCount() > 0) {
                http_response_code(409); 
                echo json_encode(["message" => "El correo ya está registrado."]);
            } else {
                $query = "INSERT INTO usuarios (nombre, correo, password, telefono, direccion, edad, descuento, fechaRegistro) VALUES (:nombre, :correo, :password, :telefono, :direccion, :edad, :descuento, :fechaRegistro)";
                $stmt = $conn->prepare($query);
                
                $stmt->bindParam(":nombre", $data->nombre);
                $stmt->bindParam(":correo", $data->correo);
                $stmt->bindParam(":password", $data->password);
                
                $telefono = isset($data->telefono) ? $data->telefono : "";
                $stmt->bindParam(":telefono", $telefono);
                
                $stmt->bindParam(":direccion", $data->direccion);
                $stmt->bindParam(":edad", $data->edad);
                $stmt->bindParam(":descuento", $data->descuento);
                $stmt->bindParam(":fechaRegistro", $data->fechaRegistro);

                if ($stmt->execute()) {
                    http_response_code(201);
                    echo json_encode(["message" => "Usuario creado."]);
                } else {
                    http_response_code(503);
                    echo json_encode(["message" => "No se pudo crear el usuario."]);
                }
            }
        } else {
            http_response_code(400);
            echo json_encode(["message" => "Datos incompletos."]);
        }
    } elseif (strpos($request_uri, '/api/login') !== false && $method == 'POST') {
        // Login
        if ($data && !empty($data->correo) && !empty($data->password)) {
            $query = "SELECT * FROM usuarios WHERE correo = :correo LIMIT 1";
            $stmt = $conn->prepare($query);
            $stmt->bindParam(":correo", $data->correo);
            $stmt->execute();
            
            // Verificar explícitamente si hay filas
            if ($stmt->rowCount() > 0) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($user) {
                    if ($data->password == $user['password']) {
                        http_response_code(200);
                        unset($user['password']); // No devolver la contraseña
                        echo json_encode($user);
                    } else {
                        http_response_code(401);
                        echo json_encode(["message" => "Contraseña incorrecta."]);
                    }
                } else {
                    // Esto no debería pasar si rowCount > 0, pero por seguridad
                    http_response_code(404);
                    echo json_encode(["message" => "Error al recuperar usuario."]);
                }
            } else {
                // IMPORTANTE: Si no hay filas, devolvemos 404 explícitamente
                http_response_code(404);
                echo json_encode(["message" => "El correo no está registrado."]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["message" => "Faltan datos de acceso."]);
        }
    } else {
        if ($method == 'POST') {
            http_response_code(404);
            echo json_encode(["message" => "Ruta no encontrada: $request_uri"]);
        }
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["message" => "Error del servidor: " . $e->getMessage()]);
}
?>