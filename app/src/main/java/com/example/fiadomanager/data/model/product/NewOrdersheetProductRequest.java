package com.example.fiadomanager.data.model.product;

import java.util.List;

public class NewOrdersheetProductRequest {

    private Long idClient;
    private Long idOrderSheet;
    private List<ProductRequest> products;

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Long getIdOrderSheet() {
        return idOrderSheet;
    }

    public void setIdOrderSheet(Long idOrderSheet) {
        this.idOrderSheet = idOrderSheet;
    }

    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }
}
