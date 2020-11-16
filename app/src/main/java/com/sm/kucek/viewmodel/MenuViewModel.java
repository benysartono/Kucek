package com.sm.kucek.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.sm.kucek.model.ModelUser;
import com.sm.kucek.view.activity.LoginActivity;

public class MenuViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Context context;
    private ModelUser user;

    public MenuViewModel(ModelUser user, Context context) {
        this.user = user;
        this.context = context;
    }

    public void logout(View view){
        Log.d("Masuk Logout", "Masuk");
        ModelUser.clearRegisteredUser(context);
        ModelUser.clearRegisteredPass(context);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    public String getUsername(){
        return ModelUser.getRegisteredUser(context);
    }

    public String getPassword(){
        return ModelUser.getRegisteredPass(context);
    }


}
