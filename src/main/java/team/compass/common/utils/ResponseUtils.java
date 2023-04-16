package team.compass.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {
    public static ResponseEntity<Object> ok(String message, Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", true);
        response.put("code", HttpStatus.OK.value());
        response.put("message", message);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<Object> notFound(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", false);
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    public static ResponseEntity<Object> badRequest(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess", false);
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

