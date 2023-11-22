package com.example.ElectronicStore;

import com.example.ElectronicStore.controller.*;
import com.example.ElectronicStore.model.Basket;
import com.example.ElectronicStore.model.Customer;
import com.example.ElectronicStore.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@DirtiesContext
public class BasketsControllerTest {

    @Autowired
    private BasketsController basketsController;
    @Autowired
    private ProductController productController;
    @Autowired
    private CustomersController customersController;
    private MockMvc mockMvc;

    private Customer customer;
    private Product product1;
    private Product product2;
    private Product product3;

    //Add product to database and Perform checking by using a function
    private Product addProductToDatabase(String productName, double price, double discount, int availability) throws Exception{
        String createProductRequestContent = String.format("{\"name\": \"%s\", \"price\": %s, \"discount\": %s, \"availability\": %s}", productName, price, discount, availability);
        MvcResult createProductResult = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProductRequestContent))
                .andExpect(jsonPath("$.name").value(productName))
                .andExpect(jsonPath("$.price").value(price))
                .andExpect(jsonPath("$.discount").value(discount))
                .andExpect(jsonPath("$.availability").value(availability))
                        .andReturn();

        String createProductResponseBody = createProductResult.getResponse().getContentAsString();
        return new ObjectMapper().readValue(createProductResponseBody, Product.class);
    }

    //Add product to basket
    private void addProductToBasket(Basket basket, MockMvc mockMvc) throws Exception{
        String addItemToBasketRequestContent = String.format("{\"customerId\": %d, \"productId\": %d, \"quantity\": %d}", basket.getCustomerId(),  basket.getProductId(), basket.getQuantity());

        MvcResult firstProductResult = mockMvc.perform(post("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addItemToBasketRequestContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(basket.getCustomerId()))
                .andExpect(jsonPath("$.productId").value(basket.getProductId()))
                .andExpect(jsonPath("$.quantity").value(basket.getQuantity()))
                .andReturn();
        String responseBody = firstProductResult.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }



    //Create customer and 3 products for each test
    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(basketsController,customersController,productController).build();

        //Create customer
        String customerName = "Pikachu";
        String createCustomerRequestContent = String.format("{\"name\": \"%s\"}", customerName);

        MvcResult createCustomerResult = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCustomerRequestContent))
                        .andReturn();

        String createCustomerResponseBody = createCustomerResult.getResponse().getContentAsString();
        this.customer = new ObjectMapper().readValue(createCustomerResponseBody, Customer.class);


        //Create first product
        String productName;
        double price;
        double discount;
        int availability = 1;

        productName = "Coke";
        price = 10;
        discount = 10;
        this.product1 = addProductToDatabase(productName, price, discount, availability);

        //Create second product
        productName = "Laptop";
        price = 20;
        discount = 20;

        this.product2 = addProductToDatabase(productName, price, discount, availability);


        //Create third product
        productName = "iPhone";
        price = 30;
        discount = 30;

        this.product3 = addProductToDatabase(productName, price, discount, availability);
    }

    @Test
    public void CustomerAddOneNormalProductToBasket() throws Exception {

        //Add first product to basket
        long customerId = this.customer.getId();
        long productId = this.product1.getId();
        int quantity = 2;
        Basket basket = new Basket(customerId, productId, quantity);
        addProductToBasket(basket, mockMvc);

    }

    @Test
    public void CustomerAddMultipleNormalProductsToBasket() throws Exception {

        //Add first product to basket
        long customerId = this.customer.getId();
        long productId = this.product1.getId();
        int quantity = 2;
        Basket basket = new Basket(customerId, productId, quantity);
        addProductToBasket(basket, mockMvc);

        //Add second normal product to basket
        basket.setProductId(this.product2.getId());
        addProductToBasket(basket, mockMvc);

        //Add third normal product to basket
        basket.setProductId(this.product3.getId());
        addProductToBasket(basket, mockMvc);
    }




    @Test
    public void CustomerDeleteProductFromBasket() throws Exception {

        long customerId = this.customer.getId();
        long productId = this.product1.getId();
        int quantity = 2;
        Basket basket = new Basket(customerId, productId, quantity);
        addProductToBasket(basket, mockMvc);

        MvcResult result = mockMvc.perform(delete(("/baskets/{customerId}/{productId}"),customerId,productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

    }

    @Test
    public void CustomerDeleteNonExistBasket() throws Exception {
        //Delete product id 1 without adding product id 1 in the database
        MvcResult result = mockMvc.perform(delete(("/baskets/{customerId}/{productId}"), 1,1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }


    @Test
    public void CustomerGetReceiptForMultipleBasketProducts() throws Exception {

        long customerId = this.customer.getId();
        long productId = this.product1.getId();
        int quantity1 = 2;
        int quantity2 = 10;
        int quantity3 = 8;
        Basket basket = new Basket(customerId, productId, quantity1);
        addProductToBasket(basket, mockMvc);

        basket.setProductId(this.product2.getId());
        basket.setQuantity(quantity2);
        addProductToBasket(basket, mockMvc);

        basket.setProductId(this.product3.getId());
        basket.setQuantity(quantity3);
        addProductToBasket(basket, mockMvc);

        MvcResult result = mockMvc.perform(get(("/baskets/receipt/{customerId}"),customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        // Parse the response body as JSON
        JSONObject responseJson = new JSONObject(responseBody);

        // Assert the discounted prices for each product
        JSONArray receiptItems = responseJson.getJSONArray("receiptItems");
        assertEquals(this.product1.calculateDiscountedPrice()*quantity1, receiptItems.getJSONObject(0).getDouble("discountedPrice"), 0.01);
        assertEquals(this.product2.calculateDiscountedPrice()*quantity2, receiptItems.getJSONObject(1).getDouble("discountedPrice"), 0.01);
        assertEquals(this.product3.calculateDiscountedPrice()*quantity3, receiptItems.getJSONObject(2).getDouble("discountedPrice"), 0.01);

        // Assert the total price
        assertEquals(this.product1.calculateDiscountedPrice()*quantity1 + this.product2.calculateDiscountedPrice()*quantity2 + this.product3.calculateDiscountedPrice()*quantity3, responseJson.getDouble("totalPrice"), 0.01);
    }






}
