package com.example.ajoudongfe;

public class AlarmStateObject {
    boolean stateAlarm = false;
    boolean eventAlarm = false;
    boolean newclubAlarm = false;
    int unreadEvent;

    public boolean isStateAlarm() {
        return stateAlarm;
    }

    public void setStateAlarm(boolean stateAlarm) {
        this.stateAlarm = stateAlarm;
    }

    public boolean isEventAlarm() {
        return eventAlarm;
    }

    public void setEventAlarm(boolean eventAlarm) {
        this.eventAlarm = eventAlarm;
    }

    public boolean isNewclubAlarm() {
        return newclubAlarm;
    }

    public void setNewclubAlarm(boolean newclubAlarm) {
        this.newclubAlarm = newclubAlarm;
    }

    public int getUnreadEvent() {
        return unreadEvent;
    }

    public void setUnreadEvent(int unreadEvent) {
        this.unreadEvent = unreadEvent;
    }

}
