package com.jpmorgan.service.impl;

import com.jpmorgan.data.StockDAO;
import com.jpmorgan.data.StockTradeDAO;
import com.jpmorgan.dto.StockTradeDTO;
import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;
import com.jpmorgan.entity.query.StockSymbolRestriction;
import com.jpmorgan.entity.query.TradeStockRestriction;
import com.jpmorgan.entity.query.TradeTimeRestriction;
import com.jpmorgan.operation.StockComputationProvider;
import com.jpmorgan.service.StockOperationService;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockOperationServiceImpl implements StockOperationService {

    @Autowired
    private StockComputationProvider stockComputationProvider;

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private StockTradeDAO stockTradeDAO;

    @Autowired
    private Clock clock;

    private static final int RESULT_ROUNDING = 2;

    @Override
    public double calculateDividendYield(String stockName, Double marketPrice) {
        final Optional<Stock> stockOptional = stockDAO.findStockByRestriction(new StockSymbolRestriction(stockName));
        if (stockOptional.isPresent()) {
            final Stock stock = stockOptional.get();
            return format(stockComputationProvider.getStockOperation(stock.getStockType()).calculateDividendYield(stock,
                    marketPrice));
        } else {
            throw new IllegalArgumentException(String.format("No stock information is present for stock '%s'", stockName));
        }
    }

    @Override
    public double calculatePERatio(String stockName, Double marketPrice) {
        final Optional<Stock> stockOptional = stockDAO.findStockByRestriction(new StockSymbolRestriction(stockName));
        if (stockOptional.isPresent()) {
            final Stock stock = stockOptional.get();
            return format(stockComputationProvider.getStockOperation(stock.getStockType()).calculatePERatio(stock,
                    marketPrice));
        } else {
            throw new IllegalArgumentException(String.format("No stock information is present for stock '%s'", stockName));
        }
    }

    @Override
    public double calculateVolumeWeightedStockPrice(String stockName) {
        final Optional<Stock> stockOptional = stockDAO.findStockByRestriction(new StockSymbolRestriction(stockName));
        if (stockOptional.isPresent()) {
            final Stock stock = stockOptional.get();
            Instant now = clock.instant();
            Instant fifteen15MinutesBefore = now.minus(15, ChronoUnit.MINUTES);
            final List<StockTrade> stockTrades = stockTradeDAO.searchTrade(new TradeStockRestriction(stock)
                    .and(new TradeTimeRestriction(Date.from(fifteen15MinutesBefore), Date.from(now))));
            if (stockTrades.size() > 0) {
                return format(stockComputationProvider.getStockOperation(stock.getStockType()).calculateVolumeWeightedStockPrice(stockTrades));
            } else {
                throw new IllegalStateException(String.format("No trade history available for stock '%s'", stockName));
            }
        } else {
            throw new IllegalArgumentException(String.format("No stock information is present for stock '%s'", stockName));
        }
    }

    @Override
    public void registerTrade(StockTradeDTO stockTradeDTO) {
        final Optional<Stock> optionalStock = stockDAO.findStockByRestriction(new StockSymbolRestriction(stockTradeDTO.getStockSymbol()));
        if (optionalStock.isPresent()) {
            final Stock stock = optionalStock.get();
            StockTrade stockTrade = new StockTrade(stock, Date.from(clock.instant()), stockTradeDTO.getQuantity(), stockTradeDTO.getTradeType(), stockTradeDTO.getTradePrice());
            stockTradeDAO.record(stockTrade);
        } else {
            throw new IllegalArgumentException(String.format("No stock information is present for stock '%s'", stockTradeDTO.getStockSymbol()));
        }
    }

    @Override
    public double calculateGBCE() {
        final List<Stock> allStocks = stockDAO.getAllStocks();
        final List<Double> stockPrices = allStocks.stream()
                .flatMap(stock -> {
                    final Comparator<StockTrade> sortByTimeDesc = Collections.reverseOrder(Comparator.comparing(StockTrade::getTimestamp));
                    return stockTradeDAO.searchTrade(new TradeStockRestriction(stock), 1, sortByTimeDesc).stream();
                })
                .map(StockTrade::getTradePrice)
                .collect(Collectors.toList());

        if (stockPrices.isEmpty()) {
            throw new IllegalStateException("No stock trade has been recorded, hence cannot calculate GBCE All Share Index");
        }

        double[] stockPriceInDouble = new double[stockPrices.size()];
        for (int i = 0; i < stockPrices.size(); i++) {
            stockPriceInDouble[i] = stockPrices.get(i);
        }

        return format(StatUtils.geometricMean(stockPriceInDouble));
    }

    private double format(double value) {
        return Precision.round(value, RESULT_ROUNDING);
    }
}
