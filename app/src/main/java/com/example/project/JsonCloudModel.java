package com.example.project;

public class JsonCloudModel {

    private String trip_name;
    private String type;
    private String amount;
    private String time;
    private String additional_comments;

    public JsonCloudModel(
            String trip_name, String type, String amount, String time, String additional_comments
    ) {
        this.trip_name = trip_name;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.additional_comments = additional_comments;
    }

    public String getTripName() {
        return trip_name;
    }

    public void setTripName(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdditionalComments() {
        return additional_comments;
    }

    public void setAdditionalComments(String additional_comments) {
        this.additional_comments = additional_comments;
    }
}
