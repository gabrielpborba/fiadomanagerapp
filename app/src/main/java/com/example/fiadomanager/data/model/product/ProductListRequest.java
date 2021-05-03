package com.example.fiadomanager.data.model.product;

import java.util.List;

public class ProductListRequest {

   private List<ProductRequest> products;

    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }
}
