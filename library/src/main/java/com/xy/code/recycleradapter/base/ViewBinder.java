package com.xy.code.recycleradapter.base;

import com.xy.code.recycleradapter.FlexbleRecyclerAdapter.ItemViewHolder;

/**
 * Created by android on 2017/3/14.
 */

public abstract class ViewBinder<T> {

   public abstract void onBindView(ItemViewHolder holder,T data);

   public void onBindHeaderView(ItemViewHolder holder){}

   public void onBindFooterView(ItemViewHolder holder){}

}
