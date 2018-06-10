package com.lzharif.fuluskita.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzharif.fuluskita.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {

    // TODO make UI summary
    // TODO make function to display report
    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

}
