package com.fourreau.readingchallenge.core;

import android.app.Application;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.fourreau.readingchallenge.BuildConfig;
import com.fourreau.readingchallenge.R;
import com.fourreau.readingchallenge.model.Category;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import timber.log.Timber;

/**
 * Application class.
 *
 * Created by Pierre on 05/07/2015.
 */
public class ReadingChallengeApplication extends Application {

  public List<Category> categories;
  public String categoryId;
  public String language;
  //categories filter : 0 -> all, 1 -> read, 2 -> unread
  public int filterCategories;
  //level -> 1 : beginner, 2 -> intermediate, 3 -> expert
  public int level;
  //display number of columns
  public int display;

  private Tracker mTracker;

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());

    //init logger
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Timber.plant(new CrashReportingTree());
    }

    //set device language
    setLanguage(Locale.getDefault().getLanguage());
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getFilterCategories() {
    return filterCategories;
  }

  public void setFilterCategories(int filterCategories) {
    this.filterCategories = filterCategories;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getDisplay() {
    return display;
  }

  public void setDisplay(int display) {
    this.display = display;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  /** A tree which logs important information for crash reporting. */
  private static class CrashReportingTree extends Timber.Tree {
    @Override protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }

      // TODO e.g., Crashlytics.log(String.format(message, args));
      Crashlytics.log(priority, tag, message);

      if (t != null) {
        if (priority == Log.ERROR) {
          Crashlytics.logException(t);
        } else if (priority == Log.WARN) {
          Crashlytics.log(t.toString());
        }
      }
    }
  }

  /**
   * Gets the default {@link Tracker} for this {@link Application}.
   *
   * @return tracker
   */
  synchronized public Tracker getDefaultTracker() {
    if (mTracker == null) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
      mTracker = analytics.newTracker(R.xml.global_tracker);
    }
    return mTracker;
  }
}
