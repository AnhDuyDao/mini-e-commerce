package com.duyanh.mini_e_commerce.repository;

import com.duyanh.mini_e_commerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
