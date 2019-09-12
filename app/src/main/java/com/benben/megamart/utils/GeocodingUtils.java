package com.benben.megamart.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.bean.GeocodingEntity;
import com.benben.megamart.config.Constants;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenContext;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: mxy
 * <p>
 * ModifiedBy:
 * <p>
 * Date 2019-06-17 10:22
 * <p>
 * Description:地理编码，反编码
 */
public class GeocodingUtils {

    /**
     * 系统原生API
     * 根据经纬度获取地理名称
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static Address getLocality(double latitude, double longitude, Locale currentLocale) {
//        Address address = getAddress(latitude, longitude,Locale.getDefault());
        Address address = getAddress(latitude, longitude, currentLocale);
       /* String country = "";
        if(address !=null){
            country = address.getCountryName();
            if(TextUtils.isEmpty(country)){
                country = "";
            } else{
                if(country.equalsIgnoreCase("null")){
                    country = "";
                }
            }
        }
        if (currentLocale.getLanguage().equals(Locale.CHINESE.getLanguage())) {//中文
            return address == null ? "" : country + address.getLocality();
            return address == null ? "" : address.getLocality();
        } else {
            return address == null ? "" : address.getLocality() + " " + country;
            return address == null ? "" :  address.getLocality();
        }*/
        if(address!= null){
            return address;
        }
        return null;
    }

    /**
     * mapbox
     * 地理反编码，根据经纬度获取地理名称
     * @param context
     * @param locale 语言
     * @param onGeocodingCompleteListener
     */
    public static void makeGeocodeSearch(Context context, double longitude,double latitude,Locale locale, OnGeocodingCompleteListener onGeocodingCompleteListener) {
        try {
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken("pk.eyJ1IjoiZmlpaXBheTIwMTkiLCJhIjoiY2pzZmd4OG44MDhxNzN5cHV1YzZpZG85ZCJ9.Dz091UTbErxtERwOXSWTmA")
                    .query(Point.fromLngLat(longitude, latitude))
                    .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                    .mode(GeocodingCriteria.MODE_PLACES)
                    .languages(locale)
                    .build();
            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call,
                                       Response<GeocodingResponse> response) {

                    List<CarmenFeature> results = response.body().features();

                    if (results.size() > 0) {

                        CarmenFeature feature = results.get(0);

                        if(onGeocodingCompleteListener != null){
                            Log.e(Constants.WHK_TAG,"反编码地理位置地址=="+feature.placeName());
                            Log.e(Constants.WHK_TAG,"反编码地理位置json=="+feature.toJson());
                            List<CarmenContext> list = feature.context();
                            if(list.size() ==2){
                                CarmenContext carmenContext = list.get(1);
                                String countryName = carmenContext.text();
                                String code = carmenContext.shortCode();
                                String detailAddr = feature.placeName();
                                String cityName = feature.text();//城市名称

                                GeocodingEntity entity = new GeocodingEntity();
                                entity.setDetailAddr(detailAddr);
                                entity.setCountryName(countryName);
                                entity.setCountryCode(code);
                                entity.setCityName(cityName);

                                onGeocodingCompleteListener.onGeocodingSuccess(entity);
                            }
                            //{"type":"Feature","bbox":[-122.1178536,37.356758,-122.0346693,37.4520779],"id":"place.15154547807111820","geometry":{"type":"Point","coordinates":[-122.082, 37.3856]},"properties":{"wikidata":"Q486860"},"text":"Mountain View","place_name":"Mountain View, California, United States of America","place_type":["place"],"center":[-122.082,37.3856],"context":[{"id":"region.11319063928738010","text":"California","short_code":"US-CA","wikidata":"Q99"},{"id":"country.9053006287256050","text":"United States of America","short_code":"us","wikidata":"Q30"}],"relevance":1.0,"language":"en"}
                            //{"type":"Feature","bbox":[-122.1178536,37.356758,-122.0346693,37.4520779],"id":"place.15154547807111820","geometry":{"type":"Point","coordinates":[-122.082, 37.3856]},"properties":{"wikidata":"Q486860"},"text":"山景城","place_name":"山景城, 加利福尼亚州, 美国","place_type":["place"],"center":[-122.082,37.3856],"context":[{"id":"region.11319063928738010","text":"加利福尼亚州","short_code":"US-CA","wikidata":"Q99"},{"id":"country.9053006287256050","text":"美国","short_code":"us","wikidata":"Q30"}],"relevance":1.0,"language":"zh"}
                        }
                    } else {

                        Log.e(Constants.WHK_TAG,"没有获取到反编码地理位置");

                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    Log.e(Constants.WHK_TAG,"Geocoding Failure: " + throwable.getMessage());
                    if(onGeocodingCompleteListener !=null){
                        onGeocodingCompleteListener.onGeocodignFailure();
                    }
                }
            });
        } catch (ServicesException servicesException) {
            Log.e(Constants.WHK_TAG,"Error geocoding: " + servicesException.toString());
            servicesException.printStackTrace();
        }
    }

    public interface OnGeocodingCompleteListener{
        void onGeocodingSuccess(GeocodingEntity geocodingEntity);
        void onGeocodignFailure();
    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    private static Address getAddress(double latitude, double longitude, Locale currentLocale) {
        Geocoder geocoder = new Geocoder(MegaMartApplication.getInstance(), currentLocale);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
