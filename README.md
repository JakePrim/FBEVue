# PrimWeb
![logo.png](https://upload-images.jianshu.io/upload_images/2005932-b8b696f850e844a2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
## PrimWeb is What?

PrimWeb 是一个代理的WebView基于的 Android WebView 和 腾讯 x5 WebView，容易、灵活使用以及功能非常强大的库，提供了 WebView 一系列的问题解决方案 ，并且轻量和灵活，
更方便 webview 的切换.

## What Support?

1. 支持动态添加WebView
2. 支持X5WebView 和 原生的WebView切换
3. 简化Js通信
4. 灵活的设置WebSetting
5. 代理WebViewClient 兼容 X5 WebView和android WebView
6. 代理WebChormeClient 兼容 android webview 和 x5 webview
7. 支持判断js方法是否存在
8. 支持input标签文件上传
9. 支持Js通信文件上传
9. 简化回退及返回键的处理
10. 简化url加载
11. webview 安全漏洞的问题修复,更加安全
12. 支持权限管理，常用的定位、相册的权限
13. 支持电话、短信、邮件的跳转
14. 支持自定义进度条指示器
15. 支持自定义错误页面
16. 支持跳转到其他应用页面

## How Do I Use ?

```
repositories {
    jcenter()
    maven{url 'https://dl.bintray.com/jakeprim/maven'}
}

dependencies {
    compile 'com.prim.lib:prim-web:1.0.2'
    //若无法依赖可以试试 末尾加上 @aar
    compile 'com.prim.lib:prim-web:1.0.2@aar'
}
```


## Update Log

- 1.0.1

1. 优化初始化
2. 添加详情页Web+原生混合功能
3. 优化部分代码


Activity调用PrimWeb | Fragment调用PrimWeb | 识别电话短信邮箱
---|---|---
![activity.gif](https://upload-images.jianshu.io/upload_images/2005932-297ecc349a263621.gif?imageMogr2/auto-orient/strip) | ![fragment.gif](https://upload-images.jianshu.io/upload_images/2005932-6a44ccaec767c20e.gif?imageMogr2/auto-orient/strip) |![sms.gif](https://upload-images.jianshu.io/upload_images/2005932-8c636d66d6628881.gif?imageMogr2/auto-orient/strip)



JS通信 | 文件上传 | 自定义错误页面
---|---|---
![js.gif](https://upload-images.jianshu.io/upload_images/2005932-74258121fbcd8b87.gif?imageMogr2/auto-orient/strip) | ![input.gif](https://upload-images.jianshu.io/upload_images/2005932-a268f8c2f1c268a7.gif?imageMogr2/auto-orient/strip) | ![error.gif](https://upload-images.jianshu.io/upload_images/2005932-5b8128c6a9831d50.gif?imageMogr2/auto-orient/strip)
## How Do I Use?

```
PrimWeb.with(getActivity())
                    .setWebParent(webParent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultUI()
                    .useDefaultTopIndicator()
                    .setWebViewType(PrimWeb.WebViewType.X5)
                    .setListenerCheckJsFunction(this)
                    .buildWeb()
                    .lastGo()
                    .launch(mParam1);
 ```

## Update Log

- v1.0.0

        完善功能
- v1.0.1

        1. 回退和返回键的简化处理
        2. 添加返回拦截，处理特殊情况
        3. 添加进度条指示器可自定义
        4. 添加错误页面可自定义
        5. 优化文件上传


## TODO

1. 实现刷新回弹功能
2. 实现JS通信文件上传
3. webview下载文件


### API 详解

#### 动态添加webView,防止内存泄漏
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

#### 动态切换 X5和Android 的webview
如果要是用x5的webview需要在application中调用此方法 PrimWeb.init(this); 初始化x5
```
//使用库中X5的webview
.setWebViewType(PrimWeb.WebViewType.X5)

//使用库中Android的webview
.setWebViewType(PrimWeb.WebViewType.Android)

public enum WebViewType {
       Android, X5
}
```

#### Javascript调Java? 可以addJavascriptInterface 多个,具体请看 SafeJsInterface
```
primWeb.getJsInterface().addJavaObject(new MyJavaObject(),"jsAgent")

/** 注入js脚本 */
public class MyJavaObject {

        @JavascriptInterface
        public void login(String data) {

        }

    }
```

#### Java调用Javascript方法,方便安全的加载js方法可传多个参数，具体请看 SafeCallJsLoaderImpl
```
primWeb.getCallJsLoader().callJS("jsMethod");
primWeb.getCallJsLoader().callJS("callByAndroidParam", 1234);
primWeb.getCallJsLoader().callJs("callByAndroidMoreParams", agentValueCallback, getJson(), "prim", true);


//可传多个参数，可使用高级的API
@RequiresApi(Build.VERSION_CODES.KITKAT)
void callJs(String method, AgentValueCallback<String> callback, String... params);
@RequiresApi(Build.VERSION_CODES.KITKAT)
void callJs(String method, AgentValueCallback<String> callback);
void callJS(String method, String... params);
void callJS(String method);
```

#### 灵活的设置WebSetting如：X5DefaultWebSetting 继承 BaseAgentWebSetting类 注意WebSettings 是X5 的 API
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

#### 设置 setWebViewClient 使用代理的WebViewClient 兼容android webview 和 x5 webview
```
.setAgentWebViewClient(agentWebViewClient)

AgentWebViewClient agentWebViewClient = new AgentWebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(IAgentWebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading: WebResourceRequest -->　" + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }
};
```

#### 代理WebChormeClient 兼容android webview 和 x5 webview
```
 .setAgentWebChromeClient(agentChromeClient)

AgentChromeClient agentChromeClient = new AgentChromeClient() {
        @Override
        public void onReceivedTitle(View webView, String s) {
            super.onReceivedTitle(webView, s);
            if (actionBar != null) {
                actionBar.setTitle(s);
            }
        }
    };
```

#### 设置进度指示器
```
如果想改变指示器的颜色可以调用如下:
.useDefaultTopIndicator(@ColorInt int color)
.useDefaultTopIndicator(@NonNull String color)

如果想改变指示器的高可以调用如下:
.useDefaultTopIndicator(@ColorInt int color, int height)

如果想自定义指示器可以调用如下:主要需要继承BaseIndicatorView
.useCustomTopIndicator(@NonNull BaseIndicatorView indicatorView)
```

#### 设置错误页面
```
不设置
.useDefaultUI()

可以设置布局的layout和点击的id
.useCustomUI(@LayoutRes int errorLayout, @IdRes int errorClickId)

可以设置为一个view
.useCustomUI(@NonNull View errorView)
```

#### 灵活安全的加载url,具体可以看UrlLoader

```
 primWeb.getUrlLoader().loadUrl();
 primWeb.getUrlLoader().reload();
 primWeb.getUrlLoader().stopLoading();
```

#### webview的生命周期
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

#### 判断js方法是否存在
```
     //设置监听
    .setListenerCheckJsFunction(this)

    //要检查的js方法
    primWeb.getCallJsLoader().checkJsMethod("returnBackHandles");

    @Override
    public void jsFunExit(Object data) {
        Toast.makeText(getActivity(), data.toString() + "方法存在", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void jsFunNoExit(Object data) {
        Toast.makeText(getActivity(), data.toString() + "方法不存在", Toast.LENGTH_SHORT).show();
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

#### webview 上传文件
```
PrimWeb库已经默认实现了文件上传

如果想禁止文件上传可以设置:
.setAllowUploadFile(false)

如果想使用第三方文件选择库上传文件设置:
.setUpdateInvokThrid(true)

同时具体的上传逻辑需要自己完成,具体的可以看Demo


@Override
    public void jsOpenVideos() {
        Log.e(TAG, "jsOpenVideos: ");
        PrimPicker.with(WebFragment.this)
                .choose(MimeType.ofVideo())
                .setMaxSelected(1)
                .setImageLoader(new MyImageLoad())
                .setShowSingleMediaType(true)
                .setCapture(false)
                .setSpanCount(3)
                .lastGo(1001);
    }

    @Override
    public void jsOpenPick() {
        PrimPicker.with(WebFragment.this)
                .choose(MimeType.ofImage())
                .setMaxSelected(1)
                .setImageLoader(new MyImageLoad())
                .setShowSingleMediaType(true)
                .setCapture(false)
                .setSpanCount(3)
                .lastGo(1001);
    }

      private ValueCallback<Uri[]> agentValueCallbacks;

    private ValueCallback<Uri> agentValueCallback;

    AgentChromeClient agentChromeClient = new AgentChromeClient() {
        @Override
        public boolean onShowFileChooser(View webView, ValueCallback<Uri[]> valueCallback, com.tencent.smtt.sdk.WebChromeClient.FileChooserParams fileChooserParams) {
            agentValueCallbacks = valueCallback;
            //注意监听需要写在这里 否则监听有时候会收不到
            primWeb.setThriedChooserListener(WebFragment.this);
            return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            agentValueCallback = valueCallback;
            primWeb.setThriedChooserListener(WebFragment.this);
            super.openFileChooser(valueCallback);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
            agentValueCallback = valueCallback;
            primWeb.setThriedChooserListener(WebFragment.this);
            super.openFileChooser(valueCallback, acceptType);
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            agentValueCallback = valueCallback;
            primWeb.setThriedChooserListener(WebFragment.this);
            super.openFileChooser(valueCallback, acceptType, capture);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode --> " + requestCode + " | resultCode --> " + resultCode);
        if (resultCode == RESULT_CANCELED && requestCode == 1001) {
            cancelFilePathCallback();
        } else if (resultCode == RESULT_OK && requestCode == 1001) {
            uploadImgVideo(PrimPicker.obtainUriResult(data).get(0));
        }
    }

    private void uploadImgVideo(Uri uri) {
        Uri result = uri;
        Uri[] results = new Uri[]{uri};
        if (agentValueCallbacks != null) {
            agentValueCallbacks.onReceiveValue(results);
            agentValueCallbacks = null;
        } else if (agentValueCallback != null) {
            if (result == null) {
                return;
            }
            agentValueCallback.onReceiveValue(result);
            agentValueCallback = null;
        }
    }

    private void cancelFilePathCallback() {
        if (agentValueCallback != null) {
            agentValueCallback.onReceiveValue(null);
            agentValueCallback = null;
        } else if (agentValueCallbacks != null) {
            agentValueCallbacks.onReceiveValue(null);
            agentValueCallbacks = null;
        }
    }
```

#### 回退的处理简化
```
返回键的回退简化:
 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (primWeb.handlerKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


 返回按钮的回退简化:
if (!primWeb.handlerBack()) {
                    this.finish();
                }
```
### 关于疑问

![有什么问题可以加此群提问](https://upload-images.jianshu.io/upload_images/2005932-587b1cc6224fa33f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 关于我
Android开发爱好者，喜欢钻研技术，目前位于北京工作，如果你有任何问题或工作机会请联系Email:sufululove@gmail.com


## Thinks

[AgentWeb](https://github.com/JakePrim/AgentWeb) SourceCode

## License
```
Copyright (C)  JakePrim(https://github.com/JakePrim/PrimWeb)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
