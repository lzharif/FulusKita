package com.lzharif.fuluskita.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.helper.Constant;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PengaturanActivity";
    private static Context mContext;
    //    private SharedFirebasePreferences mPreferences;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FirebaseApp.initializeApp(this);
        mContext = this;

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Jika tidak ada pengguna, logout ke halaman Login Activity
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authListener);
        showView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showView() {
        toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.setting_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.frameSetting, new TestPreferenceFragment()).commitAllowingStateLoss();
        findViewById(R.id.progessBar).setVisibility(View.GONE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            showView();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }

    public static class TestPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_duitkita);

            Preference changeEmail = findPreference(getString(R.string.change_email_key));
            Preference changePass = findPreference(getString(R.string.change_password_key));
            Preference logoutAccount = findPreference(getString(R.string.logout_key));

            changeEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Bundle bundlean = new Bundle();
                    bundlean.putInt(Constant.KEY_ACCOUNT, 0);
                    bundlean.putString(Constant.KEY_EMAIL, getEmail());
                    Intent intent = new Intent(mContext, EditAccountActivity.class);
                    intent.putExtras(bundlean);
                    startActivity(intent);
                    return true;
                }
            });

            changePass.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Bundle bundlean = new Bundle();
                    bundlean.putInt(Constant.KEY_ACCOUNT, 1);
                    Intent intent = new Intent(mContext, EditAccountActivity.class);
                    intent.putExtras(bundlean);
                    startActivity(intent);
                    return true;
                }
            });

            logoutAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()

            {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    signOut();
                    return true;
                }
            });
        }

        private String getEmail() {
            return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        }

        private void signOut() {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(mContext, getString(R.string.success_logout), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    }
}
