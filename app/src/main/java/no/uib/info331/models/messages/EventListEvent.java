package no.uib.info331.models.messages;

import java.util.List;

import no.uib.info331.models.Event;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class EventListEvent {
    private final List<Event> eventList;

    public EventListEvent(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
