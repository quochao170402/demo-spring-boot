package com.quochao.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quochao.demo.entities.Product;
import com.quochao.demo.entities.Student;
import com.quochao.demo.repositories.ProductRepository;
import com.quochao.demo.repositories.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class Config {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwqwccnkg",
                "api_key", "756699172569444",
                "api_secret", "x72HUrZLIj_hKhImB0K0F-0twr0",
                "secure", true));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, ProductRepository productRepository) {
        return args -> {
            Student student1 = new Student("Bui Quoc Hao1", "email1@email.com",
                    LocalDate.of(2001, 6, 18));

            Student student2 = new Student("Bui Quoc Hao2", "email2@email.com",
                    LocalDate.of(2001, 4, 8));

            Student student3 = new Student("Bui Quoc Hao3", "email2@email.com",
                    LocalDate.of(2001, 4, 8));

            studentRepository.saveAll(List.of(student1, student2, student3));

            List<Product> products = IntStream.range(1, 100)
                    .mapToObj(i -> new Product("product" + new Random().nextInt(100000), new Random().nextDouble(), "image" + new Random().nextInt(1000)))
                    .collect(Collectors.toList());
            productRepository.saveAll(products);
        };
    }
}
