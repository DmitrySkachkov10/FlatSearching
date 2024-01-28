package by.dmitryskachkov.entity;
import java.util.ArrayList;
import java.util.List;

public class Errors<T extends Error>  {
    List<T> errorList = new ArrayList<>();

    public void add(T e) {
        this.errorList.add(e);
    }

    public T getById(int id) {
        return (errorList.size() > id + 1) ? errorList.get(id) : null;
    }

    public List<T> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<T> errorList) {
        this.errorList = errorList;
    }
}