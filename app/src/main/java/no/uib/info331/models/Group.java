package no.uib.info331.models;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for modelling a group
 * @author Fredrik V. Heimsæter
 * @version 1.0
 * TODO: Speak with backend and actually do changes there.
 */

public class Group implements Serializable{
    private int ID;
    private String Name;
    private User Owner;
    private int Points;
    private ArrayList<User> Users;

    public Group(String name, User owner, int points, ArrayList<User> Users) {
        this.ID = ID;
        this.Name = name;
        this.Owner = owner;
        this.Points = points;
        this.Users = Users;
    }

    public Group(String name, User owner) {
        this.Name = name;
        this.Owner = owner;
        this.Users = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public User getOwner() {
        return Owner;
    }

    public void setOwner(User owner) {
        this.Owner = owner;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        this.Points = points;
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<User> users) {
        this.Users = users;
    }

    public int getID() {
        return ID;
    }

    public boolean addMember(User member) {
        if(member!=null) {
            if (!Users.contains(member)) {
                Users.add(member);
                return true;
            }
            return false;
        }return false;
    }

    public boolean deleteMember(User member) {
        if(Users.contains(member)) {
            Users.remove(member);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        ArrayList<String> membersOfGroup = new ArrayList<>();
        for (User member : Users) {
            membersOfGroup.add(member.getUsername());
        }
        return "Group{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Owner=" + Owner +
                ", Points=" + Points +
                ", Users=" + TextUtils.join(",", membersOfGroup) +
                '}';
    }
}
