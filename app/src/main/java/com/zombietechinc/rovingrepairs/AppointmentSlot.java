package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 9/6/2017.
 */

public class AppointmentSlot {
    String startTime;
    String endTime;
    String eventId;

    public AppointmentSlot (String startTime, String endTime, String eventId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventId = eventId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public AppointmentSlot(){

    }
}
