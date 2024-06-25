package com.app.MediQuirk.controller.Admin;


import com.app.MediQuirk.model.Orders;
import com.app.MediQuirk.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String getOrderDashboard(Model model) {
        long orderCount = orderService.getOrderCount();
        Map<String, Long> statusCounts = orderService.getOrderCountByStatus();
        List<Orders> allOrders = orderService.getAllOrders();

        model.addAttribute("orderCount", orderCount);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("allOrders", allOrders);

        return "Admin/order/order-list";
    }

    @GetMapping("/detail/{orderId}")
    public String getOrderDetail(@PathVariable Long orderId, Model model) {
        Orders order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "Admin/order/orderDetail";
    }
}