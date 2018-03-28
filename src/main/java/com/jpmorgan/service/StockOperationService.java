package com.jpmorgan.service;

import com.jpmorgan.dto.StockTradeDTO;

public interface StockOperationService {

    /**
     * Calculates dividend yield for the given stock name and market price
     *
     * @param stockName   stock symbol
     * @param marketPrice current market price
     *
     * @return dividend yield
     *
     * @throws IllegalArgumentException if no stock is registered with given stock symbol
     */
    double calculateDividendYield(String stockName, Double marketPrice);

    /**
     * Calculates PE Ratio for the given stock name and market price
     *
     * @param stockName   stock symbol
     * @param marketPrice current market price
     *
     * @return PE ratio
     *
     * @throws IllegalArgumentException if no stock is registered with given stock symbol
     */
    double calculatePERatio(String stockName, Double marketPrice);

    /**
     * Calculates volume weighted stock price for the given stock symbol
     *
     * @param stockName stock symbol
     *
     * @return volumne weighted stock price
     *
     * @throws IllegalArgumentException if no stock is registered with given stock symbol
     * @throws IllegalStateException    if no trades are recorded for the specified stock symbol
     */
    double calculateVolumeWeightedStockPrice(String stockName);

    /**
     * Registers the given trade
     *
     * @param stockTrade trade to be registered
     *
     * @throws IllegalArgumentException if no stock is registered with given stock symbol in the {@link
     *                                  StockTradeDTO#getStockSymbol()}
     */
    void registerTrade(StockTradeDTO stockTrade);

    /**
     * Calculates GBCE on the basis of all stocks registered in the system, with their last recorded market price
     *
     * @return GBCE on the basis of last recorded market price
     *
     * @throws IllegalStateException if no trades are recorded
     */
    double calculateGBCE();

}
