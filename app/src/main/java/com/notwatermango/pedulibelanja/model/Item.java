package com.notwatermango.pedulibelanja.model;

public class Item {

    protected String nama;
    protected Double harga;
    protected Integer id;
    protected Integer quantity;
    protected Double totalPrice;

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNama() {
        return nama;
    }

    public Double getHarga() {
        return harga;
    }

    public Integer getId() {
        return id;
    }

    public Double getTotalPrice() {
        return this.getHarga() * this.getQuantity();
    }

    public Integer getQuantity() {
        return quantity;
    }
}

