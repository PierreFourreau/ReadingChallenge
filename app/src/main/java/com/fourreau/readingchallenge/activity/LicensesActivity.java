package com.fourreau.readingchallenge.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.util.Utils;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class LicensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licences);
        ((WebView) findViewById(R.id.web_view)).loadUrl("file:///android_asset/licenses.html");

        //analytics
        ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("LicensesActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
