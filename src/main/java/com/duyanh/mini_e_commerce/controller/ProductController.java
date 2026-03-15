package com.duyanh.mini_e_commerce.controller;

import com.duyanh.mini_e_commerce.dto.ProductPageResponse;
import com.duyanh.mini_e_commerce.dto.ProductQuery;
import com.duyanh.mini_e_commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/products", "/product"})
    public ResponseEntity<ProductPageResponse> getProducts(
            @Valid ProductQuery query
    ) {

        return ResponseEntity.ok(
                productService.getProducts(query.getLimit(), query.getSkip())
        );
    }
}
