package com.jpmorgan.entity.query;

import com.jpmorgan.entity.StockTrade;

import java.util.Date;
import java.util.function.Predicate;

public class TradeTimeRestriction implements Predicate<StockTrade> {

    private final Date startTime;
    private final Date endTime;

    public TradeTimeRestriction(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean test(StockTrade stockTrade) {
        return stockTrade.getTimestamp().after(startTime) && stockTrade.getTimestamp().before(endTime);
    }
}
