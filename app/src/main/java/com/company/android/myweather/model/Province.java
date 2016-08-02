package com.company.android.myweather.model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Province {
    private int mId;
    private String mProvinceName;
    private String mProvinceCode;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        mProvinceCode = provinceCode;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    public void setProvinceName(String provinceName) {
        mProvinceName = provinceName;
    }
}
