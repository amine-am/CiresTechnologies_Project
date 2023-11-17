package com.java.cites.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.java.cites.model.ImportSummary;
import com.java.cites.model.User;
import com.java.cites.repository.UserRepository;

//import jdk.internal.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public ResponseEntity<byte[]> generateUsers(@RequestParam int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = generateRandomUser();
            users.add(user);
        }

        // Convert the list of users to JSON
        String json = convertUsersListToJson(users);

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "users.json");

        return new ResponseEntity<>(json.getBytes(StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }

    @Autowired
    private UserRepository userRepository;

    public ImportSummary importUsersFromFile(MultipartFile file) {
        ImportSummary importSummary = new ImportSummary();
            List<User> users = parseUsersFromJsonFile(file);
            for (User user : users) {
                if (userIsValid(user) && !(userExists(user))) {
//                    encodePassword(user);
                    userRepository.save(user);
                    importSummary.incrementSuccessfullyImported();
                }else{
                    importSummary.incrementNotImported();
                }
            }
        return importSummary;
    }


    //Called on functions
    private User generateRandomUser() {
        // Implement logic to generate random user data based on your requirements.
        // Make sure to randomize the 'role' field as well.
        Faker faker = new Faker();
        User user = new User();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setBirthDate(faker.date().birthday(18, 65).toString());
        user.setCity(faker.address().city());
        user.setCountry(faker.address().countryCode());
        user.setAvatar(faker.internet().image());
        user.setCompany(faker.company().name());
        user.setJobPosition(faker.company().profession());
        user.setMobile(faker.phoneNumber().cellPhone());
        user.setUsername(faker.name().username());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(generateRandomPassword());
        user.setRole(faker.options().option("admin", "user"));

        // Populate user fields
        return user;
    }

    private String convertUsersListToJson(List<User> users) {
        // Use a library like Jackson or Gson to convert the list of users to JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            // Handle the exception
            return "";
        }
    }

    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(6, 11);
    }

    private List<User> parseUsersFromJsonFile(MultipartFile file) {
        List<User> users = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(file.getInputStream(), new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    private boolean userIsValid(User user) {
        // Implement validation logic for user data, e.g., check required fields, format, etc.
        // Return true if the user is valid, otherwise false.
        return true;
    }

    private boolean userExists(User user) {
        return userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername());
    }

    public User showUser (String username){
        return userRepository.findByUsername(username).get();
    }

    /*@Autowired
    private PasswordEncoder passwordEncoder;
    private void encodePassword(User user) {
        // Implement password encoding logic using a secure password hashing library
        // For example, you can use BCryptPasswordEncoder from Spring Security

//        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

     */

}
