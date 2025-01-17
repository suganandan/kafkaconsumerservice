package com.suga.kafkaconsumerservice.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data transfer object representing a product")
public class ProductDto {

    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Schema(description = "Name of the product", example = "Laptop")
    private String name;

    @NotNull(message = "Price is mandatory")
    @Min(value = 0, message = "Price must be a positive value")
    @Schema(description = "Price of the product", example = "599.99")
    private Double price;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity of the product", example = "10")
    private Long qty;
}
