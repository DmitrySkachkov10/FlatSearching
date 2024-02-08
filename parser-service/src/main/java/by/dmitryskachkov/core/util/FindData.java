package by.dmitryskachkov.core.util;

import by.dmitryskachkov.core.enums.OfferType;

public class FindData {

    private String url;

    private int startPage;

    private OfferType offerType;

    private int pageCount;

    private int pageCountForThread;

    public FindData() {
    }

    public FindData(String url, int startPage, OfferType offerType, int pageCount, int pageCountForThread) {
        this.url = url;
        this.startPage = startPage;
        this.offerType = offerType;
        this.pageCount = pageCount;
        this.pageCountForThread = pageCountForThread;
    }

    public int getPageCountForThread() {
        return pageCountForThread;
    }

    public void setPageCountForThread(int pageCountForThread) {
        this.pageCountForThread = pageCountForThread;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
