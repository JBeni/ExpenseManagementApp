package com.example.project;

public class Trip {

    private long id;
    private String name; // required field
    private String destination; // required field
    private String date; // required field
    private String risk_assessment; // required field
    private String description; // optional field
    private String duration; // required field
    private String aim; // optional field
    private String status; // required field

    public Trip(
            long id, String name, String destination, String date, String risk_assessment,
            String description, String duration, String aim, String status
    ) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.risk_assessment = risk_assessment;
        this.description = description;
        this.duration = duration;
        this.aim = aim;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRiskAssessment() {
        return risk_assessment;
    }

    public void setRiskAssessment(String risk_assessment) {
        this.risk_assessment = risk_assessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
