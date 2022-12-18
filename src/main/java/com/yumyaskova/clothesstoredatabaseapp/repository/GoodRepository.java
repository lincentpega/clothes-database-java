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
import java.io.RandomAccessFile;
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
            return csvReader.readAll().stream().map(Good::of).filter(good -> !good.isDeleted()).toList();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Good> findById(long id) {
        Optional<Integer> row = hashService.getRowById(id);
        if (row.isPresent()) {
            try (FileReader fileReader = new FileReader(dbURI)) {
                CSVReader csvReader = new CSVReader(fileReader);
                csvReader.skip(row.get() - 1);
                Good good = Good.of(csvReader.readNext());
                if (!good.isDeleted()) {
                    return Optional.of(good);
                } else {
                    return Optional.empty();
                }
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
                Good good = Good.of(csvReader.readNext());
                if (!good.isDeleted()) {
                    goods.add(good);
                }
                previousRow = row;
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return goods;
    }

    public Good save(Good good) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(dbURI, "rw")) {
            if (hashService.isGoodAlreadyExists(good.getId())) {
                update(good);
            } else {
                if (good.getId() > hashService.getLastId() + 1) {
                    good.setId(hashService.getLastId() + 1);
                }
                randomAccessFile.seek(hashService.getLastByteForInserting());
                randomAccessFile.readLine();
                randomAccessFile.writeBytes(good.toCSVString() + "\n");

                hashService.refreshMapsOnInsert(good.getId(), good.toCSVString().getBytes().length, good.getSize());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return findById(good.getId()).orElseThrow(RuntimeException::new);
    }

    private void update(Good good) {
        int previousPassengerBytes = good.getId() == 1 ? 0 : hashService.getBytesById(good.getId() - 1);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(dbURI, "rw")) {

            randomAccessFile.seek(previousPassengerBytes);
            randomAccessFile.readLine();

            int targetBytes = hashService.getBytesById(good.getId()) - previousPassengerBytes;
            int currentBytes = good.toCSVString().getBytes().length;

            if (targetBytes == currentBytes) {
                randomAccessFile.writeBytes(good.toCSVString() + "\n");
            }

            hashService.fillIdBytesHashmap();
            hashService.setSizeById(good.getId(), good.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) { // works only if first id is 1
        int passengerBytes = hashService.getBytesById(id);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(dbURI, "rw")) {
            randomAccessFile.seek(passengerBytes - 2);
            randomAccessFile.writeBytes("1");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}