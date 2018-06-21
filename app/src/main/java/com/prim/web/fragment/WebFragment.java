package com.prim.web.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.prim.primweb.core.PrimWeb;
import com.prim.primweb.core.jsloader.AgentValueCallback;
import com.prim.primweb.core.jsloader.CommonJSListener;
import com.prim.primweb.core.utils.PrimWebUtils;
import com.prim.web.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment implements ItemSelected, FragmentKeyDown, View.OnClickListener
        , CommonJSListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FrameLayout webParent;

    public WebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1
     *         Parameter 1.
     * @param param2
     *         Parameter 2.
     *
     * @return A new instance of fragment WebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WebFragment newInstance(String param1, String param2) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    private PrimWeb primWeb;

    private LinearLayout ll_JS;

    private Button jsClick1, jsClick2, jsClick3, jsClick4, jsClick5, jsClick6;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webParent = (FrameLayout) view.findViewById(R.id.webParent);
        ll_JS = (LinearLayout) view.findViewById(R.id.ll_JS);
        jsClick1 = (Button) view.findViewById(R.id.jsClick1);
        jsClick2 = (Button) view.findViewById(R.id.jsClick2);
        jsClick3 = (Button) view.findViewById(R.id.jsClick3);
        jsClick4 = (Button) view.findViewById(R.id.jsClick4);
        jsClick5 = (Button) view.findViewById(R.id.jsClick5);
        jsClick6 = (Button) view.findViewById(R.id.jsClick6);
        jsClick3.setOnClickListener(this);
        jsClick2.setOnClickListener(this);
        jsClick1.setOnClickListener(this);
        jsClick4.setOnClickListener(this);
        jsClick6.setOnClickListener(this);
        jsClick5.setOnClickListener(this);
        initArg();
    }

    private void initArg() {
        if (mParam2.equals("CustomErrorPage")) {
            primWeb = PrimWeb.with(getActivity())
                    .setWebParent(webParent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useCustomUI(R.layout.custom_error_page, R.id.click_refush)
                    .useDefaultTopIndicator()
                    .setWebViewType(PrimWeb.WebViewType.Android)
                    .buildWeb()
                    .lastGo()
                    .launch(mParam1);
        } else {
            primWeb = PrimWeb.with(getActivity())
                    .setWebParent(webParent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultUI()
                    .useDefaultTopIndicator()
                    .setWebViewType(PrimWeb.WebViewType.X5)
                    .setListenerCheckJsFunction(this)
                    .buildWeb()
                    .lastGo()
                    .launch(mParam1);
        }
        if (mParam2.equals("JS")) {
            ll_JS.setVisibility(View.VISIBLE);
            primWeb.getJsInterface().addJavaObject(new MyJsInterface(), "android");
        }
    }

    @Override
    public void jsFunExit(Object data) {
        Toast.makeText(getActivity(), data.toString() + "方法存在", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void jsFunNoExit(Object data) {
        Toast.makeText(getActivity(), data.toString() + "方法不存在", Toast.LENGTH_SHORT).show();
    }

    public class MyJsInterface {

        @JavascriptInterface
        public void callAndroid(final String data) {
            PrimWebUtils.runUIRunable(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private static final String TAG = "WebFragment";

    public void jsClick1() {
        primWeb.getCallJsLoader().callJS("callByAndroid");
    }

    public void jsClick2() {
        primWeb.getCallJsLoader().callJS("callByAndroidParam", 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void jsClick3() {
        AgentValueCallback<String> agentValueCallback = new AgentValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {
                Log.e(TAG, "onReceiveValue: " + value);
            }
        };
        primWeb.getCallJsLoader().callJs("callByAndroidMoreParams", agentValueCallback, getJson(), "prim", true);

    }

    public void jsClick4() {
        primWeb.getCallJsLoader().callJS("callByAndroidInteraction", "Hello JS");
    }

    private void jsClick6() {
        primWeb.getCallJsLoader().checkJsMethod("android");
    }

    private void jsClick5() {
        primWeb.getCallJsLoader().checkJsMethod("callByAndroidInteraction");
    }

    private String getJson() {

        String result = "";
        try {

            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("id", 1);
            mJSONObject.put("name", "Prim");
            mJSONObject.put("age", 1);
            result = mJSONObject.toString();
        } catch (Exception e) {

        }

        return result;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        primWeb.webLifeCycle().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        primWeb.webLifeCycle().onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        primWeb.webLifeCycle().onDestory();
    }

    @Override
    public void handlerBack() {
        if (!primWeb.handlerBack()) {
            getActivity().finish();
        }
    }

    @Override
    public void refresh() {
        if (primWeb != null) {
            primWeb.getUrlLoader().reload();
        }
    }

    @Override
    public void copy() {
        if (primWeb != null) {
            primWeb.copyUrl();
        }
    }

    @Override
    public void openBrowser() {
        if (primWeb != null) {
            primWeb.openBrowser(primWeb.getUrl());
        }
    }

    @Override
    public void clearWebViewCache() {
        if (primWeb != null) {
            primWeb.clearWebViewCache();
        }
    }

    @Override
    public void errorPage() {
        if (primWeb != null) {
            primWeb.getUrlLoader().loadUrl("http://www.unkownwebsiteblog.me");
        }
    }

    @Override
    public void enterPage() {
        if (primWeb != null) {
            primWeb.getUrlLoader().loadUrl("https://m.jd.com/");
        }
    }

    @Override
    public boolean handlerKeyEvent(int keyCode, KeyEvent event) {
        if (primWeb == null) return false;
        return primWeb.handlerKeyEvent(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jsClick3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    jsClick3();
                }
                break;
            case R.id.jsClick2:
                jsClick2();
                break;
            case R.id.jsClick1:
                jsClick1();
                break;
            case R.id.jsClick4:
                jsClick4();
                break;
            case R.id.jsClick5:
                jsClick5();
                break;
            case R.id.jsClick6:
                jsClick6();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
