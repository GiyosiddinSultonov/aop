package uz.najottalim.javan6.service;

import org.springframework.http.ResponseEntity;
import uz.najottalim.javan6.dto.ProductDto;
import uz.najottalim.javan6.dto.ProductDtoWithCount;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ResponseEntity<List<ProductDto>> getAllProducts();

    ResponseEntity<ProductDto> getProductById(Long id);

    ResponseEntity<ProductDto> addProduct(ProductDto productDto) ;

    ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto) ;

    ResponseEntity<ProductDto> deleteProduct(Long id) ;

    ResponseEntity<List<ProductDtoWithCount>> getPopulerCuurently();

    ResponseEntity<List<ProductDto>> getByCategoryAndSortBy(String category, String sortColumnName);

    ResponseEntity<List<ProductDto>> getByCategoryAndSortByPageable(String category, Optional<String> sortColumnName, Optional<Integer> pageNum, Optional<Integer> size);

    ResponseEntity<List<ProductDto>> getByCategoryAndFilter(String category, Optional<String> sort);

    ResponseEntity<List<ProductDto>> getAllProducts(String name, List<String> category, Integer minValue, Integer maxValue);

    ResponseEntity<List<ProductDto>> searchByNameAndCategoryAndPrice(Optional<List<String>> name, Optional<List<String>> category, Optional<Double> minValue, Optional<Double> maxValue);
}
