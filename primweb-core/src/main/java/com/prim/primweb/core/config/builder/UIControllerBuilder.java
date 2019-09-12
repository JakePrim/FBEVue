package com.prim.primweb.core.config.builder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author prim
 * @version 1.0.0
 * @desc webView的UI控制器设置
 * 包括错误页面的设置和加载页面的设置
 * @time 2019-08-26 - 18:19
 */
public class UIControllerBuilder {

    private PrimBuilder primBuilder;

    public UIControllerBuilder(PrimBuilder primBuilder) {
        this.primBuilder = primBuilder;
    }

    //----------------------- 1.1.3 之后的API，建议使用此API --------------------------//

    //设置PrimWeb的父view
    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp) {
        primBuilder.mViewGroup = v;
        primBuilder.mLayoutParams = lp;
        return this;
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v) {
        primBuilder.mViewGroup = v;
        primBuilder.mLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT);
        return this;
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull ViewGroup.LayoutParams lp, int index) {
        primBuilder.mViewGroup = v;
        primBuilder.mLayoutParams = lp;
        primBuilder.mIndex = index;
        return this;
    }

    public UIControllerBuilder setWebParent(@NonNull ViewGroup v, @NonNull int index) {
        primBuilder.mViewGroup = v;
        primBuilder.mLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        primBuilder.mIndex = index;
        return this;
    }

    /**
     * 使用默认的UI
     *
     * @return 直接进入下一项配置
     */
    public PrimBuilder defaultUI() {
        return primBuilder;
    }

    /**
     * 设置错误UI配置
     *
     * @param errorLayout  xml id
     * @param errorClickId 发生错误时，点击某个view重新请求,传递view的ID即可
     */
    public UIControllerBuilder addCustomErrorUI(@LayoutRes int errorLayout, @IdRes int errorClickId) {
        this.primBuilder.errorLayout = errorLayout;
        this.primBuilder.errorClickId = errorClickId;
        return this;
    }

    /**
     * 设置errorUI
     *
     * @param errorView errorLayout id
     */
    public UIControllerBuilder addCustomErrorUI(@NonNull View errorView) {
        this.primBuilder.errorView = errorView;
        return this;
    }

    /**
     * 设置加载中的UI
     *
     * @param loadView
     * @return
     */
    public UIControllerBuilder addCustomLoadUI(@NonNull View loadView) {
        this.primBuilder.loadView = loadView;
        return this;
    }

    /**
     * 设置加载中的UI
     *
     * @param loadLayout
     * @return
     */
    public UIControllerBuilder addCustomLoadUI(@NonNull int loadLayout) {
        this.primBuilder.loadLayout = loadLayout;
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
    public IndicatorBuilder useDefaultUI() {
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 设置errorUI 和 loadUI
     *
     * @param errorLayout  errorLayout id
     * @param loadLayout   loadLayout id
     * @param errorClickId 发生错误时，点击某个view重新请求,传递view的ID即可
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder useCustomUI(@LayoutRes int errorLayout, @LayoutRes int loadLayout, @IdRes int errorClickId) {
        this.primBuilder.errorLayout = errorLayout;
        this.primBuilder.loadLayout = loadLayout;
        this.primBuilder.errorClickId = errorClickId;
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 设置错误和加载中的view，可直接传递view
     *
     * @param errorView 错误的view
     * @param loadView  加载中的view
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder useCustomUI(@NonNull View errorView, @NonNull View loadView) {
        this.primBuilder.errorView = errorView;
        this.primBuilder.loadView = loadView;
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 仅设置错误UI配置
     *
     * @param errorLayout  xml id
     * @param errorClickId 发生错误时，点击某个view重新请求,传递view的ID即可
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder useOnlyCustomErrorUI(@LayoutRes int errorLayout, @IdRes int errorClickId) {
        this.primBuilder.errorLayout = errorLayout;
        this.primBuilder.errorClickId = errorClickId;
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 仅设置错误UI配置
     *
     * @param errorView 错误UI的View
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder useOnlyCustomErrorUI(@NonNull View errorView) {
        this.primBuilder.errorView = errorView;
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 仅设置加载UI配置
     *
     * @param loadLayout xml
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder userOnlyCustomLoadUI(@LayoutRes int loadLayout) {
        this.primBuilder.loadLayout = loadLayout;
        return new IndicatorBuilder(primBuilder);
    }

    /**
     * 仅设置加载UI配置
     *
     * @param loadView view
     * @return addIndicatorBuilder 进入指示器配置
     */
    public IndicatorBuilder userOnlyCustomLoadUI(@NonNull View loadView) {
        this.primBuilder.loadView = loadView;
        return new IndicatorBuilder(primBuilder);
    }
}
