package uz.najottalim.javan6.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.najottalim.javan6.aop.Controller;
import uz.najottalim.javan6.aop.Log;
import uz.najottalim.javan6.aop.Metrics;
import uz.najottalim.javan6.dto.ErrorDto;
import uz.najottalim.javan6.dto.ProductDto;
import uz.najottalim.javan6.dto.ProductDtoWithCount;
import uz.najottalim.javan6.entity.Product;
import uz.najottalim.javan6.exeption.NoResourceFoundException;
import uz.najottalim.javan6.mapping.ProductMapper;
import uz.najottalim.javan6.repository.ProductRepository;
import uz.najottalim.javan6.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> collect = productRepository.findAll()
                .stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NoResourceFoundException("No Data found");
        } else {
            ProductDto get = product.map(ProductMapper::toDto).get();
            return ResponseEntity.ok(get);
        }
    }

    @Override
    public ResponseEntity<ProductDto> addProduct(ProductDto productDto) {
        Product save = productRepository.save(ProductMapper.toEntity(productDto));
        ProductDto saved = ProductMapper.toDto(save);
        return ResponseEntity.ok(saved);
    }

    @Override
    @Log
    @Controller
    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto) {
        System.out.println("inside update");
        ResponseEntity<ProductDto> productById = getProductById(id);
        productDto.setId(id);
        ProductDto update = ProductMapper.toDto(productRepository.save(ProductMapper.toEntity(productDto)));
        return ResponseEntity.ok(update);
    }

    @Override
    @Log
    @Metrics
    @Controller
    public ResponseEntity<ProductDto> deleteProduct(Long id) {
        ResponseEntity<ProductDto> productById = getProductById(id);
        productRepository.deleteById(id);
        return productById;
    }

    @Override
    public ResponseEntity<List<ProductDtoWithCount>> getPopulerCuurently() {
        return ResponseEntity.ok(productRepository.getPopularCurrently().stream().toList());
    }

    @Override
    public ResponseEntity<List<ProductDto>> getByCategoryAndSortBy(String category, String sortColumnName) {
        List<Product> collect = productRepository.findByCategory(category, Sort.by(sortColumnName));
        return ResponseEntity.ok(collect.stream().map(ProductMapper::toDtoWithoutOrders).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getByCategoryAndSortByPageable(String category, Optional<String> sortColumnName, Optional<Integer> pageNum, Optional<Integer> size) {
        PageRequest pr = null;
        Sort sort = null;
        if (pageNum.isPresent() && size.isPresent()) {
            pr = PageRequest.of(pageNum.get(), size.get());
        }
        if (sortColumnName.isPresent()) {
            sort = Sort.by(sortColumnName.get());
            if (pr != null) {
                pr = pr.withSort(sort);
            }
        }
        List<Product> products;
        if (pr != null) {
            products = productRepository.findByCategory(category, pr);
        } else if (sort != null) {
            products = productRepository.findByCategory(category, sort);
        } else {
            products = productRepository.findByCategory(category);
        }
        return ResponseEntity.ok(products.stream().map(ProductMapper::toDtoWithoutOrders).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getByCategoryAndFilter(String category, Optional<String> sort) {
        List<Product> productDtoList = null;
        if (sort.isPresent()) {
            String column = sort.get();
            if (column.equals("id") || column.equals("name") || column.equals("price")) {
                productDtoList = productRepository.findByCategory(category, Sort.by(column));
            } else {
                throw new NoResourceFoundException("Wrong column name");
            }
        } else {
            productDtoList = productRepository.findByCategory(category);
        }

        return ResponseEntity.ok(productDtoList.stream().map(ProductMapper::toDto).toList());
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts(String name, List<String> category, Integer minValue, Integer maxValue) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProductDto>> searchByNameAndCategoryAndPrice(Optional<List<String>> name, Optional<List<String>> category, Optional<Double> minValue, Optional<Double> maxValue) {
        return ResponseEntity.ok(
                productRepository.searchByNameAndCategoryAndPrice(name, category, minValue, maxValue)
                        .stream()
                        .map(ProductMapper::toDtoWithoutOrders)
                        .collect(Collectors.toList())
        );
    }
}
