package com.example.ElectronicStore.model;

public class ReceiptItem {
    private String productName;
    private double discountedPrice;

    public ReceiptItem(String productName, double discountedPrice) {
        this.productName = productName;
        this.discountedPrice = discountedPrice;
    }

    @Override
    public String toString() {
        return "ReceiptItem{" +
                "productName='" + productName + '\'' +
                ", discountedPrice=" + discountedPrice +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
