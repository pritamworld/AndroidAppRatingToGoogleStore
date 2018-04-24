package com.pritesh.androidappratingtogooglestore.celltower;

/**
 * Created by pritesh.patel on 2018-01-18, 1:42 PM.
 * ADESA, Canada
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Data {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("range")
    @Expose
    private Double range;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lat", lat).append("range", range).append("lon", lon).append("time", time).toString();
    }
}
