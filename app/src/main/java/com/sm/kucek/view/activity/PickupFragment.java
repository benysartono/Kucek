package com.sm.kucek.view.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sm.kucek.R;
import com.sm.kucek.viewmodel.PickupViewModel;

public class PickupFragment extends Fragment {

    private PickupViewModel mViewModel;

    public static PickupFragment newInstance() {
        return new PickupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pickup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new PickupViewModel();
        mViewModel.setTitle("PICKUP");
        // TODO: Use the ViewModel
    }

}