package com.example.agrimarket.service;

import com.example.agrimarket.dto.ProduceRequest;
import com.example.agrimarket.dto.ProduceResponse;
import com.example.agrimarket.entity.Farmer;
import com.example.agrimarket.entity.Produce;
import com.example.agrimarket.exception.ResourceNotFoundException;
import com.example.agrimarket.repository.ProduceRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProduceService {

    private final ProduceRepository produceRepository;
    private final FarmerService farmerService;

    public ProduceService(ProduceRepository produceRepository, FarmerService farmerService) {
        this.produceRepository = produceRepository;
        this.farmerService = farmerService;
    }

    public List<ProduceResponse> findAll() {
        return produceRepository.findAll().stream()
                .map(ProduceResponse::from)
                .toList();
    }

    public List<ProduceResponse> findByCategory(String category) {
        return produceRepository.findByCategoryIgnoreCase(category).stream()
                .map(ProduceResponse::from)
                .toList();
    }

    public List<ProduceResponse> findByFarmer(Long farmerId) {
        return produceRepository.findByFarmerId(farmerId).stream()
                .map(ProduceResponse::from)
                .toList();
    }

    public ProduceResponse findById(Long id) {
        return ProduceResponse.from(getProduce(id));
    }

    @Transactional
    public ProduceResponse create(ProduceRequest request) {
        Farmer farmer = farmerService.getFarmer(request.farmerId());
        Produce produce = new Produce(
                request.name().trim(),
                normalize(request.category()),
                request.unit().trim(),
                request.pricePerUnit(),
                request.quantityAvailable(),
                Boolean.TRUE.equals(request.organic()),
                request.harvestDate(),
                normalize(request.description()),
                farmer
        );
        return ProduceResponse.from(produceRepository.save(produce));
    }

    @Transactional
    public ProduceResponse update(Long id, ProduceRequest request) {
        Produce produce = getProduce(id);
        Farmer farmer = farmerService.getFarmer(request.farmerId());
        produce.setName(request.name().trim());
        produce.setCategory(normalize(request.category()));
        produce.setUnit(request.unit().trim());
        produce.setPricePerUnit(request.pricePerUnit());
        produce.setQuantityAvailable(request.quantityAvailable());
        produce.setOrganic(Boolean.TRUE.equals(request.organic()));
        produce.setHarvestDate(request.harvestDate());
        produce.setDescription(normalize(request.description()));
        produce.setFarmer(farmer);
        return ProduceResponse.from(produce);
    }

    @Transactional
    public void delete(Long id) {
        Produce produce = getProduce(id);
        produceRepository.delete(produce);
    }

    Produce getProduce(Long id) {
        return produceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produce not found with id: " + id));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
