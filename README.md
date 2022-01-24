# Trades Assessment using Spring Boot 2.6 and Java 8

## Prerequisites 👀

- Download JDK 8 https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
- Configure JDK https://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/index.html
- Download Maven https://maven.apache.org/download.cgi
- Configure Maven https://maven.apache.org/install.html

## How to run the app ▶️

1. Clone `ghco` repository which contains the project.
2. Once you have configured the JDK and Maven, run the following commands to:
    1. `mvn install` Downloads all the dependencies found in pom.xml, compiles the project and runs the test.
    2. `mvn install -DskipTests` Same as previous command without running tests
    3. `mvn test` Runs only the tests
    4. `mvn spring-boot:start` Run Spring Boot app
    5. `mvn spring-boot:stop` Stop Spring Boot app

## How to test the app 🤓

### From Shell

1. `curl http://localhost:8080/ghco/cash-position/bbgcode` - Get cash position by BBGCode.

```
{
   "QQQ US ETF":5.530347002821385E12,
   "VOO US ETF":6.875923069031262E12,
   "BC94 JPY Equity":8.17293808848315E12,
   "MSFT US Equity":7.153986291016319E12,
   "AAPL US Equity":6.669684285126732E12,
   "GOOG US Equity":5.477061685272428E12,
   "V LN Equity":3.581658661321043E12,
   "VOD LN Equity":6.518501851131637E12,
   "BRK.A US Equity":7.612554234645645E12
}
```

2. `curl http://localhost:8080/ghco/cash-position/portfolio` - Get cash position by Portfolio.
```
{
   "portfolio3":9.570673431783783E12,
   "portfolio2":9.555969209407086E12,
   "portfolio1":9.678565529598354E12,
   "portfolio6":9.741119572019805E12,
   "portfolio5":9.402893351401137E12,
   "portfolio4":9.643434074639428E12
}
```
3. `curl curl -v -F key1=value1 -F file=@filepath http://localhost:8080/ghco/new-trades` - Upload a csv file to process new trades.

Example: `curl curl -v -F key1=value1 -F file=@/Users/lorenapv/Documents/Projects/ghco/src/main/resources/trades3.csv http://localhost:8080/ghco/new-trades`
```
{
   "tradesProcessed":6,
   "addedTrades":1,
   "updatedTrades":3
}
```

### From Postman

There is a Postman Collection (_GHCO.postman_collection.json_) on resources folder which contains the existing endpoints and examples of calls  for the different scenarios

Download Postman: https://www.postman.com/downloads/

## Assumptions 👍
1. When application runs the csv file is processed automatically.
2. To get cash positions an endpoint call is needed in order to retrieve the data.
3. Repository folder acts as a "database" where the data is stored when the app is running.

## Improvements 🔨
1. Add more test cases to cover all functionalities.
2. Add validations when processing csv file
