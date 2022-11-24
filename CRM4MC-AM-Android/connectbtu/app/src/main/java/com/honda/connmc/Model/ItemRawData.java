package com.honda.connmc.Model;

public class ItemRawData {
    private String rawData;
    private boolean isSelected;

    public ItemRawData(String rawData, boolean isSelected) {
        this.rawData = rawData;
        this.isSelected = isSelected;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
