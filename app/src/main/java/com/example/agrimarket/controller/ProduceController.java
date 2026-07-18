package com.example.agrimarket.controller;

import com.example.agrimarket.dto.ProduceRequest;
import com.example.agrimarket.dto.ProduceResponse;
import com.example.agrimarket.service.ProduceService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/produce")
public class ProduceController {

    private final ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    @GetMapping
    public List<ProduceResponse> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long farmerId) {
        if (category != null && !category.isBlank()) {
            return produceService.findByCategory(category);
        }
        if (farmerId != null) {
            return produceService.findByFarmer(farmerId);
        }
        return produceService.findAll();
    }

    @GetMapping("/{id}")
    public ProduceResponse findById(@PathVariable Long id) {
        return produceService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProduceResponse> create(@Valid @RequestBody ProduceRequest request) {
        ProduceResponse response = produceService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/produce/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ProduceResponse update(@PathVariable Long id, @Valid @RequestBody ProduceRequest request) {
        return produceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
