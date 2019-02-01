package com.prim.web.activity;

import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.webview.FullyLinearLayoutManager;
import com.prim.primweb.core.webview.MyLinearLayoutManger;
import com.prim.primweb.core.webview.PrimScrollView;
import com.prim.primweb.core.webview.X5AgentWebView;
import com.prim.web.R;
import com.prim.web.adapter.DetailAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019/1/11 - 4:04 PM
 */
public class WebDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<NewBodyBean> newBodyBeanList;
    private List<NewBodyBean> testBeanList;
    DetailAdapter adapter;
    DetailAdapter testAdapter;
    private PrimScrollView scrollView;
    private TextView tv_comment, tv_comment_position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail);
        recyclerView = findViewById(R.id.recyclerView);
        scrollView = findViewById(R.id.scrollView);
        tv_comment = findViewById(R.id.tv_comment);
        tv_comment_position = findViewById(R.id.tv_comment_position);
        PrimWeb.with(this)
                .setWebParent(scrollView, 0)
                .useDefaultUI()
                .useDefaultTopIndicator()
                .setWebViewType(PrimWeb.WebViewType.X5)
                .buildWeb()
                .lastGo()
                .launch("https://www.toutiao.com/a6647258207351734787/");

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(WebDetailActivity.this));
                newBodyBeanList = new ArrayList<>();
                loadData();
                adapter = new DetailAdapter(WebDetailActivity.this, newBodyBeanList);
                testAdapter = new DetailAdapter(WebDetailActivity.this, testBeanList);
                recyclerView.setAdapter(adapter);
            }
        }, 2000);

        scrollView.setOnScrollWebToCommentListener(new PrimScrollView.OnScrollWebToCommentListener() {
            @Override
            public void onComment(boolean isComment) {
                if (!isComment) {
                    tv_comment.setText("评论");
                } else {
                    tv_comment.setText("正文");
                }
            }
        });
    }

    /**
     * 所有的数据都在一个接口中
     */
    private void loadData() {
        testBeanList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        //模拟评论盖楼
        for (int i = 0; i < 20; i++) {
            list.add("推荐阅读:" + i);
        }
        //模拟详情页接口
        for (int i = 0; i < 2; i++) {
            List<String> comments = new ArrayList<>();
            comments.add("comment::" + i);
            NewBodyBean newBodyBean = null;
//            if (i == 0) {
//                newBodyBean = new NewBodyBean(0, "推荐阅读", list, "", null);
//            } else if (i == 1) {
//                newBodyBean = new NewBodyBean(1, "广告", null, "", null);
//            } else {
            newBodyBean = new NewBodyBean(2, "test" + i, null, "url" + i, comments);
//            }
            newBodyBeanList.add(newBodyBean);
        }
    }

    public void comment(View view) {
        boolean b = scrollView.toggleScrollToListView();
        if (b) {
            tv_comment.setText("评论");
        } else {
            tv_comment.setText("正文");
        }
    }

    public void commentPosition(View view) {
        scrollView.scrollToCommentListView(5);
    }

    public void zanClick(View view) {
        Toast.makeText(this, "赞了哦", Toast.LENGTH_SHORT).show();
    }

    public void wxClick(View view) {
        Toast.makeText(this, "分享到微信了哦", Toast.LENGTH_SHORT).show();
    }

    public void wxCClick(View view) {
        Toast.makeText(this, "分享到朋友圈了哦", Toast.LENGTH_SHORT).show();
    }

    public static class NewBodyBean {
        private int tyoe;
        private String title;
        private List<String> data;
        private List<String> comments;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public NewBodyBean(int tyoe, String title, List<String> data, String url, List<String> comments) {
            this.tyoe = tyoe;
            this.title = title;
            this.data = data;
            this.comments = comments;
            this.url = url;
        }

        public List<String> getComments() {
            return comments;
        }

        public void setComments(List<String> comments) {
            this.comments = comments;
        }

        public int getTyoe() {
            return tyoe;
        }

        public void setTyoe(int tyoe) {
            this.tyoe = tyoe;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }
}
