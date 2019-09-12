package com.prim.primweb.core.config.builder;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.prim.primweb.core.uicontroller.BaseIndicatorView;

/**
 * @author prim
 * @version 1.0.0
 * @desc 顶部进度条配置
 * @time 2019-08-26 - 18:37
 */
public class IndicatorBuilder {
    private PrimBuilder primBuilder;

    public IndicatorBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    private void enableNeed() {
        this.primBuilder.needTopIndicator = true;
    }

    /**
     * 是否使用指示器,主要用于动态切换,如果不用于动态切换，不使用指示器可直接调用colseIndicator()
     *
     * @param isUse true:使用指示器 false:不使用指示器
     * @return
     */
    public IndicatorBuilder isUseIndicator(boolean isUse) {
        if (isUse) {
            enableNeed();
        } else {
            this.primBuilder.needTopIndicator = false;
            this.primBuilder.customTopIndicator = false;
        }
        return this;
    }

    /**
     * 关闭进度指示器,
     */
    public IndicatorBuilder colseIndicator() {
        this.primBuilder.needTopIndicator = false;
        this.primBuilder.customTopIndicator = false;
        this.primBuilder.height = 0;
        return this;
    }

    /**
     * 使用默认的指示器
     */
    public IndicatorBuilder useDefaultIndicator() {
        enableNeed();
        return this;
    }

    /**
     * 使用默认的指示器，设置默认指示器的颜色
     * @param color 颜色id
     */
    public IndicatorBuilder useDefaultIndicator(@ColorInt int color) {
        this.primBuilder.mColor = color;
        enableNeed();
        return this;
    }

    /**
     * 使用默认的指示器，设置默认指示器的颜色
     * @param color 颜色码
     */
    public IndicatorBuilder useDefaultIndicator(@NonNull String color) {
        enableNeed();
        this.primBuilder.colorPaser = color;
        return this;
    }

    /**
     * 设置指示器的高度
     * @param height 高度
     */
    public IndicatorBuilder setIndicatorHeight(int height) {
        this.primBuilder.height = height;
        return this;
    }


    /**
     * 使用自定义的进度指示器 注意需要继承{@link BaseIndicatorView}
     *
     * @param indicatorView 自定义的指示器view
     */
    public IndicatorBuilder useCustomIndicator(@NonNull BaseIndicatorView indicatorView) {
        enableNeed();
        this.primBuilder.mIndicatorView = indicatorView;
        this.primBuilder.customTopIndicator = true;
        return this;
    }

    /**
     * 进行下一项配置
     *
     * @return PrimBuilder
     */
    public PrimBuilder next() {
        return primBuilder;
    }

    //-------------------------- 1.1.3 之前的API调用，此API调用不利于扩展，建议尽快修改，之后版本不在维护此API --------------//

    public WebBuilder useDefaultTopIndicator(boolean isTopIndicator) {
        if (isTopIndicator) {
            enableNeed();
        } else {
            this.primBuilder.needTopIndicator = false;
            this.primBuilder.customTopIndicator = false;
            this.primBuilder.height = 0;
        }
        return new WebBuilder(primBuilder);
    }

    public WebBuilder useDefaultTopIndicator(@ColorInt int color) {
        enableNeed();
        this.primBuilder.mColor = color;
        return new WebBuilder(primBuilder);
    }

    public WebBuilder useDefaultTopIndicator(boolean isTopIndicator, @ColorInt int color) {
        if (isTopIndicator) {
            enableNeed();
            this.primBuilder.mColor = color;
        } else {
            this.primBuilder.needTopIndicator = false;
            this.primBuilder.customTopIndicator = false;
            this.primBuilder.height = 0;
        }
        return new WebBuilder(primBuilder);
    }

    public WebBuilder useDefaultTopIndicator(@ColorInt int color, int height) {
        enableNeed();
        this.primBuilder.mColor = color;
        this.primBuilder.height = height;
        return new WebBuilder(primBuilder);
    }

    public WebBuilder useDefaultTopIndicator(@NonNull String color) {
        enableNeed();
        this.primBuilder.colorPaser = color;
        return new WebBuilder(primBuilder);
    }

    public WebBuilder useDefaultTopIndicator(@NonNull String color, int height) {
        this.primBuilder.height = height;
        enableNeed();
        this.primBuilder.colorPaser = color;
        return new WebBuilder(primBuilder);
    }

    /**
     * 使用自定义的进度指示器 注意需要继承{@link BaseIndicatorView}
     *
     * @param indicatorView 自定义的指示器view
     * @return addWebBuilder
     */
    public WebBuilder useCustomTopIndicator(@NonNull BaseIndicatorView indicatorView) {
        this.primBuilder.mIndicatorView = indicatorView;
        enableNeed();
        this.primBuilder.customTopIndicator = true;
        return new WebBuilder(primBuilder);
    }

    /**
     * 关闭进度指示器
     *
     * @return addWebBuilder
     */
    public WebBuilder colseTopIndicator() {
        this.primBuilder.needTopIndicator = false;
        this.primBuilder.customTopIndicator = false;
        this.primBuilder.height = 0;
        return new WebBuilder(primBuilder);
    }
}
