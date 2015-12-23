package com.fourreau.readingchallenge.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.model.CircleDisplay;
import com.fourreau.readingchallenge.service.ApiService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class ProgressActivity extends BaseActivity {

    @Inject
    ApiService apiService;

    private TextView progressLine, progressCommentTextView;
    private int numberReadCategories = 0, totalCategories = 0;
    private ProgressDialog mProgressDialog;
    private ImageView imageHeader, progressIcon;
    private CircleDisplay circleDisplay;

    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        level = String.valueOf(((ReadingChallengeApplication) this.getApplication()).getLevel());

        //images
        imageHeader = (ImageView) findViewById(R.id.imageHeader);
        progressIcon = (ImageView) findViewById(R.id.progressIcon);
        Picasso.with(this).load(R.drawable.default_category1).fit().centerCrop().into(imageHeader);
        Picasso.with(this).load(R.mipmap.ic_launcher).into(progressIcon);

        getCategories();

        //analytics
        ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("ProgressActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void getCategories() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        //get categories from api
        apiService.listCategoriesByLevel(level, new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                calculateNumberOfReadCategories(categories);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.progress_error));
                mProgressDialog.dismiss();
                Timber.e("Error get categories progress : " + error.getMessage());
            }
        });
    }

    public void calculateNumberOfReadCategories(List<Category> categories) {
        String key = getString(R.string.category_id);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        for (Category cat : categories) {
            if (sharedPref.getInt(key + cat.getId(), 0) == 1) {
                numberReadCategories++;
            }
        }

        totalCategories = categories.size();
        progressLine = (TextView) findViewById(R.id.progress_line);
        progressCommentTextView = (TextView) findViewById(R.id.progress_comment);
        progressLine.setText(getString(R.string.progress_number_label1) + " " + numberReadCategories + " " + getString(R.string.progress_number_label2) + " " + totalCategories);

        //percentage
        float percentage = 0.0f;
        if (numberReadCategories != 0) {
            percentage = ((float)numberReadCategories) / totalCategories;
        }
        CircleDisplay cd = (CircleDisplay) findViewById(R.id.circleDisplay);
        cd.setAnimDuration(1500);
        cd.setValueWidthPercent(20f);
        cd.setTextSize(20f);
        cd.setColor(getResources().getColor(R.color.primary));
        cd.setTouchEnabled(false);
        cd.setFormatDigits(1);
        cd.setUnit("%");
        cd.setStepSize(0.5f);
        cd.showValue(percentage*100, 100f, true);

        //if user read all categories
        if (numberReadCategories == totalCategories) {
            progressCommentTextView.setText(getString(R.string.progress_comment_all));
        }
        //no one
        else if (numberReadCategories == 0) {
            progressCommentTextView.setText(getString(R.string.progress_comment0));
        }
        //beginner
        else if (level.equals("1")) {
            if (numberReadCategories < 2)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 4)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 6)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 8)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories <= 11)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }
        //intermediate
        else if (level.equals("2")) {
            if (numberReadCategories < 4)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 8)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 12)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 16)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories <= 23)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }
        //expert
        else if (level.equals("3")) {
            if (numberReadCategories < 10)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 20)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 30)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 40)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories <= 51)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }

        mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_progress, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                return true;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.action_share_progress1) + " " + numberReadCategories + " " + getString(R.string.action_share_progress2) + " " + totalCategories + " " + getString(R.string.action_share_progress3));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
