package com.duyanh.mini_e_commerce.mapper;

import com.duyanh.mini_e_commerce.dto.ProductResponse;
import com.duyanh.mini_e_commerce.model.Product;
import com.duyanh.mini_e_commerce.model.ProductImage;

import java.util.List;

public class ProductMapper {
    public static ProductResponse toDto (Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                (product.getImages() != null)
                        ? product.getImages()
                                .stream()
                                .map(ProductImage::getImageUrl)
                                .toList()
                        : List.of(),
                product.getThumbnail()
        );
    }
}
