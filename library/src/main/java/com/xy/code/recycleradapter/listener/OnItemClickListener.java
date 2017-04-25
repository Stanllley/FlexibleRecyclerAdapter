package com.xy.code.recycleradapter.listener;

import android.view.View;

/**
 * Created by android on 2017/3/14.
 */

public interface OnItemClickListener<T> {

    void onItemClick(View v, int postion, T data);

}
