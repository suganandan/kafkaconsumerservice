package com.suga.kafkaconsumerservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suga.kafkaconsumerservice.dto.ProductDto;
import com.suga.kafkaconsumerservice.entity.Product;
import com.suga.kafkaconsumerservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {

    private final ProductService productService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String requestTopic;

    @Value("${spring.kafka.response.topic.name}")
    private String responseTopic;

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void productNotify(String productPayload) {
        log.info("Received message from topic {}: {}", requestTopic, productPayload);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Optional.ofNullable(productPayload)
                    .map(payload -> {
                        try {
                            ProductDto updatedProduct = productService.updateProduct(mapper.readValue(payload, Product.class));
                            String responseMessage = "Product " + updatedProduct.getId() + " updated successfully";

                            // Send response back to producer
                            kafkaTemplate.send(responseTopic, responseMessage);
                            log.info("Sent response to Producer on topic {}: {}", responseTopic, responseMessage);

                            return updatedProduct;
                        } catch (Exception exception) {
                            log.error("Error updating product: {}", exception.getMessage());
                        }
                        return null;
                    });
        } catch (Exception exception) {
            log.error("Error processing message: {}", exception.getMessage());
        }
    }
}
