package no.uib.info331.models;

/**
 * Class for modelling a user.
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class User {
    private String username;
    private String password;
    private String photo;
    private int points;
    private Group[] groups;

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

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
}
