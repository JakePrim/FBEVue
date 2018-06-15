package com.prim.web.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.prim.web.R;
import com.prim.web.listener.OnItemClickListener;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    private PrimAdapter adapter;

    private static final String[] data = new String[]{
            "Activity 使用PrimWeb",
            "Fragment 使用PrimWeb",
            "Java 调用 Js通信",
            "Js 调用 Java通信",
            "input标签文件上传",
            "自定义进度条",
            "识别电话、邮件、短信",
            "自定义WebSetting设置",
            "下拉回弹(仿微信的效果)",
            "下拉刷新",
            "感谢 AgentWeb 开源项目提供的思路"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new PrimAdapter(data, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL_LIST));
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, CommonActivity.class));
                break;
        }
    }

    public static class PrimAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private WeakReference<Context> reference;

        private String[] list;

        private OnItemClickListener onItemClickListener;

        public PrimAdapter(String[] list, Context context, OnItemClickListener onItemClickListener) {
            this.list = list;
            this.reference = new WeakReference<>(context);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(reference.get()).inflate(R.layout.item_layout, parent, false);
            return new PrimViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((PrimViewHolder) holder).update(position);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.length;
        }

        private class PrimViewHolder extends RecyclerView.ViewHolder {

            private TextView item_tv;

            private View itemView;

            public PrimViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                this.item_tv = (TextView) itemView.findViewById(R.id.item_tv);
            }

            public void update(final int position) {
                String s = list[position];
                item_tv.setText(s);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != onItemClickListener) {
                            onItemClickListener.itemClick(position);
                        }
                    }
                });
            }
        }
    }
}
