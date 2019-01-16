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
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_web_layout, parent, false);
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

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_item_title = itemView.findViewById(R.id.tv_item_title);
        }

        @Override
        public void setItem(WebDetailActivity.NewBodyBean bodyBean, final int position) {
            tv_item_title.setText("评论内容:你点我啊 "+bodyBean.getTitle());
            tv_item_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "你点到我了！" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class WebViewHolder extends BaseViewHolder {

        private FrameLayout webParent;

        private android.webkit.WebView mWebView;

        public WebViewHolder(View itemView) {
            super(itemView);
            webParent = itemView.findViewById(R.id.webParent);
            mWebView = itemView.findViewById(R.id.mWebView);
            //支持javascript
            mWebView.getSettings().setJavaScriptEnabled(true);
            // 设置可以支持缩放
            mWebView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            mWebView.getSettings().setBuiltInZoomControls(true);
            //扩大比例的缩放
            mWebView.getSettings().setUseWideViewPort(true);
            //自适应屏幕
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
        }

        @Override
        public void setItem(WebDetailActivity.NewBodyBean bodyBean, int position) {
            //TODO 初始化WebView 占用一定的时间
//            PrimWeb.with((Activity) context)
//                    .setWebParent(webParent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//                    .useDefaultUI()
//                    .useDefaultTopIndicator().setWebViewType(PrimWeb.WebViewType.X5)
//                    .buildWeb().launch(bodyBean.getUrl());
            mWebView.loadUrl(bodyBean.getUrl());
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
            tv_item_comment_content.setText(bodyBean.getComments().get(0));
            tv_item_comment_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "哎呀，你点到我了！" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
