package com.example.customnavigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivitys extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView btnLogin,tranfer_signup;
    private EditText email,pass;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activitys);
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btn_login);
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_password);
        tranfer_signup = findViewById(R.id.tranfer_signup);
        tranfer_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivitys.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickToLoginFirebase();
            }
        });
        progressDialog = new ProgressDialog(this);
    }
    private void clickToLoginFirebase(){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivitys.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Log.d("AAA", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivitys.this, "Login failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}