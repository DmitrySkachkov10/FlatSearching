package by.skachkovdmitry.audit.core.dto;

import java.util.List;
import java.util.Objects;

public class PageOfAudit {

    private int number;

    private int size;

    private int totalPages;

    private long totalElements;

    private boolean first;

    private int numberOfElements;

    private boolean last;

    private List<Audit> content;

    public PageOfAudit() {
    }

    public PageOfAudit(int number, int size, int totalPages, long totalElements, boolean first, int numberOfElements, boolean last, List<Audit> content) {
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.last = last;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<Audit> getContent() {
        return content;
    }

    public void setContent(List<Audit> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof PageOfAudit that)) return false;
        return number == that.number && size == that.size && totalPages == that.totalPages && totalElements == that.totalElements && first == that.first && numberOfElements == that.numberOfElements && last == that.last && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, size, totalPages, totalElements, first, numberOfElements, last, content);
    }
}
