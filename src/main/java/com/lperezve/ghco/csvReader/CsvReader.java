package com.lperezve.ghco.csvReader;

import com.lperezve.ghco.constant.Constants;
import com.lperezve.ghco.model.Trade;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Component
public class CsvReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReader.class);

    public static String getFilepath(String filename) throws URISyntaxException {
        LOGGER.info("+++ Retrieving path for the file {} +++", filename);

        URL url = CsvReader.class.getClassLoader().getResource(filename); // Name changed from res to url
        File file = Paths.get(Objects.requireNonNull(url).toURI()).toFile();
        return file.getAbsolutePath();
    }

    public static List<Trade> csvReader(String filepath) throws FileNotFoundException {
        LOGGER.info("+++ Retrieving and mapping data from {} +++", filepath); // Log changed

        return new CsvToBeanBuilder(new FileReader(filepath))
                .withSeparator(Constants.COLON_CHAR)
                .withType(Trade.class)
                .build()
                .parse();
    }

    public static List<Trade> csvReader(MultipartFile file) throws IOException {
        LOGGER.info("+++ Retrieving and mapping data from {} +++", file.getName()); // Log changed

        InputStreamReader streamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);

        return new CsvToBeanBuilder(streamReader)
                .withSeparator(Constants.COLON_CHAR)
                .withType(Trade.class)
                .build()
                .parse();
    }
}
