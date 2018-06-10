package com.lzharif.fuluskita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.model.User;

public class EditAccountActivity extends AppCompatActivity {

    private EditText teks1;
    private EditText teks2;
    private Button btnSubmit;
    private int key;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private boolean apakahEmailBenar = false;
    private String newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Toolbar toolBar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSubmit = findViewById(R.id.submit_change_account);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmChangeAccount();
            }
        });

        if (bundle != null) {
            key = bundle.getInt(Constant.KEY_ACCOUNT);
            switch (key) {
                case 0:
                    getSupportActionBar().setTitle(getString(R.string.change_email_toolbar_title));
                    teks1 = findViewById(R.id.edit_old_email);
                    teks2 = findViewById(R.id.edit_new_email);
                    EditText abal = findViewById(R.id.edit_new_password);
                    TextInputLayout abal2 = findViewById(R.id.til_pass);
                    abal.setVisibility(View.GONE);
                    abal2.setVisibility(View.GONE);
                    teks1.setText(bundle.getString(Constant.KEY_EMAIL, ""));
                    teks1.setEnabled(false);
                    aturFloatingErrorEmail();
                    break;
                case 1:
                    getSupportActionBar().setTitle(getString(R.string.change_password_toolbar_title));
                    teks1 = findViewById(R.id.edit_new_password);
                    EditText abal1 = findViewById(R.id.edit_old_email);
                    EditText abal3 = findViewById(R.id.edit_new_email);
                    abal1.setVisibility(View.GONE);
                    abal3.setVisibility(View.GONE);
                    aturFloatingPasswordLabel();
                    break;
            }
        }
    }

    private void aturFloatingPasswordLabel() {
        final TextInputLayout floatingPass = findViewById(R.id.til_pass);
        floatingPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 6) {
                    floatingPass.setError(getString(R.string.short_password));
                    floatingPass.setErrorEnabled(true);
                } else {
                    floatingPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void aturFloatingErrorEmail() {
        final TextInputLayout floatingEmail = findViewById(R.id.til_new_email);
        floatingEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isEmailValid(charSequence)) {
                    floatingEmail.setErrorEnabled(true);
                    floatingEmail.setError(getString(R.string.error_invalid_email));
                    apakahEmailBenar = false;
                } else {
                    floatingEmail.setErrorEnabled(false);
                    apakahEmailBenar = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isEmailValid(CharSequence charSequence) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void confirmChangeAccount() {
        switch (key) {
            case 0:
                newEmail = teks2.getText().toString().trim();
                if (user != null && !newEmail.equals("")) {
                    //Cek apakah emailnya valid atau gak
                    if (apakahEmailBenar) {
                        updateEmailUser();
                    }
                } else if (newEmail.equals("")) {
                    teks2.setError(getString(R.string.error_invalid_email));
                }
                break;
            case 1:
                if (user != null && !teks1.getText().toString().trim().equals("")) {
                    if (teks1.getText().toString().trim().length() < 6) {
                        teks1.setError(getString(R.string.error_invalid_password));
                    } else {
                        user.updatePassword(teks1.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditAccountActivity.this, getString(R.string.success_change_password), Toast.LENGTH_SHORT).show();
                                            signOut();
                                        } else {
                                            Toast.makeText(EditAccountActivity.this, getString(R.string.failed_change_password), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else if (teks1.getText().toString().trim().equals("")) {
                    teks1.setError(getString(R.string.error_invalid_password));
                }

        }
    }

    private void updateEmailUser() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference()
                .child(Constant.DIR_USER).child(uid);
        dbUser.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User u = mutableData.getValue(User.class);
                if (u == null)
                    return Transaction.success(mutableData);
                else {
                    u.email = newEmail;
                    mutableData.setValue(u);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                user.updateEmail(newEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditAccountActivity.this, getString(R.string.success_change_email), Toast.LENGTH_SHORT).show();
                                    signOut();
                                } else {
                                    Toast.makeText(EditAccountActivity.this, getString(R.string.failed_change_email), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    private void signOut() {
        auth.signOut();
        startActivity(new Intent(EditAccountActivity.this, LoginActivity.class));
        finish();
    }
}
