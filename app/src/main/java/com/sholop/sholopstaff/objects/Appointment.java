package com.sholop.sholopstaff.objects;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.sholop.sholopstaff.utilities.CalendarTool;

/**
 * Created by Pooyan on 8/20/2016. to handle appointments
 */
public class Appointment {

    User user;
    ArrayList<Guest> guests;
    int id;

    CalendarTool day;

    String title,
            displayDate,
            dateString,
            timeString,
            enterTimeString,
            exitTimeString,
            description,
            status, location, corporationLogoUrl;


    public Appointment(JSONObject jo){
        try {
            this.setId(jo.getInt("id"));
            this.setUser(new User(jo.getJSONObject("user")));
            this.setDateString(jo.getString("date_string"));
            this.setTimeString(jo.getString("time_string"));
            this.setTitle(jo.getString("title"));
            this.setDescription(jo.getString("description"));
            this.setStatus(jo.has("status") ? jo.getString("status") : "");
            this.setLocation(jo.has("location") ? jo.getString("location") : "");
            this.setCorporationLogoUrl(jo.has("corporation_logo") ? jo.getString("corporation_logo") : "");

            JSONArray guestsJsonArray = jo.getJSONArray("guests");
            guests = new ArrayList<>();
            for( int i=0; i<guestsJsonArray.length(); i++){
                guests.add(new Guest(guestsJsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        day = new CalendarTool(this.getDateString());
    }

    public String getDisplayName() {
        String retVal = "";
        if( guests.size() == 1){
            retVal = guests.get(0).getDisplayName();
        }else if( guests.size() == 2 ){
            retVal = guests.get(0).getDisplayName() + " and " + guests.get(1).getDisplayName();
        }else if( guests.size() > 2 ){
            retVal = guests.get(0).getDisplayName() + " and " + (guests.size()-1) + " others";
        }

        return retVal;
    }

    public String getImageURL(){
        if( guests != null && guests.size() > 0){
            return guests.get(0).getImageAddress();
        }
        return null;
    }

    public String getDisplayTime(){
        if(this.getTimeString() == null || this.getTimeString().length() == 0 || this.getTimeString() == ""){
            return "Not defined";
        }
        return this.getTimeString().substring(0,2) + ":" + this.getTimeString().substring(2,4);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Guest> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<Guest> guests) {
        this.guests = guests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayDate(Context ctx) {
        return day.getGregorianDisplayDate(ctx);
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getEnterTimeString() {
        return enterTimeString;
    }

    public void setEnterTimeString(String enterTimeString) {
        this.enterTimeString = enterTimeString;
    }

    public String getExitTimeString() {
        return exitTimeString;
    }

    public void setExitTimeString(String exitTimeString) {
        this.exitTimeString = exitTimeString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location){ this.location = location;}

    public String getCorporationLogoUrl() {
        return corporationLogoUrl;
    }

    public void setCorporationLogoUrl(String corporationLogoUrl) {
        this.corporationLogoUrl = corporationLogoUrl;
    }
}
