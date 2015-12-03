package com.fourreau.readingchallenge.service;

import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.model.Suggestion;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Pierre on 05/07/2015.
 */
public interface ApiService {

    /******************************************************************************/
    /****                       Category                                       ****/
    /******************************************************************************/

    @GET("/categories/{level}")
    void listCategories(@Path("level") String level, Callback<List<Category>> callback);

    @GET("/categories/{id}")
    void getCategoryById(@Path("id") String categoryId, Callback<Category> callback);

    /******************************************************************************/
    /****                       Suggestion                                     ****/
    /******************************************************************************/

    @GET("/suggestionsByCategory/{id}")
    void listSuggestions(@Path("id") String categoryId, Callback<List<Suggestion>> callback);
}
