package com.xy.code.recycleradapter;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xy.code.recycleradapter.base.AnimatorConstrctor;
import com.xy.code.recycleradapter.base.ViewBinder;
import com.xy.code.recycleradapter.listener.OnFooterViewClickListener;
import com.xy.code.recycleradapter.listener.OnHeaderViewClickListener;
import com.xy.code.recycleradapter.listener.OnItemClickListener;
import com.xy.code.recycleradapter.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by android on 2017/3/14.
 */

public class FlexbleRecyclerAdapter<T> extends RecyclerView.Adapter<FlexbleRecyclerAdapter.ItemViewHolder> {

    private static final String TAG = FlexbleRecyclerAdapter.class.getSimpleName();

    public static final int ITEM_TYPE_HEADER = 0;

    public static final int ITEM_TYPE_FOOTER = 1;

    public static final int ITEM_TYPE_DATA = 2;


    private Builder<T> builder;

    private ViewListenerImpl viewListener;

    private int mLastAttachedPosition = -1;

    private List<Integer> mExtendViewShowingPositions;


    private FlexbleRecyclerAdapter() {

    }

    public FlexbleRecyclerAdapter(Builder<T> builder) {
        this.builder = builder;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                itemView = inflater.inflate(builder.mHeaderViewId, parent, false);
                break;
            case ITEM_TYPE_DATA:
                itemView = inflater.inflate(builder.mItemLayoutId, parent, false);
                break;
            case ITEM_TYPE_FOOTER:
                itemView = inflater.inflate(builder.mFooterViewId, parent, false);
                break;
        }
        frameLayout.addView(itemView);
        return new ItemViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_HEADER) {
            builder.mViewBinder.onBindHeaderView(holder);
        }
        if (getItemViewType(position) == ITEM_TYPE_DATA) {
            builder.mViewBinder.onBindView(holder, getItem(convertPosition(position)));
        }
        if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            builder.mViewBinder.onBindFooterView(holder);
        }
        attachListners(holder);
    }

    public T getItem(int position) {
        return getData().get(position);
    }

    public List<T> getData() {
        return builder.getData();
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (hasHeader()) {
            count++;
        }
        if (hasFooter()) {
            count++;
        }
        return getDataCount() + count;
    }

    public int getDataCount() {
        return getData() != null ? getData().size() : 0;
    }


    public boolean hasHeader() {
        return builder.mHeaderViewId > 0;
    }

    public boolean hasFooter() {
        return builder.mFooterViewId > 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (hasFooter() && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_DATA;
    }

    public void removeHeaderView() {
        if (hasHeader()) {
            builder.headerView(-1);
            notifyItemRemoved(0);
        }
    }

    public void removeFooterView() {
        if (hasFooter()) {
            builder.footerView(-1);
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    public void removeItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }
        getData().remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(T data) {
        getData().add(data);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int postion, T data) {
        getData().add(postion, data);
        notifyItemInserted(postion);
    }

    private void attachListners(ItemViewHolder viewHolder) {
        ViewListenerImpl listener = new ViewListenerImpl(this, viewHolder);
        if (builder.mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(listener);
        }
        if (builder.mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(listener);
        }
        if (hasHeader() && builder.mOnHeaderViewClickListener == null && viewHolder.getAdapterPosition() == 0) {
            viewHolder.itemView.setOnClickListener(null);
        }
        if (hasFooter() && builder.mOnFooterViewClickListener == null && viewHolder.getAdapterPosition() == getItemCount() - 1) {
            viewHolder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        createAndStartItemInAnimation(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void createAndStartItemInAnimation(ItemViewHolder holder) {
        if (holder.getAdapterPosition() <= mLastAttachedPosition) {
            return;
        }
        if (builder.mAnimatorInConstrctor != null) {
            Animator[] animators = builder.mAnimatorInConstrctor.createAnimator(holder, holder.getLayoutPosition());
            if (animators != null) {
                for (Animator animator : animators) {
                    animator.setDuration(300);
                    animator.start();
                    animator.setInterpolator(new LinearInterpolator());
                }
            }
        }
        mLastAttachedPosition = holder.getAdapterPosition() > mLastAttachedPosition ? holder.getAdapterPosition() : mLastAttachedPosition;
    }

    public Builder<T> getBuilder() {
        return builder;
    }

    public static class Builder<T> {

        private ViewBinder mViewBinder;

        private int mItemLayoutId;

        private int mHeaderViewId;

        private int mFooterViewId;

        private List<T> mData;

        private OnItemClickListener<T> mOnItemClickListener;

        private OnItemLongClickListener<T> mOnItemLongClickListener;

        private OnHeaderViewClickListener mOnHeaderViewClickListener;

        private OnFooterViewClickListener mOnFooterViewClickListener;

        private AnimatorConstrctor mAnimatorInConstrctor;


        public Builder itemLayoutId(@LayoutRes int itemLayoutId) {
            mItemLayoutId = itemLayoutId;
            return this;
        }


        public Builder data(List<T> data) {
            this.mData = data;
            return this;
        }

        protected List<T> getData() {
            return mData;
        }

        public Builder itemInAnimator(AnimatorConstrctor animatorConstrctor) {
            this.mAnimatorInConstrctor = animatorConstrctor;
            return this;
        }

        public Builder viewBinder(ViewBinder viewBinder) {
            this.mViewBinder = viewBinder;
            return this;
        }

        public Builder headerView(@LayoutRes int layoutId) {
            this.mHeaderViewId = layoutId;
            return this;
        }

        public Builder footerView(@LayoutRes int layoutId) {
            this.mFooterViewId = layoutId;
            return this;
        }

        public Builder onHeaderViewCLickListener(OnHeaderViewClickListener onHeaderViewClickListener) {
            this.mOnHeaderViewClickListener = onHeaderViewClickListener;
            return this;
        }

        public Builder onFooterViewCLickListener(OnFooterViewClickListener onFooterViewClickListener) {
            this.mOnFooterViewClickListener = onFooterViewClickListener;
            return this;
        }

        public Builder onItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
            return this;
        }

        public Builder onItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.mOnItemLongClickListener = onItemLongClickListener;
            return this;
        }

        public FlexbleRecyclerAdapter<T> build() {
            return new FlexbleRecyclerAdapter<T>(this);
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

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

        public ItemViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener onClickListener) {
            getView(viewId).setOnClickListener(onClickListener);
            return this;
        }

        public ItemViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener onTouchListener) {
            getView(viewId).setOnTouchListener(onTouchListener);
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

    private class ViewListenerImpl implements View.OnClickListener, View.OnLongClickListener {

        FlexbleRecyclerAdapter adapter;
        ItemViewHolder viewHolder;

        ViewListenerImpl(FlexbleRecyclerAdapter adapter, ItemViewHolder viewHolder) {
            this.adapter = adapter;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            if (hasHeader() && viewHolder.getAdapterPosition() == 0) {
                adapter.getBuilder().mOnHeaderViewClickListener.onClick(v);
                return;
            }
            if (hasFooter() && viewHolder.getAdapterPosition() == getItemCount() - 1) {
                adapter.getBuilder().mOnFooterViewClickListener.onCLick(v);
                return;
            }
            adapter.getBuilder().mOnItemClickListener.onItemClick(v, viewHolder.getAdapterPosition(), adapter.getItem(convertPosition(viewHolder.getAdapterPosition())));
        }

        @Override
        public boolean onLongClick(View v) {
            return adapter.getBuilder().mOnItemLongClickListener.onLongClick(v, viewHolder.getAdapterPosition(), adapter.getItem(convertPosition(viewHolder.getAdapterPosition())));
        }
    }

    private int convertPosition(int position) {
        if (hasHeader() && position > 0) {
            return position - 1;
        }
        return position;
    }

}
