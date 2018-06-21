package com.prim.web.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.prim.primweb.core.PrimWeb;
import com.prim.web.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment implements ItemSelected, FragmentKeyDown {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webParent = (FrameLayout) view.findViewById(R.id.webParent);
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
                    .setWebViewType(PrimWeb.WebViewType.Android)
                    .buildWeb()
                    .lastGo()
                    .launch(mParam1);
        }

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
