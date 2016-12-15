package com.itvedant.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText email, password;
    private Button signIn, signUp, forgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        signIn = (Button) findViewById(R.id.sign_in);
        signUp = (Button) findViewById(R.id.sign_up);
        forgotPassword = (Button) findViewById(R.id.forgot_password);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SignUpActivity.this, SignUpActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SignUpActivity.this, ForgotPasswordActivity.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();

                if (TextUtils.isEmpty(e)) {
                    Toast.makeText(SignInActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(p)) {
                    Toast.makeText(SignInActivity.this, "Enter email password!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(e, p)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SignInActivity.this, "onComplete: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

}
