package com.prim.kit_ui.tab.common;

/**
 * @author prim
 * @version 1.0.0
 * @desc KitTab 对外的接口
 * @time 2020/7/28 - 5:05 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public interface KitTab<D> extends KitTabLayout.OnTabSelectedListener<D>{
    void setTabInfo(D data);

    /**
     * 动态修改某个item的大小
     * @param height
     */
    void resetHeight(int height);
}
