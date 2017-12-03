package no.uib.info331.models.messages;

import java.util.List;

import no.uib.info331.models.Group;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class GroupListEvent {
    private final List<Group> groupList;

    public GroupListEvent(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }
}
