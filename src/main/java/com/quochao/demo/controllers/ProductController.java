package com.quochao.demo.controllers;

import com.quochao.demo.dtos.ResponseObjectDTO;
import com.quochao.demo.entities.Product;
import com.quochao.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ResponseObjectDTO> products(@RequestParam(required = false) String keyword) {
        ResponseObjectDTO response = new ResponseObjectDTO("OK", "", null);
        if (keyword == null || keyword.isEmpty()) {
            response.setData(productService.getProducts());
            response.setMessage("All product");
        } else {
            response.setData(productService.findAllByKeyWord(keyword));
            response.setMessage("All product for your keyword : " + keyword);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObjectDTO> createProduct(@ModelAttribute Product product) {
        return (productService.save(product) == null) ?
                ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                        .body(new ResponseObjectDTO("FAILED", "Can not create product has information: " + product, null)) :
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseObjectDTO("OK", "Created product: ", product));

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseObjectDTO> updateProduct(@PathVariable Long id,
                                                           @ModelAttribute Product product) {
        return (productService.updateProduct(id, product) == null) ?
                ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                        .body(new ResponseObjectDTO("FAILED", "Can not update product has information: " + product, null)) :
                ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("OK", "Updated product: ", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObjectDTO> deleteProduct(@PathVariable Long id) {
        return (productService.deleteProduct(id)) ?
                ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                        .body(new ResponseObjectDTO("FAILED", "Cannot delete product", false)) :
                ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObjectDTO("OK", "Deleted product: ", true));
    }

    //    Upload file to cloudinary
    @PostMapping(path = "/uploadToCloud")
    public Boolean uploadImageToCloudinary(@RequestParam("File") MultipartFile file) {
        if (productService.uploadToCloudinary(file)) return true;
        else throw new IllegalStateException("Can't upload file to cloud");
    }

    @GetMapping(path = "/sort")
    public ResponseEntity<ResponseObjectDTO> getProductsWithSorting(
            @RequestParam(required = false, defaultValue = "id") String field) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("OK", "All products with sorting by field " + field,
                        productService.findProductsWithSorting(field)));
    }

    @GetMapping(path = "/paging")
    public ResponseEntity<ResponseObjectDTO> getProductsWithPagination(
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObjectDTO("OK", "All products with pagination",
                        productService.findProductsWithPagination(currentPage, pageSize)));
    }

    @GetMapping(path = "/pagination-and-sorting")
    public Page<Product> getProductsWithPaginationAndSorting(
            @RequestParam(required = false, defaultValue = "id") String field,
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return productService.findProductsWithPaginationAndSorting(field, currentPage, pageSize);
    }

}
