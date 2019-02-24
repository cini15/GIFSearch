package ru.petapp.test.gifsearcher.model.giphy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {

    @SerializedName("data")
    @Expose
    private List<GIFSData> GIFSDataList;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;


    public DataContainer() {
        this.GIFSDataList = new ArrayList<>();
    }

    public List<GIFSData> getGIFSDataList() {
        return GIFSDataList;
    }

    public void setGIFSDataList(List<GIFSData> GIFSDataList) {
        this.GIFSDataList = GIFSDataList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
