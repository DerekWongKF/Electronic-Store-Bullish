package com.example.ElectronicStore.controller;

import com.example.ElectronicStore.model.Basket;
import com.example.ElectronicStore.model.Receipt;
import com.example.ElectronicStore.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baskets")
public class BasketsController {

    private final BasketService basketService;

    @Autowired
    public BasketsController(BasketService basketService) {
        this.basketService = basketService;
    }


    //Get receipt, which contain all basket items and discounter prices, and the final price, of a customer by customerId
    @GetMapping("/receipt/{customerId}")
    public ResponseEntity<Receipt> getReceiptByCustomerId(@PathVariable Long customerId) {
        Receipt receipt = basketService.getDiscountedBasketItemsWithCustomerId(customerId);
        return ResponseEntity.ok(receipt);
    }

    //Add product to basket
    @PostMapping
    public ResponseEntity<?> addBasket(@RequestBody Basket basket) {
        try{
            basket = basketService.addBasket(basket);
            return ResponseEntity.status(HttpStatus.CREATED).body(basket);
        }catch (Exception e){
            String failMessage = "Fail to add basket item : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failMessage);
        }
    }

    //Delete basket item by customerId and productId
    @DeleteMapping("/{customerId}/{productId}")
    public ResponseEntity<?> deleteBasketByCustomerIdAndProductId( @PathVariable Long customerId, @PathVariable Long productId) {
        try{
            basketService.deleteBasketByCustomerIdAndProductId(customerId, productId);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        }catch (Exception e){
            String failMessage = "Fail to delete basket item : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failMessage);
        }
    }

}