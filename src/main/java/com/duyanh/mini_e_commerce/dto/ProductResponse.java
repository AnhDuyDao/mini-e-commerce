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
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private List<String> images;
    private String thumbnail;
}
