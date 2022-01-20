package com.lperezve.ghco.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Trade {

    @CsvBindByName(column = "TradeID", required = true)
    private String tradeId;

    @CsvBindByName(column = "BBGCode", required = true)
    private String bbgCode;

    @CsvBindByName(column = "Currency", required = true)
    private String currency;

    @CsvBindByName(column = "Side", required = true)
    private String side;

    @CsvBindByName(column = "Price", required = true)
    private String price;

    @CsvBindByName(column = "Volume", required = true)
    private String volume;

    @CsvBindByName(column = "Portfolio", required = true)
    private String portfolio;

    @CsvBindByName(column = "Action", required = true)
    private String action;

    @CsvBindByName(column = "Account", required = true)
    private String account;

    @CsvBindByName(column = "Strategy", required = true)
    private String strategy;

    @CsvBindByName(column = "User", required = true)
    private String user;

    @CsvBindByName(column = "TradeTimeUTC", required = true)
    private String tradeTimeUTC;

    @CsvBindByName(column = "ValueDate", required = true)
    private String valueDate;
}
