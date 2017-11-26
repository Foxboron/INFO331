package no.uib.info331.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model for events
 * @author Fredrik V. Heimsæter
 */

public class Event {
    @SerializedName("User")
    private User user;
    @SerializedName("Group")
    private Group group;
    @SerializedName("Event")
    private String event;
    @SerializedName("Value")
    private int value;
    @SerializedName("Date")
    private Date date;


    public Event(User user, Group group, String event, int value, Date date) {
        this.user = user;
        this.group = group;
        this.event = event;
        this.value = value;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event1 = (Event) o;

        if (value != event1.value) return false;
        if (!user.equals(event1.user)) return false;
        if (!group.equals(event1.group)) return false;
        if (event != null ? !event.equals(event1.event) : event1.event != null) return false;
        return date.equals(event1.date);

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + group.hashCode();
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + value;
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "user=" + user +
                ", group=" + group +
                ", event='" + event + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
    }
}
