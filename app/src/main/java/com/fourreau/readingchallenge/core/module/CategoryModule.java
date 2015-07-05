package com.fourreau.readingchallenge.core.module;

import com.fourreau.readingchallenge.activity.HomeActivity;
import com.fourreau.readingchallenge.core.ReadingChallengeApplication;
import com.fourreau.readingchallenge.service.CategoryService;
import com.fourreau.readingchallenge.service.impl.CategoryServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pierre on 05/07/2015.
 */
@Module(
        injects = {HomeActivity.class},
        complete = false,
        library = true
)
public class CategoryModule {
    private final ReadingChallengeApplication application;

    public CategoryModule(ReadingChallengeApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public CategoryService provideCategoryService(CategoryServiceImpl impl) {
        return impl;
    }
}
