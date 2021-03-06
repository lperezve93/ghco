package com.lperezve.ghco.controller;

import com.lperezve.ghco.dto.NewTradesResponseDTO;
import com.lperezve.ghco.exception.GHCODataException;
import com.lperezve.ghco.service.GHCOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.lperezve.ghco.constant.Constants.BBGCODE;
import static com.lperezve.ghco.constant.Constants.PORTFOLIO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/ghco")
public class GHCOController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCOController.class);

    @Autowired
    private GHCOService ghcoService;

    @GetMapping(path = "/cash-position/bbgcode", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Double>> getCashPositionByBBGCode() throws GHCODataException {
        LOGGER.info(" +++ Get Cash Position By BBGCode +++");
        return new ResponseEntity<>(ghcoService.getCashPosition(BBGCODE), HttpStatus.OK);
    }

    @GetMapping(path = "/cash-position/portfolio", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Double>> getCashPositionByPortfolio() throws GHCODataException {
        LOGGER.info(" +++ Get Cash Position By Portfolio +++");
        return new ResponseEntity<>(ghcoService.getCashPosition(PORTFOLIO), HttpStatus.OK);
    }

    @PostMapping(path = "/new-trades")
    public ResponseEntity<NewTradesResponseDTO> addNewTrades(@RequestParam("file") MultipartFile file) throws IOException {

        LOGGER.info(" +++ Add new trades by csv file +++");
        if (file == null) {
            throw new RuntimeException("You must select a file for uploading");
        }
        NewTradesResponseDTO response = ghcoService.processNewTrades(file);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
