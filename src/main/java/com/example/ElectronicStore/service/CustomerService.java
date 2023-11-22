package com.example.ElectronicStore.service;

import com.example.ElectronicStore.model.Customer;
import com.example.ElectronicStore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByName(String name) {
        return customerRepository.findByName(name);
    }

    public synchronized Customer createCustomer(Customer customer) {
        if (customerRepository.findByName(customer.getName()).isPresent()){
            throw new RuntimeException("User name already exist");
        }
        return customerRepository.save(customer);
    }

    public synchronized void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}