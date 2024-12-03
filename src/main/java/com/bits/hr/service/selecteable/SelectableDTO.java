package com.bits.hr.service.selecteable;

/**
 * A DTO for Dictionary type {key: value} data structure
 */

public class SelectableDTO {

    private long key;
    private String value;

    public SelectableDTO(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
