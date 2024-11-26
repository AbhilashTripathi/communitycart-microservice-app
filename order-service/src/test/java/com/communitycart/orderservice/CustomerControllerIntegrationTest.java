package com.communitycart.orderservice;

import com.communitycart.orderservice.dtos.CustomerDTO;
import com.communitycart.orderservice.dtos.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

//    @LocalServerPort
    private int port = 8083;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private CustomerDTO testCustomer;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/customer";

        // Initialize a test CustomerDTO
        AddressDTO address = new AddressDTO("123 Street", "Apt 1", "City", "District", "State", "12345", 40.7128, -74.0060, 15.5);
        testCustomer = new CustomerDTO(
                null,               // customerId (null for new customers)
                "Abhilash",         // name
                "abhilash3.tripathi@test.com", // email
                "1234567890",       // contactPhoneNo
                address,            // address
                "password123",      // password
                "/imageUrl"                // customerImageUrl
        );
    }

    @Test
    public void testAddCustomer() {
        testCustomer.setEmail("abhilash4.tripathi@test.com");
        ResponseEntity<CustomerDTO> response = restTemplate.postForEntity(
                baseUrl + "/addCustomer",
                testCustomer,
                CustomerDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Abhilash", response.getBody().getName());
    }

    @Test
    public void testGetCustomer() {
        // Add a customer first
        testCustomer.setEmail("abhilash5.tripathi@test.com");
        CustomerDTO addedCustomer = restTemplate.postForEntity(
                baseUrl + "/addCustomer",
                testCustomer,
                CustomerDTO.class
        ).getBody();

        assertNotNull(addedCustomer);

        // Fetch the customer by ID
        ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(
                baseUrl + "/getCustomer?customerId=" + addedCustomer.getCustomerId(),
                CustomerDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Abhilash", response.getBody().getName());
    }

    @Test
    public void testUpdateCustomer() {
        testCustomer.setEmail("abhilash6.tripathi@test.com");
        // Add a customer first
        CustomerDTO addedCustomer = restTemplate.postForEntity(
                baseUrl + "/addCustomer",
                testCustomer,
                CustomerDTO.class
        ).getBody();

        assertNotNull(addedCustomer);

        // Update the customer details
        addedCustomer.setName("Abhilash Tripathi");
        ResponseEntity<CustomerDTO> response = restTemplate.postForEntity(
                baseUrl + "/updateCustomer",
                addedCustomer,
                CustomerDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Abhilash Tripathi", response.getBody().getName());
    }

//    @Test
//    public void testDeleteCustomer() {
//        testCustomer.setEmail("abhilash6.tripathi@test.com");
//        // Add a customer first
//        CustomerDTO addedCustomer = restTemplate.postForEntity(
//                baseUrl + "/addCustomer",
//                testCustomer,
//                CustomerDTO.class
//        ).getBody();
//
//        assertNotNull(addedCustomer);
//
//        // Delete the customer
//        ResponseEntity<CustomerDTO> response = restTemplate.postForEntity(
//                baseUrl + "/deleteCustomer?email=" + addedCustomer.getEmail(),
//                null,
//                CustomerDTO.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Abhilash", response.getBody().getName());
//    }

    @Test
    public void testGetCustomerByEmail() {
        testCustomer.setEmail("abhilash8.tripathi@test.com");
        // Add a customer first
        CustomerDTO addedCustomer = restTemplate.postForEntity(
                baseUrl + "/addCustomer",
                testCustomer,
                CustomerDTO.class
        ).getBody();

        assertNotNull(addedCustomer);

        // Fetch the customer by email
        ResponseEntity<Long> response = restTemplate.getForEntity(
                baseUrl + "/getCustomerByEmail?email=" + addedCustomer.getEmail(),
                Long.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(addedCustomer.getCustomerId(), response.getBody());
    }
}
