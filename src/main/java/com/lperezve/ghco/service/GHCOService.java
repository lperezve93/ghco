package com.lperezve.ghco.service;

import com.lperezve.ghco.constant.Messages;
import com.lperezve.ghco.csvReader.CsvReader;
import com.lperezve.ghco.dto.GlobalResponseDTO;
import com.lperezve.ghco.exception.GHCODataException;
import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.repository.GHCORepository;
import com.lperezve.ghco.util.GHCOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lperezve.ghco.constant.Constants.BBGCODE;
import static com.lperezve.ghco.constant.Constants.BUY;
import static com.lperezve.ghco.constant.Constants.FILENAME_CSV;
import static com.lperezve.ghco.constant.Constants.PORTFOLIO;
import static com.lperezve.ghco.constant.Constants.SELL;

@Service
public class GHCOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCOService.class);

    @Autowired
    GHCORepository ghcoRepository;

    @Autowired
    GHCOUtil ghcoUtil;

    @EventListener(ApplicationReadyEvent.class)
    public GlobalResponseDTO saveData() throws URISyntaxException, FileNotFoundException {
        List<Trade> trades = CsvReader.csvReader(CsvReader.getFilepath(FILENAME_CSV));
        LOGGER.info("+++ Number of rows retrieved from munro file {} +++ ", trades.size());
        ghcoRepository.saveData(trades);
        ghcoUtil.processTrades();
        return GlobalResponseDTO.builder().description(Messages.HTTP_200_DATA_LOADED).build();
    }

    public Map<String, Double> getCashPosition(String type) throws GHCODataException {
        if (ghcoRepository.getProcessedTrades().size() == 0)
            throw new GHCODataException();

        List<Trade> trades = new ArrayList<>(ghcoRepository.getProcessedTrades().values());
        List<String> types = new ArrayList<>();
        if (type.equals(PORTFOLIO)) {
            types = trades.stream().map(Trade::getPortfolio).distinct().collect(Collectors.toList());
        } else if (type.equals(BBGCODE)) {
            types = trades.stream().map(Trade::getBbgCode).distinct().collect(Collectors.toList());
        }
        Map<String, Double> cashPositions = new HashMap<>();
        for (String eachType : types) {
            List<Trade> typeTrades;
            if (type.equals(PORTFOLIO)) {
                typeTrades = trades.stream().filter(t -> t.getPortfolio().equals(eachType)).collect(Collectors.toList());
            } else {
                typeTrades = trades.stream().filter(t -> t.getBbgCode().equals(eachType)).collect(Collectors.toList());
            }
            double sell = 0D, buy = 0D;
            for (Trade t : typeTrades) {
                if (t.getSide().equals(BUY)) {
                    buy += Double.parseDouble(t.getVolume()) * Double.parseDouble(t.getPrice());
                } else if (t.getSide().equals(SELL)) {
                    sell += Double.parseDouble(t.getVolume()) * Double.parseDouble(t.getPrice());
                } else {
                    LOGGER.info("Transaction {} does not have Side - not applied", t.getTradeId());
                }
            }
            double cashPosition = sell + buy;
            cashPositions.put(eachType, cashPosition);
        }
        LOGGER.info("cashPositionBBGCode: " + cashPositions);
        return cashPositions;
    }
}
