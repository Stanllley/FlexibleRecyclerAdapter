package com.xy.code.recycleradapter.base;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by android on 2017/3/14.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private Context context;

    public ItemViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
        context = itemView.getContext();
    }

    public ItemViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ItemViewHolder setText(@IdRes int viewId, @StringRes int resid) {
        TextView textView = getView(viewId);
        textView.setText(resid);
        return this;
    }

    public ItemViewHolder setTextColor(@IdRes int viewId, @ColorInt int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    public ItemViewHolder setTextColor(@IdRes int viewId, ColorStateList color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    public ItemViewHolder setTextColorResource(@IdRes int viewId, @ColorRes int resid) {
        TextView textView = getView(viewId);
        textView.setTextColor(ContextCompat.getColor(context, resid));
        return this;
    }

    public ItemViewHolder setBackgroud(@IdRes int viewId, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getView(viewId).setBackground(drawable);
        } else {
            getView(viewId).setBackgroundDrawable(drawable);
        }
        return this;
    }

    public ItemViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    public ItemViewHolder setBackgroundColorResource(@IdRes int viewId, @ColorRes int colorRes) {
        getView(viewId).setBackgroundColor(ContextCompat.getColor(context, colorRes));
        return this;
    }

    public ItemViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int resid) {
        getView(viewId).setBackgroundResource(resid);
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View v = views.get(viewId);
        if (v == null) {
            v = super.itemView.findViewById(viewId);
            views.put(viewId, v);
        }
        return (T) v;
    }

}
