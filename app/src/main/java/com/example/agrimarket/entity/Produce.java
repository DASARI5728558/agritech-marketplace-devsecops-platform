package com.example.agrimarket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "produce")
public class Produce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(length = 60)
    private String category;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "quantity_available", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAvailable;

    @Column(nullable = false)
    private boolean organic;

    @Column(name = "harvest_date")
    private LocalDate harvestDate;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Produce() {
    }

    public Produce(String name, String category, String unit, BigDecimal pricePerUnit,
                   BigDecimal quantityAvailable, boolean organic, LocalDate harvestDate,
                   String description, Farmer farmer) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.quantityAvailable = quantityAvailable;
        this.organic = organic;
        this.harvestDate = harvestDate;
        this.description = description;
        this.farmer = farmer;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(BigDecimal quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
