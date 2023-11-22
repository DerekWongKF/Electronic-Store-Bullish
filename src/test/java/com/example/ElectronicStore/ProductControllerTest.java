package com.example.ElectronicStore;

import com.example.ElectronicStore.controller.*;
import com.example.ElectronicStore.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@Transactional
public class ProductControllerTest {

    @Autowired
    private ProductController productController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void AdminCreateNewNormalProduct() throws Exception {
        String name = "Coke";
        double price = 10;

        Product product = new Product(name, price);
        String requestContent = String.format("{\"name\": \"%s\", \"price\": %s}", name, price);

        MvcResult result = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value(product.getName()))
                        .andExpect(jsonPath("$.price").value(product.getPrice()))
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }


    @Test
    public void AdminDeleteProduct() throws Exception {
        String name = "Coke";
        double price = 5;
        double discount = 30;

        Product product = new Product(name, price, discount, 1);

        String requestContent = String.format("{\"name\": \"%s\", \"price\": %s,\"discount\": %s,\"availability\": %s}", name, price, discount, 1);

        //Add product to databse
        MvcResult productResult = mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andReturn();


        String productResponseBody = productResult.getResponse().getContentAsString();
        product = new ObjectMapper().readValue(productResponseBody, Product.class);

        //Delete product with product id
        MvcResult result = mockMvc.perform(delete(("/product/id/{productId}"), product.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }


    @Test
    public void AdminUpdateProductDiscount() throws Exception {

        String name = "Coke";
        double price = 5;
        double discount = 30;
        double finalDiscount = 40;

        String requestContent = String.format("{\"name\": \"%s\", \"price\": %s,\"discount\": %s,\"availability\": %s}", name, price, discount, 1);

        MvcResult productResult = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andReturn();

        String productResponseBody = productResult.getResponse().getContentAsString();
        Product product = new ObjectMapper().readValue(productResponseBody, Product.class);

        MvcResult result = mockMvc.perform(patch(("/product/id/{productId}/{discount}"), product.getId(), finalDiscount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.discount").value(finalDiscount))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }



}
