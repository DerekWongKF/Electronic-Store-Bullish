package com.example.ElectronicStore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "discount")
    private double discount = 0; // discount = {discount}% off
    @Column(name = "availability")
    private int availability = 1;

    public Product(){};

    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", availability=" + availability +
                '}';
    }

    public Product(String name, double price, double discount, int availability){
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double calculateDiscountedPrice(){
        return this.price*(1-this.discount/100);
    }
}
