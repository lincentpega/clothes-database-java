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
    String name;
    SizeEnum size;
    int amount;
    double price;
    boolean isSupplyRequired;

    public Good() {
    }

    public static Good of(String @NotNull [] stringGood) {
        Good good = new Good();
        if (stringGood.length != 6) {
            throw new IllegalArgumentException();
        }
        good.id = stringGood[0].equals("") ? -1 : Integer.parseInt(stringGood[0]);
        good.name = stringGood[1].equals("") ? null : stringGood[1];
        good.size = stringGood[2].equals("") ? null : SizeEnum.valueOf(stringGood[2]);
        good.amount = stringGood[3].equals("") ? -1 : Integer.parseInt(stringGood[3]);
        good.price = stringGood[4].equals("") ? -1 : Double.parseDouble(stringGood[3]);
        good.isSupplyRequired = stringGood[5].equals("1");

        return good;
    }

    @Deprecated
    public String[] toStringArray() {
        return new String[]{
                String.valueOf(id),
                name,
                size.toString(),
                String.valueOf(amount),
                String.valueOf(price),
                String.valueOf(isSupplyRequired)
        };
    }

    public String toCSVString() {
        return id + ","
                + name + ","
                + size + ","
                + amount + ","
                + price + ","
                + (isSupplyRequired ? 1 : 0);
    }
}
