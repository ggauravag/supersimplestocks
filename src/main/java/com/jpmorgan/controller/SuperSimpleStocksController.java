package com.jpmorgan.controller;

import com.jpmorgan.dto.OperationResult;
import com.jpmorgan.dto.OperationResult.Status;
import com.jpmorgan.dto.StockTradeDTO;
import com.jpmorgan.service.StockOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SuperSimpleStocksController {

    @Autowired
    private StockOperationService stockOperationService;

    @GetMapping("/{stockSymbol}/dividendYield")
    public OperationResult getDividedYield(@PathVariable("stockSymbol") String stockSymbol,
                                           @RequestParam("marketPrice") Double marketPrice) {
        final Double dividendYield = stockOperationService.calculateDividendYield(stockSymbol, marketPrice);
        return new OperationResult.Builder(Status.SUCCESS)
                .withResultValue(dividendYield).build();
    }

    @GetMapping("/{stockSymbol}/peRatio")
    public OperationResult getPERatio(@PathVariable("stockSymbol") String stockSymbol,
                                      @RequestParam("marketPrice") Double marketPrice) {
        final Double peRatio = stockOperationService.calculatePERatio(stockSymbol, marketPrice);
        return new OperationResult.Builder(Status.SUCCESS)
                .withResultValue(peRatio).build();
    }

    @PostMapping(path = "/trade", consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationResult registerTrade(@Valid @RequestBody StockTradeDTO stockTradeDTO) {
        stockOperationService.registerTrade(stockTradeDTO);
        return new OperationResult.Builder(Status.SUCCESS)
                .build();
    }

    @GetMapping("/{stockSymbol}/volumeWeightedStockPrice")
    public OperationResult getVolumeWeightedStockPrice(@PathVariable("stockSymbol") String stockSymbol) {
        final Double volumeWeightedStockPrice = stockOperationService.calculateVolumeWeightedStockPrice(stockSymbol);
        return new OperationResult.Builder(Status.SUCCESS)
                .withResultValue(volumeWeightedStockPrice).build();
    }

    @GetMapping("/allShareIndex")
    public OperationResult getAllShareIndex() {
        final Double volumeWeightedStockPrice = stockOperationService.calculateGBCE();
        return new OperationResult.Builder(Status.SUCCESS)
                .withResultValue(volumeWeightedStockPrice).build();
    }

}
