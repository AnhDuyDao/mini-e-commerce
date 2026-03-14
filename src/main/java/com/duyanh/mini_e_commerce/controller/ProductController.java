package com.duyanh.mini_e_commerce.controller;

import com.duyanh.mini_e_commerce.dto.ProductResponse;
import com.duyanh.mini_e_commerce.model.Product;
import com.duyanh.mini_e_commerce.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductResponse> retrieveAllProduct() {
        return productService.findAllProduct();
    }
}
