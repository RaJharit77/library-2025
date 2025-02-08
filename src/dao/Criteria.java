package dao;

public class Criteria {
    private String column;

    public Object getValue() {
        return value;
    }

    public String getColumn() {
        return column;
    }

    private Object value;

    public Criteria(String column, Object value) {
        this.column = column;
        this.value = value;
    }
}
