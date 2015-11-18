package com.example.yeshiyuan.mycsdn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeshiyuan.mycsdn.R;
import com.example.yeshiyuan.mycsdn.adapter.TabAdapter;

/**
 * Created by yeshiyuan on 11/18/15.
 */
public class MainFragment extends Fragment {

    private int newsType=0;



    public MainFragment(int position) {
        this.newsType=position;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_item_fragment_main,null);
        TextView tip = (TextView) view.findViewById(R.id.id_tip);
        tip.setText(TabAdapter.TITLES[newsType]);
        return view;
    }
}
