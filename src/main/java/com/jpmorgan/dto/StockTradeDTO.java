package com.jpmorgan.dto;

import com.jpmorgan.entity.TradeType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class StockTradeDTO {

    @NotBlank(message = "Stock Symbol must be present")
    private String stockSymbol;

    private Date timestamp;

    @Min(value = 1L, message = "Quantity of stock trade cannot be less than 1")
    private Double quantity;

    @NotNull(message = "TradeType must be either SELL or BUY")
    private TradeType tradeType;

    @Min(value = 0L, message = "Trade price cannot be negative")
    private Double tradePrice;

    public StockTradeDTO() {

    }

    public StockTradeDTO(String stockSymbol, Double quantity, TradeType tradeType, Double tradePrice) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.tradePrice = tradePrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
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

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public void setTradePrice(Double tradePrice) {
        this.tradePrice = tradePrice;
    }
}
