package com.example.agrimarket.service;

import com.example.agrimarket.dto.OrderRequest;
import com.example.agrimarket.dto.OrderResponse;
import com.example.agrimarket.dto.OrderStatusUpdateRequest;
import com.example.agrimarket.entity.Buyer;
import com.example.agrimarket.entity.Order;
import com.example.agrimarket.entity.OrderStatus;
import com.example.agrimarket.entity.Produce;
import com.example.agrimarket.exception.InsufficientStockException;
import com.example.agrimarket.exception.ResourceNotFoundException;
import com.example.agrimarket.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final BuyerService buyerService;
    private final ProduceService produceService;

    public OrderService(OrderRepository orderRepository, BuyerService buyerService, ProduceService produceService) {
        this.orderRepository = orderRepository;
        this.buyerService = buyerService;
        this.produceService = produceService;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    public List<OrderResponse> findByBuyer(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    public List<OrderResponse> findByFarmer(Long farmerId) {
        return orderRepository.findByProduceFarmerId(farmerId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.from(getOrder(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Buyer buyer = buyerService.getBuyer(request.buyerId());
        Produce produce = produceService.getProduce(request.produceId());

        BigDecimal quantity = request.quantity();
        if (produce.getQuantityAvailable().compareTo(quantity) < 0) {
            throw new InsufficientStockException(
                    "Insufficient stock for produce id " + produce.getId()
                            + ": requested " + quantity + " but only " + produce.getQuantityAvailable() + " available");
        }

        produce.setQuantityAvailable(produce.getQuantityAvailable().subtract(quantity));

        BigDecimal totalPrice = produce.getPricePerUnit().multiply(quantity);
        Order order = new Order(buyer, produce, quantity, totalPrice, OrderStatus.PENDING);
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = getOrder(id);
        order.setStatus(request.status());
        return OrderResponse.from(order);
    }

    @Transactional
    public void delete(Long id) {
        Order order = getOrder(id);
        orderRepository.delete(order);
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }
}
