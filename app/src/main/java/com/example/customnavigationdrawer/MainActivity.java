package com.example.customnavigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.customnavigationdrawer.fragment.PersonalFragment;
import com.example.customnavigationdrawer.fragment.SettingFragment;
import com.example.customnavigationdrawer.fragment.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;



public class MainActivity extends AppCompatActivity  {

    private MeowBottomNavigation bottomNavigation;
    private Fragment fragment;

    public static String mHumdity ="";
    public static String mTemperature="";


    public static final String TAG = MainActivity.class.getName();

    public static String TOKEN_DEVICE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getToken();
    }

    private void initView() {
        // init fragment home when initially application
        replaceFragment(new HomeFragment());

        // init BottomNavigation
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_settings_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_person_24));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        fragment = new SettingFragment();
                        break;
                    case 2:
                        fragment = new HomeFragment();
                        break;
                    case 3 :
                        fragment = new PersonalFragment();
                        break;
                }
                replaceFragment(fragment);
            }
        });


        // set home fragment initially selected;
        bottomNavigation.show(2,true);
    }






    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();
    }
    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("AAA", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                         TOKEN_DEVICE = task.getResult();
                    }
                });
             }
}