package no.uib.info331.models;

import no.uib.info331.R;


/**
 * Model for views that are to be included in CustomPagerAdapter.java
 *
 * @author Edvard P. Bjørgen
 */

public enum CustomPagerEnum {

    CREATE_GROUP(R.string.groups, R.layout.pager_select_create_group),
    JOIN_GROUP(R.string.action_join_group, R.layout.pager_select_join_group);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}