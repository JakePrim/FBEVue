package com.prim.primweb.core.config.builder;

import android.support.annotation.NonNull;

import com.prim.primweb.core.PrimWeb;

/**
 * @author prim
 * @version 1.0.0
 * @desc
 * @time 2019-08-26 - 18:42
 */
public class PerBuilder {
    private PrimWeb primWeb;
    private boolean isReady = false;

    public PerBuilder(PrimWeb primWeb) {
        this.primWeb = primWeb;
    }

    public PerBuilder lastGo() {
        if (!isReady) {
            primWeb.ready();
            isReady = true;
        }
        return this;
    }

    public PrimWeb launch(@NonNull String url) {
        if (!isReady) {
            lastGo();
        }
        return primWeb.launch(url);
    }

    public PrimWeb launch(@NonNull String url, boolean enablePool) {
        if (!isReady) {
            lastGo();
        }
        return enablePool ? primWeb : primWeb.launch(url);
    }
}
