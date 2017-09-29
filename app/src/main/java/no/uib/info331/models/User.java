package no.uib.info331.models;

import java.util.ArrayList;

/**
 * Class for modelling a user.
 * @author Fredrik V. Heimsæter
 * @version 1.0
 * TODO: Speak with backend and actually do changes there.
 */

public class User {
    private String username;
    private String password;
    private String photo;
    private int points;
    private ArrayList<Group> groups;

    public User(String username, String password, String photo, int points) {
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    /**
     * Adds a group to this users list of groups
     * @param group The group to add
     * @return true if the group was added successfully, false otherwise
     */
    public boolean addGroup(Group group) {
        if(!groups.contains(group)) {
            groups.add(group);
            return true;
        }
        return false;
    }
}
