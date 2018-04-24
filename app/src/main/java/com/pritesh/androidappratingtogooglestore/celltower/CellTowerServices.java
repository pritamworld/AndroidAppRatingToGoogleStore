package com.pritesh.androidappratingtogooglestore.celltower;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by pritesh.patel on 2018-01-18, 1:40 PM.
 * ADESA, Canada
 */

public interface CellTowerServices
{
    ///geolocation/cell?"v=1.1&data=open&mcc=302&mnc=720&lac=60013&cellid=2906766"
    @GET("geolocation/cell")
    Call<CellTowerLocation> listCellTowerLocation(@QueryMap Map<String, String> options);
}
