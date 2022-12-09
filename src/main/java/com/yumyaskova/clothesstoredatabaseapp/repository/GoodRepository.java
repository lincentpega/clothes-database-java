package com.yumyaskova.clothesstoredatabaseapp.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.yumyaskova.clothesstoredatabaseapp.db.HashService;
import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import com.yumyaskova.clothesstoredatabaseapp.model.Good;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GoodRepository {
    @Value("${db.uri}")
    private String dbURI;

    HashService hashService;

    public GoodRepository(HashService hashService) {
        this.hashService = hashService;
    }

    public List<Good> findAll() {
        try (FileReader fileReader = new FileReader(dbURI)) {
            CSVReader csvReader = new CSVReader(fileReader);
            return csvReader.readAll().stream().map(Good::of).toList();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Good> findById(long id) {
        Optional<Integer> row = hashService.getRowById(id);
        if (row.isPresent()) {
            try (FileReader fileReader = new FileReader(dbURI)) {
                CSVReader csvReader = new CSVReader(fileReader);
                csvReader.skip(row.get());
                return Optional.of(Good.of(csvReader.readNext()));
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        } else return Optional.empty();
    }

    public List<Good> findAllBySize(SizeEnum size) {
        List<Good> goods = new ArrayList<>();
        List<Long> ids = hashService.getIdsBySize(size);

        List<Integer> rows = ids.stream().map(id -> {
            try {
                return hashService.getRowById(id).orElseThrow(IOException::new);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        try (FileReader fileReader = new FileReader(dbURI)) {
            CSVReader csvReader = new CSVReader(fileReader);
            int previousRow = 0;
            for (Integer row : rows) {
                csvReader.skip(row - previousRow - 1);
                goods.add(Good.of(csvReader.readNext()));
                previousRow = row;
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return goods;
    }

    public Good save(Good good) {
        throw new UnsupportedOperationException();
    }

    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }
}