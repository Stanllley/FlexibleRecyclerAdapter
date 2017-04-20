package com.xy.code.recycleradapter.base;

/**
 * Created by android on 2017/3/14.
 */

public abstract class ViewBinder<T> {

   public abstract void onBindView(ItemViewHolder holder, T data);

}
