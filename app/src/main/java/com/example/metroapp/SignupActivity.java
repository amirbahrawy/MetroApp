package com.example.metroapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText Email;
    private EditText Password;
    private TextView LoginBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_signup);
        initView();
    }


    private void initView() {
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void Register(View view) {
        final String name=username.getText().toString().trim();
        final String email=Email.getText().toString().trim();
        final String phone_num=phone.getText().toString().trim();
        final String pass=Password.getText().toString().trim();
        if (name.isEmpty()){
            username.setError("name is required");
            username.requestFocus();
            return;
        }
        else if (email.isEmpty()){
            Email.setError("name is required");
            Email.requestFocus();
            return;
        }
        else if (phone_num.isEmpty()){
            phone.setError("name is required");
            phone.requestFocus();
            return;
        }
       else  if (pass.isEmpty()){
            Password.setError("name is required");
            Password.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("enter a vaild mail");
            Email.requestFocus();
            return;
        }
        else if (pass.length()<8){
           Password.setError("pass is too short");
           Password.requestFocus();
           return;
        }
        else if (phone_num.length()<11){
            phone.setError("enter a vaild number");
            phone.requestFocus();
            return;
        }
      else {
          progressBar.setVisibility(View.VISIBLE);
          firebaseAuth.createUserWithEmailAndPassword(email,pass)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          progressBar.setVisibility(View.GONE);
                          if (task.isSuccessful()){
                                final User user=new User(name,email,phone_num);
                                databaseReference.child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignupActivity.this, "Registertion is Done", Toast.LENGTH_SHORT).show();
                                            Email.setText("");
                                            phone.setText("");
                                            Password.setText("");
                                            username.setText("");
                                        }
                                    }
                                });
                          }
                          else
                              Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });
        }
    }
}
