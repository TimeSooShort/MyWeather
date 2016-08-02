package com.company.android.myweather.model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class City {
    private int mId;
    private String mCityName;
    private String mCityCode;
    private int mProvinceId;

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(int provinceId) {
        mProvinceId = provinceId;
    }
}
