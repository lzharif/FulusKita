package com.lzharif.fuluskita.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.helper.SharedPreferencesManager;
import com.lzharif.fuluskita.helper.Utils;
import com.lzharif.fuluskita.model.User;

/**
 * Created by lzharif on 20/04/18.
 */

public class LoginActivity extends AppCompatActivity {

    // View
    private EditText mEmail;
    private EditText mPassword;
    private ProgressBar mProgressBar;

    // Firebase
    private FirebaseAuth mFirebaseAuth;
    private User user;
    private DatabaseReference mDatabase = Utils.getDatabase().getReference();
    private SharedPreferencesManager sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for current user
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        sharedPreferences = new SharedPreferencesManager(this);

        // Set up the login form.
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        Button mButtonLogin = findViewById(R.id.btn_login);
        Button mButtonForgotPass = findViewById(R.id.btn_reset_password);
        Button mButtonRegister = findViewById(R.id.btn_signup);
        mProgressBar = findViewById(R.id.progressBar);

        // Set listener for button
        mButtonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewFocus = getCurrentFocus();
                if (viewFocus != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                }
                String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email))
                    Toast.makeText(LoginActivity.this, getString(R.string.insert_email), Toast.LENGTH_SHORT).show();
                else if (!Utils.isEmailValid(email))
                    Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(password))
                    Toast.makeText(LoginActivity.this, getString(R.string.insert_password), Toast.LENGTH_SHORT).show();
                else if (password.length() < 6)
                    Toast.makeText(LoginActivity.this, getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    // authenticate user
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    } else
                                        syncUserData();

                                }
                            }
                    );
                }
            }
        });

        mButtonForgotPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        mButtonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void syncUserData() {
        mDatabase.child(Constant.DIR_USER).child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;
                sharedPreferences.setHasAdminAccess(user.hasAdminAccess);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}

