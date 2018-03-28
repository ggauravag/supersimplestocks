package com.jpmorgan.data;

import com.jpmorgan.entity.StockTrade;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface StockTradeDAO {

    /**
     * Persists teh given trade and returns the same
     *
     * @param stockTrade to be persisted
     *
     * @return persisted trade
     */
    StockTrade record(StockTrade stockTrade);

    /**
     * Searches the trades with the given restrictions and specified limit, and sorting logic
     *
     * @param stockTradeRestriction restriction to be applied
     * @param limit                 maximum number of results to be returned
     * @param sortLogic             sorting logic for the entities
     *
     * @return list of maximum <code>limit</code> number of stocktrades matching given restrictions, sorted by provided
     *         logic
     */
    List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction, Integer limit,
                                 Comparator<StockTrade> sortLogic);

    /**
     * Searches the trades with the given restrictions, with no limit, sorted by recorded time
     *
     * @param stockTradeRestriction restriction to be applied
     *
     * @return list of stocktrades matching given restrictions, sorted by record time
     */
    List<StockTrade> searchTrade(Predicate<StockTrade> stockTradeRestriction);
}
