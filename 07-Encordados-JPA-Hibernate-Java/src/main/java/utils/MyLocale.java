package utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class MyLocale {
    public static String toLocalDate(LocalDate date) {
        return date.format(
                DateTimeFormatter
                        .ofLocalizedDate(FormatStyle.MEDIUM).withLocale(new Locale("es", "ES"))
        );
    }

    public static String toLocalMoney(Double money) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(money);
    }

    public static String toLocalNumber(Double number) {
        return NumberFormat.getNumberInstance(new Locale("es", "ES")).format(number);
    }
}
