package com.quochao.demo.services;

import com.quochao.demo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product save(Product product);

    boolean deleteProduct(Long id);

    @Transactional
    Product updateProduct(Long id, Product product);

    List<Product> getProducts();

    boolean uploadToCloudinary(MultipartFile file);

    List<Product> findProductsWithSorting(String field);

    Page<Product> findProductsWithPagination(int currentPage, int pageSize);

    Page<Product> findProductsWithPaginationAndSorting(String field, int currentPage, int pageSize);

    Product findById(Long id);

    List<Product> findAllByKeyWord(String keyword);
}
