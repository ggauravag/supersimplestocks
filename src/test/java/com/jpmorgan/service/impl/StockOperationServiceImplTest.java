package com.jpmorgan.service.impl;

import com.jpmorgan.data.StockDAO;
import com.jpmorgan.data.StockTradeDAO;
import com.jpmorgan.dto.StockTradeDTO;
import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockTrade;
import com.jpmorgan.entity.StockType;
import com.jpmorgan.entity.TradeType;
import com.jpmorgan.entity.query.StockSymbolRestriction;
import com.jpmorgan.entity.query.TradeStockRestriction;
import com.jpmorgan.operation.StockComputation;
import com.jpmorgan.operation.StockComputationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockOperationServiceImplTest {

    @Mock
    private StockComputationProvider stockComputationProvider;

    @Mock
    private StockDAO stockDAO;

    @Mock
    private StockTradeDAO stockTradeDAO;

    @Mock
    private Clock clock;

    @InjectMocks
    private StockOperationServiceImpl stockOperationService;

    private StockComputation stockComputation;

    @Before
    public void setUp() throws Exception {
        stockComputation = mock(StockComputation.class);
    }

    @Test
    public void shouldCalculateDividendYield() {
        // having
        final Stock stock = new Stock("TEST", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.findStockByRestriction(eq(new StockSymbolRestriction("TEST"))))
                .thenReturn(Optional.of(stock));

        when(stockComputationProvider.getStockOperation(eq(StockType.COMMON)))
                .thenReturn(stockComputation);

        // when
        stockOperationService.calculateDividendYield("TEST", 210.0);

        // then
        verify(stockComputation).calculateDividendYield(eq(stock), eq(210.0));
    }

    @Test
    public void shouldCalculatePERatio() {
        // having
        final Stock stock = new Stock("TEST", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.findStockByRestriction(eq(new StockSymbolRestriction("TEST"))))
                .thenReturn(Optional.of(stock));

        when(stockComputationProvider.getStockOperation(eq(StockType.COMMON)))
                .thenReturn(stockComputation);

        // when
        stockOperationService.calculatePERatio("TEST", 210.0);

        // then
        verify(stockComputation).calculatePERatio(eq(stock), eq(210.0));
    }

    @Test
    public void shouldCalculateVolumeWeightedStockPrice() {
        // having
        final Stock stock = new Stock("TEST", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.findStockByRestriction(eq(new StockSymbolRestriction("TEST"))))
                .thenReturn(Optional.of(stock));

        when(clock.instant()).thenReturn(Clock.systemDefaultZone().instant());

        final List<StockTrade> emptyList = Collections.singletonList(mock(StockTrade.class));
        when(stockTradeDAO.searchTrade(any(Predicate.class))).thenReturn(emptyList);

        when(stockComputationProvider.getStockOperation(eq(StockType.COMMON))).thenReturn(stockComputation);

        // when
        stockOperationService.calculateVolumeWeightedStockPrice("TEST");

        // then
        verify(stockComputation).calculateVolumeWeightedStockPrice(eq(emptyList));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhileCalculatingVolumeWeightedStockPrice() {
        // having
        final Stock stock = new Stock("TEST", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.findStockByRestriction(eq(new StockSymbolRestriction("TEST"))))
                .thenReturn(Optional.of(stock));

        when(clock.instant()).thenReturn(Clock.systemDefaultZone().instant());

        final List<StockTrade> emptyList = Collections.emptyList();
        when(stockTradeDAO.searchTrade(any(Predicate.class))).thenReturn(emptyList);

        // when
        stockOperationService.calculateVolumeWeightedStockPrice("TEST");
    }

    @Test
    public void shouldRegisterTrade() {
        // having
        final Stock stock = new Stock("TEST", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.findStockByRestriction(eq(new StockSymbolRestriction("TEST"))))
                .thenReturn(Optional.of(stock));

        when(clock.instant()).thenReturn(Clock.systemDefaultZone().instant());

        final StockTradeDTO stockTradeDTO = new StockTradeDTO("TEST", 10.0, TradeType.BUY, 25.0);

        // when
        stockOperationService.registerTrade(stockTradeDTO);

        // then
        verify(stockTradeDAO).record(argThat(
                stockTrade -> stockTrade.getQuantity().equals(stockTradeDTO.getQuantity())
                        && stockTrade.getTradeType() == stockTradeDTO.getTradeType()
                        && stockTrade.getStock() == stock
                        && stockTrade.getTradePrice().equals(stockTradeDTO.getTradePrice())
        ));
    }

    @Test
    public void shouldCalculateGBCE() {
        // having
        final Stock stock1 = new Stock("TEST1", StockType.COMMON, 35.0, null, 90);
        final Stock stock2 = new Stock("TEST2", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.getAllStocks()).thenReturn(Arrays.asList(stock1, stock2));

        final StockTrade stockTrade1 = mock(StockTrade.class);
        when(stockTrade1.getTradePrice()).thenReturn(25.0);

        final StockTrade stockTrade2 = mock(StockTrade.class);
        when(stockTrade2.getTradePrice()).thenReturn(25.0);

        when(stockTradeDAO.searchTrade(eq(new TradeStockRestriction(stock1)), eq(1), any()))
                .thenReturn(Collections.singletonList(stockTrade1));

        when(stockTradeDAO.searchTrade(eq(new TradeStockRestriction(stock2)), eq(1), any()))
                .thenReturn(Collections.singletonList(stockTrade2));

        // when
        final double gbce = stockOperationService.calculateGBCE();

        // then
        assertThat(gbce, is(25.0));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailToCalculateGBCE() {
        // having
        final Stock stock = new Stock("TEST1", StockType.COMMON, 35.0, null, 90);

        when(stockDAO.getAllStocks()).thenReturn(Arrays.asList(stock));

        when(stockTradeDAO.searchTrade(eq(new TradeStockRestriction(stock)), eq(1), any()))
                .thenReturn(Collections.emptyList());

        // when
        stockOperationService.calculateGBCE();
    }
}
