package com.prim.kit_ui.tab.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.prim.kit_ui.tab.common.KitTab;

/**
 * @author prim
 * @version 1.0.0
 * @desc 每一个tab的layout
 * @time 2020/7/28 - 5:41 PM
 * @contact https://jakeprim.cn
 * @name AKitDemo
 */
public class KitTabBottom extends RelativeLayout implements KitTab<KitTabBottomInfo<?>> {
    public KitTabBottom(Context context) {
        super(context);
    }

    public KitTabBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KitTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabInfo(KitTabBottomInfo<?> data) {

    }

    @Override
    public void resetHeight(int height) {

    }

    @Override
    public void onTabSelectedChange(int index, @NonNull KitTabBottomInfo<?> prevInfo, @NonNull KitTabBottomInfo<?> nextInfo) {

    }
}
