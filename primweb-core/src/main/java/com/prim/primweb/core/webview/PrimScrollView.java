package com.prim.primweb.core.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;

import com.prim.primweb.core.R;
import com.prim.primweb.core.utils.PWLog;
import com.prim.primweb.core.utils.ViewUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author prim
 * @version 1.0.0
 * @desc 自定义ScrollView 主要用于H5于原生混合使用 嵌套WebView ListView 等
 * @time 2019/1/11 - 6:59 PM
 */
public class PrimScrollView extends ViewGroup {

    private static final String TAG = "PrimScrollView";
    //子view的数量
    private int mChildrenSize = 0;
    private Scroller mScroller;
    private float mLastY;
    private VelocityTracker mVelocityTracker;
    private int mMinimumVelocity;
    private int maxScrollY;
    private boolean isTouched;
    private int oldScrollY;
    private int oldWebViewScrollY;
    private int mTouchSlop;
    private IDetailWebView detailWebView;
    private IDetailListView commentListView;
    private MyScrollBarShowListener scrollBarShowListener;
    private MyOnScrollChangeListener onScrollChangeListener;
    public static final int DIRECT_BOTTOM = 1;
    public static final int DIRECT_TOP = -1;
    private boolean isIntercept = false;
    private View mWebView;
    private int mWebHeight = 0;
    private int mListHeight = 0;
    private int mOtherHeight = 0;

    public PrimScrollView(Context context) {
        this(context, null);
    }

    public PrimScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrimScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PrimScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();
        scrollBarShowListener = new MyScrollBarShowListener();
        onScrollChangeListener = new MyOnScrollChangeListener();
        //初始化滚动条
        boolean hasScrollBarVerticalThumb = false;//有的手机没有滚动条，开启滚动条的话会崩溃
        try {
            TypedArray a = context.obtainStyledAttributes(R.styleable.View);
            Method method = View.class.getDeclaredMethod("initializeScrollbars", TypedArray.class);
            method.setAccessible(true);
            method.invoke(this, a);
            a.recycle();
            Field field = View.class.getDeclaredField("mScrollCache");
            field.setAccessible(true);
            Object mScrollCache = field.get(this);
            if (mScrollCache != null) {
                Field scrollbarField = mScrollCache.getClass().getDeclaredField("scrollBar");
                scrollbarField.setAccessible(true);
                Object scrollBar = scrollbarField.get(mScrollCache);
                Field verticalThumbField = scrollBar.getClass().getDeclaredField("mVerticalThumb");
                verticalThumbField.setAccessible(true);
                Object verticalThumb = verticalThumbField.get(scrollBar);
                hasScrollBarVerticalThumb = verticalThumb != null;//有滚动条才显示滚动条
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有滚动条
        if (hasScrollBarVerticalThumb) {
            //Define whether the vertical scrollbar should be drawn or not. The scrollbar is not drawn by default.
            setVerticalScrollBarEnabled(true);
            //定义当视图不滚动时滚动条是否会淡出 android:fadeScrollbars
            setScrollbarFadingEnabled(true);
            //awakenScrollBars的时候会调用invalidate，设置这个为false，可以使invalidate调用draw方法，从而达到画进度条
            setWillNotDraw(false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof IDetailListView) {
                commentListView = (IDetailListView) childAt;
            } else if (childAt instanceof IDetailWebView) {
                detailWebView = (IDetailWebView) childAt;
            }
        }
        if (commentListView != null) {
            commentListView.setScrollView(this);
            commentListView.setOnScrollBarShowListener(scrollBarShowListener);
            commentListView.setOnDetailScrollChangeListener(onScrollChangeListener);
        }
        if (detailWebView != null) {
            detailWebView.setScrollView(this);
            detailWebView.setOnScrollBarShowListener(scrollBarShowListener);
            detailWebView.setOnDetailScrollChangeListener(onScrollChangeListener);
            observeWebViewHeightChange();
        }
    }

    /**
     * 监听WebView的高度是否发生变化
     */
    private void observeWebViewHeightChange() {
        if (detailWebView instanceof IAgentWebView) {
            mWebView = ((IAgentWebView) detailWebView).getAgentWebView();
            mWebView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                private int mOldWebViewContentHeight;

                private Rect outRect = new Rect();

                @Override
                public boolean onPreDraw() {
                    PWLog.e(TAG + ".onPreDraw...update");
                    mWebView.getGlobalVisibleRect(outRect);
                    int distBottom = PrimScrollView.this.getHeight() - outRect.height();
                    //if判断代码有问题 不能及时的更新webView的高度
//                    if (distBottom <= 0
////                            || PrimScrollView.this.getScrollY() == 0 //没触发滑动的时候不扩展
//                            || mWebView.getHeight() < PrimScrollView.this.getHeight()) {//webview的高度本省就小于ScrollView
//                        return true;
//                    }
                    int newWebViewContentHeight = ((IAgentWebView) detailWebView).getAgentContentHeight();
                    if (mOldWebViewContentHeight == newWebViewContentHeight) {//新的高度等于老的高度不改变
                        return true;
                    }
                    mOldWebViewContentHeight = newWebViewContentHeight;
                    setWebHeight(mWebView.getMeasuredHeight() + distBottom);
                    mWebView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
    }

    public void setWebHeight(int height) {
        LayoutParams layoutParams = mWebView.getLayoutParams();
        layoutParams.height = height;
        mWebView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //final 设置变量不可变 编译时有助于提高性能
        final int childCount = getChildCount();
        final int parentLeft = getPaddingLeft();
        int parentBottom = getPaddingTop();
        //获取当前父view的高度
        final int parentHeight = b - t;
        mChildrenSize = childCount;
        maxScrollY = 0;
        mWebHeight = 0;
        mListHeight = 0;
        mOtherHeight = 0;
        PWLog.e(TAG + ".onLayout...parentBottom+" + parentBottom);
        //遍历子view 设置子view的高度和宽度
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);//获得子view
            if (childAt.getVisibility() != GONE) {
                final MarginLayoutParams lp = (MarginLayoutParams) childAt.getLayoutParams();
                int height = childAt.getMeasuredHeight();//子 view的高度
                final int width = childAt.getMeasuredWidth();//子view的宽度
                if (height == 0) {
                    height = parentHeight;//默认webView高度等于ScrollView高度
                }
                int childLeft = parentLeft + lp.leftMargin;
                //距离顶部的距离
                int childTop = parentBottom + lp.topMargin;
                //设置子 view的layout
                PWLog.e(TAG + ".onLayout...layout:" + "\n"
                        + childAt.getClass().getName() + " height:" + height + "\n"
                        + " ,top:" + childTop + "\n"
                        + " ,bottom" + (childTop + height));
                childAt.layout(childLeft, childTop, childLeft + width, childTop + height);
                //改变 parentBottom 下一个view的位置在当前子view的下方 改变顶部的距离
                parentBottom = childTop + height + lp.bottomMargin;
                maxScrollY += lp.topMargin;
                maxScrollY += lp.bottomMargin;
                if (childAt instanceof IDetailWebView) {
                    mWebHeight = height;
                    PWLog.e(TAG + ".onLayout...mWebHeight:" + mWebHeight);
                } else if (childAt instanceof IDetailListView) {
                    mListHeight = height;
                    PWLog.e(TAG + ".onLayout...mListHeight:" + mListHeight);
                } else {
                    mOtherHeight += height;
                    PWLog.e(TAG + ".onLayout...mOtherHeight:" + mOtherHeight);
                }
            }
        }
        //计算最大滑动区域
        maxScrollY += mWebHeight + mListHeight + mOtherHeight - parentHeight;
        PWLog.e(TAG + ".onLayout...maxScrollY:" + maxScrollY);
        if (maxScrollY < 0) {
            maxScrollY = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //onMeasure 在onLayout之前
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            //如果子View也为ViewGroup则先测量子View
            measureChildWithMargins(childAt, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //重新生成子view的LayoutParams -> MarginLayoutParams extends ViewGroup.LayoutParams
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        isTouched = (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE);
        float y = event.getY();
        boolean isAtTop = getScrollY() >= maxScrollY;//判断是否滑动到头部了
        boolean isAtBottom = getScrollY() <= 0;//判断是否滑动到底部了
        acquireVelocityTracker(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                //按下停止滚动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float delta = y - mLastY;
                //计算移动的距离
                int dY = adjustScrollY((int) (-delta));
                PWLog.e(TAG + ".onTouchEvent.Move----dY = " + dY + "\n"
                        + ",delta=" + delta + "\n"
                        + ",isAtTop=" + isAtTop + "\n"
                        + ",isBottom=" + isAtBottom + "\n"
                        + ",maxScrollY=" + maxScrollY + "\n"
                        + ",getScaleY=" + getScrollY());
                if (dY != 0) {//下滑操作
                    if (commentListView != null && commentListView.canScrollVertically(DIRECT_TOP) && isAtTop) {////因为ListView上滑操作导致ListView可以继续下滑，故要先ListView滑到顶部，再滑动MyScrollView
                        commentListView.customScrollBy((int) -delta);
                        PWLog.e(TAG + ".onTouchEvent dY != 0.commentListView.customScrollBy...交给listview 滚动delta:" + delta);
                    } else if (detailWebView != null && detailWebView.canScrollVertically(DIRECT_BOTTOM) && isAtBottom) {////因为WebView下滑，导致WebView可以继续上滑，故要先让WebView滑到底部，再滑动MyScrollView
                        detailWebView.customScrollBy((int) -delta);
                        PWLog.e(TAG + ".onTouchEvent dY != 0.commentListView.customScrollBy...交给listview 滚动delta:" + delta);
                    } else {//当listview处于顶部 webview处于底部时 滑动ScrollView
                        customScrollBy(dY);
//                        if (webToCommentListener != null) {
//                            webToCommentListener.onComment(false);
//                        }
                        PWLog.e(TAG + ".onTouchEvent dY != 0.customScrollBy...交给ScrollView 滚动adjustScrollY:" + dY);
                    }
                } else {//dY == 0表示滑动到顶部或者底部了
                    if (delta < 0 && isAtTop && commentListView != null) {//上滑操作
                        PWLog.e(TAG + ".onTouchEvent dY = 0.customScrollBy...交给listView 滚动delta:" + delta);
                        commentListView.customScrollBy((int) -delta);
//                        if (webToCommentListener != null) {
//                            webToCommentListener.onComment(true);
//                        }
                    } else if (delta > 0 && isAtBottom && detailWebView != null) {
                        detailWebView.customScrollBy((int) -delta);
                        PWLog.e(TAG + ".onTouchEvent dY = 0.customScrollBy...交给WebView 滚动delta:" + delta);
                    }
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                PWLog.e(TAG + ".ACTION_CANCEL..." + event.getY());
                //设置滑动速度
                final VelocityTracker velocityTracker = mVelocityTracker;
                //计算当前的速度
                velocityTracker.computeCurrentVelocity(1000);
                //获得当前Y轴的移动速度
                float yVelocity = velocityTracker.getYVelocity(0);
                if (Math.abs(yVelocity) > mMinimumVelocity) {
                    if (isAtTop) {
                        if (commentListView != null && commentListView.canScrollVertically(DIRECT_TOP)) {
                            commentListView.startFling((int) -yVelocity);
                        }
                    } else if (isAtBottom) {
                        if (detailWebView != null && detailWebView.canScrollVertically(DIRECT_BOTTOM)) {
                            detailWebView.startFling((int) -yVelocity);
                        }
                    } else {
                        fling((int) -yVelocity);
                    }
                }
                releaseVelocityTracker();
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getChildCount() < 2) {//子view的数量小于2个不进行拦截
            return false;
        }
        boolean isCanScrollBottom = getScrollY() < maxScrollY && mScroller.isFinished();//滑动的Y的距离小于最大的滑动距离 并且 滑动动画已完成才允许滑动
        boolean isCanScrollTop = getScrollY() > 0 && mScroller.isFinished();//滑动的Y的距离大于0 如果小于或等于0 则表示已经滑动到底部了
        boolean isWebViewScrollBottom = detailWebView != null && detailWebView.canScrollVertically(DIRECT_BOTTOM);//判断web是否继续向下滑//
        boolean isListViewScrollTop = commentListView != null && commentListView.canScrollVertically(DIRECT_TOP);//listview 是否可以继续向上滑动
        int action = ev.getAction();
        acquireVelocityTracker(ev);
        switch (action & MotionEvent.ACTION_MASK) {//ACTION_MASK应用于多点触摸操作可以处理 ACTION_POINTER_DOWN ACTION_POINTER_UP 而ACTION_DOWN ACTION_UP是单点触摸操作
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getY();
                //滚动状态下点击屏幕的处理
                isIntercept = !mScroller.isFinished();//如果正在滚动进行拦截停止
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) ev.getY();
                int dY = (int) (y - mLastY);
                if (Math.abs(dY) < mTouchSlop) {//如果移动的距离小于最小的滑动距离则不处理
                    return false;
                }
                if (dY < 0) {//向下滚动
                    if (isTouchPointInView((View) detailWebView, ev)) {
                        isIntercept = !isWebViewScrollBottom && isCanScrollBottom;//如果触摸的是webView webview可以向下滚动 则滚动交给webview自己处理 如果webView 不能向下滚动则进行拦截
                        PWLog.e(TAG + ".onInterceptTouchEvent...down...detailWebView -> " + isIntercept);
                    } else if (isTouchPointInView((View) commentListView, ev)) {
                        isIntercept = isCanScrollBottom;
                        PWLog.e(TAG + ".onInterceptTouchEvent...down...commentListView -> " + isIntercept);
                    } else {
                        isIntercept = false;
                        PWLog.e(TAG + ".onInterceptTouchEvent...down...scrollview -> " + isIntercept);
                    }
                } else if (dY > 0) {//向上滚动
                    if (isTouchPointInView((View) detailWebView, ev)) {
                        isIntercept = isCanScrollTop;
                        PWLog.e(TAG + ".onInterceptTouchEvent...up...detailWebView -> " + isIntercept);
                    } else if (isTouchPointInView((View) commentListView, ev)) {
                        isIntercept = !isListViewScrollTop && isCanScrollTop;
                        PWLog.e(TAG + ".onInterceptTouchEvent...up...commentListView -> " + isIntercept);
                    } else {
                        isIntercept = false;
                        PWLog.e(TAG + ".onInterceptTouchEvent...up...scrollview -> " + isIntercept);
                    }
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                releaseVelocityTracker();
                break;
        }
        PWLog.e(TAG + ".onInterceptTouchEvent...isIntercept:" + isIntercept);
        return isIntercept;
    }


    //滑动速度跟踪
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean isTouchPointInView(View child, MotionEvent ev) {
        if (child == null) return false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        final int scrollY = getScrollY();
        return !(y < child.getTop() - scrollY
                || y >= child.getBottom() - scrollY
                || x < child.getLeft()
                || x >= child.getRight());
    }

    public void customScrollBy(int dy) {
        int oldY = getScrollY();
        scrollBy(0, dy);
//        LogE(TAG + ".customScrollBy.......oldY=" + oldY + ",getScrollY()=" + getScrollY());
        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), oldY);
    }

    public int adjustScrollY(int delta) {
        int dy;
        if (delta + getScrollY() >= maxScrollY) {
            dy = maxScrollY - getScrollY();
        } else if ((delta + getScrollY()) <= 0) {
            dy = -getScrollY();
        } else {
            dy = delta;
        }
        PWLog.e(TAG + ".adjustScrollY...finally...dy=" + dy + ",delta=" + delta);
        return dy;
    }

    public void fling(int velocity) {
        PWLog.e("ScrollView fling...." + velocity + "\n"
                + ",mScroller.isFinished()=" + mScroller.isFinished() + "\n"
                + "isTouched=" + isTouched);
        if (isTouched)//当webview不能继续向下滑的时候，继续下拉会触发scrollView下滑，此时webview不能再响应dispatchTouchEvent事件，scrollView响应onTouch事件，然后由scrollView去判断是否是isTouched
            return;
        if (!mScroller.isFinished())
            return;
        int minY = -detailWebView.customGetContentHeight();
        PWLog.e("ScrollView do fling...." + velocity);
        mScroller.fling(getScrollX(), getScrollY(), 0, velocity, 0, 0, minY, computeVerticalScrollRange());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        if (direction > 0) {
            return getScrollY() > 0;
        } else {
            return getScrollY() < maxScrollY;
        }
    }

    private int getCappedCurVelocity() {
        return (int) this.mScroller.getCurrVelocity();
    }

    @Override
    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            super.computeScroll();
            return;
        }
        int oldX = getScrollX();
        int oldY = getScrollY();
        int currX = mScroller.getCurrX();
        int currY = mScroller.getCurrY();
        int curVelocity = getCappedCurVelocity();
        if (currY < oldY && oldY <= 0) {
            if (curVelocity != 0) {
                this.mScroller.forceFinished(true);
                this.detailWebView.startFling(-curVelocity);
                return;
            }
        } else if (currY > oldY && oldY >= maxScrollY && curVelocity != 0 && commentListView != null && commentListView.startFling(curVelocity)) {
            mScroller.forceFinished(true);
            return;
        }
        int toY = Math.max(0, Math.min(currY, maxScrollY));
        if (oldX != currX || oldY != currY) {
            scrollTo(currX, toY);
        }
        if (!awakenScrollBars()) {//这句一定要执行，否则会导致mScroller永远不会finish，从而导致一些莫名其妙的bug
            ViewCompat.postInvalidateOnAnimation(this);
        }
        super.computeScroll();
    }

    /**
     * 显示区域的高度
     *
     * @return
     */
    @Override
    protected int computeVerticalScrollExtent() {
        try {
            int webExtent = ViewUtils.computeVerticalScrollExtent((View) detailWebView);
            int listExtent = ViewUtils.computeVerticalScrollExtent((View) commentListView);
            return webExtent + listExtent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.computeVerticalScrollExtent();
    }

    /**
     * 已经向下滚动的距离，为0时表示已处于顶部。
     *
     * @return
     */
    @Override
    protected int computeVerticalScrollOffset() {
        try {
            int webOffset = ViewUtils.computeVerticalScrollOffset((View) detailWebView);
            int listOffset = ViewUtils.computeVerticalScrollOffset((View) commentListView);
            return webOffset + getScrollY() + listOffset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.computeVerticalScrollOffset();
    }

    /**
     * 整体的高度，注意是整体，包括在显示区域之外的。
     *
     * @return
     */
    @Override
    protected int computeVerticalScrollRange() {
        try {
            int webScrollRange = detailWebView.customComputeVerticalScrollRange();
            int listScrollRange = ViewUtils.computeVerticalScrollRange((View) commentListView);
            return webScrollRange + maxScrollY + listScrollRange;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.computeVerticalScrollRange();
    }

    /**
     * 跳转到列表区域，如果已经在列表区域，则跳回去，如果滚动动画没有结束，则无响应
     *
     * @return false 说明跳转到了评论列表 true 说明跳转到了正文
     */
    public boolean toggleScrollToListView() {
        if (!mScroller.isFinished()) {
            return false;
        }
        final boolean isWebComment;
        View webView = (View) detailWebView;
        int webHeight = webView.getHeight() - webView.getPaddingTop() - webView.getPaddingBottom();
        int dy;
        int webViewToY;
        int scrollY = getScrollY();
        if (scrollY >= maxScrollY) {//不是toggle模式才回到原来的位置
            isWebComment = false;
            dy = oldScrollY - maxScrollY;
            webViewToY = oldWebViewScrollY;
        } else {
            isWebComment = true;
            dy = maxScrollY - scrollY;
            oldScrollY = scrollY;
            //存储当前web的位置
            oldWebViewScrollY = detailWebView.customGetWebScrollY();
            webViewToY = detailWebView.customComputeVerticalScrollRange() - webHeight;
        }
        //评论永远保持在第一个
        commentListView.scrollToFirst();
        //同时滚动webView
        detailWebView.customScrollTo(webViewToY);
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy);
        ViewCompat.postInvalidateOnAnimation(this);
        return isWebComment;
    }

    /**
     * 强制滑动列表区域
     */
    public void forceScrollToListView() {
        int dy = maxScrollY - getScrollY();
        View webView = (View) mWebView;
        int webHeight = webView.getHeight() - webView.getPaddingTop() - webView.getPaddingBottom();
        int webViewToY = detailWebView.customComputeVerticalScrollRange() - webHeight;
        commentListView.scrollToFirst();
        detailWebView.customScrollTo(webViewToY);
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 回到顶部
     */
    public void scrollToTop() {
        detailWebView.customScrollTo(0);
        mScroller.startScroll(getScrollX(), getScrollY(), 0, -getScrollY());
        commentListView.scrollToFirst();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public interface OnScrollBarShowListener {
        void onShow();
    }

    private class MyScrollBarShowListener implements OnScrollBarShowListener {

        private long mOldTimeMills;

        @Override
        public void onShow() {
            long timeMills = AnimationUtils.currentAnimationTimeMillis();
            if (timeMills - mOldTimeMills > 16) {
                mOldTimeMills = timeMills;
                awakenScrollBars();
            }
        }
    }

    private OnScrollWebToCommentListener webToCommentListener;

    public interface OnScrollWebToCommentListener {
        void onComment(boolean isComment);
    }

    public void setOnScrollWebToCommentListener(OnScrollWebToCommentListener webToCommentListener) {
        this.webToCommentListener = webToCommentListener;
    }

    public enum ViewType {
        WEB,
        COMMENT,
        OTHER
    }

    public interface OnScrollChangeListener {
        void onChange(ViewType viewType);
    }

    private class MyOnScrollChangeListener implements OnScrollChangeListener {

        @Override
        public void onChange(ViewType viewType) {
            switch (viewType) {
                case WEB:
                    if (webToCommentListener != null) {
                        webToCommentListener.onComment(false);
                    }
                    break;
                case COMMENT:
                    if (webToCommentListener != null) {
                        webToCommentListener.onComment(true);
                    }
                    break;
                case OTHER:
                    if (webToCommentListener != null) {
                        webToCommentListener.onComment(false);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            if (t >= maxScrollY) {
                onScrollChangeListener.onChange(ViewType.COMMENT);
            } else {
                onScrollChangeListener.onChange(ViewType.OTHER);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
