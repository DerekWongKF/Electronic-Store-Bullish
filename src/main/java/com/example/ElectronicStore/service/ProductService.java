package com.example.ElectronicStore.service;

import com.example.ElectronicStore.model.Product;
import com.example.ElectronicStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public synchronized Product createProduct(Product product){
        // perform 3 checking

        // checking 1: check valid price
        if (product.getPrice() < 0){
            throw new RuntimeException("Invalid price range. Price should be larger than 0");
        }
        // checking 2: check valid discount
        if (product.getDiscount() < 0 || product.getDiscount() >= 100){
            throw new RuntimeException("Invalid discount range. Discount should be larger than 0 and smaller than 100");
        }

        // checking 3 : check duplicate product name
        Optional<Product> possibleDuplicateProduct = productRepository.findByName(product.getName());
        if (possibleDuplicateProduct.isPresent()){
            if (possibleDuplicateProduct.get().getAvailability() == 1){
                throw new RuntimeException("Duplicated Product name");
            }
            else{
                // if the original product is not available, then replace the old product with new product
                possibleDuplicateProduct = Optional.of(product);
                throw new RuntimeException("Duplicated Product name, replaced the previous product");
            }
        }

        // pass all checking, start inserting product
        productRepository.save(product);
        return product;
    }

    public synchronized Product changeAvailability(long productId, int availability){
        Optional<Product> product = productRepository.findById(productId);
        //check availability range
        if(availability != 0 && availability != 1){
            throw new RuntimeException("Invalid availability");
        }
        //Check if the product exist in the db or not
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setAvailability(availability);
            productRepository.save(existingProduct);
        }
        else{
            throw new RuntimeException("No product found with id " + productId);
        }
        return product.get();
    }




    public synchronized Product changeDiscount(long productId, double discount){
        Optional<Product> product = productRepository.findById(productId);
        //check if discount is valid
        if (discount > 0 && discount < 100){}
        else{
            throw new RuntimeException("Invalid discount range, discount must be larger than 0 and smaller than 100.");
        }

        //Check if the product exist in the db or not
        if (product.isPresent()) {
            product.get().setDiscount(discount);
            productRepository.save(product.get());
        }
        else{
            throw new RuntimeException("No product found with id " + productId);
        }
        return product.get();
    }




}
