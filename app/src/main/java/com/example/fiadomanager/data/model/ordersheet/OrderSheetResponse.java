package com.example.fiadomanager.data.model.ordersheet;

import com.example.fiadomanager.data.model.client.ClientResponse;
import com.example.fiadomanager.data.model.product.ProductResponse;

import java.util.Date;
import java.util.List;

public class OrderSheetResponse {

    private ClientResponse client;
    private List<ProductResponse> products;
    private Long id;
    private Date dateCreate;
    private Date datePayment;
    private String totalValue;
    private Integer status;

    public ClientResponse getClient() {
        return client;
    }

    public void setClient(ClientResponse client) {
        this.client = client;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
