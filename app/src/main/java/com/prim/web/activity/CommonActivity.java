package com.prim.web.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.prim.web.R;
import com.prim.web.fragment.FragmentKeyDown;
import com.prim.web.fragment.ItemSelected;
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        extra = getIntent().getStringExtra(TYPE);
        supportFragmentManager = this.getSupportFragmentManager();
        openFragment(extra);
    }

    private WebFragment webFragment;

    private void openFragment(String extra) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        String url;
        switch (extra) {
            case "0":
                url = "https://m.jd.com/";
                fragmentTransaction.add(R.id.frame_layout, webFragment = WebFragment.newInstance(url, "Fragment"), WebFragment.class.getSimpleName());
                break;
            case "1":
                url = "file:///android_asset/sms.html";
                fragmentTransaction.add(R.id.frame_layout, webFragment = WebFragment.newInstance(url, "SMS"), WebFragment.class.getSimpleName());
                break;
            case "2":
                url = "https://m.jd.com/";
                fragmentTransaction.add(R.id.frame_layout, webFragment = WebFragment.newInstance(url, "CustomErrorPage"), WebFragment.class.getSimpleName());
                break;
            case "3":
                url = "file:///android_asset/js_interaction/hello.html";
                fragmentTransaction.add(R.id.frame_layout, webFragment = WebFragment.newInstance(url, "JS"), WebFragment.class.getSimpleName());
                break;
            case "4":
                break;
            case "5":
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webFragment != null) {
                    ItemSelected itemSelected = webFragment;
                    itemSelected.handlerBack();
                }
                return true;
            case R.id.more:
                showPoPup(findViewById(R.id.more));
                return true;
            case R.id.close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private PopupMenu mPopupMenu;

    /**
     * 显示更多菜单
     *
     * @param view
     *         菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }


    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            ItemSelected itemSelected = null;
            if (webFragment != null) {
                itemSelected = webFragment;
            }
            switch (item.getItemId()) {
                case R.id.refresh:
                    if (itemSelected != null) {
                        itemSelected.refresh();
                    }
                    return true;
                case R.id.copy:
                    if (itemSelected != null) {
                        itemSelected.copy();
                    }
                    return true;
                case R.id.default_browser:
                    if (itemSelected != null) {
                        itemSelected.openBrowser();
                    }
                    return true;
                case R.id.default_clean:
                    if (itemSelected != null) {
                        itemSelected.clearWebViewCache();
                    }
                    return true;
                case R.id.error_website:
                    if (itemSelected != null) {
                        itemSelected.errorPage();
                    }
                    return true;
                case R.id.enter_website:
                    if (itemSelected != null) {
                        itemSelected.enterPage();
                    }
                    return true;
                default:
                    return false;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webFragment != null) {
            FragmentKeyDown fragmentKeyDown = webFragment;
            if (fragmentKeyDown.handlerKeyEvent(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
