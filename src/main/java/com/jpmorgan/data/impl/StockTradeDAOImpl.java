package com.jpmorgan.data.impl;

import com.jpmorgan.data.StockTradeDAO;
import com.jpmorgan.entity.StockTrade;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class StockTradeDAOImpl implements StockTradeDAO {

    private final List<StockTrade> trades;

    private static final Comparator<StockTrade> DEFAULT_TIME_DESC_COMPARISION = Collections.reverseOrder(Comparator.comparing(StockTrade::getTimestamp));

    public StockTradeDAOImpl() {
        this.trades = new CopyOnWriteArrayList<>();
    }

    @Override
    public StockTrade record(StockTrade stockTrade) {
        trades.add(stockTrade);
        return stockTrade;
    }

    @Override
    public List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction, Integer limit, Comparator<StockTrade> sortLogic) {
        Objects.requireNonNull(stockTradeRestriction, "Restriction cannot be null");
        Objects.requireNonNull(limit, "Result limit cannot be null");
        Objects.requireNonNull(sortLogic, "Sort Logic cannot be null");

        final List<StockTrade> stockTrades = trades.stream()
                .filter(stockTradeRestriction)
                .sorted(sortLogic)
                .limit(limit)
                .collect(Collectors.toList());

        return stockTrades;
    }

    @Override
    public List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction) {
        return searchTrade(stockTradeRestriction, Integer.MAX_VALUE, DEFAULT_TIME_DESC_COMPARISION);
    }
}
