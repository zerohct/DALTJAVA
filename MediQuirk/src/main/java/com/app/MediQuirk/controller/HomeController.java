package com.app.MediQuirk.controller;

import com.app.MediQuirk.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/")
    public String HomePage(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "User/index";
    }
}
