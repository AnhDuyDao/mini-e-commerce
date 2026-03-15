package com.duyanh.mini_e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long id;

    private String title;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal total;

    private String thumbnail;
}
