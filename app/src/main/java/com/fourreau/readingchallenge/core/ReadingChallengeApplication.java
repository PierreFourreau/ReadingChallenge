package com.fourreau.readingchallenge.core;

import android.app.Application;

import com.fourreau.readingchallenge.BuildConfig;
import com.fourreau.readingchallenge.core.module.RestModule;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Application class.
 *
 * Created by Pierre on 05/07/2015.
 */
public class ReadingChallengeApplication extends Application {

    public String categoryId;
    public String language;
    //categories filter : 0 -> all, 1 -> read, 2 -> unread
    public int filterCategories;
    //level -> 1 : beginner, 2 -> intermediate, 3 -> expert
    public int level;
    //display number of columns
    public int display;

    private ObjectGraph applicationGraph;

    @Override public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());

        //init logger
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //Timber.plant(new CrashReportingTree());
        }

        //set device language
        setLanguage(Locale.getDefault().getLanguage());

        applicationGraph = ObjectGraph.create(getModules().toArray());
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

    /**
     * A list of modules to use for the application graph. Subclasses can override this method to
     * provide additional modules provided they call {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new RestModule(this));
    }

    public void inject(Object target) {
        applicationGraph.inject(target);
    }

    /** A tree which logs important information for crash reporting. */
//    private static class CrashReportingTree extends Timber.Tree {
//        @Override
//        protected void log(int priority, String tag, String message, Throwable t) {
//            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
//                return;
//            }
//
//            // TODO e.g., Crashlytics.log(String.format(message, args));
//            Crashlytics.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    Crashlytics.logException(t);
//                } else if (priority == Log.WARN) {
//                    Crashlytics.log(t.toString());
//                }
//            }
//        }
//    }
}
