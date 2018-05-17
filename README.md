# PrimWeb
![下载.png](https://upload-images.jianshu.io/upload_images/2005932-f696f5226030a1f5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


PrimWeb 是一个基于的 Android WebView 和 腾讯 x5 WebView，容易、灵活使用以及功能非常强大的库，提供了 WebView 一系列的问题解决方案 ，并且轻量和灵活，
更方便 webview 的切换, bug 再也不用担心webview出问题了,库已经默认的实现了webSetting  WebViewClient WebChromeClient,如果没有特殊的项目需求,以下是最简单的调用方式.
```
primWeb = PrimWeb.with(this)
                .setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .setWebViewType(PrimWeb.WebViewType.X5)
                .setModeType(PrimWeb.ModeType.Normal)
                .addJavascriptInterface("nativeBridge", new MyJavaObject())
                .buildWeb()
                .readyOk()
                .launch("http://front.52yingzheng.com/test/shiluTest/h5-standard/h5-standard.html");
 ```

## TODO
1. webview UI--> 进度、加载和错误UI设置
2. webview下载文件

## FINISH
v2.0.0
1. 添加权限管理，常用的定位、相册的权限
2. 可以上传文件
3. 添加回退键的处理
4. 判断js方法是否在H5页面存在,处理特殊的情况

v1.1.0

1. webview 安全漏洞的问题修复
2. 代理WebChormeClient 兼容android webview 和 x5 webview
3. 添加webview的生命周期管理

v1.0.0

1. 第一次提交



### API 详解
#### 0.webView经常内存泄漏？以后不会再有内存泄漏了,动态的new webview太麻烦？你只需要一句代码，然后去喝一杯咖啡吧
```
.setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

内存泄漏不用担心，已经在内部处理了
    @Override
    public void destroy() {
        removeAllViewsInLayout();
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {//从父容器中移除webview
            ((ViewGroup) parent).removeAllViewsInLayout();
        }
        releaseConfigCallback();
        super.destroy();
    }
```

#### 1.现流行的腾讯x5 webview, 很火，但是总会有一些限制，不想用换起来很麻烦？ 一行代码动态切换 X5和Android 的webview
```
 public enum WebViewType {
        Android, X5
    }
//使用库中默认的webview
.setWebViewType(PrimWeb.WebViewType.X5)

//使用自定义的webview 内部会自动判断使用的是android 或者 x5 的webview
.setAgentWebView(new X5AgentWebView(this))
```

#### 2.Javascript调Java? 可以addJavascriptInterface 多个,具体请看 SafeJsInterface
```
.addJavascriptInterface("jsAgent", new MyJavaObject())
//设置严格模式或标准模式Strict - 严格的模式：api小于17 禁止注入js,大于 17 注入js的对象所有方法必须都包含JavascriptInterface注解
//Normal - 为正常模式
.setModeType(PrimWeb.ModeType.Normal)

如果用严格模式 以下js 脚本注入不正确
/** 注入js脚本 */
public class MyJavaObject {

        @JavascriptInterface
        public void login(String data) {

        }

        public void medth() {

        }

    }
```

#### 3.调用Javascript方法拼接太麻烦？请看方便安全的加载js方法可传多个参数，具体请看 SafeCallJsLoaderImpl
```
primWeb.getCallJsLoader().callJS("jsMethod");


//可传多个参数，可使用高级的API
@RequiresApi(Build.VERSION_CODES.KITKAT)
void callJs(String method, AgentValueCallback<String> callback, String... params);
@RequiresApi(Build.VERSION_CODES.KITKAT)
void callJs(String method, AgentValueCallback<String> callback);
void callJS(String method, String... params);
void callJS(String method);
```

#### 4.灵活的设置webview WebSetting，我们可以多个setting 换着来 如：X5DefaultWebSetting 继承 BaseAgentWebSetting类
```
.setAgentWebSetting(new X5DefaultWebSetting(this))

public class X5DefaultWebSetting extends BaseAgentWebSetting<WebSettings> {
    private Context context;
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    public X5DefaultWebSetting(Context context) {
        this.context = context;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void toSetting(WebSettings webSetting) {
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            webSetting.setAllowFileAccessFromFileURLs(false);
            // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            webSetting.setAllowUniversalAccessFromFileURLs(false);
        }
        }
       ......
   }
```

#### 5.灵活的设置 setWebViewClient 使用代理的WebViewClient 兼容android webview 和 x5 webview，但是只兼容了一部分的方法，多数已经适用于项目的使用,无法做到全面兼容
```
.setAgentWebViewClient(new MyWebViewClient(this))

/** 使用代理的WebViewClient */
public class MyWebViewClient extends WebViewClient {
        MyWebViewClient(Context context) {
            super(context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
```

如果不想使用代理的方法? 可以使用以下API，调用android和x5 系统自带的类，当然不兼容 android webview 和 x5 webview，只能分开使用
```
.setAndroidWebViewClient(new ...)
.setX5WebViewClient(new ...)
```

#### 6.代理WebChormeClient 兼容android webview 和 x5 webview, 兼容的部分方法适用于项目开发
注意代理的WebChromeClient 需要传递一个泛型 FileChooserParams 原因是android 和 x5 是不一样的
```
 .setAgentWebChromeClient(new AgentWebChromeClient(this))

 public class AgentWebChromeClient extends WebChromeClient<android.webkit.WebChromeClient.FileChooserParams> {

        public AgentWebChromeClient(Context context) {
            super(context);
        }

        @Override
        public void onProgressChanged(View webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }
```
如果不想使用代理的方法? 可以使用以下API，但是不兼容android webview 和 x5 webview 需要使用哪个webview 需要自己实现相应的方法

```
setAndroidWebChromeClient(new ...)
setX5WebChromeClient(new ...)
```

#### 7.灵活安全的加载url,具体可以看UrlLoader

```
 primWeb.getUrlLoader().loadUrl();
 primWeb.getUrlLoader().reload();
 primWeb.getUrlLoader().stopLoading();
```

#### 8.控制webview的生命周期
```
    @Override
    protected void onResume() {
        super.onResume();
        primWeb.webLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        primWeb.webLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        primWeb.webLifeCycle().onDestory();
    }
```

#### 9.判断js方法是否存在？不存在需要做特殊的处理？ 当然可以注入多个js脚本
```
.addJavascriptInterface("checkJsBridge", new MyJavaObject())

primWeb.getCallJsLoader().checkJsMethod("returnBackHandles");

public class MyJavaObject {
        @JavascriptInterface
        public void jsFunctionExit() {
            Log.e(TAG, "jsFunctionExit: JS 方法存在");
        }

        @JavascriptInterface
        public void jsFunctionNo() {
            Log.e(TAG, "jsFunctionNo: JS 方法不存在 ");
        }
    }
```
内部是这样处理的,我自己写了一个js方法，来专门判断, 具体请看BaseCallJsLoader
```
 @Override
    public void checkJsMethod(String method) {
        StringBuilder sb = new StringBuilder();
        sb.append("function checkJsFunction(){ if(typeof ")
                .append(method)
                .append(" != \"undefined\" && typeof ")
                .append(method)
                .append(" == \"function\")")
                .append("{console.log(\"")
                .append(method)
                .append("\");")
                .append("checkJsBridge['jsFunctionExit']();")
                .append("}else{")
                .append("if(typeof checkJsBridge == \"undefined\") return false;")
                .append("checkJsBridge['jsFunctionNo']();}}");
        call("javascript:" + sb.toString() + ";checkJsFunction()", null);
    }
```