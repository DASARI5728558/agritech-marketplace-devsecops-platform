package com.example.agrimarket.controller;

import com.example.agrimarket.dto.FarmerRequest;
import com.example.agrimarket.dto.FarmerResponse;
import com.example.agrimarket.service.FarmerService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @GetMapping
    public List<FarmerResponse> findAll() {
        return farmerService.findAll();
    }

    @GetMapping("/{id}")
    public FarmerResponse findById(@PathVariable Long id) {
        return farmerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<FarmerResponse> create(@Valid @RequestBody FarmerRequest request) {
        FarmerResponse response = farmerService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/farmers/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public FarmerResponse update(@PathVariable Long id, @Valid @RequestBody FarmerRequest request) {
        return farmerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        farmerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
