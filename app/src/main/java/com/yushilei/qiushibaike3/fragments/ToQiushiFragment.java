package com.yushilei.qiushibaike3.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yushilei.qiushibaike3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToQiushiFragment extends Fragment {
    private static final String TEXT = "text";
    private TextView textView;

    public ToQiushiFragment() {
        // Required empty public constructor
    }

    public static ToQiushiFragment newInstance(String text) {
        ToQiushiFragment fragment = new ToQiushiFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEXT, text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_qiushi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments.containsKey(TEXT)) {
            String s = arguments.getString(TEXT);
            textView = (TextView) view.findViewById(R.id.toqiushi_text);
            textView.setText(s);
        }
    }
}
