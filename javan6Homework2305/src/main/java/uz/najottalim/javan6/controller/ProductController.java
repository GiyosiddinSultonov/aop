package uz.najottalim.javan6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.najottalim.javan6.dto.OrderDto;
import uz.najottalim.javan6.dto.ProductDto;
import uz.najottalim.javan6.dto.ProductDtoWithCount;
import uz.najottalim.javan6.service.ProductService;

import java.util.*;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping({"/{id}"})

    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping()
    public ResponseEntity<ProductDto> addProduct(@Validated @RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@Validated @RequestBody ProductDto productDto, @PathVariable Long id) {
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("popular-currently")
    public ResponseEntity<List<ProductDtoWithCount>> getPopularCurrently() {
        return productService.getPopulerCuurently();
    }

//    @GetMapping("category/{category}/sortBy/{sortColumnName}")
//    public ResponseEntity<List<ProductDto>> getByCategoryAndSortBy(@PathVariable String category, @PathVariable String sortColumnName) {
//        return productService.getByCategoryAndSortBy(category, sortColumnName);
//    }

    @GetMapping("category01/{category}")
    public ResponseEntity<List<ProductDto>> getByCategoryAndSortByPageable(@PathVariable String category,
                                                                           @RequestParam Optional<String> sortColumnName,
                                                                           @RequestParam Optional<Integer> pageNum,
                                                                           @RequestParam Optional<Integer> size) {
        return productService.getByCategoryAndSortByPageable(category, sortColumnName, pageNum, size);
    }

    /*2023-05-30*/

    /*Homework 6.2*/

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> getByCategoryAndFilter(@PathVariable String category,
                                                                   @RequestParam Optional<String> sort){
        return productService.getByCategoryAndFilter(category,sort);
    }

    /*Homework 7*/

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchByNameAndCategoryAndPrice(@RequestParam Optional<List<String>> name,
                                                                            @RequestParam Optional<List<String>> category,
                                                                            @RequestParam Optional<Double> minValue,
                                                                            @RequestParam Optional<Double> maxValue){
        return productService.searchByNameAndCategoryAndPrice(name, category, minValue, maxValue);
    }
}
