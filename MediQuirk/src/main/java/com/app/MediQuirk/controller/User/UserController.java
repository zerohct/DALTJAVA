package com.app.MediQuirk.controller.User;

import com.app.MediQuirk.model.Users;
import com.app.MediQuirk.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "User/users/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new Users()); // Thêm một đối tượng User mới vào model
        return "User/users/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") Users users, // Validate đối tượng User
                                   @NotNull BindingResult bindingResult, // Kết quả của quátrình validate
 Model model) {
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "User/users/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(users); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(users.getUsername()); // Gán vai trò mặc định chongười dùng
        return "redirect:/login"; // Chuyển hướng người dùng tới trang "login"
    }
}