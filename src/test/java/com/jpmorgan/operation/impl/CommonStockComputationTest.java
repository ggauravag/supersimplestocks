package com.jpmorgan.operation.impl;

import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;
import com.jpmorgan.entity.StockType;
import com.jpmorgan.entity.TradeType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CommonStockComputationTest {

    private CommonStockComputation commonStockComputation;

    private Stock stock;

    @Before
    public void setUp() throws Exception {
        commonStockComputation = new CommonStockComputation();
        stock = new Stock("TEA", StockType.COMMON, 50.0, null, 100);
    }

    @Test
    public void shouldComputeDividendYield() {
        final double dividendYield = commonStockComputation.calculateDividendYield(stock, 10.0);
        assertThat(dividendYield, is(5.0));
    }

    @Test
    public void shouldComputePERatio() {
        final double peRatio = commonStockComputation.calculatePERatio(stock, 10.0);
        assertThat(peRatio, is(0.2));
    }

    @Test
    public void shouldComputeVolumeWeightedStockPrice() {
        // having
        final StockTrade stockTrade1 = new StockTrade(stock, null, 10.0,
                TradeType.BUY, 20.0);

        final StockTrade stockTrade2 = new StockTrade(stock, null, 10.0,
                TradeType.BUY, 25.0);

        final double volumeWeightedStockPrice = commonStockComputation.calculateVolumeWeightedStockPrice(Arrays.asList(stockTrade1, stockTrade2));
        assertThat(volumeWeightedStockPrice, is(22.5));
    }
}
