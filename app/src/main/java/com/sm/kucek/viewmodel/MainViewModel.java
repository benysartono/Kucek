package com.sm.kucek.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sm.kucek.R;
import com.sm.kucek.model.ModelUser;
import com.sm.kucek.view.activity.DeliverFragment;
import com.sm.kucek.view.activity.LoginActivity;
import com.sm.kucek.view.activity.PickupFragment;

/**
 * Created by tofin on 22/04/16.
 */
public class MainViewModel {
    private Context context;
    private ModelUser user;
    private FragmentManager fragmentManager;

    public MainViewModel(Context context, ModelUser user, FragmentManager fragmentManager) {
        this.context = context;
        this.user = user;
        this.fragmentManager = fragmentManager;
    }

    public void logout(View view){
        Log.d("Masuk Logout", "Masuk");
        ModelUser.clearRegisteredUser(context);
        ModelUser.clearRegisteredPass(context);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void onBtnActPickupClick(View v) {
        loadFragment(new PickupFragment());
    }

    public void onBtnActDeliverClick(View v) {
        loadFragment(new DeliverFragment());
    }

    private void loadFragment(Fragment fragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit(); // save the changes
    }

    public String getUsername(){
        return ModelUser.getRegisteredUser(context);
    }

    public String getPassword(){
        return ModelUser.getRegisteredPass(context);
    }
}
