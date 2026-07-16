package com.example.agrimarket.repository;

import com.example.agrimarket.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findByProduceFarmerId(Long farmerId);
}
