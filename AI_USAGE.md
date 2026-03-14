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