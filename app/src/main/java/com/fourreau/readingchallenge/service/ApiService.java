package com.fourreau.readingchallenge.service;

import com.fourreau.readingchallenge.model.Category;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Pierre on 05/07/2015.
 */
public interface ApiService {
    @GET("/categories")
    void listCategories(Callback<List<Category>> callback);
}
