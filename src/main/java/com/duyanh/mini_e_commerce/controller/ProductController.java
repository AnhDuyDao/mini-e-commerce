package com.duyanh.mini_e_commerce.controller;

import com.duyanh.mini_e_commerce.dto.ProductPageResponse;
import com.duyanh.mini_e_commerce.dto.ProductQuery;
import com.duyanh.mini_e_commerce.dto.ProductResponse;
import com.duyanh.mini_e_commerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/products", "/product"})
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> getProducts(
            @Valid ProductQuery query
    ) {

        return ResponseEntity.ok(
                productService.getProducts(query.getLimit(), query.getSkip())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable
            @Positive(message = "id must be greater than 0")
            Long id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
