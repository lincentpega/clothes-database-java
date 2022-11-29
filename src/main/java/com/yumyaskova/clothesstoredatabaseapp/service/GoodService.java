package com.yumyaskova.clothesstoredatabaseapp.service;

import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import com.yumyaskova.clothesstoredatabaseapp.model.Good;

import java.util.List;
import java.util.Optional;

public interface GoodService {
    List<Good> findAll();

    List<Good> findAllBySize(SizeEnum size);

    Optional<Good> findById(long id);

    Good save(Good good);

    Good update(Good newGood, long id);

    void deleteById(long id);
}
