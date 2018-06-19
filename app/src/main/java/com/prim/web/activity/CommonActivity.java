package com.prim.web.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.prim.web.R;
import com.prim.web.fragment.WebFragment;

public class CommonActivity extends AppCompatActivity implements WebFragment.OnFragmentInteractionListener {

    private FrameLayout frame_layout;

    public static final String TYPE = "0";

    private String extra;

    FragmentManager supportFragmentManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        extra = getIntent().getStringExtra(TYPE);
        supportFragmentManager = this.getSupportFragmentManager();
        openFragment(extra);
    }

    private void openFragment(String extra) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        String url;
        switch (extra) {
            case "0":
                url = "https://m.vip.com/?source=www&jump_https=1";
                fragmentTransaction.add(R.id.frame_layout, WebFragment.newInstance(url, ""), WebFragment.class.getSimpleName());
                break;
            case "1":
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                url = "file:///android_asset/sms.html";
                fragmentTransaction.add(R.id.frame_layout, WebFragment.newInstance(url, ""), WebFragment.class.getSimpleName());
                break;
            case "6":
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
