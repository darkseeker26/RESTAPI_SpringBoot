package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

   @NotNull(message = "Provide the quantity value")
   @Min(value = 1, message = "Quantity must greater than zero.")
   @Max(value = 100, message = "Quantity must below or less than 100 ")
   private Integer quantity;
}
