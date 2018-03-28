package com.jpmorgan;

import com.jpmorgan.data.StockDAO;
import com.jpmorgan.data.impl.StockDAOImpl;
import com.jpmorgan.entity.Stock;
import com.jpmorgan.entity.StockType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StockDAO stockDAOImpl() {
        List<Stock> stocks = new CopyOnWriteArrayList<>();
        stocks.add(new Stock("TEA", StockType.COMMON, 0.0, null, 100));
        stocks.add(new Stock("POP", StockType.COMMON, 8.0, null, 100));
        stocks.add(new Stock("ALE", StockType.COMMON, 23.0, null, 60));
        stocks.add(new Stock("GIN", StockType.PREFERRED, 8.0, 0.02, 100));
        stocks.add(new Stock("JOE", StockType.COMMON, 13.0, null, 250));
        return new StockDAOImpl(stocks);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
