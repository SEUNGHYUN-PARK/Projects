package com.example.seunghyeonpark.dailycondition.Tutorial;

/**
 * Created by SeungHyeonPark on 2016. 11. 10..
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seunghyeonpark.dailycondition.R;

public class SettingTutorial extends Fragment {

    public static SettingTutorial newInstance() {
        SettingTutorial fragment = new SettingTutorial();
        return fragment;
    }

    public SettingTutorial() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.setting_tutorial, null);
        return root;
    }
}