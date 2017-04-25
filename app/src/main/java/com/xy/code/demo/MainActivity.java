package com.xy.code.demo;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xy.code.recycleradapter.FlexbleRecyclerAdapter;
import com.xy.code.recycleradapter.FlexbleRecyclerAdapter.ItemViewHolder;
import com.xy.code.recycleradapter.animator.AnimatorExtractor;
import com.xy.code.recycleradapter.base.AnimatorConstrctor;
import com.xy.code.recycleradapter.base.ViewBinder;
import com.xy.code.recycleradapter.listener.OnItemClickListener;
import com.xy.code.recycleradapter.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView listView;
    FlexbleRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter = new FlexbleRecyclerAdapter.Builder<String>()
                .data(getData())
                .itemLayoutId(R.layout.item_layout)
                .itemInAnimator(new AnimatorConstrctor() {
                    @Override
                    public Animator[] createAnimator(ItemViewHolder viewHolder, int postion) {
                        return AnimatorExtractor.extractSlideInAnimator(viewHolder.itemView, AnimatorExtractor.SLIDE_IN_BOTTOM);
                    }
                })
                .footerView(R.layout.footer_layout)
                .headerView(R.layout.header_layout)
                .onItemClickListener(new OnItemClickListener<String>() {
                    @Override
                    public void onItemClick(View v, int postion, String data) {
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                    }
                })
                .onItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onLongClick(View v, int position, Object data) {
                        adapter.removeItem(position);
                        return true;
                    }
                })
                .viewBinder(new ViewBinder<String>() {
                    @Override
                    public void onBindView(final ItemViewHolder holder, String data) {
                        Log.i("onBindView",holder.getAdapterPosition()+"");
                        holder.setText(R.id.tv_text, data);
                        holder.setTextColorResource(R.id.tv_text, R.color.colorAccent);
                    }
                })
                .build());
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

}
