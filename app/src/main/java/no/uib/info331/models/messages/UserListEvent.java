package no.uib.info331.models.messages;

import java.util.List;

import no.uib.info331.models.User;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class UserListEvent {
    private final List<User> userList;

    public UserListEvent(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }
}
