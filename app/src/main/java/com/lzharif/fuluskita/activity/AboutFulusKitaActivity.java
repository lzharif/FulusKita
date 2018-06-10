package com.lzharif.fuluskita.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.lzharif.fuluskita.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFulusKitaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String versi = "Beta 0.1";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versi = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Element detailElement = new Element();
        Element iklanElement = new Element();
        Element versiElement = new Element();

        detailElement.setTitle(getString(R.string.detail_about_fuluskita));
        iklanElement.setTitle(getString(R.string.ads_contact_us));
        versiElement.setTitle(versi);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.detail_about_fuluskita))
                .setImage(R.mipmap.ic_launcher)
                .addGroup(getString(R.string.contact_us))
                .addEmail(getString(R.string.email_fuluskita), getString(R.string.email_fuluskita))
                .addWebsite(getString(R.string.website_fuluskita), getString(R.string.website_fuluskita_text))
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setGravity(Gravity.CENTER);
        return copyRightsElement;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
