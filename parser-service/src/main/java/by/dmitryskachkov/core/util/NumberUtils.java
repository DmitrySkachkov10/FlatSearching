package by.dmitryskachkov.core.util;

public class NumberUtils {

    public static Number extractNumberFromString(String text) {
        String numericValue = text.replaceAll("[^0-9.]", "");

        if (!numericValue.isEmpty()) {
            try {
                if (numericValue.contains(".")) {
                    System.out.println("numericValue - " + numericValue.toUpperCase());
                    return Float.parseFloat(numericValue);
                } else {
                    return Integer.parseInt(numericValue);
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }
}