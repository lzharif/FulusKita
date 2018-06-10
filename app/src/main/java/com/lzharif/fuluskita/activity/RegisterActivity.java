package com.lzharif.fuluskita.activity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.helper.Utils;
import com.lzharif.fuluskita.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzharif on 20/04/18.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mUsername;
    private EditText mPassword;
    private ProgressBar mProgressBar;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference = Utils.getDatabase().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.email_register);
        mUsername = findViewById(R.id.username_register);
        mPassword = findViewById(R.id.password_register);
        Button mRegister = findViewById(R.id.register_btn);
        Button mForgotPassword = findViewById(R.id.reset_password_btn);
        Button mLogin = findViewById(R.id.login_btn);
        mProgressBar = findViewById(R.id.progressBar_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString();

                Query query = mDatabaseReference.orderByChild(Constant.USERNAME).equalTo(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.username_exist), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (TextUtils.isEmpty(email))
                    Toast.makeText(RegisterActivity.this, getString(R.string.insert_email), Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(username))
                    Toast.makeText(RegisterActivity.this, getString(R.string.insert_user), Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(password))
                    Toast.makeText(RegisterActivity.this, getString(R.string.insert_password), Toast.LENGTH_SHORT).show();
                else if (!Utils.isEmailValid(email))
                    Toast.makeText(RegisterActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                else if (password.length() < 6)
                    Toast.makeText(RegisterActivity.this, getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful())
                                        Toast.makeText(RegisterActivity.this, getString(R.string.error_register), Toast.LENGTH_SHORT).show();
                                    else {
                                        final String uid = mFirebaseAuth.getCurrentUser().getUid();
                                        // For now, default admin access is true
                                        // TODO create authentication system for getting admin access
                                        User user = new User(username, email, true, 0, 0);
                                        Map<String, Object> userValue = user.toMapRegister();
                                        Map<String, Object> childUpdates = new HashMap<>();

                                        childUpdates.put("/user/" + uid, userValue);
                                        mDatabaseReference.updateChildren(childUpdates);

                                        // Prepare to go to MainActivity
                                        mProgressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }


            }
        });
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, ResetPasswordActivity.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
