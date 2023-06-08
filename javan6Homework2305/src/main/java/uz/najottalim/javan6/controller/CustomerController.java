package uz.najottalim.javan6.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.najottalim.javan6.dto.CustomerDto;
import uz.najottalim.javan6.dto.CustomerDtoForMostValuable;
import uz.najottalim.javan6.dto.CustomerFilterDto;
import uz.najottalim.javan6.dto.ProductDto;
import uz.najottalim.javan6.service.CustomerService;
import uz.najottalim.javan6.validation.customervalidations.PINFL;
import uz.najottalim.javan6.validation.validationgroups.OnCreate;
import uz.najottalim.javan6.validation.validationgroups.OnUpdate;
import uz.najottalim.javan6.validation.validators.PINFLValidator;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("customers")
@Validated
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping()
    @Validated(OnCreate.class)
    public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.addCustomer(customerDto);
    }

    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto, @PathVariable Long id) {
        return customerService.upadteCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }

    @GetMapping("/most-valuable")
    public ResponseEntity<List<CustomerDtoForMostValuable>> getMostValuable() {
        return customerService.getMostValuable();
    }

    @GetMapping("/tier/{tier}/sortBy/{sortColumnName}")
    public ResponseEntity<List<CustomerDto>> getByTierAndSort(@PathVariable Integer tier, @PathVariable String sortColumnName) {
        return customerService.getBYTierAndSort(tier, sortColumnName);
    }

    @GetMapping("/tier/{tier}/sortBy/{sortColumnName}/page-num/{pageNum}/size/{size}")
    public ResponseEntity<List<CustomerDto>> getByTierAndSortPageable(@PathVariable Integer tier,
                                                                      @PathVariable String sortColumnName,
                                                                      @PathVariable Integer pageNum,
                                                                      @PathVariable Integer size) {
        return customerService.getByTierAndSortPageable(tier, sortColumnName, pageNum, size);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> getCustomersByFilter(@RequestParam Optional<String> name,
                                                                  @RequestParam Optional<Integer> tier,
                                                                  @RequestParam("order_date") Optional<LocalDate> orderDate) {

        return customerService.getCustomersByFilter(name, tier,
                orderDate);
    }

    @GetMapping("/list-example")
    public ResponseEntity<List<String>> getRequestParamList(@RequestParam List<String> list) {
        return ResponseEntity.ok(list);
    }

    /*2023-05-30*/

    /*Homework 6.1*/
    @GetMapping("/tier/{tier}")
    public ResponseEntity<List<CustomerDto>> getCustomerByTier(@PathVariable Integer tier,
                                                               @RequestParam Optional<String> sort) {
        return customerService.getCustomerByTierAndFilter(tier, sort);
    }

    /*Homework 6.1*/
//    @GetMapping("/tier/{tier}")
//    public ResponseEntity<List<CustomerDto>> getByTierAndSortByPageable(@PathVariable Integer tier,
//                                                                       @RequestParam Optional<String> sortColumnName,
//                                                                       @RequestParam Optional<Integer> pageNum,
//                                                                       @RequestParam Optional<Integer> size) {
//        return customerService.getByTierAndSortByPageable(tier, sortColumnName, pageNum, size);
//    }

}
