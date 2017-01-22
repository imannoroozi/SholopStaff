package com.sholop.sholopstaff.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Iman on 12/3/2015. edited
 */
public class Guest implements Serializable {
    int ID;
    String displayName, firstName, lastName, imageAddress, email, tell;

    public Guest(){

    }
    public Guest(JSONObject jo){
        /*
        'user_ID'
        'user_name'
        'user_image_url'
        */
        try {
            this.setID(jo.getInt("ID"));
            this.setImageAddress("");
            this.setDisplayName(jo.getString("name"));
            this.setEmail(jo.getString("email"));
            this.setTell(jo.getString("tell"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return this.getEmail() + "," +
                this.getDisplayName() + "," +
                this.getTell();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }


}
