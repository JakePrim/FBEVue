package com.prim.kit_ui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author prim
 * @version 1.0.0
 * @desc TabLayout 接口
 * @param <Tab> TabView
 * @param <D> 数据
 * @time 2020/7/28 - 5:00 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public interface KitTabLayout<Tab extends ViewGroup,D> {
    /**
     * 查找tab
     * @param data
     * @return
     */
    Tab findTab(@NonNull D data);

    /**
     * 监听tab的点击事件
     */
    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    /**
     * 默认选中的tab
     * @param defaultInfo
     */
    void defaultSelected(@NonNull D defaultInfo);

    void inflateInfo(@NonNull List<D> infoList);

    interface OnTabSelectedListener<D>{
        /**
         *
         * @param index 当前tab 的索引
         * @param prevInfo 上一个tab
         * @param nextInfo 下一个tab
         */
        void onTabSelectedChange(int index,@NonNull D prevInfo,@NonNull D nextInfo);

        //TODO 扩展双击事件 监听手势事件 -- 双击
    }
}
