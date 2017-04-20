package com.xy.code.recycleradapter;

import android.animation.Animator;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.xy.code.recycleradapter.base.AnimatorConstrctor;
import com.xy.code.recycleradapter.base.ItemViewHolder;
import com.xy.code.recycleradapter.base.ViewBinder;
import com.xy.code.recycleradapter.listener.OnItemClickListener;
import com.xy.code.recycleradapter.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by android on 2017/3/14.
 */

public class FlexbleRecyclerAdapter<T> extends RecyclerView.Adapter<ItemViewHolder> {

    private static final String TAG = FlexbleRecyclerAdapter.class.getSimpleName();

    private Builder<T> builder;

    private ViewListenerImpl viewListener;

    private FlexbleRecyclerAdapter() {

    }

    public FlexbleRecyclerAdapter(Builder builder) {
        this.builder = builder;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(builder.getItemLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        builder.getViewBinder().onBindView(holder, getItem(position));
        attachListners(holder);
    }

    public T getItem(int position) {
        return (T) builder.getData().get(position);
    }

    @Override
    public int getItemCount() {
        return builder.getData() != null ? builder.getData().size() : 0;
    }

    public void removeItem(int position) {
        builder.getData().remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(T data) {
        builder.getData().add(data);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int postion, T data) {
        builder.getData().add(postion, data);
        notifyItemInserted(postion);
    }

    private void attachListners(ItemViewHolder viewHolder) {

        if (builder.getOnItemClickListener() != null) {
            viewHolder.itemView.setOnClickListener(viewListener == null ? new ViewListenerImpl(this, viewHolder) : viewListener);
        }
        if (builder.getOnItemLongClickListener() != null) {
            viewHolder.itemView.setOnLongClickListener(viewListener == null ? new ViewListenerImpl(this, viewHolder) : viewListener);
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

    private void createAndStartItemInAnimation(ItemViewHolder holder) {
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
    }

    public Builder<T> getBuilder() {
        return builder;
    }

    public static class Builder<T> {

        private ViewBinder mViewBinder;

        private int mItemLayoutId;

        private List<T> mData;

        private OnItemClickListener<T> mOnItemClickListener;

        private OnItemLongClickListener<T> mOnItemLongClickListener;

        private AnimatorConstrctor mAnimatorInConstrctor;

        private AnimatorConstrctor mAnimatorOutConstrctor;


        public Builder itemLayoutId(@LayoutRes int itemLayoutId) {
            mItemLayoutId = itemLayoutId;
            return this;
        }

        protected int getItemLayoutId() {
            return mItemLayoutId;
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

        public Builder itemOutAnimator(AnimatorConstrctor animatorConstrctor) {
            this.mAnimatorOutConstrctor = animatorConstrctor;
            return this;
        }

        public Builder viewBinder(ViewBinder viewBinder) {
            this.mViewBinder = viewBinder;
            return this;
        }

        protected ViewBinder getViewBinder() {
            return mViewBinder;
        }

        public Builder onItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
            return this;
        }

        protected OnItemClickListener getOnItemClickListener() {
            return mOnItemClickListener;
        }

        public Builder onItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
            this.mOnItemLongClickListener = onItemLongClickListener;
            return this;
        }

        protected OnItemLongClickListener getOnItemLongClickListener() {
            return mOnItemLongClickListener;
        }

        public FlexbleRecyclerAdapter build() {
            return new FlexbleRecyclerAdapter(this);
        }
    }

    private class ViewListenerImpl implements View.OnClickListener, View.OnLongClickListener {

        FlexbleRecyclerAdapter adapter;
        ItemViewHolder viewHolder;

        public ViewListenerImpl(FlexbleRecyclerAdapter adapter, ItemViewHolder viewHolder) {
            this.adapter = adapter;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            adapter.getBuilder().getOnItemClickListener().onItemClick(v, viewHolder.getAdapterPosition(), adapter.getItem(viewHolder.getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View v) {
            return adapter.getBuilder().getOnItemLongClickListener().onLongClick(v, viewHolder.getAdapterPosition(), adapter.getItem(viewHolder.getAdapterPosition()));
        }
    }

}
