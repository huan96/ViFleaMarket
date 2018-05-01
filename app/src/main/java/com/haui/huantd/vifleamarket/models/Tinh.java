package com.haui.huantd.vifleamarket.models;

import java.util.Objects;

public class Tinh {
    private String id;
    private String name;
    private String type;

    public Tinh() {
    }

    public Tinh(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tinh tinh = (Tinh) o;
        return Objects.equals(id, tinh.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
