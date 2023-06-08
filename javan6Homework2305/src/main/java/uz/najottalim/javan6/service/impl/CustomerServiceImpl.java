package uz.najottalim.javan6.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.najottalim.javan6.aop.Controller;
import uz.najottalim.javan6.aop.Log;
import uz.najottalim.javan6.dto.CustomerDto;
import uz.najottalim.javan6.dto.CustomerDtoForMostValuable;
import uz.najottalim.javan6.entity.Customer;
import uz.najottalim.javan6.exeption.NoResourceFoundException;
import uz.najottalim.javan6.mapping.CustomerMapper;
import uz.najottalim.javan6.repository.CustomerRepository;
import uz.najottalim.javan6.service.CustomerService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> collect = customerRepository.findAll()
                .stream().map(CustomerMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new NoResourceFoundException("No Data Found");
        } else {
            CustomerDto customerDto = CustomerMapper.toDto(customer.get());
            return ResponseEntity.ok(customerDto);
        }
    }

    @Override
    public ResponseEntity<CustomerDto> addCustomer(CustomerDto customerDto) {
        CustomerDto add = CustomerMapper.toDto(customerRepository.save(CustomerMapper.toEntity(customerDto)));
        return ResponseEntity.ok(add);
    }

    @Override
    @Log
    public ResponseEntity<CustomerDto> upadteCustomer(Long id, CustomerDto customerDto) {
        ResponseEntity<CustomerDto> customerById = getCustomerById(id);
        customerDto.setId(id);
        CustomerDto update =
                CustomerMapper.toDto(customerRepository.save(CustomerMapper.toEntityForUpdate(customerDto)));
        return ResponseEntity.ok(update);
    }

    @Override
    @Log
    public ResponseEntity<CustomerDto> deleteCustomer(Long id) {
        ResponseEntity<CustomerDto> customerById = getCustomerById(id);
        customerRepository.deleteById(id);
        return customerById;
    }

    @Override
    public ResponseEntity<List<CustomerDtoForMostValuable>> getMostValuable() {
        List<CustomerDtoForMostValuable> mostValuable = customerRepository.getMostValuable().stream().toList();
        return ResponseEntity.ok(mostValuable);
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getBYTierAndSort(Integer tier, String sortColumnName) {
        List<Customer> customers = customerRepository.findByTier(tier, Sort.by(sortColumnName));
        return ResponseEntity.ok(customers.stream().map(CustomerMapper::toDtoWithoutOrders).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getByTierAndSortPageable(Integer tier, String sortColumnName, Integer pageNum, Integer size) {
        List<Customer> collect = customerRepository.findByTier(tier, PageRequest.of(pageNum, size, Sort.by(sortColumnName)));
        return ResponseEntity.ok(collect.stream().map(CustomerMapper::toDtoWithoutOrders).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getCustomersByFilter(Optional<String> name, Optional<Integer> tier, Optional<LocalDate> orderDate) {
        return ResponseEntity.ok(customerRepository.getCustomerWithSomeFilter(name, tier, orderDate));
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getCustomerByTierAndFilter(Integer tier, Optional<String> sort) {
        List<Customer> customerDtoList = null;
        if (sort.isPresent()){
            customerDtoList = customerRepository.findByTier(tier,Sort.by(sort.get()));
        } else {
            customerDtoList = customerRepository.findByTier(tier);
        }
        return ResponseEntity.ok(customerDtoList.stream().map(CustomerMapper::toDtoWithoutOrders).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<CustomerDto>> getByTierAndSortByPageable(Integer tier, Optional<String> sortColumnName, Optional<Integer> pageNum, Optional<Integer> size) {
        PageRequest pageable = null;
        List<Customer> byTier = null;
        if (pageNum.isPresent() && size.isPresent()){
            pageable = sortColumnName.map(s -> PageRequest.of(pageNum.get(), size.get(), Sort.by(s))).orElseGet(() -> PageRequest.of(pageNum.get(), size.get()));
        }
        if (pageable == null) {
            byTier = customerRepository.findByTier(tier);
        } else {
            byTier = customerRepository.findByTier(tier,pageable);
        }

        return ResponseEntity.ok(byTier.stream().map(CustomerMapper::toDto).toList());
    }

}
