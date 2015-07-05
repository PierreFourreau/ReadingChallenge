package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.service.ApiService;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class HomeActivity extends AppCompatActivity {

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        Timber.d("Begin...");
        apiService.listCategories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                afficherCategories(categories);
            }

            @Override
            public void failure(RetrofitError error) {
                afficherError();
                Timber.d("Error : " + error.toString());
            }
        });
        Timber.d("End...");
    }

    public void afficherError() {
        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
    }


    public void afficherCategories(List<Category> categories) {
        Toast.makeText(this,"nb cat : "+categories.size(), Toast.LENGTH_SHORT).show();
        for(Category cat : categories) {
            Timber.d(""+cat.getCategorie_label());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
