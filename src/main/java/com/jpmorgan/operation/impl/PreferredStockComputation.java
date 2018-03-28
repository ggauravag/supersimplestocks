package com.jpmorgan.operation.impl;

import com.jpmorgan.entity.Stock;
import com.jpmorgan.operation.StockComputation;

public class PreferredStockComputation implements StockComputation {
    @Override
    public double calculateDividendYield(Stock stock, Double marketPrice) {
        return stock.getFixedDividend() * stock.getParValue() / marketPrice;
    }
}
