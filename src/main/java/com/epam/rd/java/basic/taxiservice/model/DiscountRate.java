package com.epam.rd.java.basic.taxiservice.model;

import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;

import java.math.BigDecimal;

public class DiscountRate {
    private Integer id;
    private BigDecimal moneySpent;
    private Integer discountRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(BigDecimal moneySpent) {
        this.moneySpent = moneySpent;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }
}
