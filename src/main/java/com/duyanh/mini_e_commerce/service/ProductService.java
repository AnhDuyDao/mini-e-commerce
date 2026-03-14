package com.duyanh.mini_e_commerce.service;

import com.duyanh.mini_e_commerce.dto.ProductResponse;
import com.duyanh.mini_e_commerce.mapper.ProductMapper;
import com.duyanh.mini_e_commerce.model.Product;
import com.duyanh.mini_e_commerce.model.ProductImage;
import com.duyanh.mini_e_commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAllProduct() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductMapper::toDto)
                .toList();
    }
}
