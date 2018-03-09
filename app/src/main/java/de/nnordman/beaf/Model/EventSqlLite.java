package de.nnordman.beaf.Model;


public class EventSqlLite {
    private int id;
    private String eventNameSql, locationSql, dateSql, timeSql, notesSql;

    public EventSqlLite(){

    }

    public EventSqlLite(int id, String eventNameSql, String locationSql, String dateSql, String timeSql, String notesSql) {
        this.id = id;
        this.eventNameSql = eventNameSql;
        this.locationSql = locationSql;
        this.dateSql = dateSql;
        this.timeSql = timeSql;
        this.notesSql = notesSql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventNameSql() {
        return eventNameSql;
    }

    public void setEventNameSql(String eventNameSql) {
        this.eventNameSql = eventNameSql;
    }

    public String getLocationSql() {
        return locationSql;
    }

    public void setLocationSql(String locationSql) {
        this.locationSql = locationSql;
    }

    public String getDateSql() {
        return dateSql;
    }

    public void setDateSql(String dateSql) {
        this.dateSql = dateSql;
    }

    public String getTimeSql() {
        return timeSql;
    }

    public void setTimeSql(String timeSql) {
        this.timeSql = timeSql;
    }

    public String getNotesSql() {
        return notesSql;
    }

    public void setNotesSql(String notesSql) {
        this.notesSql = notesSql;
    }
}
