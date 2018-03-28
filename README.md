#Super Simple Stocks

This application is designed to serve as data store and computation provider for a stock market

## How to run it?

Just run ```mvn clean install``` and run ```java -jar target/supersimplestocks-1.0-SNAPSHOT.jar```

## Run Unit Tests
```mvn test```

## Run Integration Tests
```mvn verify```

## Assumptions

The application was designed under following assumptions:

- The only stocks which are stored are part of StockDAOImpl bean declaration, hence it only supports reading, it could 
be extended to support other operations as well
- The implementation is in-memory hence, the data structures used are thread-safe, in case of ideal implementation it 
would be replaced by actual persistent stores
- The restriction for data querying API could be more enhanced and vibrant
- Since, no where it was mentioned, I took the liberty to expose it via REST, it could also be used via asynchronous 
messaging, RPC or SOAP with appropriate modifications
- Currently there is no security mechanism in place
- It was assumed that Common and Preferred Stocks Computations could be diverse in operations hence they are built this 
way to be easily maintainable
- There could be more financial constraints related to values being stored or received as per business requirements
- Code coverage could have been enhanced, though, the core business logic in service and computations is 100% covered
- Haven't done javadocs, though it would be done ideally