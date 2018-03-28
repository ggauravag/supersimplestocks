package com.jpmorgan.operation;

import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;

import java.util.List;

public interface StockComputation {

    double calculateDividendYield(Stock stock, Double marketPrice);

    default double calculatePERatio(Stock stock, Double marketPrice) {
        return marketPrice / stock.getLastDividend();
    }

    default double calculateVolumeWeightedStockPrice(List<StockTrade> stockTrades) {
        final double totalTradeAmount = stockTrades.stream()
                .mapToDouble(StockTrade::getTradeAmount)
                .sum();

        final double totalTradeQuantity = stockTrades.stream()
                .mapToDouble(StockTrade::getQuantity)
                .sum();

        return totalTradeAmount / totalTradeQuantity;
    }

}
