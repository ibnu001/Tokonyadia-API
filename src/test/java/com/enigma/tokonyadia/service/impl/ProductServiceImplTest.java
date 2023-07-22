package com.enigma.tokonyadia.service.impl;

import com.enigma.tokonyadia.entity.Product;
import com.enigma.tokonyadia.entity.ProductPrice;
import com.enigma.tokonyadia.entity.Store;
import com.enigma.tokonyadia.model.request.ProductRequest;
import com.enigma.tokonyadia.model.response.ProductResponse;
import com.enigma.tokonyadia.model.response.StoreResponse;
import com.enigma.tokonyadia.repository.ProductRepository;
import com.enigma.tokonyadia.service.ProductPriceService;
import com.enigma.tokonyadia.service.ProductService;
import com.enigma.tokonyadia.service.StoreService;
import com.enigma.tokonyadia.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private StoreService storeService;
    @Mock
    private ProductPriceService productPriceService;
    @Mock
    private ValidationUtil validationUtil;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository,
                storeService,
                productPriceService,
                validationUtil);
    }

    @Test
    void itShouldReturnProductResponseWhenCreateNewProduct() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setProductId("1");
        request.setProductName("Baju Wibu");
        request.setDescription("Baju Wibu");
        request.setPrice(50000L);
        request.setStock(10);
        request.setStoreId("store-1");

        doNothing().when(validationUtil).validate(request);
        Store store = Store.builder()
                .id("store-1")
                .name("tokopakedi")
                .address("jl. h. dahlan")
                .build();
        when(storeService.getById(anyString())).thenReturn(store);

        Product product = Product.builder()
                .name(request.getProductName())
                .description(request.getDescription())
                .build();
        when(productRepository.saveAndFlush(any())).thenReturn(product);


        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .store(store)
                .product(product)
                .isActive(true)
                .build();
        when(productPriceService.create(any())).thenReturn(productPrice);

        ProductResponse actual = productService.create(request);
        assertNotNull(actual);
        assertEquals("Baju Wibu", actual.getProductName());
    }

    @Test
    void createBulk() {
    }

    @Test
    void getById() {
    }

    @Test
    void itShouldReturnPageOfProductResponseWhenGetAllByNameOrPrice() {

    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}