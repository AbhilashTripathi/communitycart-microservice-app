//package com.communitycart.productservice;
//
//import com.communitycart.productservice.entity.Category;
//import org.json.JSONException;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@TestMethodOrder(OrderAnnotation.class)
//@SpringBootTest
//public class CategoryControllerIntegrationTests {
//    @Order(1)
//    @Test
//    void getCategoriesIntegrationTest() throws JSONException {
//
//        //expected response, copy from postman and paste below
//        String expected = "[\n" +
//                "    {\n" +
//                "        \"categoryId\": 19,\n" +
//                "        \"categoryName\": \"Test Category\",\n" +
//                "        \"categoryDescription\": \"Test Category Description\",\n" +
//                "        \"categorySlug\": null,\n" +
//                "        \"catIconUrl\": null\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"categoryId\": 20,\n" +
//                "        \"categoryName\": \"Technology5\",\n" +
//                "        \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "        \"categorySlug\": \"technology\",\n" +
//                "        \"catIconUrl\": \"https://example.com/icons/technology.png\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"categoryId\": 21,\n" +
//                "        \"categoryName\": \"Technology3\",\n" +
//                "        \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "        \"categorySlug\": \"technology\",\n" +
//                "        \"catIconUrl\": \"https://example.com/icons/technology.png\"\n" +
//                "    }\n" +
//                "]";
//
//        //used to consume the URL
//        TestRestTemplate restTemplate = new TestRestTemplate();
//
//        //response have status code, body, etc
//        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8082/category/getAllCategories", String.class); //(url, response)
//        System.out.println(response.getStatusCode());
//        System.out.println(response.getBody());
//
//        //verify the response
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//    }
//
//    @Order(1)
//    @Test
//    void addCategoryIntegrationTest() throws JSONException {
//        Category category = new Category(22L, "Technology5", "All about technology and gadgets", "technology", "https://example.com/icons/technology.png");
//
//        String expected = "{\n" +
//                "    \"categoryId\": 22,\n" +
//                "    \"categoryName\": \"Technology5\",\n" +
//                "    \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "    \"categorySlug\": \"technology\",\n" +
//                "    \"catIconUrl\": \"https://example.com/icons/technology.png\"\n" +
//                "}";
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Category> request = new HttpEntity<Category>(category, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8082/category/addCategory", request, String.class);
//        System.out.println(response.getBody());
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//    }
//
//    @Order(1)
//    @Test
//    void updateCategoryIntegrationTest() throws JSONException {
//        Category category = new Category(22L, "updated4Technology", "All about technology and gadgets", "technology", "/catIconUrl");
//
//        String expected = "{\n" +
//                "    \"categoryId\": 22,\n" +
//                "    \"categoryName\": \"updated4Technology\",\n" +
//                "    \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "    \"categorySlug\": \"technology\",\n" +
//                "    \"catIconUrl\": \"/catIconUrl\"\n" +
//                "}";
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Category> request = new HttpEntity<Category>(category, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8082/category/updateCategory", HttpMethod.PUT, request, String.class);
//        System.out.println(response.getBody());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//    }
//
//    @Order(1)
//    @Test
//    void deleteCategoryIntegrationTest() throws JSONException {
//        Category category = new Category(19L, "Technology5", "All about technology and gadgets", "technology", "https://example.com/icons/technology.png");
//
//        String expected = "{\n" +
//                "    \"categoryId\": 19,\n" +
//                "    \"categoryName\": \"Technology5\",\n" +
//                "    \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "    \"categorySlug\": \"technology\",\n" +
//                "    \"catIconUrl\": \"https://example.com/icons/technology.png\"\n" +
//                "}";
//
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Category> request = new HttpEntity<Category>(category, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8082/category/deleteCategory?categoryId=19", HttpMethod.DELETE, request, String.class);
//        System.out.println(response.getBody());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//    }
//
//    @Order(1)
//    @Test
//    void getCategoryById() throws JSONException {
//        Category category = new Category(21L, "updated4Technology", "All about technology and gadgets", "technology", "/catIconUrl");
//
//        String expected = "{\n" +
//                "    \"categoryId\": 21,\n" +
//                "    \"categoryName\": \"updated4Technology\",\n" +
//                "    \"categoryDescription\": \"All about technology and gadgets\",\n" +
//                "    \"categorySlug\": \"technology\",\n" +
//                "    \"catIconUrl\": \"/catIconUrl\"\n" +
//                "}";
//
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Category> request = new HttpEntity<Category>(category, headers);
//
//        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8082/category/getCategoryById?categoryId=21", String.class);
//        System.out.println(response.getBody());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//    }
//}
