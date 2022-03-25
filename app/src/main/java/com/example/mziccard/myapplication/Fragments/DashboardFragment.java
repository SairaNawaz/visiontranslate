package com.example.mziccard.myapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mziccard.myapplication.MainActivity;
import com.example.mziccard.myapplication.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    View v;
    RelativeLayout btn_camera, btn_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews();
        return v;
    }

    void initViews() {

        btn_camera = v.findViewById(R.id.btn_camera);
        btn_text = v.findViewById(R.id.btn_text);

        btn_camera.setOnClickListener(this);
        btn_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_camera) {
            ((MainActivity) getActivity()).loadFragment(getActivity()
                    , R.string.tag_ocr, null);
        } else if (v == btn_text) {
            ((MainActivity) getActivity()).loadFragment(getActivity()
                    , R.string.tag_text, null);
        }
    }
}
