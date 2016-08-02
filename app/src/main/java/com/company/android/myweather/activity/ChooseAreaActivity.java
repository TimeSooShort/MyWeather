package com.company.android.myweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.android.myweather.R;
import com.company.android.myweather.model.City;
import com.company.android.myweather.model.Country;
import com.company.android.myweather.model.MyWeatherDB;
import com.company.android.myweather.model.Province;
import com.company.android.myweather.util.HttpCallbackListener;
import com.company.android.myweather.util.HttpUtil;
import com.company.android.myweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;

    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private MyWeatherDB mMyWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> mProvinceList;

    private List<City> mCityList;

    private List<Country> mCountryList;

    private Province selectedProvince;

    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        mListView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_list_item_1, dataList);
        mListView.setAdapter(adapter);
        mMyWeatherDB = MyWeatherDB.getInstance(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = mProvinceList.get(i);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY) {
                    selectedCity = mCityList.get(i);
                    queryCountries();
                }
            }
        });
        queryProvinces();  //加载省级数据
    }

    private void queryProvinces() {
        mProvinceList = mMyWeatherDB.loadProvinces();
        if (mProvinceList.size() > 0) {
            dataList.clear();
            for (Province province : mProvinceList
                 ) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else {
            queryFromServer(null, "province");
        }
    }

    private void queryCities() {
        mCityList = mMyWeatherDB.loadCities(selectedProvince.getId());
        if (mCityList.size() > 0) {
            dataList.clear();
            for (City city : mCityList
                 ) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCountries() {
        mCountryList = mMyWeatherDB.loadCountries(selectedCity.getId());
        if (mCountryList.size() > 0) {
            dataList.clear();
            for (Country country : mCountryList
                 ) {
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTRY;
        }else {
            queryFromServer(selectedCity.getCityCode(), "country");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(mMyWeatherDB, response);
                }else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(mMyWeatherDB,
                            response, selectedProvince.getId());
                }else if ("country".equals(type)) {
                    result = Utility.handleCountriesResponse(mMyWeatherDB,
                            response, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if ("city".equals(type)) {
                                queryCities();
                            }else if ("country".equals(type)) {
                                queryCountries();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTRY) {
            queryCities();
        }else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }else {
            finish();
        }
    }
}
