package com.example.honeybeehaven.classes;

public class Keyword {
    public String value;
    public boolean checked;

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Keyword(String value, boolean checked) {
        this.value = value;
        this.checked = checked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
