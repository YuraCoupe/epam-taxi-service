package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.model.Car.CarCategory;
import com.epam.rd.java.basic.taxiservice.model.DiscountRate;
import com.epam.rd.java.basic.taxiservice.model.PriceRate;
import com.epam.rd.java.basic.taxiservice.repository.DiscountRateRepository;
import com.epam.rd.java.basic.taxiservice.repository.PriceRateRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceService {
    private final PriceRateRepository priceRateRepository;
    private final DiscountRateRepository discountRateRepository;

    public PriceService(PriceRateRepository priceRateRepository, DiscountRateRepository discountRateRepository) {
        this.priceRateRepository = priceRateRepository;
        this.discountRateRepository = discountRateRepository;
    }

    public BigDecimal calculateTripPrice(Double distance, BigDecimal moneySpent, CarCategory category, int carsNumber) {
        PriceRate priceRate = priceRateRepository.findByCategory(category).orElseThrow();
        DiscountRate discountRate = discountRateRepository.findByMoneySpent(moneySpent).orElseThrow();
        BigDecimal distanceRate = priceRate.getRate().multiply(BigDecimal.valueOf(distance)).multiply(BigDecimal.valueOf(carsNumber));
        if (distanceRate.compareTo(priceRate.getMinOrderPrice()) < 0) {
            distanceRate = priceRate.getMinOrderPrice();
        }
        return distanceRate.multiply(BigDecimal.valueOf((100.0 - discountRate.getDiscountRate()) / 100.0)).setScale(2, RoundingMode.UP);
    }
}
