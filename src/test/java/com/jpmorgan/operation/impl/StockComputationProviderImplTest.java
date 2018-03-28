package com.jpmorgan.operation.impl;

import com.jpmorgan.entity.StockType;
import com.jpmorgan.operation.StockComputation;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class StockComputationProviderImplTest {

    private StockComputationProviderImpl stockComputationProvider;

    @Before
    public void setUp() {
        stockComputationProvider = new StockComputationProviderImpl();
    }

    @Test
    public void shouldReturnCommonStockComputationProvider() {
        final StockComputation stockOperation = stockComputationProvider.getStockOperation(StockType.COMMON);

        assertThat(stockOperation, instanceOf(CommonStockComputation.class));
    }

    @Test
    public void shouldReturnPreferredStockComputationProvider() {
        final StockComputation stockOperation = stockComputationProvider.getStockOperation(StockType.PREFERRED);

        assertThat(stockOperation, instanceOf(PreferredStockComputation.class));
    }
}
