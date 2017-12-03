package no.uib.info331.util;

import android.util.Log;

import java.util.Comparator;

import no.uib.info331.models.Event;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class EventComparator implements Comparator<Event>{

    @Override
    public int compare(Event event, Event t1) {
        return t1.getDate().compareTo(event.getDate());
    }
}
