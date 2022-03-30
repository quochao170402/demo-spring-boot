package com.quochao.demo.repositories;

import com.quochao.demo.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByCartId(Long id);
}
