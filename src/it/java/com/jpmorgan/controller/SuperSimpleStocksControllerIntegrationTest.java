package com.jpmorgan.controller;

import com.jpmorgan.controller.util.OperationResultDTO;
import com.jpmorgan.data.StockDAO;
import com.jpmorgan.data.impl.StockDAOImpl;
import com.jpmorgan.dto.StockTradeDTO;
import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockType;
import com.jpmorgan.entity.TradeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuperSimpleStocksControllerIntegrationTest {

    @TestConfiguration
    static class IntegrationTestConfiguration {
        static Clock clock = Mockito.mock(Clock.class);

        @Bean
        @Primary
        public StockDAO stockDAOImpl() {
            List<Stock> stocks = new ArrayList<>();
            stocks.add(new Stock("POP", StockType.COMMON, 8.0, null, 100));
            stocks.add(new Stock("GIN", StockType.PREFERRED, 8.0, 0.02, 100));
            return new StockDAOImpl(stocks);
        }

        @Bean
        @Primary
        public Clock getClock() {
            return clock;
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRecordTrade() {
        // having
        final StockTradeDTO stockTradeDTO = new StockTradeDTO();
        stockTradeDTO.setQuantity(1.0);
        stockTradeDTO.setStockSymbol("POP");
        stockTradeDTO.setTradePrice(25.0);
        stockTradeDTO.setTradeType(TradeType.BUY);
        when(IntegrationTestConfiguration.clock.instant()).thenAnswer(mock -> Instant.now(Clock.systemDefaultZone()));

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .postForEntity("/trade", stockTradeDTO, OperationResultDTO.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, hasProperty("status", is("SUCCESS")));
        reset(IntegrationTestConfiguration.clock);
    }

    @Test
    public void shouldCalculateDividendYieldForCommonStock() {
        // having
        final double marketPrice = 55.0;
        final String stockSymbol = "POP";

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .getForEntity("/{stockSymbol}/dividendYield?marketPrice={marketPrice}", OperationResultDTO.class,
                        stockSymbol, marketPrice);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, allOf(
                hasProperty("status", is("SUCCESS")),
                hasProperty("resultValue", is(0.15))) // for 'POP' 8.0 / 55.0 up to two digits precision
        );
    }

    @Test
    public void shouldCalculateDividendYieldForPreferredStock() {
        // having
        final double marketPrice = 40.0;
        final String stockSymbol = "GIN";

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .getForEntity("/{stockSymbol}/dividendYield?marketPrice={marketPrice}", OperationResultDTO.class,
                        stockSymbol, marketPrice);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, allOf(
                hasProperty("status", is("SUCCESS")),
                hasProperty("resultValue", is(0.05))) // 0.02 * 100 / 40.0 for 'GIN'
        );
    }

    @Test
    public void shouldCalculatePERatio() {
        // having
        final double marketPrice = 40.0;
        final String stockSymbol = "POP";

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .getForEntity("/{stockSymbol}/peRatio?marketPrice={marketPrice}", OperationResultDTO.class,
                        stockSymbol, marketPrice);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, allOf(
                hasProperty("status", is("SUCCESS")),
                hasProperty("resultValue", is(5.0))) // Market Price / Dividend for 'POP'
        );
    }

    @Test
    public void shouldCalculateGBCEAllShareIndex() {
        // having
        final StockTradeDTO stockTradePOP = new StockTradeDTO("POP", 1.0, TradeType.BUY, 5.0);
        final StockTradeDTO stockTradeGIN = new StockTradeDTO("GIN", 1.0, TradeType.BUY, 18.0);
        final StockTradeDTO stockTradeGINLatest = new StockTradeDTO("GIN", 1.0, TradeType.BUY, 16.0);

        when(IntegrationTestConfiguration.clock.instant()).thenAnswer(mock -> Instant.now(Clock.systemDefaultZone()));

        restTemplate.postForEntity("/trade", stockTradePOP, OperationResultDTO.class);
        restTemplate.postForEntity("/trade", stockTradeGIN, OperationResultDTO.class); // This trade price should be ignored
        restTemplate.postForEntity("/trade", stockTradeGINLatest, OperationResultDTO.class);

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .getForEntity("/allShareIndex", OperationResultDTO.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, allOf(
                hasProperty("status", is("SUCCESS")),
                hasProperty("resultValue", is(8.94))) // sqrt(5.0 * 16.0)
        );

        reset(IntegrationTestConfiguration.clock);
    }

    @Test
    public void shouldCalculateVolumeWeightedStockPrice() {
        // having
        final String stockSymbol = "GIN";
        final StockTradeDTO stockTradeGIN1 = new StockTradeDTO("GIN", 1.0, TradeType.BUY, 26.0);
        final StockTradeDTO stockTradeGIN2 = new StockTradeDTO("GIN", 2.0, TradeType.BUY, 12.0);
        final StockTradeDTO stockTradeGIN3 = new StockTradeDTO("GIN", 4.0, TradeType.BUY, 13.0);

        // Register first trade > 15 min ago
        when(IntegrationTestConfiguration.clock.instant())
                .thenReturn(Instant.now(Clock.systemDefaultZone()).minus(16, ChronoUnit.MINUTES));

        restTemplate.postForEntity("/trade", stockTradeGIN1, OperationResultDTO.class); // This trade price should be ignored

        reset(IntegrationTestConfiguration.clock);

        // Register other trades at current time
        when(IntegrationTestConfiguration.clock.instant()).thenAnswer(mock -> Instant.now(Clock.systemDefaultZone()));

        restTemplate.postForEntity("/trade", stockTradeGIN2, OperationResultDTO.class);
        restTemplate.postForEntity("/trade", stockTradeGIN3, OperationResultDTO.class);

        // when
        final ResponseEntity<OperationResultDTO> responseEntity = restTemplate
                .getForEntity("/{stockSymbol}/volumeWeightedStockPrice", OperationResultDTO.class, stockSymbol);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final OperationResultDTO operationResult = responseEntity.getBody();
        assertThat(operationResult, allOf(
                hasProperty("status", is("SUCCESS")),
                hasProperty("resultValue", is(12.67))) // sqrt(5.0 * 16.0)
        );
        reset(IntegrationTestConfiguration.clock);
    }
}
