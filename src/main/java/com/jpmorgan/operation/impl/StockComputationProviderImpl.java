package com.jpmorgan.operation.impl;

import com.jpmorgan.entity.StockType;
import com.jpmorgan.operation.StockComputation;
import com.jpmorgan.operation.StockComputationProvider;
import org.springframework.stereotype.Service;

@Service
public class StockComputationProviderImpl implements StockComputationProvider {
    @Override
    public StockComputation getStockOperation(StockType stockType) {
        switch (stockType) {
            case COMMON:
                return new CommonStockComputation();
            case PREFERRED:
                return new PreferredStockComputation();
            default:
                throw new UnsupportedOperationException(String.format("StockType: %s is not supported", stockType));
        }
    }
}
