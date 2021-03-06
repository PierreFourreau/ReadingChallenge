package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SettingsActivity extends BaseActivity {

    private int level, levelBefore, display, displayBefore;
    private RadioButton radioButtonLevel1, radioButtonLevel2, radioButtonLevel3;
    private RadioButton radioButtonDisplay1, radioButtonDisplay2, radioButtonDisplay3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //level
        level = ((ReadingChallengeApplication) this.getApplication()).getLevel();
        levelBefore = level;
        radioButtonLevel1 = (RadioButton) findViewById(R.id.settingsLevel1);
        radioButtonLevel2 = (RadioButton) findViewById(R.id.settingsLevel2);
        radioButtonLevel3 = (RadioButton) findViewById(R.id.settingsLevel3);
        switch (level) {
            case 1:
                radioButtonLevel1.setChecked(true);
                break;
            case 2:
                radioButtonLevel2.setChecked(true);
                break;
            case 3:
                radioButtonLevel3.setChecked(true);
                break;
        }

        //display
        display = ((ReadingChallengeApplication) this.getApplication()).getDisplayColumns();
        displayBefore = display;
        radioButtonDisplay1 = (RadioButton) findViewById(R.id.settingsDisplay1);
        radioButtonDisplay2 = (RadioButton) findViewById(R.id.settingsDisplay2);
        radioButtonDisplay3 = (RadioButton) findViewById(R.id.settingsDisplay3);
        switch (display) {
            case 1:
                radioButtonDisplay1.setChecked(true);
                break;
            case 2:
                radioButtonDisplay2.setChecked(true);
                break;
            case 3:
                radioButtonDisplay3.setChecked(true);
                break;
        }

        //analytics
        ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("SettingsActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                backToPreviousActivity();
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backToPreviousActivity();
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected void backToPreviousActivity() {
        getAndSetLevelByRadio();
        getAndSetDisplayByRadio();
        Intent intent = new Intent();
        if (levelBefore == ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).getLevel()) {
            intent.putExtra("levelChanged", false);
        } else {
            intent.putExtra("levelChanged", true);
        }
        if (displayBefore == ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).getDisplayColumns()) {
            intent.putExtra("displayChanged", false);
        } else {
            intent.putExtra("displayChanged", true);
        }
        setResult(RESULT_OK, intent);
    }

    protected void getAndSetLevelByRadio() {
        if (radioButtonLevel1.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(1);
            writeSharedPreferences(getString(R.string.level), 1);
        } else if (radioButtonLevel2.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(2);
            writeSharedPreferences(getString(R.string.level), 2);
        } else if (radioButtonLevel3.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setLevel(3);
            writeSharedPreferences(getString(R.string.level), 3);
        }
    }

    protected void getAndSetDisplayByRadio() {
        if (radioButtonDisplay1.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setDisplay(1);
            writeSharedPreferences(getString(R.string.display), 1);
        } else if (radioButtonDisplay2.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setDisplay(2);
            writeSharedPreferences(getString(R.string.display), 2);
        } else if (radioButtonDisplay3.isChecked()) {
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setDisplay(3);
            writeSharedPreferences(getString(R.string.display), 3);
        }
    }
}
