package com.sm.kucek.view.activity;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sm.kucek.databinding.ActivityMainBinding;
import com.sm.kucek.R;
import com.sm.kucek.model.ModelUser;
import com.sm.kucek.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "extra_user";

    ActivityMainBinding binding;
    MainViewModel viewModel;

    public static Intent newIntent(Context context, ModelUser user){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //GET USER DATA
        //ModelUser user = getIntent().getParcelableExtra(EXTRA_USER);
        ModelUser user = new ModelUser();
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewModel = new MainViewModel(this, user, fragmentManager);
        binding.setViewmodel(viewModel);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, new PickupFragment()).commit();

    }

}
