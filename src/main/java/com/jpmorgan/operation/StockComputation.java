package com.jpmorgan.operation;

import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;

import java.util.List;

public interface StockComputation {

    /**
     * Calculates dividend yield for given stock and market price
     *
     * @param stock       stock
     * @param marketPrice non negative market price
     *
     * @return calculated dividend yield
     */
    double calculateDividendYield(Stock stock, Double marketPrice);

    /**
     * Calculates PE Ratio for the given stock and market price
     *
     * @param stock       stock
     * @param marketPrice non negative market price
     *
     * @return calculated PE Ratio
     */
    default double calculatePERatio(Stock stock, Double marketPrice) {
        return marketPrice / stock.getLastDividend();
    }

    /**
     * Calculates volume weighted stock price among the given list of trades
     *
     * @param stockTrades list of stock trades to be considered for calculation
     *
     * @return volume weighted stock price
     */
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
