package com.communitycart.productservice;
import com.communitycart.productservice.dtos.ProductDTO;
import com.communitycart.productservice.dtos.ProductOutOfStock;
import com.communitycart.productservice.entity.Category;
import com.communitycart.productservice.entity.Product;
import com.communitycart.productservice.entity.Seller;
import com.communitycart.productservice.repository.CategoryRepository;
import com.communitycart.productservice.repository.ProductRepository;
import com.communitycart.productservice.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private String baseUrl;
    private Product testProduct;
    private Seller testSeller;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/product";

        // Clear existing data
        productRepository.deleteAll();
        sellerRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create test seller
        testSeller = new Seller();
        testSeller.setName("Test Seller");
        testSeller.setEmail("seller@test.com");
        testSeller = sellerRepository.save(testSeller);

        // Create test category
        testCategory = new Category();
        testCategory.setCategoryName("Test Category");
        testCategory.setCategoryDescription("A category for testing");
        testCategory = categoryRepository.save(testCategory);

        // Create test product
        testProduct = new Product();
        testProduct.setProductName("Test Product");
        testProduct.setSellerId(testSeller.getSellerId());
        testProduct.setCategoryId(testCategory.getCategoryId());
        testProduct.setProductQuantity(100L);
        testProduct.setAvailable(true);
        testProduct.setRating(0.0);
        testProduct.setProductPrice(19.99);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    public void testGetAllProducts() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                baseUrl + "/getAllProducts",
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

//    @Test
//    public void testGetProductsBySellerIdAndCategory() {
//        ResponseEntity<List> response = restTemplate.getForEntity(
//                baseUrl + "/getProductsBySellerIdAndCategory?sellerId=" + testSeller.getSellerId() +
//                        "&categoryId=" + testCategory.getCategoryId(),
//                List.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().size() > 0);
//    }

    @Test
    public void testGetProductById() {
        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(
                baseUrl + "/getProduct?productId=" + testProduct.getProductId(),
                ProductDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testProduct.getProductId(), response.getBody().getProductId());
    }

    @Test
    public void testUpdateProduct() {
        // Fetch the existing product
        ProductDTO existingProduct = restTemplate.getForObject(
                baseUrl + "/getProduct?productId=" + testProduct.getProductId(),
                ProductDTO.class
        );

        // Modify the product
        existingProduct.setProductName("Updated Product Name");

        // Send PUT request
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                baseUrl + "/updateProduct",
                HttpMethod.PUT,
                new HttpEntity<>(existingProduct),
                ProductDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Product Name", response.getBody().getProductName());
    }

//    @Test
//    public void testSetProductOutOfStock() {
//        ProductOutOfStock outOfStock = new ProductOutOfStock();
//        outOfStock.setProductId(testProduct.getProductId());
//        outOfStock.setSellerId(testSeller.getSellerId());
//        outOfStock.setAvailable(false);
//
//        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(
//                baseUrl + "/setOutOfStock",
//                outOfStock,
//                ProductDTO.class
//        );
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertFalse(response.getBody().isAvailable());
//        assertEquals(0L, response.getBody().getProductQuantity());
//    }

    @Test
    public void testDeleteProduct() {
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                baseUrl + "/deleteProduct?productId=" + testProduct.getProductId(),
                HttpMethod.DELETE,
                null,
                ProductDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify product is actually deleted
        ResponseEntity<ProductDTO> checkDeletedProduct = restTemplate.getForEntity(
                baseUrl + "/getProduct?productId=" + testProduct.getProductId(),
                ProductDTO.class
        );

        assertNull(checkDeletedProduct.getBody());
    }

    @Test
    public void testUpdateProductRating() {
        // Update rating
        restTemplate.postForEntity(
                baseUrl + "/updateRating?productId=" + testProduct.getProductId() + "&rating=4.5",
                null,
                Void.class
        );

        // Retrieve and verify
        Product updatedProduct = productRepository.findById(testProduct.getProductId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(4.5, updatedProduct.getRating());
    }
}