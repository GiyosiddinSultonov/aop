package uz.najottalim.javan6.repository.extension;

import org.springframework.beans.PropertyValues;
import uz.najottalim.javan6.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductExtension {
    List<Product> searchByNameAndCategoryAndPrice(Optional<List<String>> name, Optional<List<String>> category, Optional<Double> minValue, Optional<Double> maxValue);
}
