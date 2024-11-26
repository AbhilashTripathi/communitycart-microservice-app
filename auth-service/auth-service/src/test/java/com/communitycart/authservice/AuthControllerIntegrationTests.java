package com.communitycart.authservice;

import com.communitycart.authservice.dtos.AuthRequest;
import com.communitycart.authservice.dtos.UserDTO;
import com.communitycart.authservice.entity.User;
import com.communitycart.authservice.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTests {

    @LocalServerPort
    private int port =  8081;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/auth";
        usersRepository.deleteAll();

        // Create a test user
        User user = new User();
        user.setEmailId("testuser@test.com");
        user.setPassword("password");
        user.setRole("BUYER");
        usersRepository.save(user);
    }

    @Test
    public void testHelloWorld() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello!!", response.getBody());
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmailId("newuser@test.com");
        userDTO.setPassword("password");
        userDTO.setRole("SELLER");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/createUser",
                userDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added.", response.getBody());
        assertNotNull(usersRepository.findByEmailId("newuser@test.com"));
    }

//    @Test
//    public void testAuthenticateAndGetToken() {
//        AuthRequest authRequest = new AuthRequest();
//        authRequest.setEmail("testuser@test.com");
//        authRequest.setPassword("password");
//        authRequest.setSso(false);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/authenticate",
//                authRequest,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }

    @Test
    public void testGetRole() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/getRole/testuser@test.com",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BUYER", response.getBody());
    }

    @Test
    public void testForgotPassword() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/forgotPassword?email=testuser@test.com",
                null,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testChangePassword() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("testuser@test.com");
        authRequest.setPassword("newpassword");

        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/changePassword",
                authRequest,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = usersRepository.findByEmailId("testuser@test.com");
        assertNotNull(user);
        assertNotEquals("password", user.getPassword()); // Assuming password is encoded
    }

    @Test
    public void testDeleteUser() {
        restTemplate.delete(baseUrl + "/deleteUser/testuser@test.com");

        assertNull(usersRepository.findByEmailId("testuser@test.com"));
    }
}
