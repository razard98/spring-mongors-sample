package com.example.mongo.rs;

import com.example.mongo.rs.user.domain.Customer;
import com.example.mongo.rs.user.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MongoRsApplication implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(MongoRsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /*Customer customer1 = new Customer("hong", "kildong");
        Customer customer2 = new Customer("kang", "sunghyun");

        customerRepository.insert(Arrays.asList(customer1, customer2));*/

        List<Customer> customers = customerRepository.findAll();
        System.out.println(customers);
    }
}
