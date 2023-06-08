package uz.najottalim.javan6.repository.extension;
import org.springframework.data.domain.Sort;
import uz.najottalim.javan6.dto.CustomerDto;
import uz.najottalim.javan6.entity.Customer;

import java.time.LocalDate;
import java.util.*;
public interface CustomerRepositoryExtension {
    List<CustomerDto> getCustomersByFilter(Map<String,Object> criteria);
    List<CustomerDto> getCustomerWithSomeFilter(Optional<String> name, Optional<Integer> tier, Optional<LocalDate> orderDate);
}
