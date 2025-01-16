package com.suga.kafkaconsumerservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suga.kafkaconsumerservice.entity.Product;
import com.suga.kafkaconsumerservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class InventoryListeners {

    private final ProductService productService;

    public InventoryListeners(ProductService productService) {
        this.productService = Objects.requireNonNull(productService, "Product service cannot be null");
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}")
    public void productNotify(String productPayload) {
        log.info("Listen message {}", productPayload);
        final ObjectMapper mapper = new ObjectMapper();
        try {
            Optional.ofNullable(productPayload)
                    .map(payload -> {
                        try {
                            return productService.updateProduct(mapper.readValue(payload, Product.class));
                        } catch (Exception exception) {
                            log.error(exception.getMessage());
                        }
                        return null;
                    });
        } catch (final Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
