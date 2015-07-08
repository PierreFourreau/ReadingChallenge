package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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


public class HomeActivity extends BaseActivity {

    @Inject
    ApiService apiService;

    private GridView gridView;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        //get categories from api
        apiService.listCategories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                displayCategories(categories);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.activity_home_error));
                Timber.e("Error get categories : " + error.getMessage());
            }
        });
    }


    /**
     * Display categories from api.
     * @param categories
     */
    public void displayCategories(List<Category> categories) {
        Timber.d("Number of categories retrieved : " + categories.size());

        gridView = (GridView)findViewById(R.id.gridview);
        categoryAdapter = new CategoryAdapter(this, categories);

        //on click category item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get id category from hidden textview
                String idCategory = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setCategoryId(idCategory);
                startActivity(intent);
            }
        });
        gridView.setAdapter(categoryAdapter);
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
