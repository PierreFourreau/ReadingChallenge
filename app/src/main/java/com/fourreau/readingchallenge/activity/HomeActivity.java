package com.fourreau.readingchallenge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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


public class HomeActivity extends AppCompatActivity {

    @Inject
    ApiService apiService;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((ReadingChallengeApplication) getApplication()).inject(this);

       button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);

            }

        });

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new CategoryAdapter(this));

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

//        ObservableListView listView = (ObservableListView) findViewById(R.id.list);
//        listView.setScrollViewCallbacks(this);
//
//        // TODO These are dummy. Populate your data here.
//        ArrayList<String> items = new ArrayList<String>();
//        for (int i = 1; i <= 100; i++) {
//            items.add("Item " + i);
//        }
//        listView.setAdapter(new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, items));
    }

//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll,
//                                boolean dragging) {
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//    }
//
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        ActionBar ab = getSupportActionBar();
//        if (scrollState == ScrollState.UP) {
//            if (ab.isShowing()) {
//                ab.hide();
//            }
//        } else if (scrollState == ScrollState.DOWN) {
//            if (!ab.isShowing()) {
//                ab.show();
//            }
//        }
//    }

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
