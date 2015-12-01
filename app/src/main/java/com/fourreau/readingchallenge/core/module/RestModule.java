package com.fourreau.readingchallenge.core.module;

import com.fourreau.readingchallenge.activity.CategoryActivity;
import com.fourreau.readingchallenge.activity.HomeActivity;
import com.fourreau.readingchallenge.activity.ProgressActivity;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.service.ApiService;
import com.fourreau.readingchallenge.util.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Created by Pierre on 05/07/2015.
 */
@Module(
        injects = {HomeActivity.class, CategoryActivity.class, ProgressActivity.class},
        complete = false,
        library = true
)
public class RestModule {

    private final ReadingChallengeApplication application;

    public RestModule(ReadingChallengeApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public ApiService provideApiService() {
        return new RestAdapter.Builder()
                .setEndpoint(Utils.BASE_URL_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Retrofit"))
                .build()
                .create(ApiService.class);
    }
}
