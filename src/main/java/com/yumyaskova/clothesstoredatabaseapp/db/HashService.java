package com.yumyaskova.clothesstoredatabaseapp.db;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
public class HashService {
    @Value("${db.uri}")
    private String dbURI;

    private final HashMap<Long, Integer> idRowHashMap = new HashMap<>();
    private final HashMap<Long, SizeEnum> idSizeHashMap = new HashMap<>();

    public void rehashDB() {
        idRowHashMap.clear();
        idSizeHashMap.clear();
        fillIdRowHashMap();
        fillIdSizeHashMap();
    }

    private void fillIdRowHashMap() {
        try (FileReader fileReader = new FileReader(dbURI)) {
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> goods = csvReader.readAll();

            for (int i = 0; i < goods.size(); i++) {
                String goodId = goods.get(i)[0];
                idRowHashMap.put(goodId.equals("") ? null : Long.parseLong(goodId), i);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillIdSizeHashMap() {
        try (FileReader fileReader = new FileReader(dbURI)) {
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> goods = csvReader.readAll();

            for (String[] good : goods) {
                String goodId = good[0];
                String goodSize = good[2];
                idSizeHashMap.put(
                        goodId.equals("") ? null : Long.parseLong(goodId),
                        goodSize.equals("") ? null : SizeEnum.valueOf(goodSize));
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Integer> getRowById(Long id) {
        return Optional.ofNullable(idRowHashMap.get(id));
    }

    public List<Long> getIdsBySize(SizeEnum size) {
        ArrayList<Long> rowsList = new ArrayList<>();
        return idSizeHashMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(size))
                    .map(Map.Entry::getKey).toList();
    }
}
