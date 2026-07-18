package com.example.agrimarket.controller;

import com.example.agrimarket.dto.BuyerRequest;
import com.example.agrimarket.dto.BuyerResponse;
import com.example.agrimarket.service.BuyerService;
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
@RequestMapping("/api/v1/buyers")
public class BuyerController {

    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping
    public List<BuyerResponse> findAll() {
        return buyerService.findAll();
    }

    @GetMapping("/{id}")
    public BuyerResponse findById(@PathVariable Long id) {
        return buyerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BuyerResponse> create(@Valid @RequestBody BuyerRequest request) {
        BuyerResponse response = buyerService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/buyers/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public BuyerResponse update(@PathVariable Long id, @Valid @RequestBody BuyerRequest request) {
        return buyerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        buyerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
