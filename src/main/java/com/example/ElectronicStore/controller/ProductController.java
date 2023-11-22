package com.example.ElectronicStore.controller;

import com.example.ElectronicStore.model.Product;
import com.example.ElectronicStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //Admin Requirement 1: Create Product
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try{
            // Create a new product and return a response
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        }catch (Exception e){
            String failMessage = "Fail to create product : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failMessage);
        }
    }

    //Admin Requirement 2: Remove Product
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable Long id) {
        try{
            // Create a new product and return a response
            Product changedProduct = productService.changeAvailability(id, 0);
            return ResponseEntity.status(HttpStatus.OK).body("Removed Successfully " + changedProduct);
        }catch (Exception e){
            String failMessage = "Fail to remove product : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failMessage);
        }
    }


    //Admin Requirement 3: Update Discount
    @PatchMapping("/id/{id}/{discount}")
    public ResponseEntity<?> updateDiscount(@PathVariable long id, @PathVariable double discount){
        try{
            Product product = productService.changeDiscount(id, discount);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            String failMessage = "Fail to update product discount : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failMessage);
        }
    }

}
