package com.duyanh.mini_e_commerce.dto;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductQuery {
    @Positive(message = "limit must be greater than 0")
    private int limit = 12;

    @PositiveOrZero(message = "skip must be greater or equal to 0")
    private int skip = 0;
}
