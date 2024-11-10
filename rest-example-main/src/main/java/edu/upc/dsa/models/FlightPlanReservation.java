package edu.upc.dsa.models;

import java.util.Date;

public class FlightPlanReservation {
    private String dronId;
    private Date date;
    private int estimatedTime;
    private String startPosition;
    private String endPosition;
    private String pilotoId;

    public FlightPlanReservation(String dronId, Date date, int estimatedTime, String startPosition, String endPosition, String pilotoId) {
        this.dronId = dronId;
        this.date = date;
        this.estimatedTime = estimatedTime;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.pilotoId = pilotoId;
    }

    public FlightPlanReservation() {
    }

    public String getDronId() {
        return dronId;
    }

    public void setDronId(String dronId) {
        this.dronId = dronId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getPilotoId() {
        return pilotoId;
    }

    public void setPilotoId(String pilotoId) {
        this.pilotoId = pilotoId;
    }
}
