package org.example.basiccrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.basiccrud.repository")
public class BasicCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicCrudApplication.class, args);
    }

}
