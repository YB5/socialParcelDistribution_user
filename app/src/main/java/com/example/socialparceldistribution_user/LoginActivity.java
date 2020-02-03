package com.example.socialparceldistribution_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText fullNameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private String fullName;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        fullNameEt = findViewById(R.id.et_full_name);
        passwordEt = findViewById(R.id.et_password);
        emailEt = findViewById(R.id.et_email);
        Button signIn_bt = findViewById(R.id.bt_sign_in);
        Button signUp_bt = findViewById(R.id.bt_sign_up);
        final ConstraintLayout constraintLayout = findViewById(R.id.container);


        signUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_validate()) {
                    Toast.makeText(LoginActivity.this, "input is not valid, try again", Toast.LENGTH_LONG).show();
                    return;
                }
                fullName = fullNameEt.getText().toString();
                String password = passwordEt.getText().toString();
                String email = emailEt.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Toast.makeText(LoginActivity.this,"successful, welcome!!!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", fullName);
                            startActivity(intent);
                        } else {
                            Snackbar.make(constraintLayout, "unsuccess to sign-up", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_validate()) {
                    return;
                }
                String password = passwordEt.getText().toString();
                String email = emailEt.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", firebaseAuth.getCurrentUser().getDisplayName());
                            startActivity(intent);
                        } else {
                            Snackbar.make(constraintLayout, "unsuccess to sign-in", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        Button quickEntering = findViewById(R.id.bt_enter);
        quickEntering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameEt.setText("iii");
                passwordEt.setText("123456");
                emailEt.setText("u@u.com");
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // TextView userTv = findViewById(R.id.user_tv);
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {//sign up or sign in
                    userName = user.getDisplayName();
                    if (fullName != null && !fullName.isEmpty()) { //sign up - update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullName = null;
                                if (task.isSuccessful())
                                    Toast.makeText(LoginActivity.this, user.getDisplayName() + " logged in. Welcome!!", Toast.LENGTH_LONG).show();
                                //Snackbar.make(linearLayout, user.getDisplayName() + " Welcome!!!", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //userTv.setText(user.getDisplayName() + " logged in");
                } //else {
                //userTv.setText("please log in");

                //}
            }
        };
    }

    private boolean check_validate() {
        if (fullNameEt.getText().toString().isEmpty() || emailEt.getText().toString().isEmpty() || passwordEt.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "input is not valid, try again", Toast.LENGTH_LONG).show();
            return false;
        }
        if (passwordEt.getText().length() < 6) {
            Toast.makeText(this, "password should be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailEt.getText().toString());
        if (!matcher.matches()) {
            Toast.makeText(this, "email must be in valid address format", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}

