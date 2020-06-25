package com.example.ajoudongfe;

public class AlarmStateObject {
    boolean stateAlarm;
    boolean eventAlarm;
    boolean newclubAlarm;
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
