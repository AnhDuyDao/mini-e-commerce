package com.duyanh.mini_e_commerce.service;

import com.duyanh.mini_e_commerce.dto.AddCartRequest;
import com.duyanh.mini_e_commerce.dto.CartListResponse;
import com.duyanh.mini_e_commerce.dto.CartProductRequest;
import com.duyanh.mini_e_commerce.dto.CartResponse;
import com.duyanh.mini_e_commerce.exception.InsufficientStockException;
import com.duyanh.mini_e_commerce.exception.ProductNotFoundException;
import com.duyanh.mini_e_commerce.mapper.CartMapper;
import com.duyanh.mini_e_commerce.model.Cart;
import com.duyanh.mini_e_commerce.model.CartItem;
import com.duyanh.mini_e_commerce.model.Product;
import com.duyanh.mini_e_commerce.repository.CartRepository;
import com.duyanh.mini_e_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartListResponse getCartsByUser(Long userId) {

        List<Cart> carts = cartRepository.findCartsByUserId(userId);

        List<CartResponse> cartResponses = carts.stream()
                .map(this::buildCartResponse)
                .toList();

        return buildCartListResponse(cartResponses);
    }

    @Transactional
    public CartResponse addCart(AddCartRequest request) {

        Cart cart = new Cart();
        cart.setUserId(request.getUserId());

        List<CartItem> items = new ArrayList<>();

        for (CartProductRequest p : request.getProducts()) {

            Product product = getProductOrThrow(p.getProductId());

            CartItem existingItem = findExistingItem(items, product.getId());

            int newQuantity = existingItem == null
                    ? p.getQuantity()
                    : existingItem.getQuantity() + p.getQuantity();

            validateStock(product, newQuantity);

            if (existingItem != null) {
                existingItem.setQuantity(newQuantity);
            } else {
                items.add(buildCartItem(cart, product, p.getQuantity()));
            }
        }

        cart.setItems(items);

        Cart savedCart = cartRepository.save(cart);

        return buildCartResponse(savedCart);
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

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private void validateStock(Product product, Integer quantity) {
        if (quantity > product.getStock()) {
            throw new InsufficientStockException(product.getId());
        }
    }

    private CartItem findExistingItem(List<CartItem> items, Long productId) {

        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private CartItem buildCartItem(Cart cart, Product product, int quantity) {

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);

        return item;
    }
}
