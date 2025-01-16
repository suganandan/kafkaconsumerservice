package com.suga.kafkaconsumerservice.controller;

import com.suga.kafkaconsumerservice.dto.ProductDto;
import com.suga.kafkaconsumerservice.entity.Product;
import com.suga.kafkaconsumerservice.mapper.ProductMapper;
import com.suga.kafkaconsumerservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "APIs for managing products in the inventory")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "API to create a new product in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Creating product with name: {}", productDto.getName());
        return ResponseEntity.status(201).body(productService.createProduct(productDto));
    }

    @Operation(summary = "Fetch a product by ID", description = "API to fetch a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product fetched successfully",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> fetchProductById(
            @Parameter(description = "ID of the product to be fetched", example = "1")
            @PathVariable @Min(value = 1, message = "Product ID must be a positive number") Long id) {
        log.info("Fetching product with ID: {}", id);
        return ResponseEntity.ok(productService.fetchProductById(id));
    }

    @Operation(summary = "Fetch all products", description = "API to fetch all products from the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products fetched successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))),
            @ApiResponse(responseCode = "404", description = "No products found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>> fetchAllProducts() {
        log.info("Fetching all products");
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    @Operation(summary = "Update a product", description = "API to update an existing product in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable @Min(value = 1, message = "Product ID must be a positive number") Long id,
            @Valid @RequestBody ProductDto productDto) {
        log.info("Updating product with ID: {}", id);
        Product product = ProductMapper.INSTANCE.dtoToEntity(productDto);
        product.setId(id);
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @Operation(summary = "Delete a product by ID", description = "API to delete a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = @Content(schema = @Schema(type = "string", example = "Product deleted successfully"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(
            @PathVariable @Min(value = 1, message = "Product ID must be a positive number") Long id) {
        log.info("Deleting product with ID: {}", id);
        return ResponseEntity.ok(productService.deleteProductById(id));
    }
}
