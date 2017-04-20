package com.xy.code.recycleradapter.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by android on 2017/4/20.
 */

public class AnimatorExtractor {

    public static final int SLIDE_IN_LEFT = 4;
    public static final int SLIDE_IN_BOTTOM = 2;
    public static final int SLIDE_IN_RIGHT = 6;
    public static final int SLIDE_IN_TOP = 8;

    public static Animator[] extractScaleInAnimator(View v) {
        return extractScaleInAnimator(v, 0.5F);
    }

    public static Animator[] extractScaleInAnimator(View v, float from) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", from, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", from, 1f);
        return new Animator[]{scaleX, scaleY};
    }

    public static Animator[] extractAlphaInAnimator(View v) {
        return extractAlphaInAnimator(v, 0F);
    }

    public static Animator[] extractAlphaInAnimator(View v, float from) {
        return new Animator[]{ObjectAnimator.ofFloat(v, "alpha", from, 1f)};
    }

    public static Animator[] extractSlideInAnimator(View v,int slideInType){
        Animator animator = null;
        switch (slideInType){
            case SLIDE_IN_LEFT:
                animator = ObjectAnimator.ofFloat(v, "translationX", -v.getMeasuredWidth(), 0);
                break;
            case SLIDE_IN_BOTTOM:
                animator = ObjectAnimator.ofFloat(v, "translationY", v.getMeasuredHeight(), 0);
                break;
            case SLIDE_IN_RIGHT:
                animator = ObjectAnimator.ofFloat(v, "translationX", v.getMeasuredWidth(), 0);
                break;
            case SLIDE_IN_TOP:
                animator = ObjectAnimator.ofFloat(v, "translationY", -v.getMeasuredHeight(), 0);
                break;
        }
        if(animator != null){
            return new Animator[]{animator};
        }
        return null;
    }

}
