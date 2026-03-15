package com.duyanh.mini_e_commerce.controller;

import com.duyanh.mini_e_commerce.dto.CartListResponse;
import com.duyanh.mini_e_commerce.model.Cart;
import com.duyanh.mini_e_commerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartListResponse> getUserCarts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(cartService.getCartsByUser(userId));
    }
}
