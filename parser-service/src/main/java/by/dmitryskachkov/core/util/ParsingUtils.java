package by.dmitryskachkov.core.util;

import by.dmitryskachkov.core.enums.OfferType;

public class ParsingUtils {

    private String url;

    private OfferType offerType;

    public ParsingUtils() {
    }

    public ParsingUtils(String url, OfferType offerType) {
        this.url = url;
        this.offerType = offerType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }
}
