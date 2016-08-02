package com.company.android.myweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.company.android.myweather.db.MyWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class MyWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "my_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static MyWeatherDB sMyWeatherDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private MyWeatherDB(Context context) {
        MyWeatherOpenHelper dbHelper = new MyWeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     *获取MyWeatherDB的实例
     */
    public synchronized static MyWeatherDB getInstance(Context context) {
        if (sMyWeatherDB == null) {
            sMyWeatherDB = new MyWeatherDB(context);
        }
        return sMyWeatherDB;
    }

    /**
     * 将province实例存储到数据库
     * @param province
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name", province.getProvinceName());
            contentValues.put("province_code", province.getProvinceCode());
            db.insert("Province", null, contentValues);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return
     */
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db
                .query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor
                        .getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor
                        .getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将city实例存储到数据库
     * @param city
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name", city.getCityName());
            contentValues.put("city_code", city.getCityCode());
            contentValues.put("province_id", city.getProvinceId());
            db.insert("City", null, contentValues);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     * @return
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[] {String.valueOf(provinceId)}, null, null, null);
        if (cursor != null) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor
                        .getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor
                        .getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将country实例存储到数据库
     * @param country
     */
    public void SaveCountry(Country country) {
        if (country != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("country_name", country.getCountryName());
            contentValues.put("country_code", country.getCountryCode());
            contentValues.put("city_id", country.getCityId());
            db.insert("Country", null, contentValues);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     * @param cityId
     * @return
     */
    public List<Country> loadCountries(int cityId) {
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id = ?",
                new String[] {String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cityId);
                list.add(country);
            }while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
