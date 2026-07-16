package com.example.agrimarket.service;

import com.example.agrimarket.dto.BuyerRequest;
import com.example.agrimarket.dto.BuyerResponse;
import com.example.agrimarket.entity.Buyer;
import com.example.agrimarket.exception.ResourceNotFoundException;
import com.example.agrimarket.repository.BuyerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public List<BuyerResponse> findAll() {
        return buyerRepository.findAll().stream()
                .map(BuyerResponse::from)
                .toList();
    }

    public BuyerResponse findById(Long id) {
        return BuyerResponse.from(getBuyer(id));
    }

    @Transactional
    public BuyerResponse create(BuyerRequest request) {
        Buyer buyer = new Buyer(
                request.name().trim(),
                request.email().trim().toLowerCase(),
                normalize(request.phone()),
                normalize(request.deliveryAddress())
        );
        return BuyerResponse.from(buyerRepository.save(buyer));
    }

    @Transactional
    public BuyerResponse update(Long id, BuyerRequest request) {
        Buyer buyer = getBuyer(id);
        buyer.setName(request.name().trim());
        buyer.setEmail(request.email().trim().toLowerCase());
        buyer.setPhone(normalize(request.phone()));
        buyer.setDeliveryAddress(normalize(request.deliveryAddress()));
        return BuyerResponse.from(buyer);
    }

    @Transactional
    public void delete(Long id) {
        Buyer buyer = getBuyer(id);
        buyerRepository.delete(buyer);
    }

    Buyer getBuyer(Long id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + id));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
