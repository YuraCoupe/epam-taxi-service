package com.epam.rd.java.basic.taxiservice.model;

import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;

import java.math.BigDecimal;

public class PriceRate {
    private Integer id;
    private CarCategory category;
    private BigDecimal rate;
    private BigDecimal minOrderPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinOrderPrice() {
        return minOrderPrice;
    }

    public void setMinOrderPrice(BigDecimal minOrderPrice) {
        this.minOrderPrice = minOrderPrice;
    }
}
