package com.lperezve.ghco.service;

import com.lperezve.ghco.constant.Messages;
import com.lperezve.ghco.csvReader.CsvReader;
import com.lperezve.ghco.dto.GlobalResponseDTO;
import com.lperezve.ghco.model.Trade;
import com.lperezve.ghco.repository.GHCORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import static com.lperezve.ghco.constant.Constants.FILENAME_CSV;

@Service
public class GHCOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GHCOService.class);

    @Autowired
    GHCORepository ghcoRepository;

    @EventListener(ApplicationReadyEvent.class)
    public GlobalResponseDTO saveData() throws URISyntaxException, FileNotFoundException {
        List<Trade> trades = CsvReader.csvReader(CsvReader.getFilepath(FILENAME_CSV));
        LOGGER.info("+++ Number of rows retrieved from munro file {} +++ ", trades.size());
        ghcoRepository.saveData(trades);
        return GlobalResponseDTO.builder().description(Messages.HTTP_200_DATA_LOADED).build();
    }



}
