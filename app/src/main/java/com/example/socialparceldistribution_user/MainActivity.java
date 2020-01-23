package com.example.socialparceldistribution_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText fullNameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private Button signIn_bt;
    private Button signOut_bt;
    private Button signUp_bt;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        fullNameEt = findViewById(R.id.et_full_name);
        passwordEt = findViewById(R.id.et_password);
        emailEt = findViewById(R.id.et_email);
        signIn_bt = findViewById(R.id.bt_sign_in);
        signOut_bt = findViewById(R.id.bt_sign_out);
        signUp_bt = findViewById(R.id.bt_sign_up);
        final LinearLayout linearLayout = findViewById(R.id.linearLayout);

        signUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullNameEt.getText().toString();
                String password = passwordEt.getText().toString();
                String email = emailEt.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(linearLayout, "success sign-up", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(linearLayout, "unsuccess to sign-up", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signIn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEt.getText().toString();
                String email = emailEt.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(linearLayout, "Wwlcome", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(linearLayout, "unsuccess to sign-in", Snackbar.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                TextView userTv = findViewById(R.id.user_tv);
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {//sign up or sign in
                    if (fullName!=null&&!fullName.isEmpty()) { //sign up - update profile with full name

                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                fullName = null;
                                if (task.isSuccessful())
                                    Snackbar.make(linearLayout, user.getDisplayName() + " Welcome!!!", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                    userTv.setText(user.getDisplayName() + " logged in");
                } else {
                    userTv.setText("please log in");

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}

