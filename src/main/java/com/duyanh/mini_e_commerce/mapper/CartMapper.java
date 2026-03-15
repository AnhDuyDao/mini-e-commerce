package com.duyanh.mini_e_commerce.mapper;

import com.duyanh.mini_e_commerce.dto.CartItemResponse;
import com.duyanh.mini_e_commerce.dto.CartResponse;
import com.duyanh.mini_e_commerce.model.Cart;
import com.duyanh.mini_e_commerce.model.CartItem;
import com.duyanh.mini_e_commerce.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper {
    public static CartResponse toCartDto(Cart cart,
                                         BigDecimal total,
                                         int totalProducts,
                                         int totalQuantity) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartMapper::toCartItemDto)
                .toList();

        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setProducts(items);
        response.setUserId(cart.getUserId());
        response.setTotal(total);
        response.setTotalProducts(totalProducts);
        response.setTotalQuantity(totalQuantity);

        return response;
    }

    private static CartItemResponse toCartItemDto(CartItem item) {

        Product product = item.getProduct();

        CartItemResponse res = new CartItemResponse();

        res.setId(product.getId());
        res.setTitle(product.getTitle());
        res.setPrice(product.getPrice());
        res.setThumbnail(product.getThumbnail());

        res.setQuantity(item.getQuantity());

        res.setTotal(
                product.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        return res;
    }
}
