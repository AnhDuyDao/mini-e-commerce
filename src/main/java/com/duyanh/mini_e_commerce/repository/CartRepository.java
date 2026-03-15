package com.duyanh.mini_e_commerce.repository;

import com.duyanh.mini_e_commerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("""
        SELECT DISTINCT c
        FROM Cart c
        LEFT JOIN FETCH c.items ci
        LEFT JOIN FETCH ci.product
        WHERE c.userId = :userId
    """)
    List<Cart> findCartsByUserId(@Param("userId") Long userId);
}
