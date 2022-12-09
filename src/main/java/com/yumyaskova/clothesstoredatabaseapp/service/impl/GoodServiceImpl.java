package com.yumyaskova.clothesstoredatabaseapp.service.impl;

import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import com.yumyaskova.clothesstoredatabaseapp.model.Good;
import com.yumyaskova.clothesstoredatabaseapp.repository.GoodRepository;
import com.yumyaskova.clothesstoredatabaseapp.service.GoodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodServiceImpl implements GoodService {

    GoodRepository repository;

    public GoodServiceImpl(GoodRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Good> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Good> findAllBySize(SizeEnum size) {
        return repository.findAllBySize(size);
    }

    @Override
    public Optional<Good> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Good save(Good good) {
        return repository.save(good);
    }

    @Override
    public Good update(Good newGood, long id) {
        return repository.findById(id)
                .map(good -> {
                    good.setId(newGood.getId());
                    good.setTitle(newGood.getTitle());
                    good.setSize(newGood.getSize());
                    good.setPrice(newGood.getPrice());
                    good.setAmount(newGood.getAmount());
                    good.setSupplyRequired(newGood.isSupplyRequired());
                    return repository.save(good);
                }).orElseGet(() -> {
                    newGood.setId(id);
                    return repository.save(newGood);
                });
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
