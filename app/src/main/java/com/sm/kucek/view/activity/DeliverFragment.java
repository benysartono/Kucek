package com.sm.kucek.view.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sm.kucek.R;
import com.sm.kucek.viewmodel.DeliverViewModel;

public class DeliverFragment extends Fragment {

    private DeliverViewModel mViewModel;

    public static DeliverFragment newInstance() {
        return new DeliverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deliver, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new DeliverViewModel();
    }

}
