package no.uib.info331.models;

import java.util.ArrayList;

/**
 * Class for modelling a group
 * @author Fredrik V. Heimsæter
 * @version 1.0
 * TODO: Speak with backend and actually do changes there.
 */

public class    Group {
    private String name;
    private User owner;
    private int points;
    private ArrayList<User> members;

    public Group(String name, User owner, int points) {
        this.name = name;
        this.owner = owner;
        this.points = points;
        members = new ArrayList<>();
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

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public boolean addMember(User member) {
        if(member!=null) {
            if (!members.contains(member)) {
                members.add(member);
                return true;
            }
            return false;
        }return false;
    }

    public boolean deleteMember(User member) {
        if(members.contains(member)) {
            members.remove(member);
            return true;
        }
        return false;
    }
}
