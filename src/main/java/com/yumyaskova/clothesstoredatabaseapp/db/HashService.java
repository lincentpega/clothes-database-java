package com.yumyaskova.clothesstoredatabaseapp.db;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

@Log4j2
@Service
public class HashService {
    @Value("${db.uri}")
    private String dbURI;

    private final HashMap<Long, Integer> idRowHashMap = new HashMap<>();
    private final HashMap<Long, SizeEnum> idSizeHashMap = new HashMap<>();
    private final HashMap<Long, Integer> idBytesHashMap = new HashMap<>();


    public void refreshDB() {
        idRowHashMap.clear();
        idSizeHashMap.clear();
        idBytesHashMap.clear();
        fillIdRowHashMap();
        fillIdSizeHashMap();
        fillIdBytesHashmap();
    }


    /**
     * Function fills HashMap with corresponding to goods ids (keys) values, representing row (values) value in db (starts from 1)
     */
    private void fillIdRowHashMap() {
        try (FileReader fileReader = new FileReader(dbURI)) {
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> goods = csvReader.readAll();

            for (int i = 0; i < goods.size(); i++) {
                String goodId = goods.get(i)[0];
                idRowHashMap.put(goodId.equals("") ? null : Long.parseLong(goodId), i + 1);
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

    public void fillIdBytesHashmap() {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(dbURI, "rw")) {

            int currentBytes = 0;

            for (Long key : idRowHashMap.keySet()) {
                String line = randomAccessFile.readLine();
                currentBytes = currentBytes + line.getBytes().length + 1;

                idBytesHashMap.put(key, currentBytes);

            }
        } catch (IOException e) {
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

    public int getBytesById(Long id) {
        return idBytesHashMap.get(id);
    }

    public int getLastByteForInserting() {
        return idBytesHashMap.get((long) idBytesHashMap.size());
    }

    public boolean isGoodAlreadyExists(long id) {
        return idRowHashMap.containsKey(id);
    }

    public void refreshMapsOnInsert(long id, int bytes, SizeEnum size) {
        idRowHashMap.put(id, idRowHashMap.get((long) idRowHashMap.size()) + 1);
        idBytesHashMap.put(id, bytes + idBytesHashMap.get((long) idBytesHashMap.size()));
        idSizeHashMap.put(id, size);
    }

    public long getLastId() {
        return idBytesHashMap.size();
    }

    public void setSizeById(long id, SizeEnum size){
        idSizeHashMap.put(id, size);
    }
}
