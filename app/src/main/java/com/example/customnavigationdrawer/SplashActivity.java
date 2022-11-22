package com.example.customnavigationdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);
    }
    private void nextActivity() {
        FirebaseUser firebaseUser   = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            Intent intent = new Intent(this,LoginActivitys.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}