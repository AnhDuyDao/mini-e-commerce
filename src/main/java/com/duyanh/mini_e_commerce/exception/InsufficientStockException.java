package com.duyanh.mini_e_commerce.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(Long productId) {
        super("Not enough stock for product id: " + productId);
    }
}
