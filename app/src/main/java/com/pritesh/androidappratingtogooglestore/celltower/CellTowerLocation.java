package com.pritesh.androidappratingtogooglestore.celltower;

/**
 * Created by pritesh.patel on 2018-01-18, 1:42 PM.
 * ADESA, Canada
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CellTowerLocation {

    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("result", result).append("data", data).toString();
    }

}
