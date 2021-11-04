package com.example.app;

import java.io.Serializable;

public class Trip implements Serializable {

    private long id;
    private String name;
    private String destination;
    private String date;
    private String risk_assessment;
    private String description;
    private String duration;
    private String aim;
    private String status;

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

    @Override
    public String toString() {
        return "Trip{" +
                "  id=" + this.id +
                ", name='" + this.name + '\'' +
                ", destination='" + this.destination + '\'' +
                ", date=" + this.date + '\'' +
                ", risk_assessment='" + this.risk_assessment + '\'' +
                ", description='" + this.description + '\'' +
                ", duration='" + this.duration + '\'' +
                ", aim='" + this.aim + '\'' +
                ", status='" + this.status +
                '}';
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

    public String getRisk_assessment() {
        return risk_assessment;
    }

    public void setRisk_assessment(String risk_assessment) {
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
