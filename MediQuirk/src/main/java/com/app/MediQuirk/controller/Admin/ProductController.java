package com.app.MediQuirk.controller.Admin;

import com.app.MediQuirk.model.Product;
import com.app.MediQuirk.services.CategoryService;
import com.app.MediQuirk.services.ProductService;
import com.app.MediQuirk.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SupplierService supplierService;

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/";

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "Admin/products/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "Admin/products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, BindingResult result, @RequestParam("file") MultipartFile file, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "Admin/products/add-product";
        }

        try {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                File uploadDir = new File(UPLOADED_FOLDER);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Tạo thư mục nếu nó không tồn tại
                }
                File uploadedFile = new File(uploadDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                file.transferTo(uploadedFile);
                product.setImageUrl("/images/" + file.getOriginalFilename());
            }
            productService.addProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "File upload failed"); // Thêm thông báo lỗi
            model.addAttribute("categories", categoryService.getAllCategories());
            return "Admin/products/add-product";
        }
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Admin/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (result.hasErrors()) {
            product.setProductId(id);
            return "Admin/products/update-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}