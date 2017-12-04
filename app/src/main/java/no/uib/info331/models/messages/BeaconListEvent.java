package no.uib.info331.models.messages;

import java.util.List;

import no.uib.info331.models.Beacon;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class BeaconListEvent {
    private final List<Beacon> beaconList;

    public BeaconListEvent(List<Beacon> beaconList) {
        this.beaconList = beaconList;
    }

    public List<Beacon> getBeaconList() {
        return beaconList;
    }
}
