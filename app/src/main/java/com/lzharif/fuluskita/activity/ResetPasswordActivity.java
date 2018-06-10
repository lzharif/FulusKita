package com.lzharif.fuluskita.activity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Utils;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mEmail;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEmail = findViewById(R.id.email_reset);
        Button mButtonReset = findViewById(R.id.btn_reset_password);
        Button mButtonBack = findViewById(R.id.btn_back);
        mProgressBar = findViewById(R.id.progressBar_reset);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                if (TextUtils.isEmpty(email))
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.insert_email), Toast.LENGTH_SHORT).show();
                else if (!Utils.isEmailValid(email))
                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(ResetPasswordActivity.this, getString(R.string.success_send_reset), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_send_reset), Toast.LENGTH_SHORT).show();

                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
