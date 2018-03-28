package com.jpmorgan.entity.query;

import com.jpmorgan.entity.Stock;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;

public class StockSymbolRestriction implements Predicate<Stock> {

    private final String stockSymbol;

    public StockSymbolRestriction(String stockSymbol) {
        if (StringUtils.isEmpty(stockSymbol)) {
            throw new IllegalArgumentException("Stock Symbol cannot be empty or null");
        }
        this.stockSymbol = stockSymbol;
    }

    @Override
    public boolean test(Stock stock) {
        return stock.getSymbol().equals(stockSymbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockSymbolRestriction that = (StockSymbolRestriction) o;
        return Objects.equals(stockSymbol, that.stockSymbol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stockSymbol);
    }
}
