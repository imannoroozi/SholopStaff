package com.sholop.sholopstaff.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pooyan on 8/20/2016.
 */
public class Corporation {

    String email, name, tell, address, logo_url;
    int id;

    public Corporation(JSONObject jo){
        try {
            this.setId(jo.getInt("id"));
            this.setName(jo.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
