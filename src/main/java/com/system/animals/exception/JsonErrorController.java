package com.system.animals.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JsonErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                new ServletWebRequest(request),
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));

        int statusCode = (int) attributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("Message", attributes.getOrDefault("message", status.getReasonPhrase()));
        body.put("Status", status.value());

        return ResponseEntity.status(status).body(body);
    }
}
