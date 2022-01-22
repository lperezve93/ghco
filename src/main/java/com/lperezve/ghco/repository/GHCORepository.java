package com.lperezve.ghco.repository;

import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.service.GHCOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GHCORepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCORepository.class);

    // Original trades from csv
    private static List<Trade> csvTrades = new ArrayList<>();

    // Proccessed trades
    private static Map<String, Trade> processedTrades = new HashMap<>();

    public List<Trade> saveData(List<Trade> data) {
        LOGGER.info("+++ Repository - saving data +++");
        csvTrades = data;
        return csvTrades;
    }

    public Map<String, Trade> saveProcessedData(Map<String, Trade> processedData) {
        LOGGER.info("+++ Repository - saving processed data +++");
        processedTrades = processedData;
        return processedTrades;
    }

    public List<Trade> getCsvTrades() {
        return csvTrades;
    }

    public Map<String, Trade> getProcessedTrades(){
        return processedTrades;
    }
 }
