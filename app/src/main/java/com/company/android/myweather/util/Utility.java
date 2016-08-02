package com.company.android.myweather.util;

import android.text.TextUtils;

import com.company.android.myweather.model.City;
import com.company.android.myweather.model.Country;
import com.company.android.myweather.model.MyWeatherDB;
import com.company.android.myweather.model.Province;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Utility {

    /**
     *解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(MyWeatherDB myWeatherDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces
                     ) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    // 将解析出来的数据存储到Province表
                    myWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return true;
    }

    /**
     *解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB,
                                               String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities
                     ) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //将解析出来的数据存储到City表
                    myWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountriesResponse(MyWeatherDB myWeatherDB,
                                               String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String c : allCountries
                        ) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    //将解析出来的数据存储到Country表
                    myWeatherDB.SaveCountry(country);
                }
                return true;
            }
        }
        return false;
    }
}
