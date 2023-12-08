package com.artsolo.goods;

public class Product {

    private final int id;
    private String special;
    private String title;
    private double price;
    private byte[] imageBytes;

    public Product(int id, String special, String title, double price, byte[] imageBytes) {
        this.id = id;
        this.special = special;
        this.title = title;
        this.price = price;
        this.imageBytes = imageBytes;
    }

    public int getId() {
        return id;
    }

    public String getSpecial() {
        return special;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
