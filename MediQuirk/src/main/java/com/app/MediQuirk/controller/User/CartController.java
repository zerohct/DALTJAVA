package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.*;
import com.app.MediQuirk.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @Autowired
    private MomoService momoService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity, Model model) {
        try {
            cartService.addToCart(productId, quantity);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/cart?error=" + e.getMessage();
        }
        return "redirect:/products/detail/" + productId;
    }

    @GetMapping
    public String showCart(Model model, @RequestParam(value = "error", required = false) String error) {
        List<CartItem> cartItems = cartService.getCartItems();
        BigDecimal totalPrice = cartService.getTotalPrice();
        int totalItems = cartService.calculateTotalItems(cartItems);

        if (cartItems.isEmpty()) {
            model.addAttribute("errorMessage", "Your cart is empty.");
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalItems", totalItems);
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }

        return "User/cart/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeItemFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/increase")
    public String increaseQuantity(@RequestParam("productId") Long productId) {
        cartService.increaseQuantity(productId);
        return "redirect:/cart";
    }

    @PostMapping("/decrease")
    public String decreaseQuantity(@RequestParam("productId") Long productId) {
        cartService.decreaseQuantity(productId);
        return "redirect:/cart";
    }
    @PostMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        BigDecimal totalPrice = cartService.getTotalPrice();
        int totalItems = cartService.calculateTotalItems(cartItems);

        Users currentUser = getCurrentUser();
        String fullName = currentUser.getUserProfile().getLastName() + " " + currentUser.getUserProfile().getFirstName();
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userName", fullName);
        model.addAttribute("userPhone", currentUser.getPhone());
        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("userAddress", currentUser.getUserProfile().getAddress());
        return "User/cart/checkout";
    }

    @PostMapping("/payment")
    public String processPayment(@RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam("customerName") String customerName,
                                 @RequestParam("phoneCustomer") String phoneCustomer,
                                 @RequestParam("addressCustomer") String addressCustomer,
                                 @RequestParam("emailCustomer") String emailCustomer,
                                 @RequestParam("descriptionOrder") String descriptionOrder) {
        String orderId = "ORDER_" + System.currentTimeMillis();
        BigDecimal totalAmount = cartService.getTotalPrice();
        List<CartItem> cartItems = cartService.getCartItems();

        try {
            // Create the order
            Orders order = createOrder(orderId, totalAmount, "Pending", getCurrentUser().getUserId());
            orderService.saveOrderDetails(order, cartItems);
            // Create the payment
            Payment payment = createPayment(order, totalAmount, paymentMethod);

            switch (paymentMethod) {
                case "momo":
                    String paymentUrl;
                    try {
                        String orderInfo = "Payment for order " + orderId;
                        paymentUrl = momoService.createPaymentUrl(orderId, orderInfo);
                        return "redirect:" + paymentUrl;
                    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
                        return "redirect:/cart?error=Momo payment processing failed: " + e.getMessage();
                    }

                case "vnpay":
                    paymentUrl = vnPayService.createPaymentUrl(orderId, totalAmount);
                    return "redirect:" + paymentUrl;
                case "cod":
                    payment.setPaymentStatus("Pending");
                    paymentService.savePayment(payment);
                    cartService.clearCart();
                    return "redirect:/cart/success?orderId=" + order.getOrderNumber();
                default:
                    return "redirect:/cart?error=Invalid payment method";
            }
        } catch (Exception e) {
            return "redirect:/cart?error=Payment processing failed: " + e.getMessage();
        }
    }

    private Orders createOrder(String orderId, BigDecimal totalAmount, String orderStatus, Long userId) {
        String uniqueOrderId = "MQ_" + orderId + "_" + System.nanoTime();
        return orderService.createOrder(uniqueOrderId, totalAmount, orderStatus, userId);
    }

    private Payment createPayment(Orders order, BigDecimal amount, String paymentMethod) {
        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .amount(amount)
                .paymentStatus("Initiated")
                .order(order)
                .build();
        return paymentService.savePayment(payment);
    }

    @GetMapping("/success")
    public String orderSuccess(@RequestParam("orderId") String orderId, Model model) {
        Orders order = orderService.getOrderByOrderNumber(orderId);
        if (order == null) {
            return "redirect:/cart?error=Order not found";
        }

        model.addAttribute("order", order);

        Users currentUser = getCurrentUser();
        String fullName = currentUser.getUserProfile().getLastName() + " " + currentUser.getUserProfile().getFirstName();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userName", fullName);
        model.addAttribute("userPhone", currentUser.getPhone());
        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("userAddress", currentUser.getUserProfile().getAddress());

        return "User/cart/order_success";
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.findByUsername(currentUsername).orElse(null);
    }
}