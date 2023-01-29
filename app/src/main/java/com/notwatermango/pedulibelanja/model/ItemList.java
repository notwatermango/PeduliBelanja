package com.notwatermango.pedulibelanja.model;

import java.util.ArrayList;

public class ItemList {
    public ArrayList<Item> cartList = new ArrayList<Item>();

    public ArrayList<Item> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Item> cartList) {
        this.cartList = cartList;
    }
}
