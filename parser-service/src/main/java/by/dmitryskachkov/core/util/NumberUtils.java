package by.dmitryskachkov.core.util;

public class NumberUtils {

    public static Number extractNumberFromString(String text) {
        String numericValue = text.replaceAll("[^0-9.]", "");

        if (numericValue.contains(".")) {
            return Float.parseFloat(numericValue);
        } else {
            return Integer.parseInt(numericValue);
        }
    }
}