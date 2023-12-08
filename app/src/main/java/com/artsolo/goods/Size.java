package com.artsolo.goods;

public class Size {

    private int id, productId;
    private String name;
    private int amount; // fixed amount

    public Size(int id, int productId, String name, int amount) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.amount = amount;
    }

    public int getSizeId() {
        return id;
    }

    public void setSizeId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSizeName() {
        return name;
    }

    public void setSizeName(String name) {
        this.name = name;
    }

    public int getSizeAmount() {
        return amount;
    }

    public void setSizeAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount() {
        amount++;
    }

    public void decreaseAmount() {
        if (amount - 1 >= 0) amount--;
    }
}
