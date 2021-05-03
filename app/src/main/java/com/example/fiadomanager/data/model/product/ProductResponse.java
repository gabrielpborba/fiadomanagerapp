package com.example.fiadomanager.data.model.product;

public class ProductResponse {

    private Long id;
    private String description;
    private String value;
    private String quantity;
    private Long idOrderSheetProduct;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getIdOrderSheetProduct() {
        return idOrderSheetProduct;
    }

    public void setIdOrderSheetProduct(Long idOrderSheetProduct) {
        this.idOrderSheetProduct = idOrderSheetProduct;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  description;
    }
}
