package com.suga.kafkaconsumerservice.service;


import com.suga.kafkaconsumerservice.dto.ProductDto;
import com.suga.kafkaconsumerservice.entity.Product;
import com.suga.kafkaconsumerservice.exception.ResourceNotFoundException;
import com.suga.kafkaconsumerservice.mapper.ProductMapper;
import com.suga.kafkaconsumerservice.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductDto createProduct(ProductDto productDto) {
        try {
            return ProductMapper.INSTANCE.entityToDto(productRepository.save(ProductMapper.INSTANCE.dtoToEntity(productDto)));
        } catch (final Exception exception) {
            log.error("Exception in creating the product : {}", exception.getMessage());
            throw new ResourceNotFoundException(exception.getMessage());
        }
    }

    public ProductDto fetchProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> {
            log.error("Product with ID {} not found", productId);
            return new ResourceNotFoundException("Product Not Found");
        });
        return ProductMapper.INSTANCE.entityToDto(product);
    }

    public List<ProductDto> fetchAllProducts() {
        final List<Product> products = Optional.of(productRepository.findAll()).filter(list -> !list.isEmpty()).orElseThrow(() -> {
            log.error("No products found in the database");
            return new ResourceNotFoundException("No products available");
        });
        return products.stream().map(ProductMapper.INSTANCE::entityToDto).toList();
    }

    public ProductDto updateProduct(Product product) {
        return productRepository.findById(product.getId())
                .map(existingProduct -> {
                    log.info("Updating product with ID {}", product.getId());
                    Product updatedProduct = productRepository.save(product);
                    log.info("Product with ID {} successfully updated", product.getId());
                    return ProductMapper.INSTANCE.entityToDto(updatedProduct);
                }).orElseThrow(() -> {
                    log.error("Product with ID {} does not exist", product.getId());
                    return new ResourceNotFoundException("Product does not exist.");
                });
    }


    public String deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(
                        product -> {
                            productRepository.deleteById(productId);
                            log.info("Product with ID {} deleted successfully", productId);
                        },
                        () -> {
                            log.error("Product with ID {} not found for deletion", productId);
                            throw new ResourceNotFoundException("Product Not Found");
                        }
                );
        return "Record Deleted Successfully";
    }


}
