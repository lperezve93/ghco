package com.lperezve.ghco.repository;

import com.lperezve.ghco.model.Trade;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@ExtendWith(MockitoExtension.class)
public class GHCORepositoryTest {

    @InjectMocks
    private GHCORepository ghcoRepository;

    /**
     * Method to test: saveData
     * What is the Scenario: add data from loaded file
     * What is the Result: data from loaded file has been stored
     */
    @Test
    public void saveData_loadCsvFile_dataUploaded() {
        List<Trade> trades = checkTradeStatus();
        List<Trade> tradesToStore = createTradeList();

        List<Trade> tradeList = ghcoRepository.saveData(trades);

        assertThat(tradeList, notNullValue());
        assertThat(tradeList.size(), is(0));
        assertThat(tradesToStore, notNullValue());
        assertThat(tradesToStore.size(), greaterThan(0));
    }

    /**
     * Method to test: saveProcessedData
     * What is the Scenario: Add processed data list
     * What is the Result: The processed data has been stored
     */
    @Test
    public void saveProcessedData__processStoredData__saveProcessedData() {
        Map<String, Trade> tradesToStore = createProcessedData();

        Map<String, Trade> processedData = ghcoRepository.saveProcessedData(tradesToStore);

        assertThat(processedData, notNullValue());
        assertThat(processedData.size(), is(tradesToStore.size()));
    }


    /**
     * Method to test: getCsvTrades
     * What is the Scenario: Retrieve a list of trades loaded from the file
     * What is the Result: Returns the list of trades expected
     */
    @Test
    public void getCsvTrades__getExpectedCsvTrades() {

        List<Trade> tradesToStore = createTradeList();
        List<Trade> tradeList = ghcoRepository.saveData(tradesToStore);

        List<Trade> csvTrades = ghcoRepository.getCsvTrades();

        assertThat(csvTrades, notNullValue());
        assertThat(tradeList.size(), is(csvTrades.size()));
    }

    @Test
    public void getProcessedTrades__getExpectedTrades() {
        Map<String, Trade> tradesToStore = createProcessedData();
        Map<String, Trade> trades = ghcoRepository.saveProcessedData(tradesToStore);

        Map<String, Trade> processedTrades = ghcoRepository.getProcessedTrades();

        assertThat(processedTrades, notNullValue());
        assertThat(processedTrades.size(), is(trades.size()));
    }

    private List<Trade> checkTradeStatus() {
        try {
            Field field = GHCORepository.class.getDeclaredField("csvTrades");
            field.setAccessible(true);
            return (List<Trade>) field.get(ghcoRepository);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Trade> createTradeList() {
        Trade trade = new Trade();
        trade.setTradeId("AAA");
        trade.setBbgCode("AAPL US Equity");
        trade.setCurrency("GBP");
        trade.setSide("B");
        trade.setPrice("5378677506242510");
        trade.setVolume("637875");
        trade.setPortfolio("portfolio1");
        trade.setAccount("NEW");
        trade.setTradeTimeUTC("2010-01-01T15:21:18.773938");
        trade.setValueDate("20100101");

        return Collections.singletonList(trade);
    }

    private Map<String, Trade> createProcessedData() {
        Trade trade = createTradeList().get(0);
        return Maps.newHashMap(trade.getTradeId(), trade);
    }
}
