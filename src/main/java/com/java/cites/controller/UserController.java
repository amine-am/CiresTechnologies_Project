package com.java.cites.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.java.cites.model.ImportSummary;
import com.java.cites.model.User;
import com.java.cites.service.UserService;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateUsers(@RequestParam int count) {
        return userService.generateUsers(count);
        }


    @PostMapping(value = "/batch", consumes = {"multipart/form-data"})
    public ResponseEntity<ImportSummary> uploadUsers(@RequestParam("file") MultipartFile file) {
        ImportSummary importSummary = userService.importUsersFromFile(file);
        return ResponseEntity.ok(importSummary);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> showUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.showUser(username));
    }


    @Autowired
    private ObjectMapper objectMapper;
    public void parseJson(String jsonString) {
        try {
            User user = objectMapper.readValue(jsonString, User.class);
            // 'user' contains the parsed data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

