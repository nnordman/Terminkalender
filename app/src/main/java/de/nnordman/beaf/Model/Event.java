package de.nnordman.beaf.Model;


public class Event {
    private String eventName;
    private String location;
    private String date;
    private String time;
    private String notes;

    public Event(){

    }

    public Event(String eventName, String location, String date, String time, String notes) {
        this.eventName = eventName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
