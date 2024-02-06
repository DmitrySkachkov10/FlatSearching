package by.dmitryskachkov.core.util;

import by.dmitryskachkov.core.enums.OfferType;

public class FindData {

    private String url;

    private int startPage;

    private OfferType offerType;

    public FindData() {
    }

    public FindData(String url, int startPage, OfferType offerType) {
        this.url = url;
        this.startPage = startPage;
        this.offerType = offerType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStartPage() {

        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }
}
