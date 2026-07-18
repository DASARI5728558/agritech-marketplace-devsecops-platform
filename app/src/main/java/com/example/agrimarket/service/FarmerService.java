package com.example.agrimarket.service;

import com.example.agrimarket.dto.FarmerRequest;
import com.example.agrimarket.dto.FarmerResponse;
import com.example.agrimarket.entity.Farmer;
import com.example.agrimarket.exception.ResourceNotFoundException;
import com.example.agrimarket.repository.FarmerRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FarmerService {

    private final FarmerRepository farmerRepository;

    public FarmerService(FarmerRepository farmerRepository) {
        this.farmerRepository = farmerRepository;
    }

    public List<FarmerResponse> findAll() {
        return farmerRepository.findAll().stream()
                .map(FarmerResponse::from)
                .toList();
    }

    public FarmerResponse findById(Long id) {
        return FarmerResponse.from(getFarmer(id));
    }

    @Transactional
    public FarmerResponse create(FarmerRequest request) {
        Farmer farmer = new Farmer(
                request.name().trim(),
                request.email().trim().toLowerCase(),
                normalize(request.phone()),
                normalize(request.farmName()),
                normalize(request.location())
        );
        return FarmerResponse.from(farmerRepository.save(farmer));
    }

    @Transactional
    public FarmerResponse update(Long id, FarmerRequest request) {
        Farmer farmer = getFarmer(id);
        farmer.setName(request.name().trim());
        farmer.setEmail(request.email().trim().toLowerCase());
        farmer.setPhone(normalize(request.phone()));
        farmer.setFarmName(normalize(request.farmName()));
        farmer.setLocation(normalize(request.location()));
        return FarmerResponse.from(farmer);
    }

    @Transactional
    public void delete(Long id) {
        Farmer farmer = getFarmer(id);
        farmerRepository.delete(farmer);
    }

    Farmer getFarmer(Long id) {
        return farmerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with id: " + id));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
