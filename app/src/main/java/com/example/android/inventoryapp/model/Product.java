package com.example.android.inventoryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    public static final String KEY = "product_key";
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
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

    private Product(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        supplierName = in.readString();
        supplierPhoneNumber = in.readString();
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeInt(quantity);
        parcel.writeString(supplierName);
        parcel.writeString(supplierPhoneNumber);
    }
}
