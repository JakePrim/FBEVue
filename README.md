# PrimWeb
PrimWeb 是一个基于的 Android WebView 和 腾讯 x5 WebView，极度容易使用以及功能强大的库，提供了 WebView 一系列的问题解决方案 ，并且轻量和极度灵活，
更方便 webview 切换, 库已经默认的实现了webSetting  WebViewClient WebChromeClient,如果没有特殊的项目需求,一下是最简单的调用方式.
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
0. webview生命周期管理，及缓存的清理
1. webview的安全设置，及 webview 安全注入js脚本
2. webview UI--> 加载和错误UI设置
3. webview上传文件，及权限设置
4. webview下载文件

## FINISH
5/16

1. webview 安全漏洞的问题修复
2. 代理WebChormeClient 兼容android webview 和 x5 webview

5/15

1. 第一次提交

### API 详解
1. 动态切换X5和Android 的webview
```
动态的设置webview的父view
.setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
 public enum WebViewType {
        Android, X5
    }
//使用库中默认的webview
.setWebViewType(PrimWeb.WebViewType.X5)

//使用自定义的webview 注意如果使用自定义的view
.setAgentWebView(new X5AgentWebView(this))
```
2. 动态的注入JS脚本 具体请看 SafeJsInterface
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
3. 方便安全的加载js方法 ，具体请看 SafeCallJsLoaderImpl
```
primWeb.getCallJsLoader().callJS("jsMethod");
```

4. 灵活的设置webview WebSetting，如：X5DefaultWebSetting 继承 BaseAgentWebSetting类
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

5. 灵活的设置 setWebViewClient 使用代理的WebViewClient 兼容android webview 和 x5 webview，但是只兼容了一部分的方法，已经适用于项目的使用,无法做到全面兼容
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
如果不想使用代理的方法，可以使用以下API，但是不兼容android webview 和 x5 webview 需要使用哪个webview 需要自己实现相应的方法
```
.setAndroidWebViewClient(new ...)
.setX5WebViewClient(new ...)
```

6. 代理WebChormeClient 兼容android webview 和 x5 webview, 兼容的部分方法适用于项目开发
```
 setAgentWebChromeClient(IAgentWebChromeClient agentWebChromeClient)
```
如果不想使用代理的方法，可以使用以下API，但是不兼容android webview 和 x5 webview 需要使用哪个webview 需要自己实现相应的方法

```
setAndroidWebChromeClient(new ...)
setX5WebChromeClient(new ...)
```

7. 灵活安全的加载url,具体可以看UrlLoader
```
 primWeb.getUrlLoader().loadUrl();
 primWeb.getUrlLoader().reload();
 primWeb.getUrlLoader().stopLoading();
```