package com.duyanh.mini_e_commerce.service;

import com.duyanh.mini_e_commerce.dto.CartListResponse;
import com.duyanh.mini_e_commerce.dto.CartResponse;
import com.duyanh.mini_e_commerce.mapper.CartMapper;
import com.duyanh.mini_e_commerce.model.Cart;
import com.duyanh.mini_e_commerce.model.CartItem;
import com.duyanh.mini_e_commerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartListResponse getCartsByUser(Long userId) {

        List<Cart> carts = cartRepository.findCartsByUserId(userId);

        List<CartResponse> cartResponses = carts.stream()
                .map(this::buildCartResponse)
                .toList();

        return buildCartListResponse(cartResponses);
    }

    private CartResponse buildCartResponse(Cart cart) {

        int totalProducts = calculateTotalProducts(cart);

        int totalQuantity = calculateTotalQuantity(cart);

        BigDecimal total = calculateTotal(cart);

        return CartMapper.toCartDto(
                cart,
                total,
                totalProducts,
                totalQuantity
        );
    }

    private BigDecimal calculateTotal(Cart cart) {

        return cart.getItems().stream()
                .map(item ->
                        item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private int calculateTotalQuantity(Cart cart) {

        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    private int calculateTotalProducts(Cart cart) {
        return cart.getItems().size();
    }
    private CartListResponse buildCartListResponse(List<CartResponse> carts) {

        CartListResponse response = new CartListResponse();
        response.setCarts(carts);
        response.setTotal((long) carts.size());
        response.setSkip(0);
        response.setLimit(carts.size());

        return response;
    }
}
