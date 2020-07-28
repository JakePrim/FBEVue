package com.prim.kit_ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * @author prim
 * @version 1.0.0
 * @desc tab bottom 的实体类信息
 * @time 2020/7/28 - 5:07 PM
 * @param <Color> 颜色可扩展 int string
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class KitTabBottomInfo<Color> {
    /**
     * Tab类型可以包括:Bitmap Icon Lottie动画
     */
    public enum TabType{
        BITMAP,ICON,LOTTIE
    }

    /**
     * tab的Fragment 用于创建fragment实例
     */
    public Class<? extends Fragment> fragment;
    /**
     * tab name
     */
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;
    public String iconFont;

    public String defaultIconName;
    public String selectedIconName;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public KitTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public KitTabBottomInfo(String name, String iconFont, String defaultIconName, String selectedIconName, Color defaultColor, Color tintColor, TabType tabType) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }
}
