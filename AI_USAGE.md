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

