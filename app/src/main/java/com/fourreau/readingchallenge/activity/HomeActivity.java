package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
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
    private PullRefreshLayout layout;

    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((ReadingChallengeApplication) getApplication()).inject(this);

        gridView = (GridView) findViewById(R.id.gridview);

        //listen refresh event
        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
            }
        });
        getCategories();
    }

    public void getCategories() {
        layout.setRefreshing(true);
        level = String.valueOf(((ReadingChallengeApplication) this.getApplication()).getLevel());
        //get categories from api
        apiService.listCategories(level, new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                displayCategories(categories);
                layout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                displayErrorSnackBar(getString(R.string.activity_home_error));
                layout.setRefreshing(false);
                Timber.e("Error get categories : " + error.getMessage());
            }
        });
    }

    /**
     * Display categories from api.
     *
     * @param categories
     */
    public void displayCategories(List<Category> categories) {
        Timber.d("Number of categories retrieved : " + categories.size());
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
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
        gridView.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        int filterCategories = ((ReadingChallengeApplication) this.getApplicationContext()).getFilterCategories();
        if (filterCategories == 1) {
            menu.findItem(R.id.menu_filter_read).setChecked(true);
            setTitle(getString(R.string.radio_categories_read));
        } else if (filterCategories == 2) {
            menu.findItem(R.id.menu_filter_unread).setChecked(true);
            setTitle(getString(R.string.radio_categories_unread));
        } else {
            menu.findItem(R.id.menu_filter_all).setChecked(true);
            setTitle(getString(R.string.radio_categories));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            gridView.setAdapter(null);
            getCategories();
        }

        if (id == R.id.action_progress) {
            Intent intent = new Intent(HomeActivity.this, ProgressActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            return true;
        }

        //all categories
        if (id == R.id.menu_filter_all) {
            item.setChecked(true);
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setFilterCategories(0);
            writeSharedPreferences(getString(R.string.filter), 0);
            gridView.setAdapter(null);
            getCategories();
            setTitle(getString(R.string.radio_categories));
            return true;
        }
        //readed categories
        if (id == R.id.menu_filter_read) {
            item.setChecked(true);
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setFilterCategories(1);
            gridView.setAdapter(null);
            getCategories();
            setTitle(getString(R.string.radio_categories_read));
            return true;
        }
        //unreaded categories
        if (id == R.id.menu_filter_unread) {
            item.setChecked(true);
            ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setFilterCategories(2);
            gridView.setAdapter(null);
            getCategories();
            setTitle(getString(R.string.radio_categories_unread));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
