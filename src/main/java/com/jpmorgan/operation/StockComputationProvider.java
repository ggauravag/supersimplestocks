package com.jpmorgan.operation;

import com.jpmorgan.entity.StockType;

public interface StockComputationProvider {

    StockComputation getStockOperation(StockType stockType);

}
