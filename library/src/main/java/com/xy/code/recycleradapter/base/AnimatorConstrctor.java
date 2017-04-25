package com.xy.code.recycleradapter.base;

import android.animation.Animator;

import com.xy.code.recycleradapter.FlexbleRecyclerAdapter.ItemViewHolder;

/**
 * Created by android on 2017/4/19.
 */

public abstract class AnimatorConstrctor {

    public abstract Animator[] createAnimator(ItemViewHolder viewHolder, int postion);

}
