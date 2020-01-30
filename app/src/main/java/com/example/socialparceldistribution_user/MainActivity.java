package com.example.socialparceldistribution_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText fullNameEt;
    private EditText passwordEt;
    private EditText emailEt;
    private Button signIn_bt;
    private Button signOut_bt;
    private Button signUp_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main)  ;

        firebaseAuth = FirebaseAuth.getInstance();
        fullNameEt= findViewById(R.id.et_full_name);
        passwordEt= findViewById(R.id.et_password);
        emailEt= findViewById(R.id.et_email);
        signIn_bt= findViewById(R.id.bt_sign_in);
        signOut_bt= findViewById(R.id.bt_sign_out);
        signUp_bt=findViewById(R.id.bt_sign_up);

        signUp_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName= fullNameEt.getText().toString();
                String password= passwordEt.getText().toString();
                String email =emailEt.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                });

            }
        });






    }


}

