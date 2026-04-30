package com.works.repository;

import com.works.CacheTestConfig;
import com.works.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(CacheTestConfig.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void testFindByEmailEqualsOrPhoneEqualsAllIgnoreCase_emailMatch() {
        // Prepare data
        Customer customer1 = new Customer();
        customer1.setName("John");
        customer1.setSurname("Doe");
        customer1.setEmail("test@example.com");
        customer1.setPhone("1234567890");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Jane");
        customer2.setSurname("Doe");
        customer2.setEmail("example@test.com");
        customer2.setPhone("9876543210");
        customer2.setPassword("password456");
        customerRepository.save(customer2);

        // Test
        List<Customer> results = customerRepository.findByEmailEqualsOrPhoneEqualsAllIgnoreCase("TEST@example.com", "0000000000");
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());
    }

    @Test
    public void testFindByEmailEqualsOrPhoneEqualsAllIgnoreCase_phoneMatch() {
        // Prepare data
        Customer customer1 = new Customer();
        customer1.setName("John");
        customer1.setSurname("Doe");
        customer1.setEmail("test@example.com");
        customer1.setPhone("1234567890");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Jane");
        customer2.setSurname("Doe");
        customer2.setEmail("example@test.com");
        customer2.setPhone("9876543210");
        customer2.setPassword("password456");
        customerRepository.save(customer2);

        // Test
        List<Customer> results = customerRepository.findByEmailEqualsOrPhoneEqualsAllIgnoreCase("notfound@example.com", "1234567890");
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getName());
    }

    @Test
    public void testFindByEmailEqualsOrPhoneEqualsAllIgnoreCase_noMatch() {
        // Prepare data
        Customer customer1 = new Customer();
        customer1.setName("John");
        customer1.setSurname("Doe");
        customer1.setEmail("test@example.com");
        customer1.setPhone("1234567890");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Jane");
        customer2.setSurname("Doe");
        customer2.setEmail("example@test.com");
        customer2.setPhone("9876543210");
        customer2.setPassword("password456");
        customerRepository.save(customer2);

        // Test
        List<Customer> results = customerRepository.findByEmailEqualsOrPhoneEqualsAllIgnoreCase("example@example.com", "9876543210");
        assertEquals(0, results.size());
    }

}




