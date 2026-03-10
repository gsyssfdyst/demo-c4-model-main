package com.biblioteca.common.dto;

public class KPICardDTO {
    private String id;
    private String title;
    private String value;
    private String icon;
    private String color; // primary, success, warning, danger, info

    public KPICardDTO() {
    }

    public KPICardDTO(String id, String title, String value, String icon, String color) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.icon = icon;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
