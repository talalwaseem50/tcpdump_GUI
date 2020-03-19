package sample;

import javafx.beans.property.SimpleStringProperty;

public class Protocol {

    private SimpleStringProperty value;

    private String pName;
    private int pCount;


    Protocol(String pn) {
        pName = pn;
    }

    public final String getValue() {
        if (value != null)
            return value.get();
        return pName + ": 0";
    }

    public final void setValue(String value) {
        this.valueProperty().set(value);
    }

    public final SimpleStringProperty valueProperty() {
        if (value == null)
            value = new SimpleStringProperty(pName + ": " + pCount);
        return value;
    }

    public void increment() {
        pCount += 1;
        value.setValue(pName + ": " + pCount);
    }


    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }
}
