package com.suga.kafkaconsumerservice.repo;


import com.suga.kafkaconsumerservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}