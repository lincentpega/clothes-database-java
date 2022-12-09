package com.yumyaskova.clothesstoredatabaseapp.config;

import com.yumyaskova.clothesstoredatabaseapp.db.HashService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(HashService hashService) {
        return args -> {
            hashService.rehashDB();
            log.info("DB initialized");
        };
    }
}
