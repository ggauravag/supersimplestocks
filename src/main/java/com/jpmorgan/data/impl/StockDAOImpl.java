package com.jpmorgan.data.impl;

import com.jpmorgan.data.StockDAO;
import com.jpmorgan.entity.Stock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class StockDAOImpl implements StockDAO {

    private final List<Stock> stocks;

    public StockDAOImpl(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public Optional<Stock> findStockByRestriction(Predicate<Stock> stockRestriction) {
        return stocks.stream()
                .filter(stockRestriction)
                .findFirst();
    }

    @Override
    public List<Stock> getAllStocks() {
        return Collections.unmodifiableList(stocks);
    }
}
