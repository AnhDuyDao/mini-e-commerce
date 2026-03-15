package com.duyanh.mini_e_commerce.service;

import com.duyanh.mini_e_commerce.dto.ProductPageResponse;
import com.duyanh.mini_e_commerce.dto.ProductResponse;
import com.duyanh.mini_e_commerce.exception.ProductNotFoundException;
import com.duyanh.mini_e_commerce.mapper.ProductMapper;
import com.duyanh.mini_e_commerce.model.Product;
import com.duyanh.mini_e_commerce.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductPageResponse getProducts(int limit, int skip) {

        List<Product> products = productRepository.findProducts(limit,skip);

        List<ProductResponse> productsResponse = products
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        long total = productRepository.count();

        return new ProductPageResponse(
                productsResponse,
                total,
                skip,
                limit
        );
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException(id));
        return ProductMapper.toDto(product);
    }

}
