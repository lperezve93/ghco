package com.lperezve.ghco.repository;

import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.service.GHCOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GHCORepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCORepository.class);

    private static List<Trade> csvTrades = new ArrayList<>();

    public List<Trade> saveData(List<Trade> trades) {
        LOGGER.info("+++ Repository - saving data +++");
        csvTrades = trades;
        return csvTrades;
    }
}
