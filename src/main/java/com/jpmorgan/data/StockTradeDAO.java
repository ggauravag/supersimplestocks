package com.jpmorgan.data;

import com.jpmorgan.entity.StockTrade;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface StockTradeDAO {

    StockTrade record(StockTrade stockTrade);

    List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction, Integer limit,
                                              Comparator<StockTrade> sortLogic);

    List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction);
}
