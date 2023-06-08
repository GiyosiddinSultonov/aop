package uz.najottalim.javan6.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import org.springframework.validation.annotation.Validated;
import uz.najottalim.javan6.validation.customervalidations.PINFL;
import uz.najottalim.javan6.validation.validationgroups.OnCreate;
import uz.najottalim.javan6.validation.validationgroups.OnUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @NotNull(message = "id cannot be null", groups = OnUpdate.class)
    @Null(message = "you dont give null", groups = OnCreate.class)
    private Long id;
    @NotBlank(message = "name can not null")
//    @Pattern(regexp = "^[ш,к].")
    private String name;
    @Max(value = 5, message = "max tier is 5")
    @Min(value = 1, message = "min tier is 1")
    private Integer tier;
    @PINFL
    private String pinfl;
    public CustomerDto(Long id, String name, Integer tier, List<OrderDto> orders) {
        this.id = id;
        this.name = name;
        this.tier = tier;
        this.orders = orders;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderDto> orders;

    public CustomerDto(Long id, String name, Integer tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
    }
}
