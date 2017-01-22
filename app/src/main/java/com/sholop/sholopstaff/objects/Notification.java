package com.sholop.sholopstaff.objects;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Pooyan to handle notifications
 * on 9/13/2016.
 */
public class Notification {

    String title, content, type, imageUrl;
    boolean playSound, vibrate, read;
    int id, appointmentId;
    HashMap<String, String> data;

    public Notification(Bundle bundle){
        setTitle(bundle.getString("title"));
        setId(Integer.parseInt(bundle.getString("id")));
        setContent(bundle.getString("content"));
        setType(bundle.getString("type"));
        setAppointmentId(Integer.parseInt(bundle.getString("appointment_id")));
        setPlaySound("Y".equals(bundle.getString("play_sound")));
        setVibrate("Y".equals(bundle.getString("vibrate")));
        setRead("Y".equals(bundle.getString("read")));
        setData(bundle.getString("data"));
    }

    public Notification(JSONObject jo) {
        try {
            this.setId(jo.getInt("id"));
            this.setTitle(jo.getString("title"));
            this.setContent(jo.getString("content"));
            this.setImageUrl(jo.getString("image_url"));
            this.setType(jo.getString("type"));
            this.setAppointmentId(jo.getInt("appointment_id"));
            this.setPlaySound(jo.getString("playSound").equals("Y"));
            this.setVibrate(jo.getString("vibrate").equals("Y"));
            this.setRead(jo.getString("isRead").equals("Y"));
            this.setData(jo.getJSONObject("json_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(String data) {
        if( data == null || data.equals("")){
            return;
        }

        this.data = new HashMap<>();
        try {
            JSONObject jo = new JSONObject(data);
            Iterator<?> keys = jo.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                this.data.put(key, jo.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setData(JSONObject jsonData) {
        this.data = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

}
