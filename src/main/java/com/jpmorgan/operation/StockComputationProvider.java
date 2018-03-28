package com.jpmorgan.operation;

import com.jpmorgan.entity.StockType;

public interface StockComputationProvider {

    /**
     * Returns Computation for the specified type of stock
     *
     * @param stockType type of stock
     *
     * @return computation for the stock type
     *
     * @throws UnsupportedOperationException if no operation is supported for given stock type
     */
    StockComputation getStockOperation(StockType stockType);

}
