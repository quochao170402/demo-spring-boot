package com.quochao.demo.controllers;

import com.quochao.demo.entities.Product;
import com.quochao.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public List<Product> products() {
        return productService.getProducts();
    }

    @PostMapping
    public Product createProduct(@ModelAttribute Product product) {
        return productService.save(product);
    }


    @PutMapping(path = "/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @ModelAttribute Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    //    Upload file to cloudinary
    @PostMapping(path = "/uploadToCloud")
    public Boolean uploadImageToCloudinary(@RequestParam("File") MultipartFile file) {
        if (productService.uploadToCloudinary(file)) return true;
        else throw new IllegalStateException("Can't upload file to cloud");
    }

    @GetMapping(path = "/sort")
    public List<Product> getProductsWithSorting(
            @RequestParam(required = false, defaultValue = "id") String field) {
        return productService.findProductsWithSorting(field);
    }

    @GetMapping(path = "/paging")
    public Page<Product> getProductsWithPagination(
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return productService.findProductsWithPagination(currentPage, pageSize);
    }

    @GetMapping(path = "/pagination-and-sorting")
    public Page<Product> getProductsWithPaginationAndSorting(
            @RequestParam(required = false, defaultValue = "id") String field,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return productService.findProductsWithPaginationAndSorting(field, currentPage, pageSize);
    }
}
