# PrimWeb
PrimWeb 是一个基于的 Android WebView 和 腾讯 x5 WebView，极度容易使用以及功能强大的库，提供了 WebView 一系列的问题解决方案 ，并且轻量和极度灵活，
更方便 webview 切换
```
 PrimWeb primWeb = PrimWeb.with(this)
                .setWebParent(frameLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .setAgentWebView(new X5AgentWebView(this))
                .setAgentWebSetting(new X5DefaultWebSetting(this))
//                .setAgentWebSetting(new DefaultWebSetting(this))
//                .setAgentWebView(new PrimAgentWebView(this))
                .addJavascriptInterface("jsAgent", new MyJavaObject())
                .setModeType(PrimWeb.ModeType.Normal)
                .setWebViewClient(new MyWebViewClient(this))
                .build()
                .ready()
                .launch("https://blog.csdn.net/yy1300326388/article/details/43965493");

        primWeb.callJsLoader().callJS("jsMethod");
 ```
