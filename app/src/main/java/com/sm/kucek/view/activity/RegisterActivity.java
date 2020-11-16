package com.sm.kucek.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sm.kucek.R;
import com.sm.kucek.databinding.ActivityRegisterBinding;
import com.sm.kucek.model.ModelUser;
import com.sm.kucek.viewmodel.RegisterViewModel;

import java.io.UnsupportedEncodingException;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    RegisterViewModel viewModel;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        viewModel = new RegisterViewModel(this);
        binding.setRegisterviewmodel(viewModel);
    }

}

