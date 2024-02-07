package by.dmitryskachkov.core.enums;

public enum OfferType {
    RENT("rent-flat-for-long/object/"),

    RENT_FOR_DAY("rent-flat-for-long/object/"),
    SALE("sale-flats/object/");

    private final String parameter;

    OfferType(String parameter) {
        this.parameter = parameter;
    }
    public String getParameter() {
        return parameter;
    }
}