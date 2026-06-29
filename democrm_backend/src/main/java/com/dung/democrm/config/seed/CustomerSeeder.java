package com.dung.democrm.config.seed;

import com.dung.democrm.entity.Customer;
import com.dung.democrm.repository.CustomerRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerSeeder {

    private final Dotenv dotenv;

    private final CustomerRepository customerRepository;

    public void seed() {
        if(customerRepository.count() > 0) return;

        List<Customer> customers = new ArrayList<>();

        for(int i = 1; i <= 1000; i++){
            Customer customer = new Customer();

            customer.setName("customer " + i);
            customer.setCompany("company " + i);
            customer.setPhone(generatePhone(i));
            customer.setEmail(dotenv.get("CUSTOMER_EMAIL_NAME") + i + "@gmail.com");

            customers.add(customer);
        }

        customerRepository.saveAll(customers);

    }

    private String generatePhone(int index){
        return String.format("090%07d",index);
    }
}
