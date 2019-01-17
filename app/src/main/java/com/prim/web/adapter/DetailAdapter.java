package com.prim.web.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.web.R;
import com.prim.web.activity.WebDetailActivity;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019/1/11 - 4:17 PM
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.BaseViewHolder> {

    private List<WebDetailActivity.NewBodyBean> list;

    private Context context;

    public DetailAdapter(Context context, List<WebDetailActivity.NewBodyBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: position --> ");
        if (viewType == 0) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_detail_title, parent, false);
            return new TitleViewHolder(inflate);
        } else if (viewType == 1) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_detail_title, parent, false);
            return new WebViewHolder(inflate);
        } else if (viewType == 2) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_comment_layout, parent, false);
            return new CommentViewHolder(inflate);
        }
        return null;
    }

    private static final String TAG = "DetailAdapter";

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: position --> " + position);
        holder.setItem(list.get(position), position);
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getTyoe();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setItem(WebDetailActivity.NewBodyBean bodyBean, int position);
    }

    class TitleViewHolder extends BaseViewHolder {

        TextView tv_item_title;
        LinearLayout item_list;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_item_title = itemView.findViewById(R.id.tv_item_title);
            item_list = itemView.findViewById(R.id.item_list);
        }

        @Override
        public void setItem(WebDetailActivity.NewBodyBean bodyBean, final int position) {
            tv_item_title.setText(bodyBean.getTitle());
            List<String> data = bodyBean.getData();
            for (String datum : data) {
                TextView textView = new TextView(context);
                textView.setText(datum);
                textView.setPadding(10, 10, 10, 10);
                item_list.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            tv_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "我是推荐阅读啦，你点到我了！" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class WebViewHolder extends BaseViewHolder {

        TextView tv_item_title;

        public WebViewHolder(View itemView) {
            super(itemView);
            tv_item_title = itemView.findViewById(R.id.tv_item_title);
        }

        @Override
        public void setItem(WebDetailActivity.NewBodyBean bodyBean, final int position) {
            tv_item_title.setText(bodyBean.getTitle());
            tv_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "我是广告啦！你点到我了！" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class CommentViewHolder extends BaseViewHolder {

        TextView tv_item_comment_content;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tv_item_comment_content = itemView.findViewById(R.id.tv_item_comment_content);
        }

        @Override
        public void setItem(WebDetailActivity.NewBodyBean bodyBean, final int position) {
            tv_item_comment_content.setText("评论内容:你点我啊 " + bodyBean.getComments().get(0));
            tv_item_comment_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "哎呀，你点到我了！" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
