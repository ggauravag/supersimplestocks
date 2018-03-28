package com.jpmorgan.entity;

import java.util.Objects;

public class Stock {

    private String symbol;

    private StockType stockType;

    private Double lastDividend;

    private Double fixedDividend;

    private Integer parValue;

    public Stock(String symbol, StockType stockType, Double lastDividend, Double fixedDividend, Integer parValue) {
        this.symbol = symbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockType getStockType() {
        return stockType;
    }

    public Double getLastDividend() {
        return lastDividend;
    }

    public Double getFixedDividend() {
        return fixedDividend;
    }

    public Integer getParValue() {
        return parValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol) &&
                stockType == stock.stockType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, stockType);
    }
}
