package com.example.exampleclock;

import java.util.UUID;

class Alarm {
    String time;
    String owner;
    String alarmColor;
    String alarmId;

    public Alarm(String alarmTime, String alarmColor) {
        this.time = alarmTime;
        this.alarmColor = alarmColor;
        setAlarmId();
    }

    public void setAlarmId() {
        this.alarmId = UUID.randomUUID().toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAlarmColor(String alarmColor) {
        this.alarmColor = alarmColor;
    }


    public String getAlarmColor() {
        return alarmColor;
    }
}
