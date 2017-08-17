package com.optimustechproject.project2.Models;

import org.json.JSONObject;

/**
 * Created by Anurag on 14-11-2016.
 */
public class NotificationsPOJO {
    String title;
    String message;
    String timeStamp;
    String imageUrl;
    boolean isBackground;
    JSONObject payload;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }
}
