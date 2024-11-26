package com.communitycart.productservice;

import com.communitycart.productservice.dtos.CategoryDTO;
import com.communitycart.productservice.dtos.Location;
import com.communitycart.productservice.dtos.SellerDTO;
import com.communitycart.productservice.entity.Category;
import com.communitycart.productservice.entity.Product;
import com.communitycart.productservice.entity.Seller;
import com.communitycart.productservice.repository.CategoryRepository;
import com.communitycart.productservice.repository.ProductRepository;
import com.communitycart.productservice.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SellerControllerIntegrationTests {

//    @LocalServerPort
    private int port = 8082;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private String baseUrl;
    private Seller testSeller;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/seller";

        // Clear repositories
        sellerRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create a test category
        testCategory = new Category();
        testCategory.setCategoryName("Test Category");
        testCategory.setCategoryDescription("Test Category Description");
        testCategory = categoryRepository.save(testCategory);

        // Create a test seller
        testSeller = new Seller();
        testSeller.setName("Test Seller");
        testSeller.setEmail("seller@test.com");
        testSeller = sellerRepository.save(testSeller);

        // Create a test product
        testProduct = new Product();
        testProduct.setProductName("Test Product");
        testProduct.setCategoryId(testCategory.getCategoryId());
        testProduct.setSellerId(testSeller.getSellerId());
        testProduct.setProductQuantity(50L);
        testProduct.setProductPrice(15.99);
        testProduct.setAvailable(true);
        testProduct = productRepository.save(testProduct);
    }

//    @Test
//    public void testAddSeller() {
//        SellerDTO sellerDTO = new SellerDTO();
//        sellerDTO.setName("New Seller");
//        sellerDTO.setEmail("new_seller@test.com");
//
//        ResponseEntity<SellerDTO> response = restTemplate.postForEntity(
//                baseUrl + "/addSeller",
//                sellerDTO,
//                SellerDTO.class
//        );
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("New Seller", response.getBody().getName());
//    }

    @Test
    public void testGetSellerById() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/getSeller?sellerId=" + testSeller.getSellerId(),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetAllSellers() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/getAllSellers",
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetSellerCategories() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/getSellerCategories?sellerId=" + testSeller.getSellerId(),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

//    @Test
//    public void testGetNearbySellers() {
//        Location location = new Location(12.9716, 77.5946, 0.0);
//
//        ResponseEntity<List> response = restTemplate.getForEntity(
//                baseUrl + "/getNearbySellers?sourceLat=" + location.getLatitude() +
//                        "&sourceLng=" + location.getLongitude() +
//                        "&elevation=" + location.getElevation(),
//                List.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }

    @Test
    public void testDeleteSeller() {
        ResponseEntity<SellerDTO> response = restTemplate.postForEntity(
                baseUrl + "/deleteSeller",
                new ModelMapper().map(testSeller, SellerDTO.class),
                SellerDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify seller is deleted
        assertFalse(sellerRepository.findById(testSeller.getSellerId()).isPresent());
    }
}
