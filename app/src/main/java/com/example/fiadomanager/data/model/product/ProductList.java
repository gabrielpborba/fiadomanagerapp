package com.example.fiadomanager.data.model.product;

import java.util.List;

public class ProductList {

    private List<ProductResponse> products;

    public ProductList(List<ProductResponse> products) {
        this.products = products;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
