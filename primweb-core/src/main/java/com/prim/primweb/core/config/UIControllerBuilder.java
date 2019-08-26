package com.prim.primweb.core.config;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.prim.primweb.core.PrimWeb;

/**
 * @author prim
 * @version 1.0.0
 * @desc webview的UI控制器设置
 * 包括错误页面的设置和加载页面的设置
 * @time 2019-08-26 - 18:19
 */
public class UIControllerBuilder {
    private PrimBuilder primBuilder;

    public UIControllerBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    public IndicatorBuilder useDefaultUI() {
        return new IndicatorBuilder(primBuilder);
    }

    public IndicatorBuilder useCustomUI(@LayoutRes int errorLayout, @LayoutRes int loadLayout, @IdRes int errorClickId) {
        this.primBuilder.errorLayout = errorLayout;
        this.primBuilder.loadLayout = loadLayout;
        this.primBuilder.errorClickId = errorClickId;
        return new IndicatorBuilder(primBuilder);
    }

    public IndicatorBuilder useCustomUI(@LayoutRes int errorLayout, @IdRes int errorClickId) {
        this.primBuilder.errorLayout = errorLayout;
        this.primBuilder.errorClickId = errorClickId;
        return new IndicatorBuilder(primBuilder);
    }

    public IndicatorBuilder useCustomUI(@LayoutRes int errorLayout) {
        this.primBuilder.errorLayout = errorLayout;
        return new IndicatorBuilder(primBuilder);
    }

    public IndicatorBuilder useCustomUI(@NonNull View errorView, @NonNull View loadView) {
        this.primBuilder.errorView = errorView;
        this.primBuilder.loadView = loadView;
        return new IndicatorBuilder(primBuilder);
    }

    public IndicatorBuilder useCustomUI(@NonNull View errorView) {
        this.primBuilder.errorView = errorView;
        return new IndicatorBuilder(primBuilder);
    }
}
