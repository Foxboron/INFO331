package no.uib.info331.models.messages;

import no.uib.info331.models.Event;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class EventEvent {
    private final Event event;

    public EventEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
