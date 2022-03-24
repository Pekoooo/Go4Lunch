package com.example.go4lunch.model.GooglePlacesAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class OpeningHours {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekDay;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<String> getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(List<String> weekDay) {
        this.weekDay = weekDay;
    }

    public String toFormattedString() {
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay.get(day - 1);
    }
}
