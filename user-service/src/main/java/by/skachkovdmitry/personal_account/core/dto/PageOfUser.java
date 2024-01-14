package by.skachkovdmitry.personal_account.core.dto;

import java.util.ArrayList;
import java.util.List;

public class PageOfUser {

    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private int numberOfElements;


    private boolean last;

    private List<User> userList = new ArrayList<>();


    public PageOfUser(int number, int size, int totalPages, long totalElements, boolean first, int numberOfElements, boolean last, List<User> userList) {
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.last = last;
        this.userList = userList;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElemens() {
        return totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public List<User> getUserList() {
        return userList;
    }
}
