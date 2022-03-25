package com.quochao.demo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quochao.demo.entities.Product;
import com.quochao.demo.repositories.ProductRepository;
import com.quochao.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Cloudinary cloudinary) {
        this.productRepository = productRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public Product save(Product product) {
        if (product == null) throw new IllegalStateException("Product is null");
        MultipartFile file = product.getFile();

        if (productRepository.findByName(product.getName()).isPresent())
            throw new IllegalStateException("Product name was existed");

        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadFileToCloud(file, product.getName());
            if (imageUrl != null && !imageUrl.isEmpty())
                product.setImage(imageUrl);
        }
        return productRepository.save(product);
    }


    @Override
    public boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product with id " + id + " does not exists"));
        productRepository.delete(product);
        return true;
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Product with id " + product.getId() + " does not exists"));

        if (product.getName() != null && !product.getName().isEmpty() && !product.getName().equals(updatedProduct.getName()))
            updatedProduct.setName(product.getName());

        if (product.getPrice() != null && product.getPrice().equals(updatedProduct.getPrice()))
            updatedProduct.setPrice(product.getPrice());

        if (product.getFile() != null && !product.getFile().isEmpty()) {
//            Remove existing file
            removeFileFromCloud(product.getName());
//            upload new file
            String imageUrl = uploadFileToCloud(product.getFile(), updatedProduct.getName());
            if (imageUrl != null && !imageUrl.isEmpty())
//                set file to entity
                updatedProduct.setImage(imageUrl);
        }
        return updatedProduct;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public boolean uploadToCloudinary(MultipartFile file) {
        try {
            String fileUrl = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"
//                            public_id : folder/sub_folder/..../filename => image is a filename
//                            "public_id", "H3/new_image"
                    )).get("url").toString();
            System.out.println(fileUrl);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Can not upload file to cloud");
        }
    }

    @Override
    public List<Product> findProductsWithSorting(String field) {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Override
    public Page<Product> findProductsWithPagination(int currentPage, int pageSize) {
        return productRepository.findAll(PageRequest.of(currentPage, pageSize));
    }

    @Override
    public Page<Product> findProductsWithPaginationAndSorting(String field, int currentPage, int pageSize) {
        return productRepository.findAll(PageRequest.of(currentPage, pageSize).withSort(Sort.by(Sort.Direction.ASC, field)));
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


    private String uploadFileToCloud(MultipartFile file, String name) {
        try {
            return cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto",
//                            public_id : folder/sub_folder/..../filename => image is a filename
                            "public_id", "H3/product/" + name.toLowerCase().replaceAll(" ", ""),
                            "overwrite", true
                    )).get("url").toString();
        } catch (IOException e) {
            throw new IllegalStateException("Can not upload file");
        }
    }

    private void removeFileFromCloud(String filename) {
        try {
            cloudinary.uploader().destroy(filename.toLowerCase().replaceAll(" ", ""), ObjectUtils.asMap());
        } catch (IOException e) {
            throw new IllegalStateException("Can not remove file");
        }
    }
}
