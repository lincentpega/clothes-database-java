package com.yumyaskova.clothesstoredatabaseapp.controller;

import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import com.yumyaskova.clothesstoredatabaseapp.exception.GoodNotFoundException;
import com.yumyaskova.clothesstoredatabaseapp.model.Good;
import com.yumyaskova.clothesstoredatabaseapp.service.GoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodController {

    private final GoodService goodService;

    public GoodController(GoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Good>> getAll() {
        List<Good> goods = goodService.findAll();
        return new ResponseEntity<>(goods, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Good>> getAllGoodsBySize(@RequestParam(name = "size") SizeEnum size) {
        List<Good> goods = goodService.findAllBySize(size);
        if (goods.size() == 0) {
            throw new GoodNotFoundException("Not found goods with size " + size.getValue());
        }
        return new ResponseEntity<>(goods, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Good> getGoodById(@PathVariable long id) {
        Good good = goodService.findById(id).orElseThrow(() -> new GoodNotFoundException(id));
        return new ResponseEntity<>(good, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Good> createGood(@RequestBody Good good) {
        Good newGood = goodService.save(good);
        return new ResponseEntity<>(newGood, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Good> updateGood(@RequestBody Good good, @PathVariable long id) {
        Good updatedGood = goodService.update(good, id);
        return new ResponseEntity<>(updatedGood, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteGood(@PathVariable long id) {
        goodService.deleteById(id);
    }
}
