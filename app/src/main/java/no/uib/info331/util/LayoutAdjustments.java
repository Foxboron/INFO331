package no.uib.info331.util;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class for general methods for adjusting layouts programmatically
 * @author Edvard P. Bjørgen
 */

public class LayoutAdjustments {
/**
* Set margins for view, beware, all of the params in this methods overwrites the margins set in XML-layout
* */
    public void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    /**
     * Method for getting the statusbar height. After API 24, the statusbar height is either defined by screen size or is a standard at 25 DPI, nobody really knows.
     * Therefore, it's better to retrieve the height directly from the API.
     * @param resource resources
     * @return
     */
    public int getStatusBarHeight(Resources resource) {
        int result = 0;
        int resourceId = resource.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resource.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
