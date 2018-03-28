package com.jpmorgan.entity.query;

import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;

import java.util.Objects;
import java.util.function.Predicate;

public class TradeStockRestriction implements Predicate<StockTrade> {

    private final Stock stock;

    public TradeStockRestriction(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean test(StockTrade stockTrade) {
        return stockTrade.getStock().equals(stock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeStockRestriction that = (TradeStockRestriction) o;
        return Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stock);
    }
}
