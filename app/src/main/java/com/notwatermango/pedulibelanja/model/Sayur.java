package com.notwatermango.pedulibelanja.model;

public class Sayur extends Item {
    public String jenis;

    public Sayur(Integer _id, String _nama, Double _harga, String _jenis) {
        this.id = _id;
        this.nama = _nama;
        this.harga = _harga;
        this.jenis = _jenis;
        this.quantity = 0;
        this.totalPrice = 0.0;
    }
}
