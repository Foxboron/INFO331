package no.uib.info331.models;

import com.google.gson.annotations.SerializedName;

/**
 * Class for modelling a Bluetooth Low Energy Beacon
 * @author Fredrik V. Heimsæter
 * @version 1.0
 *
 */

public class Beacon {
    private String ID;
    @SerializedName("UUID")
    private String UUID;
    @SerializedName("Major")
    private String major;
    @SerializedName("Minor")
    private String minor;
    @SerializedName("Name")
    private String name;
    private int latitude;
    private int longitude;

    public Beacon(String ID, String UUID, String major, String minor, String name, int latitude, int longitude) {
        this.ID = ID;
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
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

    @Override
    public String toString() {
        return "Beacon{" +
                "ID='" + ID + '\'' +
                ", UUID='" + UUID + '\'' +
                ", major='" + major + '\'' +
                ", minor='" + minor + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beacon beacon = (Beacon) o;

        if (!ID.equals(beacon.ID)) return false;
        if (!UUID.equals(beacon.UUID)) return false;
        if (!major.equals(beacon.major)) return false;
        return minor.equals(beacon.minor);

    }

    @Override
    public int hashCode() {
        int result = ID.hashCode();
        result = 31 * result + UUID.hashCode();
        result = 31 * result + major.hashCode();
        result = 31 * result + minor.hashCode();
        return result;
    }
}
