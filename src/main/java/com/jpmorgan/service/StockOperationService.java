package com.jpmorgan.service;

import com.jpmorgan.dto.StockTradeDTO;

public interface StockOperationService {

    double calculateDividendYield(String stockName, Double marketPrice);

    double calculatePERatio(String stockName, Double marketPrice);

    double calculateVolumeWeightedStockPrice(String stockName);

    void registerTrade(StockTradeDTO stockTrade);

    double calculateGBCE();

}
