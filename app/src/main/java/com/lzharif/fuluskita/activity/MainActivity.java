package com.lzharif.fuluskita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lzharif.fuluskita.R;
import com.lzharif.fuluskita.fragment.TransactionFragment;
import com.lzharif.fuluskita.helper.Constant;
import com.lzharif.fuluskita.helper.Utils;
import com.lzharif.fuluskita.model.User;

/**
 * Created by lzharif on 20/04/18.
 */

// TODO create function to support currency other than Rupiah
public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference = Utils.getDatabase().getReference();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView mUsername;
    private TextView mEmail;
    private Toolbar toolbar;
    private String[] activityTitles;

    private String CURRENT_TAG = "transaction";
    private int navItemIndex = 0;
    private String uid;
    private User user;
    private Handler mHandler;
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // TODO add function to customize Nav Header and imgProfile
        navHeader = navigationView.getHeaderView(0);
        mUsername = navHeader.findViewById(R.id.name_header);
        mEmail = navHeader.findViewById(R.id.email_header);
        imgNavHeaderBg = navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);

        // Set auth listener
        initAuth();

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = Constant.TAG_TRANSACTION;
            loadHomeFragment();
        }
    }

    private void initLoadProfile() {
        DatabaseReference db = mDatabaseReference.child(Constant.DIR_USER).child(uid);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                // load nav menu header data
                loadNavHeader(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    uid = firebaseAuth.getCurrentUser().getUid();
                    initLoadProfile();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable);

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void loadNavHeader(User userModel) {
        if (userModel != null) {
            String username = userModel.username;
            String email = userModel.email;
            mUsername.setText(username);
            mEmail.setText(email);
        } else {
            mUsername.setText(getString(R.string.loading));
            mEmail.setText(getString(R.string.loading));
        }
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Transaction Fragment
                return new TransactionFragment();
//            case 1:
//                // Profile Fragment
//                return new ProfileFragment();
//            case 2:
//                // Summary Fragment
//                return new SummaryFragment();
            case 3:
                // Setting Activity
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
                return new TransactionFragment();
            default:
                return new TransactionFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_balance:
                        navItemIndex = 0;
                        CURRENT_TAG = Constant.TAG_TRANSACTION;
                        break;
//                    case R.id.nav_profile:
//                        navItemIndex = 1;
//                        CURRENT_TAG = Constant.TAG_PROFILE;
//                        break;
//                    case R.id.nav_summary:
//                        navItemIndex = 2;
//                        CURRENT_TAG = Constant.TAG_SUMMARY;
//                        break;
                    case R.id.nav_settings:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutFulusKitaActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_donate:
                        new MaterialDialog.Builder(MainActivity.this)
                                .content(getString(R.string.content_dialog_donation))
                                .positiveText(getString(R.string.oke_dialog))
                                .neutralText(getString(R.string.paypal_dialog_donation))
                                .show();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = Constant.TAG_TRANSACTION;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }
}
