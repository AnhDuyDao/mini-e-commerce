### AI-USAGE LOG
1.
- Ngày giờ: 13/03/2006 9:14
- Công cụ: ChatGPT
- Prompt: Kiểm tra entity Product đã viết như vậy được chưa? Làm sao để có array images của Product.
    ```
    @Entity
    @Data
    public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        private String title;
        
        private String description;
        
        private BigDecimal price;
        
        private String thumbnail;
        
        private Integer stock;
    
    } 
    ```
- Sau đó: thêm các annotation, field và entity Product Image
    ```
    @Column(nullable = false), thêm @Column(length = 2000)
    @Column(nullable = false, precision = 10, scale = 2)
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;
    ```
    ```
    @Entity
    @Data
    public class ProductImage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private String imageUrl;
    
        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;
    }
    ```

2.
- Ngày giờ: 14/03/2006 9:22
- Công cụ: ChatGPT
- Prompt: Lỗi stack overflow ở List images
- Sau đó: tạo dto/ProductResponse, tạo ProductMapper với hàm toDto, thêm code vào ProductService
    ```
    return products.stream()
                    .map(ProductMapper::toDto)
                    .toList();
    ```

3.
- Ngày giờ: 14/03/2006 9:35
- Công cụ: ChatGPT
- Prompt:Xây dựng endpoint GET https://dummyjson.com/products?limit=12
```
{ 
    "products": [
        {
            "id": 1,
            "title": "Essence Mascara Lash Princess",
            "description": "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.",
            "price": 9.99,
            "stock": 99,
            "images": [
            "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/1.webp"
            ],
            "thumbnail": "https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp"
        },...
    ], 
    "total": 194, 
    "skip": 0, 
    "limit": 12 
}
```
- Sau đó: tạo dto/ProductPageResponse, thêm code vào ProductRepository, sửa code ở ProductService và ProductController
```
    @Query(value = "SELECT * FROM product LIMIT :limit OFFSET :skip", nativeQuery = true)
    List<Product> findProducts(int limit, int skip);
```
```
    @GetMapping({"/products", "/product"})
    public ProductPageResponse getProducts(
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int skip
    ) {
        return productService.getProducts(limit, skip);
    }
```
```
public ProductPageResponse getProducts(int limit, int skip) {

        List<Product> products = productRepository.findProducts(limit,skip);

        List<ProductResponse> productsResponse = products
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        long total = productRepository.count();

        return new ProductPageResponse(
                productsResponse,
                total,
                skip,
                limit
        );
    }
```
4.
- Ngày giờ: 15/03/2006 11:50
- Công cụ: ChatGPT
- Prompt: Xử lí lỗi không đúng enpoint GET /producs thay vì GET /products
- Sau đó: tạo dto/ErrorResponse, tạo GlobalExceptionHandler với method handleNoHandlerFound, thêm properties vào application.
```
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
```
5.
- Ngày giờ: 15/03/2006 1:10
- Công cụ: ChatGPT
- Prompt: Xử lí lỗi validation với enpoint GET /products?limit=-1 hoặc limit=ab
- Sau đó: tạo dto/ProductQuery, tạo method handleMethodArgumentNotValid, refactor getProducts tại ProductController với @Valild
```
@GetMapping({"/products", "/product"})
    public ResponseEntity<ProductPageResponse> getProducts(
            @Valid ProductQuery query
    ) {

        return ResponseEntity.ok(
                productService.getProducts(query.getLimit(), query.getSkip())
        );
    }
```
6.
- Ngày giờ: 15/03/2006 11:4
- Công cụ: ChatGPT
- Prompt: Xây dựng GET localhost:8080/products/{id}. Rồi xử lí các lỗi validation và exception
- Sau đó: thêm class ProductNotFoundException, thêm hàm getProductById vào ProductService, thêm vào ProductController, thêm 3 ExceptionHandler vào GlobalExceptionHandler là handleProductNotFound, handleProductConstraintViolation, handleProductMethodArgumentTypeMismatch
```
@GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable
            @Positive(message = "id must be greater than 0")
            Long id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
```
```
@ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
```
```
@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleProductConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
```
```
@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleProductMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {

        String paramName = ex.getName();
        String message = paramName + " must be a number";

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
```
7.
- Ngày giờ: 15/03/2006 13:05
- Công cụ: ChatGPT
- Prompt: Thiết kế Cart và CartItem entity
- Sau đó: tạo Cart và CartItem, thêm dự liệu vào file data.sql
```
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;
}
```
```
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
```
8.
- Ngày giờ: 15/03/2006 18:30
- Công cụ: ChatGPT
- Prompt: Xem giỏ hàng (list items + tổng tiền). Với GET http://localhost:8080/cart/user/1
- Sau đó: tạo CartController, tạo dto/CartItemResponse, dto/CartListResponse, dto/CartResponse, CartMapper, CartRepository, CartService
```
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartListResponse> getUserCarts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(cartService.getCartsByUser(userId));
    }
}
```
```
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("""
        SELECT DISTINCT c
        FROM Cart c
        LEFT JOIN FETCH c.items ci
        LEFT JOIN FETCH ci.product
        WHERE c.userId = :userId
    """)
    List<Cart> findCartsByUserId(@Param("userId") Long userId);
}
```
```
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
```
```
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
```
9.
- Ngày giờ: 15/03/2006 21:8
- Công cụ: ChatGPT
- Prompt: Thêm sản phẩm vào giỏ hàng. Với POST http://localhost:8080/carts
```
{
  "userId": 1,
  "products": [
    { "productId": 2, "quantity": 2 },
    { "productId": 5, "quantity": 1 }
  ]
}
```
- Sau đó: thêm addCart CartController, tạo dto/AddCartRequest, dto/CartProductRequest, tạo exception/InsufficientStockException, thêm vào CartService addCart xử lí thêm sản phẩm vào cart cùng các method support
```
@PostMapping
    public ResponseEntity<CartResponse> addCart(
            @Valid @RequestBody AddCartRequest request
    ) {
        CartResponse response = cartService.addCart(request);
        return ResponseEntity.ok(response);
    }
```
```
public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(Long productId) {
        super("Not enough stock for product id: " + productId);
    }
}
```
```
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
```
```
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
```
10.
- Ngày giờ: 15/03/2006 21:57
- Công cụ: ChatGPT
- Prompt: Xóa sản phẩm ở giỏ hàng. Với DELETE /carts/items/{itemId}
- Sau đó: thêm vào CartController, hàm removeItemFromCart(), tạo CartItemRepository, thêm vào CartService hàm removeItem(), tạo CartItemNotFoundException để xử lí lỗi không tìm thấy id của cartItem cần xóa
```
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable Long itemId
    ) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }
```
```
    @Transactional
    public void removeItem(Long itemId) {

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        cartItemRepository.delete(item);
    }
```
```
public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(Long id) {
        super("Cart item not found with id: " + id);
    }
}
```