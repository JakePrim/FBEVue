package com.prim.alibrary.log.printer;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.prim.alibrary.utils.DisplayUtils;

/**
 * @author prim
 * @version 1.0.0
 * @desc ViewPrinter 的辅助类来控制view的显示和隐藏
 * @time 2020/7/28 - 3:46 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ViewPrinterProvider {
    private FrameLayout rootView;
    private View floatingView;
    private boolean isOpen;
    private FrameLayout logView;
    private RecyclerView recyclerView;

    public ViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        this.rootView = rootView;
        this.recyclerView = recyclerView;
    }

    /**
     * 定义两个TAG 来作为悬浮窗的显示和隐藏
     */
    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";

    public void showFloatingView(){
        if (rootView.findViewWithTag(TAG_FLOATING_VIEW) != null){
            //悬浮窗已经显示 直接返回
            return;
        }
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        //获取悬浮窗view
        View floatingView = getFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        layoutParams.bottomMargin = DisplayUtils.dp2px(100,rootView.getResources());
        rootView.addView(floatingView,layoutParams);
    }

    public void closeFloatingView(){
        rootView.removeView(getFloatingView());
    }

    /**
     * 获取悬浮窗view
     * @return
     */
    private View getFloatingView(){
        if (floatingView != null){
            return floatingView;
        }
        TextView textView = new TextView(rootView.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示日志信息view
                if (!isOpen){
                    showLogView();
                }
            }
        });
        textView.setText("ALog");
        return  floatingView = textView;
    }

    /**
     * 显示日志信息view
     */
    private void showLogView() {
        if (rootView.findViewWithTag(TAG_LOG_VIEW) != null){
            return;
        }
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(160,rootView.getResources()));
        layoutParams.gravity = Gravity.BOTTOM;
        View logView = getLogView();
        logView.setTag(TAG_LOG_VIEW);
        rootView.addView(logView,layoutParams);
        isOpen = true;
    }

    private View getLogView() {
        if (logView != null){
            return logView;
        }
        //日志显示控制台
        FrameLayout logView = new FrameLayout(rootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(recyclerView);

        //创建一个关闭按钮
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DisplayUtils.dp2px(160,rootView.getResources()));
        layoutParams.gravity = Gravity.END;
        TextView closeView = new TextView(rootView.getContext());
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogView();
            }
        });
        closeView.setText("Close");
        logView.addView(closeView,layoutParams);
        return this.logView = logView;
    }

    private void closeLogView() {
        isOpen = false;
        rootView.removeView(getLogView());
    }
}
