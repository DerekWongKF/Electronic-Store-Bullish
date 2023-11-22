package com.example.ElectronicStore.service;

import com.example.ElectronicStore.model.*;
import com.example.ElectronicStore.repository.BasketRepository;
import com.example.ElectronicStore.repository.CustomerRepository;
import com.example.ElectronicStore.repository.ProductRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public BasketService(BasketRepository basketRepository,ProductRepository productRepository, CustomerRepository customerRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    //Get the receipt for one customer, which include
    public Receipt getDiscountedBasketItemsWithCustomerId(Long customerId) {
        List<Basket> baskets = basketRepository.findByCustomerId(customerId);
        List<ReceiptItem> receiptItems = new ArrayList<>();

        for (Basket basket : baskets) {
            Optional<Product> product = productRepository.findById(basket.getProductId());

            if (product.isPresent()) {
                Product p = product.get();
                double discountedPrice = (p.getPrice() * (1 - p.getDiscount() / 100)) * basket.getQuantity();
                ReceiptItem basketItem = new ReceiptItem(p.getName(), discountedPrice);
                receiptItems.add(basketItem);
            }
        }
        Receipt receipt = new Receipt(receiptItems);
        return receipt;
    }

    //Delete basket item by customer and product id
    public synchronized void deleteBasketByCustomerIdAndProductId(Long customerId, Long productId) {
        Optional<Basket> basket = basketRepository.findByCustomerIdAndProductId(customerId, productId);
        if(basket.isPresent()){
            basketRepository.deleteByCustomerIdAndProductId(customerId, productId);
        }
        else {
            throw new RuntimeException("No basket found with given customer and product id");
        }

    }

    //Add basket to database
    public synchronized Basket addBasket(Basket basket) {
        long customerId = basket.getCustomerId();
        long productId = basket.getProductId();
        int quantity = basket.getQuantity();

        Optional<Basket> possibleBasket = basketRepository.findByCustomerIdAndProductId(customerId, productId);
        //check valid customerId and productId and quantity
        if (customerRepository.findById(customerId).isEmpty()){
            throw new RuntimeException("Fail to add basket: No customer with the given customer id exist: " + customerId);
        } else if (productRepository.findById(productId).isEmpty()) {
            throw new RuntimeException("Fail to add basket: No product with the given product id exist: " + productId);
        } else if (quantity < 0 ) {
            throw new RuntimeException("Fail to add basket: Invalid quantity: " + quantity);
        }

        // If basket with same customer and product id exist, add the quantity to the existing one
        if (possibleBasket.isPresent()){
            Basket existBasket = possibleBasket.get();
            existBasket.setQuantity(existBasket.getQuantity() + basket.getQuantity());
            return basketRepository.save(existBasket);
        }

        return basketRepository.save(basket);
    }
}