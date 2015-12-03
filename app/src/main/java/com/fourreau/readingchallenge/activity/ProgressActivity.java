package com.fourreau.readingchallenge.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.adapter.CategoryAdapter;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.service.ApiService;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class ProgressActivity extends BaseActivity {

    @Inject
    ApiService apiService;

    private TextView numberReadCategoriesTextView, numberTotalCategoriesTextView, progressCommentTextView, percentageRead;
    private int numberReadCategories = 0;
    private ProgressDialog mProgressDialog;

    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        level = String.valueOf(((ReadingChallengeApplication) this.getApplication()).getLevel());

        getCategories();
    }

    public void getCategories() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();

        //get categories from api
        apiService.listCategoriesByLevel(level, new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                calculateNumberOfReadCategories(categories);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.activity_home_error));
                mProgressDialog.dismiss();
                Timber.e("Error get categories progress : " + error.getMessage());
            }
        });
    }

    public void calculateNumberOfReadCategories(List<Category> categories) {
        Timber.d("Number of categories retrieved : " + categories.size());

        String key = getString(R.string.category_id);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
        for (Category cat : categories) {
            if (sharedPref.getInt(key + cat.getId(), 0) == 1) {
                numberReadCategories++;
            }
        }

        numberReadCategoriesTextView = (TextView) findViewById(R.id.progress_number_read);
        numberTotalCategoriesTextView = (TextView) findViewById(R.id.progress_number_total);
        progressCommentTextView = (TextView) findViewById(R.id.progress_comment);
        percentageRead = (TextView) findViewById(R.id.progress_percentage);

        numberReadCategoriesTextView.setText(" " + numberReadCategories + " ");
        numberTotalCategoriesTextView.setText(" " + categories.size() + " ");
        //percentage
        percentageRead.setText(categories.size()/numberReadCategories + " %");

        //if user read all categories
        if (numberReadCategories == categories.size()) {
            progressCommentTextView.setText(getString(R.string.progress_comment_all));
        }
        //no one
        else if (numberReadCategories == 0) {
            progressCommentTextView.setText(getString(R.string.progress_comment0));
        }
        //beginner
        else if (level == "1") {
            if (numberReadCategories < 2)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 4)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 6)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 8)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories < 10)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }
        //intermediate
        else if (level == "2") {
            if (numberReadCategories < 4)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 8)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 12)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 16)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories < 20)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }
        //expert
        else if (level == "3") {
            if (numberReadCategories < 10)
                progressCommentTextView.setText(getString(R.string.progress_comment10));
            else if (numberReadCategories < 20)
                progressCommentTextView.setText(getString(R.string.progress_comment20));
            else if (numberReadCategories < 30)
                progressCommentTextView.setText(getString(R.string.progress_comment30));
            else if (numberReadCategories < 40)
                progressCommentTextView.setText(getString(R.string.progress_comment40));
            else if (numberReadCategories < 50)
                progressCommentTextView.setText(getString(R.string.progress_comment50));
        }

        mProgressDialog.dismiss();
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
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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