package com.duyanh.mini_e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long id;

    private List<CartItemResponse> products;

    private BigDecimal total;

    private Long userId;

    private Integer totalProducts;

    private Integer totalQuantity;
}
