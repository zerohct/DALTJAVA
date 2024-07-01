package com.app.MediQuirk.services;

import com.app.MediQuirk.model.*;
import com.app.MediQuirk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;

    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    private  UsersRepository usersRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderDetailRepository orderDetailRepository) {
        this.ordersRepository = ordersRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public Orders createOrder(String orderId, BigDecimal totalAmount, String orderStatus,
                              Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        Orders order = new Orders();
        order.setOrderNumber(orderId);
        order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(orderStatus);
        order.setUser(user);

        return ordersRepository.save(order);
    }

    public long getOrderCount() {
        return ordersRepository.count();
    }

    public Map<String, Long> getOrderCountByStatus() {
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderStatus, Collectors.counting()));
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Orders getOrderById(Long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Orders getOrderByOrderNumber(String orderNumber) {
        return ordersRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
    }

    public void updateOrderStatus(String orderNumber, String newStatus) {
        Orders order = getOrderByOrderNumber(orderNumber);
        order.setOrderStatus(newStatus);
        ordersRepository.save(order);
    }

    public Orders getOrderByOrderId(long orderId) {
        return ordersRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));
    }
    public void saveOrderDetails(Orders order, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getProduct().getPrice())
                    .build();
            orderDetailRepository.save(orderDetail);
        }
    }
}