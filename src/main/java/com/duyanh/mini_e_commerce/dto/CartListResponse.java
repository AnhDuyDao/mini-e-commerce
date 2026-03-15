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
public class CartListResponse {
    private List<CartResponse> carts;

    private Long total;

    private Integer skip;

    private Integer limit;
}
