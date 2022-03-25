package com.quochao.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Product {

    @Id
//    @SequenceGenerator(
//            name = "product_sequence",
//            sequenceName = "product_sequence",
//            allocationSize = 1
//    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
//            strategy = GenerationType.SEQUENCE,
//            generator = "product_sequence"
    )
    private Long id;
    private String name;
    private Double price;
    private String image;
    @Transient
    @JsonIgnore
    private MultipartFile file;

    public Product() {
    }

    public Product(String name, Double price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Product(Long id, String name, Double price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getCreatedDate() {
        return LocalDate.now();
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
