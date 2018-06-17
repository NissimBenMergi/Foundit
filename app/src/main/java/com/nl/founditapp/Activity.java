package com.nl.founditapp;

import android.location.Location;
import android.location.LocationListener;
import java.util.*;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class Activity {
    private String action;
    private String title;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String category;
    private String location;
    private User user;
    private Double lat;
    private Double lng ;
    private Double distance;

    public Activity() {
        super();
        this.action = "broken";
        day = 1;
        month = 1;
        year = 2010;
        this.category = "study";
        this.title = "broken";
        location = null;
        hour = 0;
        minute = 0;
        user = new User();
        lat=32.104690;
        lng=35.206666;
        distance=0.0;

    }

    public Activity(String ntitle, String action, int day, int month, int year, String loc, int h, int m, User us,Double la,Double ln,Double dis) {
        this.action = action;
        this.day = day;
        this.month = month;
        this.year = year;
        this.category = category;
        this.title = ntitle;
        location = loc;
        hour = h;
        minute = m;
        user = us;
        lat=la;
        lng=ln;
        distance=dis;
    }

    public Activity(String ntitle, String action, String category, String loc) {
        this.action = action;
        day = 1;
        month = 1;
        year = 2010;
        this.category = category;
        this.title = ntitle;
        location = loc;
    }

    public Activity(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {return lat;}

    public void setLat(Double lat) {this.lat = lat;}

    public Double getLng() {return lng;}

    public void setLng(Double lng) {this.lng = lng;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getDistance() {return distance;}

    public void setDistance(Double distance) {this.distance = distance;}

    public ArrayList<Activity> locSort(ArrayList<Activity> list)
    {
        ArrayList<Double> order = new ArrayList<>();
        ArrayList<Double> oldOrder = new ArrayList<>();
        ArrayList<Activity> activitiesList = new ArrayList<>();
        for(Activity act : list)
        {

            int Radius = 6371;// radius of earth in Km
            double lat1 = 32.104690;
            double lat2 = act.getLat();
            double lon1 =  35.206666;
            double lon2 = act.getLng();
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;
            double km = valueResult / 1;
            DecimalFormat newFormat = new DecimalFormat("####");
            int kmInDec = Integer.valueOf(newFormat.format(km));
            double meter = valueResult % 1000;
            int meterInDec = Integer.valueOf(newFormat.format(meter));


            order.add(Radius * c);
            oldOrder.add(Radius * c);
        }

           Collections.sort(order);


        for(int j=0;j<order.size();j++) {
            for (int i = 0; i < oldOrder.size(); i++) {
                if (order.get(j) - oldOrder.get(i) == 0) {
                    list.get(i).setDistance(oldOrder.get(i));
                    activitiesList.add(list.get(i));
                }

            }

        }
        /*
        for(int i=0;i<order.size();i++)
        {
            list.get(i).setDistance(order.get(i));
            activitiesList.add(list.get(i));
        }
        */

        return activitiesList;
    };





}