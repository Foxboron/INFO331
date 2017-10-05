package no.uib.info331.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import no.uib.info331.R;

/**
 * Created by moled on 03.10.2017.
 */

public class Animations {

    public int getShortAnimTime(Context context) {
        return context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }
    public int getMediumAnimTime(Context context) {
        return context.getResources().getInteger(android.R.integer.config_mediumAnimTime);
    }
    public int getLongAnimTime(Context context) {
        return context.getResources().getInteger(android.R.integer.config_longAnimTime);
    }

    public void fadeInView(View view, int startDelay, int duration) {
        view.setAlpha(0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha",  0, 1f);
        fadeIn.setStartDelay(startDelay);
        fadeIn.setDuration(duration);
        fadeIn.start();
    }
    public void fadeOutView(View view, int startDelay, int duration) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha",  1f, 0);
        fadeIn.setStartDelay(startDelay);
        fadeIn.setDuration(duration);
        fadeIn.start();
    }

    public void moveViewToTranslationY(final View VIEW, int startDelay, int duration, int offset, final boolean goneOnAnimEnd) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(VIEW, "TranslationY", offset);
        fadeIn.setStartDelay(startDelay);
        fadeIn.setDuration(duration);
        fadeIn.start();
        fadeIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(!goneOnAnimEnd){
                    VIEW.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(goneOnAnimEnd){
                    VIEW.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void fadeBackgroundFromColorToColor(View view, int duration, int fromColor, int toColor){
        ColorDrawable[] color2 = {new ColorDrawable(fromColor), new ColorDrawable(toColor)};
        TransitionDrawable trans = new TransitionDrawable(color2);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        view.setBackground(trans);
        trans.startTransition(duration);

    }

}
