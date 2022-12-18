package com.yumyaskova.clothesstoredatabaseapp.model;

import com.yumyaskova.clothesstoredatabaseapp.enumeration.SizeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Good {
    long id;
    String title;
    SizeEnum size;
    int amount;
    double price;
    boolean isSupplyRequired;
    boolean isDeleted;

    public Good() {
    }

    public Good(long id, String title, SizeEnum size, int amount, double price, boolean isSupplyRequired, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.amount = amount;
        this.price = price;
        this.isSupplyRequired = isSupplyRequired;
        this.isDeleted = isDeleted;
    }

    public static Good of(String @NotNull [] stringGood) {
        Good good = new Good();
        if (stringGood.length != 7) {
            throw new IllegalArgumentException();
        }
        good.id = stringGood[0].equals("") ? -1 : Integer.parseInt(stringGood[0]);
        good.title = stringGood[1].equals("") ? null : stringGood[1];
        good.size = stringGood[2].equals("") ? null : SizeEnum.valueOf(stringGood[2]);
        good.amount = stringGood[3].equals("") ? -1 : Integer.parseInt(stringGood[3]);
        good.price = stringGood[4].equals("") ? -1 : Double.parseDouble(stringGood[4]);
        good.isSupplyRequired = stringGood[5].equals("1");
        good.isDeleted = stringGood[6].equals("1");

        return good;
    }

    public String toCSVString() {
        return id + ","
                + title + ","
                + size + ","
                + amount + ","
                + price + ","
                + (isSupplyRequired ? 1 : 0) + ","
                + (isDeleted ? 1 : 0);
    }
}
