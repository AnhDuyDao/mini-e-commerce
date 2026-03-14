package com.duyanh.mini_e_commerce.controller;

import com.duyanh.mini_e_commerce.dto.ProductPageResponse;
import com.duyanh.mini_e_commerce.service.ProductService;
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
    public ProductPageResponse getProducts(
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int skip
    ) {
        return productService.getProducts(limit, skip);
    }
}
