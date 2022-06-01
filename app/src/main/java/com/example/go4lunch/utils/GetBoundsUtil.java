package com.example.go4lunch.utils;

public class GetBoundsUtil {

    /*
    From → https://tinyurl.com/5a9brxv7
     */
    public static double[] getBoundsFromLatLng(double radius, double lat, double lng) {
        double lat_change = radius / 111.2f;
        double lon_change = Math.abs(Math.cos(lat * (Math.PI / 180)));
        return new double[]{
                lat - lat_change,
                lng - lon_change,
                lat + lat_change,
                lng + lon_change
        };
    }
}
