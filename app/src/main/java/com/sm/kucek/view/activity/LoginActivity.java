package com.sm.kucek.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sm.kucek.R;
import com.sm.kucek.databinding.ActivityLoginBinding;
import com.sm.kucek.model.ModelUser;
import com.sm.kucek.viewmodel.LoginViewModel;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        try {
            viewModel = new LoginViewModel(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        binding.setViewmodel(viewModel);
    }
}
