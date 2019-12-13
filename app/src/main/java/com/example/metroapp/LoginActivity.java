package com.example.metroapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    protected EditText Email_log;
    protected EditText Password;
    protected TextView LoginBtn;
    protected TextView sign_up;
    private FirebaseAuth firebaseAuth;
    private String email;
    private String password;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup) {

            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        }
        if (view.getId() == R.id.Login_btn) {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));

        }
    }

    private void initView() {
        Email_log= findViewById(R.id.Email_login);
        Password =findViewById(R.id.Password);
        sign_up=findViewById(R.id.signup);
        sign_up.setOnClickListener(LoginActivity.this);
        LoginBtn=findViewById(R.id.Login_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
    }



    public void Login(View view) {
        progressBar.setVisibility(View.VISIBLE);
        email=Email_log.getText().toString().trim();
        password=Password.getText().toString().trim();
        if (email.isEmpty()){
            Email_log.setError("please enter your email");
            Email_log.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (password.length()<6){
            Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
            Password.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        signInUser(email,password);
}

    private void signInUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }

