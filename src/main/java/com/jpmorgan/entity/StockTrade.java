package com.jpmorgan.entity;

import java.util.Date;

public class StockTrade {

    private final Stock stock;

    private final Date timestamp;

    private final Double quantity;

    private final TradeType tradeType;

    private final Double tradePrice;

    public StockTrade(Stock stock, Date timestamp, Double quantity, TradeType tradeType, Double tradePrice) {
        this.stock = stock;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.tradePrice = tradePrice;
    }

    public Stock getStock() {
        return stock;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getQuantity() {
        return quantity;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public Double getTradePrice() {
        return tradePrice;
    }

    public Double getTradeAmount() {
        return tradePrice * quantity;
    }
}
