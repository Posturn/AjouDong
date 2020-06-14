package com.example.ajoudongfe;

public class AlarmStateObject {
    boolean stateAlarm = false;
    boolean eventAlarm = false;
    boolean newclubAlarm = false;
    boolean unreadEvent = false;

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

    public boolean isUnreadEvent() {
        return unreadEvent;
    }

    public void setUnreadEvent(boolean unreadEvent) {
        this.unreadEvent = unreadEvent;
    }
}
