package com.prim.alibrary.log.printer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.prim.alibrary.R;
import com.prim.alibrary.log.LogBean;
import com.prim.alibrary.log.LogConfig;
import com.prim.alibrary.log.LogType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc 将log显示在界面上 可视化显示
 * @time 2020/7/28 - 3:23 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class ViewPrinter implements LogPrinter {

    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private ViewPrinterProvider provider;

    public ViewPrinter(Activity activity) {
        //将日志信息显示在content布局上
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        this.recyclerView = new RecyclerView(activity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.logAdapter = new LogAdapter(activity.getLayoutInflater());
        this.recyclerView.setAdapter(logAdapter);
        provider = new ViewPrinterProvider(rootView,recyclerView);
    }

    /**
     * 暴露provider 外部可以调用内部的方法
     * @return
     */
    public ViewPrinterProvider getViewProvider(){
        return provider;
    }

    @Override
    public void print(@NonNull LogConfig config, int level, String tag, String content) {
        logAdapter.addItem(new LogBean(System.currentTimeMillis(),level,tag,content));
        //滚动到log显示的最新位置
        recyclerView.smoothScrollToPosition(logAdapter.getItemCount() - 1);
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder>{
        private LayoutInflater inflater;
        private List<LogBean> logs = new ArrayList<>();
        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        public void addItem(LogBean logBean){
            logs.add(logBean);
            notifyItemInserted(logs.size() - 1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = inflater.inflate(R.layout.log_item_layout,parent,false);
            return new LogViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            LogBean logBean = logs.get(position);
            int color = getLogLevelColor(logBean.level);
            holder.tagView.setTextColor(color);
            holder.messageView.setTextColor(color);
            holder.tagView.setText(logBean.getFlattened());
            holder.messageView.setText(logBean.log);
        }

        private int getLogLevelColor(int logLevel){
            int highlight;
            switch (logLevel){
                case LogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case LogType.D:
                    highlight = 0xffffffff;
                    break;
                case LogType.I:
                    highlight = 0xff6a8759;
                    break;
                case LogType.W:
                    highlight = 0xffbbb529;
                    break;
                case LogType.E:
                    highlight = 0xffff6b68;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;

            }
            return highlight;
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder{
        public TextView tagView;
        public TextView messageView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            messageView = itemView.findViewById(R.id.message);
        }
    }
}
