package com.lperezve.ghco.util;

import com.lperezve.ghco.dto.NewTradesResponseDTO;
import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.repository.GHCORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GHCOUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCOUtil.class);

    @Autowired
    GHCORepository ghcoRepository;

    public void processTrades() {
        List<Trade> trades = ghcoRepository.getCsvTrades();
        if (trades.size() > 0) {
            Map<String, Trade> newTrades = processAmendAndCancelTrades(trades);
            ghcoRepository.saveProcessedData(newTrades);
        }
    }

    public NewTradesResponseDTO processNewTrades(List<Trade> trades) {
        LOGGER.info("+++ Process New Trades - trades to proceed: " +  trades.size() + " +++");
        Map<String, Trade> newTrades = processAmendAndCancelTrades(trades);
        Map<String, Trade> processedTrades = ghcoRepository.getProcessedTrades();
        Integer tradesToAdd = 0, tradesToUpdate = 0;
        for (Trade at : newTrades.values()) {
            if (processedTrades.get(at.getTradeId()) != null) {
                processedTrades.replace(at.getTradeId(), at);
                tradesToUpdate++;
            }
            else {
                processedTrades.put(at.getTradeId(), at);
                tradesToAdd++;
            }
        }
        ghcoRepository.saveProcessedData(processedTrades);
        return NewTradesResponseDTO.from(trades.size(), tradesToAdd, tradesToUpdate);
    }

    private Map<String, Trade> processAmendAndCancelTrades(List<Trade> trades) {
        // Cancel trades
        Map<String, Trade> canceledTrades = trades.stream()
                .filter(t -> t.getAction().equals("CANCEL"))
                .collect(Collectors.toMap(Trade::getTradeId, trade -> trade));
        // Amend trades
        Map<String, Trade> amendedTrades = trades.stream()
                .filter(t -> t.getAction().equals("AMEND"))
                .collect(Collectors.toMap(Trade::getTradeId, trade -> trade));

        // Remove amended trades due cancel
        for (Trade ct : canceledTrades.values()) {
            if (amendedTrades.get(ct.getTradeId()) != null)
                amendedTrades.remove(ct.getTradeId());
        }

        // Merge cancel and amend action trades
        Map<String, Trade> actionTrades = Stream.concat(
                        canceledTrades.entrySet().stream(), amendedTrades.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1));

        // Get trades with new as action - When more than 1 trade, get recent trade
        Map<String, Trade> newTrades = trades.stream()
                .filter(t -> t.getAction().equals("NEW"))
                .collect(Collectors.toMap(Trade::getTradeId, Function.identity(), BinaryOperator.maxBy(Comparator.comparing(Trade::getValueDate))));

        // Replace new trades by actions - amend and cancel
        for (Trade at : actionTrades.values()) {
            if (newTrades.get(at.getTradeId()) != null)
                newTrades.replace(at.getTradeId(), at);
        }

        return newTrades;
    }
}
