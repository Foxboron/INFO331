package no.uib.info331.models;

/**
 * Class for modelling a Bluetooth Low Energy Beacon
 * @author Fredrik V. Heimsæter
 * @version 1.0
 *
 */

public class Beacon {
    private String ID;
    private String UUID;
    private String major;
    private String minor;
    private String name;
    private int latitude;
    private int longitude;

    public Beacon(String ID, String name, int latitude, int longitude) {
        this.ID = ID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getID() {
        //TODO Create UUID, major and minor from ID
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}
