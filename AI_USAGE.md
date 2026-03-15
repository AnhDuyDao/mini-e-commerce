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
