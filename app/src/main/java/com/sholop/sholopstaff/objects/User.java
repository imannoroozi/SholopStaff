package com.sholop.sholopstaff.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Iman on 12/3/2015.
 */
public class User implements Serializable {
    int ID;
    Corporation corporation;
    String displayName, firstName, lastName, imageAddress, email, designation, tell;

    public User(JSONObject jo){
        /*
        'user_ID'
        'user_name'
        'user_image_url'
        */
        try {
            this.setID(jo.getInt("user_id"));
            this.setImageAddress(jo.getString("user_image_url"));
            this.setDisplayName(jo.getString("user_display_name"));
            this.setEmail(jo.getString("user_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }


}
