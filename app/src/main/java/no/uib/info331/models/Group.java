package no.uib.info331.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class for modelling a group
 * @author Fredrik V. Heimsæter
 * @version 1.0
 * TODO: Speak with backend and actually do changes there.
 */

public class Group implements Serializable{
    @SerializedName("ID")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Owner")
    private User owner;
    @SerializedName("Points")
    private int points;
    @SerializedName("Users")
    private ArrayList<User> users;
    @SerializedName("Beacon")
    private Beacon beacon;

    public Group(int id, String name, User owner, int points, ArrayList<User> Users, Beacon beacon) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.points = points;
        this.users = Users;
        this.beacon = beacon;
    }

    public Group(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public boolean addMember(User member) {
        if(member!=null) {
            if (!users.contains(member)) {
                users.add(member);
                return true;
            }
            return false;
        }return false;
    }

    public boolean deleteMember(User member) {
        if(users.contains(member)) {
            users.remove(member);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        ArrayList<String> membersOfGroup = new ArrayList<>();
        for (User member : users) {
            membersOfGroup.add(member.getUsername());
        }
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", points=" + points +
                ", users=" + TextUtils.join(",", membersOfGroup) +
                ", beacon=" + beacon.toString() +
                '}';
    }
}
