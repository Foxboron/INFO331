package no.uib.info331.models.messages;

import no.uib.info331.models.Beacon;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class BackgroundBeaconEvent {
    private final Beacon beacon;

    public BackgroundBeaconEvent(Beacon beacon) {
        this.beacon = beacon;
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
