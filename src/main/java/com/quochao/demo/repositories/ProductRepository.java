package com.quochao.demo.repositories;

import com.quochao.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query("SELECT p from Product p where p.name like %?1%")
    List<Product> findAllByName(String keyword);
}
