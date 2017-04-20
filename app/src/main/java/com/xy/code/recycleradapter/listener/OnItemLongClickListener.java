package com.xy.code.recycleradapter.listener;

import android.view.View;

/**
 * Created by android on 2017/3/14.
 */

public interface OnItemLongClickListener<T> {
    boolean onLongClick(View v, int position, T data);
}
