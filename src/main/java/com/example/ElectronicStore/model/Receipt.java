package com.example.ElectronicStore.model;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItems;

    private double totalPrice = 0;

    public Receipt(List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public double getTotalPrice(){
        this.totalPrice = 0;
        for (ReceiptItem receiptItem : receiptItems){
            this.totalPrice += receiptItem.getDiscountedPrice();
        }
        return this.totalPrice;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }


    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        this.receiptItems = receiptItems;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptItems=" + receiptItems +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
