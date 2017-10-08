package no.uib.info331.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for modelling a user.
 * @author Fredrik V. Heimsæter
 * @version 1.0
 * TODO: Speak with backend and actually do changes there.
 */

public class User implements Serializable{

    private int ID;
    private String Username;
    private String Password;
    private String Photo;
    private int Points;
    private ArrayList<Group> Groups;

    public User(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public User(String username, String password, String photo, int points) {
        this.Username = username;
        this.Password = password;
        this.Photo = photo;
        this.Points = points;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        this.Points = points;
    }

    public ArrayList<Group> getGroups() {
        return Groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.Groups = groups;
    }

    /**
     * Adds a group to this allUsers list of Groups
     * @param group The group to add
     * @return true if the group was added successfully, false otherwise
     */
    public boolean addGroup(Group group) {
        if(!Groups.contains(group)) {
            Groups.add(group);
            return true;
        }
        return false;
    }
}
