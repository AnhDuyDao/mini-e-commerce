package com.duyanh.mini_e_commerce.repository;

import com.duyanh.mini_e_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
