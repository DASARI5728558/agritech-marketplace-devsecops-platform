package com.example.agrimarket.repository;

import com.example.agrimarket.entity.Produce;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceRepository extends JpaRepository<Produce, Long> {

    List<Produce> findByCategoryIgnoreCase(String category);

    List<Produce> findByFarmerId(Long farmerId);
}
