package com.example.db_curs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Row {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty column1;
    private final SimpleStringProperty column2;
    private final SimpleStringProperty column3;
    private final SimpleStringProperty column4;

    public Row(int idn, String col1, String col2, String col3, String col4) {
        this.id = new SimpleIntegerProperty(idn);
        this.column1 = new SimpleStringProperty(col1);
        this.column2 = new SimpleStringProperty(col2);
        this.column3 = new SimpleStringProperty(col3);
        this.column4 = new SimpleStringProperty(col4);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getColumn1() {
        return column1.get();
    }

    public SimpleStringProperty column1Property() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1.set(column1);
    }

    public String getColumn2() {
        return column2.get();
    }

    public SimpleStringProperty column2Property() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2.set(column2);
    }

    public String getColumn3() {
        return column3.get();
    }

    public SimpleStringProperty column3Property() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3.set(column3);
    }

    public String getColumn4() {
        return column4.get();
    }

    public SimpleStringProperty column4Property() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4.set(column4);
    }
}
