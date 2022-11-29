package com.yumyaskova.clothesstoredatabaseapp.repository;

import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import com.yumyaskova.clothesstoredatabaseapp.model.Good;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GoodRepository {
    public List<Good> findAll() {
        throw new UnsupportedOperationException();
    }

    public List<Good> findAllBySize(SizeEnum size) {
        throw new UnsupportedOperationException();
    }

    public Good save(Good good) {
        throw new UnsupportedOperationException();
    }

    public Optional<Good> findById(long id) {
        throw new UnsupportedOperationException();
    }

    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }
}