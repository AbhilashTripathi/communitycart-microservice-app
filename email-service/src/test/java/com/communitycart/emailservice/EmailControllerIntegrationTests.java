//package com.communitycart.emailservice;
//
//import com.communitycart.emailservice.dtos.EmailDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = EmailServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class EmailControllerIntegrationTests {
//
//    private int port = 8084;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private String baseUrl;
//
//    @BeforeEach
//    public void setUp() {
//        baseUrl = "http://localhost:" + port + "/email";
//    }
//
//    @Test
//    public void testSendSimpleEmail() {
//        String toEmail = "test@example.com";
//        String body = "This is a test email.";
//        String subject = "Test Subject";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendSimpleEmail?toEmail=" + toEmail + "&body=" + body + "&subject=" + subject,
//                null,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendHtmlEmail() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("htmltest@example.com");
//        emailDTO.setSubject("HTML Email Test");
//        emailDTO.setBody("<h1>This is a test HTML email.</h1>");
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendHtmlEmail",
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendUpdateDeliveryDate() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("deliverydate@example.com");
//        emailDTO.setSubject("Delivery Date Update");
//        emailDTO.setBody("Your delivery date has been updated.");
//
//        String date = "2024-12-01";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendUpdateDeliveryDate?date=" + date,
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendDeliveryStatus() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("status@example.com");
//        emailDTO.setSubject("Delivery Status");
//        emailDTO.setBody("Your delivery status has been updated.");
//
//        String status = "In Transit";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendDeliveryStatus?status=" + status,
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendDeliveredStatus() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("delivered@example.com");
//        emailDTO.setSubject("Delivery Complete");
//        emailDTO.setBody("Your package has been delivered.");
//
//        String status = "Delivered";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendDeliveredStatus?status=" + status,
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendCancelledStatus() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("cancelled@example.com");
//        emailDTO.setSubject("Order Cancelled");
//        emailDTO.setBody("Your order has been cancelled.");
//
//        String status = "Cancelled";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendCancelledStatus?status=" + status,
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//
//    @Test
//    public void testSendCancelledStatusSeller() {
//        EmailDTO emailDTO = new EmailDTO();
//        emailDTO.setToEmail("sellercancelled@example.com");
//        emailDTO.setSubject("Order Cancelled by Customer");
//        emailDTO.setBody("A customer has cancelled an order.");
//
//        String status = "Cancelled";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                baseUrl + "/sendCancelledStatusSeller?status=" + status,
//                emailDTO,
//                String.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Mail sending...", response.getBody());
//    }
//}

package com.communitycart.emailservice;

import com.communitycart.emailservice.dtos.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmailServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    private EmailDTO emailDTO;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/email";

        // Initialize a sample EmailDTO for testing
        emailDTO = new EmailDTO(
                1L,                     // customerId
                101L,                   // orderId
                999.99,                 // totalPrice
                "Placed",               // orderStatus
                new Date(),             // deliveryDate
                5,                      // noOfItems
                "customer@test.com",    // customerEmail
                "John Doe",             // customerName
                "seller@test.com",      // sellerEmail
                "Jane Seller",          // sellerName
                "In Transit"            // status
        );
    }

    @Test
    public void testSendSimpleEmail() {
        String toEmail = "test@example.com";
        String body = "This is a test email.";
        String subject = "Test Subject";

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendSimpleEmail?toEmail=" + toEmail + "&body=" + body + "&subject=" + subject,
                null,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendHtmlEmail() {
        emailDTO.setOrderStatus("Shipped");
        emailDTO.setCustomerName("Alice");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendHtmlEmail",
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendUpdateDeliveryDate() {
        String updatedDate = "2024-12-01";

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendUpdateDeliveryDate?date=" + updatedDate,
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendDeliveryStatus() {
        String status = "Delivered";

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendDeliveryStatus?status=" + status,
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendDeliveredStatus() {
        emailDTO.setStatus("Delivered");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendDeliveredStatus?status=Delivered",
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendCancelledStatus() {
        emailDTO.setStatus("Cancelled");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendCancelledStatus?status=Cancelled",
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }

    @Test
    public void testSendCancelledStatusSeller() {
        emailDTO.setStatus("Cancelled");

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/sendCancelledStatusSeller?status=Cancelled",
                emailDTO,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sending...", response.getBody());
    }
}
