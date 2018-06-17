package com.example.android.inventoryapp.model;

public class Product {

    private String name;
    private double price;
    private int quantity;
    private String supplierName;
    private String supplierPhoneNumber;

    public Product(String name, double price, int quantity, String supplierName, String supplierPhoneNumber) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }
}
