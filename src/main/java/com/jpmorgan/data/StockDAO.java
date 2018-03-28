package com.jpmorgan.data;

import com.jpmorgan.entity.Stock;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface StockDAO {

    Optional<Stock> findStockByRestriction(Predicate<Stock> stockRestriction);

    List<Stock> getAllStocks();

}
