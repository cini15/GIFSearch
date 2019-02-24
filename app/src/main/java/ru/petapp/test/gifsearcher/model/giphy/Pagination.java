package ru.petapp.test.gifsearcher.model.giphy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pagination  implements Serializable {
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @Expose
    @SerializedName("count")
    private Integer count;
    @SerializedName("offset")
    @Expose
    private Integer offset;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
