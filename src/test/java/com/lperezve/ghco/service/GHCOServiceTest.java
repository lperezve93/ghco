package com.lperezve.ghco.service;

import com.lperezve.ghco.dto.GlobalResponseDTO;
import com.lperezve.ghco.exception.GHCODataException;
import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.repository.GHCORepository;
import com.lperezve.ghco.util.GHCOUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.lperezve.ghco.constant.Constants.BBGCODE;
import static com.lperezve.ghco.constant.Constants.PORTFOLIO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GHCOServiceTest {

    @InjectMocks
    private GHCOService ghcoService;

    @Mock
    private GHCORepository ghcoRepository;

    @Mock
    private GHCOUtil ghcoUtil;

    /**
     * Method to test: saveData
     * What is the Scenario: Successful data saved
     * What is the Result: Returns a GlobalResponseDTO with expected description
     */
    @Test
    public void saveData_validGlobalResponseDto() throws FileNotFoundException, URISyntaxException {
        Mockito.doNothing().when(ghcoUtil).processTrades();

        GlobalResponseDTO globalResponseDTO = ghcoService.saveData();

        assertThat(globalResponseDTO, notNullValue());
        assertThat(globalResponseDTO.getDescription(), is("SUCCESS: data loaded"));

    }

    /**
     * Method to test: getCashPosition by BBGCode
     * What is the Scenario: retrieve cash position by bbgcode after processed trades
     * What is the Result: throw GHCODataException due to no processed data
     */
    @Test
    public void getCashPosition_bbgCode_throwsException() {
        Map<String, Trade> trades = new HashMap<>();
        Mockito.when(ghcoRepository.getProcessedTrades()).thenReturn(trades);

        assertThrows(GHCODataException.class, () -> ghcoService.getCashPosition(BBGCODE));
    }

    /**
     * Method to test: getCashPosition by BBGCode
     * What is the Scenario: retrieve cash position by bbgcode after processed trades
     * What is the Result: retrieve list of bbgCode and its cash positions
     */
    @Test
    public void getCashPosition_bbgCode_returnValidResponse() throws GHCODataException {

        Map<String, Trade> tradesFromRepository = createCashPositionForBBGCode();
        Mockito.when(ghcoRepository.getProcessedTrades()).thenReturn(tradesFromRepository);

        Map<String, Double> cashPosition = ghcoService.getCashPosition(BBGCODE);

        assertThat(cashPosition, notNullValue());
        assertThat(cashPosition.size(), is(2));
        assertThat(cashPosition.get("AAPL US Equity"), notNullValue());
        assertThat(cashPosition.get("V LN Equity"), notNullValue());
    }

    /**
     * Method to test: getCashPosition by Portfolio
     * What is the Scenario: retrieve cash position by portfolio after processed trades
     * What is the Result: retrieve list of portfolio and its cash positions
     */
    @Test
    public void getCashPosition_portfolio_returnValidResponse() throws GHCODataException {

        Map<String, Trade> tradesFromRepository = createCashPositionForBBGCode();
        Mockito.when(ghcoRepository.getProcessedTrades()).thenReturn(tradesFromRepository);

        Map<String, Double> cashPosition = ghcoService.getCashPosition(PORTFOLIO);

        assertThat(cashPosition, notNullValue());
        assertThat(cashPosition.size(), is(2));
        assertThat(cashPosition.get("portfolio1"), notNullValue());
        assertThat(cashPosition.get("portfolio3"), notNullValue());
    }

    private Map<String, Trade> createCashPositionForBBGCode() {
        Map<String, Trade> trades = new HashMap<>();
        Trade trade1 = new Trade();
        trade1.setTradeId("AAA1");
        trade1.setBbgCode("AAPL US Equity");
        trade1.setCurrency("GBP");
        trade1.setSide("B");
        trade1.setPrice("5378677506242510");
        trade1.setVolume("637875");
        trade1.setPortfolio("portfolio1");
        trade1.setAccount("NEW");
        trade1.setTradeTimeUTC("2010-01-01T15:21:18.773938");
        trade1.setValueDate("20100101");

        Trade trade2 = new Trade();
        trade2.setTradeId("AAA2");
        trade2.setBbgCode("AAPL US Equity");
        trade2.setCurrency("GBP");
        trade2.setSide("S");
        trade2.setPrice("2078609345242510");
        trade2.setVolume("637075");
        trade2.setPortfolio("portfolio3");
        trade2.setAccount("NEW");
        trade2.setTradeTimeUTC("2010-01-01T15:21:18.773938");
        trade2.setValueDate("20102001");

        Trade trade3 = new Trade();
        trade3.setTradeId("AAA2");
        trade3.setBbgCode("V LN Equity");
        trade3.setCurrency("GBP");
        trade3.setSide("B");
        trade3.setPrice("609345242510");
        trade3.setVolume("7075");
        trade3.setPortfolio("portfolio3");
        trade3.setAccount("NEW");
        trade3.setTradeTimeUTC("2010-01-10T15:21:18.773938");
        trade3.setValueDate("22001");

        trades.put(trade1.getTradeId(), trade1);
        trades.put(trade2.getTradeId(), trade2);
        trades.put(trade3.getTradeId(), trade3);

        return trades;
    }
}
