package com.fourreau.readingchallenge.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.baoyz.widget.PullRefreshLayout;
import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.adapter.CategoryAdapter;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.service.ApiService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class HomeActivity extends BaseActivity {

  @Inject ApiService apiService;

  private GridView gridView;
  private CategoryAdapter categoryAdapter;
  private PullRefreshLayout layout;

  private String level;

  private CategoryAdapter.Item categoryChoosen;
  private ImageView categoryChoosenPictureRead;

  static final int CATEGORY_REQUEST = 1;
  static final int SETTINGS_REQUEST = 2;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ((ReadingChallengeApplication) getApplication()).inject(this);

    gridView = (GridView) findViewById(R.id.gridview);

    ((ReadingChallengeApplication) this.getApplication()).setDisplay(readSharedPreferences(getString(R.string.display)));

    //listen refresh event
    layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        getCategories();
      }
    });
    getCategories();

    //admobs
    AdView mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    //analytics
    ReadingChallengeApplication application = (ReadingChallengeApplication) getApplication();
    Tracker mTracker = application.getDefaultTracker();
    mTracker.setScreenName("HomeActivity");
    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
  }

  public void getCategories() {
    layout.setRefreshing(true);
    level = String.valueOf(((ReadingChallengeApplication) this.getApplication()).getLevel());
    //get categories from api
    try {
      Type listType;
      InputStream is = getAssets().open("categories.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);

      String json = new String(buffer, "UTF-8");
      listType = new TypeToken<ArrayList<Category>>() {
      }.getType();
      List<Category> categories = new Gson().fromJson(json, listType);
      ((ReadingChallengeApplication) this.getApplication()).setCategories(categories);
      displayCategories(categories);
      layout.setRefreshing(false);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    //apiService.listCategoriesByLevel(level, new Callback<List<Category>>() {
    //  @Override public void success(List<Category> categories, Response response) {
    //    displayCategories(categories);
    //    layout.setRefreshing(false);
    //  }
    //
    //  @Override public void failure(RetrofitError error) {
    //    displayAlertDialog(getString(R.string.error), getString(R.string.activity_home_error));
    //    layout.setRefreshing(false);
    //    Timber.e("Error get categories : " + error.getMessage());
    //  }
    //});
  }

  /**
   * Display categories from api.
   */
  public void displayCategories(List<Category> categories) {
    categoryAdapter = new CategoryAdapter(this, categories);

    //on click category item
    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        categoryChoosen = categoryAdapter.getItem(position);
        categoryChoosenPictureRead = (ImageView) view.getTag(R.id.picture_category_read);

        //get id category from hidden textview
        String idCategory = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        ((ReadingChallengeApplication) getApplicationContext().getApplicationContext()).setCategoryId(idCategory);
        startActivityForResult(intent, CATEGORY_REQUEST);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
      }
    });

    gridView.setNumColumns(((ReadingChallengeApplication) this.getApplication()).getDisplay());
    gridView.setAdapter(categoryAdapter);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == CATEGORY_REQUEST) {
        if (data.getExtras() != null
            && data.getExtras().getBoolean("checkChanged")
            && ((ReadingChallengeApplication) getApplicationContext()).getFilterCategories() != 0) {
          gridView.setAdapter(null);
          getCategories();
        } else {
          setImageReadOnCategory();
        }
      } else if (requestCode == SETTINGS_REQUEST) {
        if (data.getExtras().getBoolean("levelChanged") || data.getExtras().getBoolean("displayChanged")) {
          gridView.setAdapter(null);
          getCategories();
        }
      }
    }
  }

  /**
   * When user go to category and go back to home -> refresh icon read
   */
  protected void setImageReadOnCategory() {
    //if book is already read or not
    SharedPreferences sharedPref = getSharedPreferences("readingchallenge", Context.MODE_PRIVATE);
    if (categoryChoosen != null) {
      if (sharedPref.getInt(getString(R.string.category_id) + categoryChoosen.id, 0) == 1) {
        Picasso.with(this).load(R.drawable.circle_check).into(categoryChoosenPictureRead);
        categoryChoosenPictureRead.setVisibility(View.VISIBLE);
      } else {
        categoryChoosenPictureRead.setVisibility(View.GONE);
      }
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
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

  @Override public boolean onOptionsItemSelected(MenuItem item) {
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

    if (id == R.id.action_help) {
      Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
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
      startActivityForResult(intent, SETTINGS_REQUEST);
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
