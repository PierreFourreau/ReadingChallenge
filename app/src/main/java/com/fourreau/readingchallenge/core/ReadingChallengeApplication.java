package com.fourreau.readingchallenge.core;

import android.app.Application;

import com.fourreau.readingchallenge.BuildConfig;
import com.fourreau.readingchallenge.core.module.CategoryModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Application class.
 *
 * Created by Pierre on 05/07/2015.
 */
public class ReadingChallengeApplication extends Application {

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

        applicationGraph = ObjectGraph.create(getModules().toArray());
    }

    /**
     * A list of modules to use for the application graph. Subclasses can override this method to
     * provide additional modules provided they call {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new CategoryModule(this));
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
