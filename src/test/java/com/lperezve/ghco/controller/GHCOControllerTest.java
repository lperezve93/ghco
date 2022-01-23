package com.lperezve.ghco.controller;

import com.lperezve.ghco.exception.GHCODataException;
import com.lperezve.ghco.service.GHCOService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@ExtendWith(MockitoExtension.class)
public class GHCOControllerTest {

    @InjectMocks
    private GHCOController ghcoController;

    @Mock
    private GHCOService ghcoService;

    /**
     * Method to test: getCashPositionByBBGCode
     * What is the Scenario: Retrieve a list of bbgCode and its cash position for each of them
     * What is the Result: Returns the list of bbgCodes and cash positions
     */
    @Test
    public void ggetCashPositionByBBGCode_successServiceCall_validResponse() throws GHCODataException {
        Map<String, Double> bbgCodes = createBBGCodeResponse();
        Mockito.when(ghcoService.getCashPosition(ArgumentMatchers.any())).thenReturn(bbgCodes);

        ResponseEntity<Map<String, Double>> responseEntity = ghcoController.getCashPositionByBBGCode();
        Map<String, Double> response = responseEntity.getBody();

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(response, notNullValue());
        assertThat(response.size(), is(3));

        assertThat(response.get("AAA"), is(50D));
        assertThat(response.get("BBB"), is(100D));
        assertThat(response.get("CCC"), is(-25D));

    }

    /**
     * Method to test: getCashPositionByBBGCode
     * What is the Scenario: Retrieve a list of bbgCode and its cash position for each of them
     * What is the Result: Returns the list of bbgCodes and cash positions
     */
    @Test
    public void ggetCashPositionByPortfolio_successServiceCall_validResponse() throws GHCODataException {
        Map<String, Double> portfolios = createPortfolioResponse();
        Mockito.when(ghcoService.getCashPosition(ArgumentMatchers.any())).thenReturn(portfolios);

        ResponseEntity<Map<String, Double>> responseEntity = ghcoController.getCashPositionByPortfolio();
        Map<String, Double> response = responseEntity.getBody();

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(response, notNullValue());
        assertThat(response.size(), is(3));

        assertThat(response.get("portfolio1"), is(10D));
        assertThat(response.get("portfolio2"), is(-20D));
        assertThat(response.get("portfolio3"), is(30D));

    }

    private Map<String, Double> createBBGCodeResponse() {
        Map<String, Double> bbgCodes = new HashMap<>();
        bbgCodes.put("AAA", 50D);
        bbgCodes.put("BBB", 100D);
        bbgCodes.put("CCC", -25D);
        return bbgCodes;
    }

    private Map<String, Double> createPortfolioResponse() {
        Map<String, Double> bbgCodes = new HashMap<>();
        bbgCodes.put("portfolio1", 10D);
        bbgCodes.put("portfolio2", -20D);
        bbgCodes.put("portfolio3", 30D);
        return bbgCodes;
    }
}
