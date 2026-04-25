package com.example.shop.controller;

import com.example.shop.dto.ProductForm;
import com.example.shop.model.Product;
import com.example.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        return "admin/product-form";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute ProductForm form) {
        Product product = new Product(
                form.getName(),
                form.getDescription(),
                form.getPrice(),
                form.getImageUrl()
        );
        productService.save(product);
        return "redirect:/admin";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductForm form = new ProductForm();
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setImageUrl(product.getImageUrl());
        model.addAttribute("productForm", form);
        model.addAttribute("id", id);
        return "admin/product-form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductForm form) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setImageUrl(form.getImageUrl());
        productService.save(product);
        return "redirect:/admin";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin";
    }
}
