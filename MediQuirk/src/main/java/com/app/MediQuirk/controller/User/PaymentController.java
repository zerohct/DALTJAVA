package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.Orders;
import com.app.MediQuirk.services.CartService;
import com.app.MediQuirk.services.MomoService;
import com.app.MediQuirk.services.OrderService;
import com.app.MediQuirk.services.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private MomoService momoService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/momo-return")
    public String momoReturn(@RequestParam Map<String, String> queryParams, Model model) {
        String errorCode = queryParams.get("errorCode");
        String orderId = queryParams.get("orderId");

        if (orderId == null) {
            return "redirect:/cart?error=Order not found";
        }

        try {
            if (!momoService.verifySignature(queryParams)) {
                return "redirect:/cart?error=Invalid signature";
            }

            Orders order = orderService.getOrderByOrderNumber(orderId);

            if (errorCode == null || "0".equals(errorCode)) {
                orderService.updateOrderStatus(orderId, "PAID");
                cartService.clearCart();
                return "redirect:/cart/success?orderId=" + orderId;
            } else {
                return "redirect:/cart?error=Payment failed. Please try again.";
            }
        } catch (Exception e) {
            return "redirect:/cart?error=Payment processing failed: " + e.getMessage();
        }
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> queryParams, Model model) {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String orderNumber = queryParams.get("vnp_TxnRef");

        if (orderNumber == null) {
            model.addAttribute("message", "Không tìm thấy thông tin đơn hàng.");
            return "payment-failure";
        }

        try {
            Orders order = orderService.getOrderByOrderNumber(orderNumber);

            if ("00".equals(vnp_ResponseCode)) {
                orderService.updateOrderStatus(orderNumber, "PAID");
                cartService.clearCart();
                model.addAttribute("message", "Thanh toán thành công!");
                return "payment-success";
            } else {
                model.addAttribute("message", "Thanh toán thất bại. Vui lòng thử lại.");
                return "payment-failure";
            }
        } catch (RuntimeException e) {
            model.addAttribute("message", "Không tìm thấy đơn hàng: " + e.getMessage());
            return "payment-failure";
        }
    }

    @GetMapping("/cod-success")
    public String codSuccess(@RequestParam("orderNumber") String orderNumber, Model model) {
        try {
            Orders order = orderService.getOrderByOrderNumber(orderNumber);
            model.addAttribute("order", order);
            return "cod-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Không tìm thấy đơn hàng: " + e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/failure")
    public String paymentFailure(Model model) {
        model.addAttribute("message", "Thanh toán thất bại. Vui lòng thử lại.");
        return "payment-failure";
    }

}