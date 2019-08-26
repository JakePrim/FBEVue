package com.prim.primweb.core.config;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.uicontroller.BaseIndicatorView;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019-08-26 - 18:37
 */
public class IndicatorBuilder {
    private PrimBuilder primBuilder;

    public IndicatorBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    public CommonBuilder useDefaultTopIndicator(boolean isTopIndicator) {
        if (isTopIndicator) {
            this.primBuilder.needTopIndicator = true;
        } else {
            this.primBuilder.needTopIndicator = false;
            this.primBuilder.customTopIndicator = false;
            this.primBuilder.height = 0;
        }
        return new CommonBuilder(primBuilder);
    }

    public CommonBuilder useDefaultTopIndicator(@ColorInt int color) {
        this.primBuilder.needTopIndicator = true;
        this.primBuilder.mColor = color;
        return new CommonBuilder(primBuilder);
    }

    public CommonBuilder useDefaultTopIndicator(boolean isTopIndicator, @ColorInt int color) {
        if (isTopIndicator) {
            this.primBuilder.needTopIndicator = true;
            this.primBuilder.mColor = color;
        } else {
            this.primBuilder.needTopIndicator = false;
            this.primBuilder.customTopIndicator = false;
            this.primBuilder.height = 0;
        }
        return new CommonBuilder(primBuilder);
    }

    public CommonBuilder useDefaultTopIndicator(@ColorInt int color, int height) {
        this.primBuilder.needTopIndicator = true;
        this.primBuilder.mColor = color;
        this.primBuilder.height = height;
        return new CommonBuilder(primBuilder);
    }

    public CommonBuilder useDefaultTopIndicator(@NonNull String color) {
        this.primBuilder.needTopIndicator = true;
        this.primBuilder.colorPaser = color;
        return new CommonBuilder(primBuilder);
    }

    public CommonBuilder useDefaultTopIndicator(@NonNull String color, int height) {
        this.primBuilder.height = height;
        this.primBuilder.needTopIndicator = true;
        this.primBuilder.colorPaser = color;
        return new CommonBuilder(primBuilder);
    }

    /**
     * 使用自定义的进度指示器 注意需要继承{@link BaseIndicatorView}
     *
     * @param indicatorView 自定义的指示器view
     * @return CommonBuilder
     */
    public CommonBuilder useCustomTopIndicator(@NonNull BaseIndicatorView indicatorView) {
        this.primBuilder.mIndicatorView = indicatorView;
        this.primBuilder.needTopIndicator = true;
        this.primBuilder.customTopIndicator = true;
        return new CommonBuilder(primBuilder);
    }

    /**
     * 关闭进度指示器
     *
     * @return CommonBuilder
     */
    public CommonBuilder colseTopIndicator() {
        this.primBuilder.needTopIndicator = false;
        this.primBuilder.customTopIndicator = false;
        this.primBuilder.height = 0;
        return new CommonBuilder(primBuilder);
    }
}
