package com.haui.huantd.vifleamarket.models;

import java.util.Objects;

public class DanhMuc {
    private String id;
    private String name;

    public DanhMuc() {
    }

    public DanhMuc(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DanhMuc danhMuc = (DanhMuc) o;
        return Objects.equals(id, danhMuc.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
