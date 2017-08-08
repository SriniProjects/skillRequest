package com.optimustechproject.project2.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 30/7/17.
 */

public class TrainingsPOJO {
    @SerializedName("id")
    @Expose
    private List<String> id = null;
    @SerializedName("title")
    @Expose
    private List<String> title = null;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("key_learning1")
    @Expose
    private List<String> keyLearning1 = null;
    @SerializedName("key_learning2")
    @Expose
    private List<String> keyLearning2 = null;
    @SerializedName("key_learning3")
    @Expose
    private List<String> keyLearning3 = null;
    @SerializedName("price")
    @Expose
    private List<String> price = null;
    @SerializedName("duration")
    @Expose
    private List<String> duration = null;
    @SerializedName("availability")
    @Expose
    private List<String> availability = null;
    @SerializedName("date")
    @Expose
    private List<String> date = null;
    @SerializedName("description")
    @Expose
    private List<String> description = null;
    @SerializedName("timings")
    @Expose
    private List<String> timings = null;
    @SerializedName("venue")
    @Expose
    private List<String> venue = null;
    @SerializedName("venue_latitude")
    @Expose
    private List<String> venueLatitude = null;
    @SerializedName("venue_longitude")
    @Expose
    private List<String> venueLongitude = null;
    @SerializedName("enquiry_status")
    @Expose
    private List<String> enquiryStatus = null;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getKeyLearning1() {
        return keyLearning1;
    }

    public void setKeyLearning1(List<String> keyLearning1) {
        this.keyLearning1 = keyLearning1;
    }

    public List<String> getKeyLearning2() {
        return keyLearning2;
    }

    public void setKeyLearning2(List<String> keyLearning2) {
        this.keyLearning2 = keyLearning2;
    }

    public List<String> getKeyLearning3() {
        return keyLearning3;
    }

    public void setKeyLearning3(List<String> keyLearning3) {
        this.keyLearning3 = keyLearning3;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getDuration() {
        return duration;
    }

    public void setDuration(List<String> duration) {
        this.duration = duration;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getTimings() {
        return timings;
    }

    public void setTimings(List<String> timings) {
        this.timings = timings;
    }

    public List<String> getVenue() {
        return venue;
    }

    public void setVenue(List<String> venue) {
        this.venue = venue;
    }

    public List<String> getVenueLatitude() {
        return venueLatitude;
    }

    public void setVenueLatitude(List<String> venueLatitude) {
        this.venueLatitude = venueLatitude;
    }

    public List<String> getVenueLongitude() {
        return venueLongitude;
    }

    public void setVenueLongitude(List<String> venueLongitude) {
        this.venueLongitude = venueLongitude;
    }

    public List<String> getEnquiryStatus() {
        return enquiryStatus;
    }

    public void setEnquiryStatus(List<String> enquiryStatus) {
        this.enquiryStatus = enquiryStatus;
    }
}
