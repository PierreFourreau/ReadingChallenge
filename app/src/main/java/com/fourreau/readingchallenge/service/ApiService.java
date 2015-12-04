package com.fourreau.readingchallenge.service;

import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.model.Suggestion;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Pierre on 05/07/2015.
 */
public interface ApiService {

    /******************************************************************************/
    /****                       Category                                       ****/
    /******************************************************************************/

    @GET("/categories")
    void listCategories(Callback<List<Category>> callback);
    
    @GET("/categoriesByLevel/{level}")
    void listCategoriesByLevel(@Path("level") String level, Callback<List<Category>> callback);

    @GET("/categories/{id}")
    void getCategoryById(@Path("id") String categoryId, Callback<Category> callback);

    /******************************************************************************/
    /****                       Suggestion                                     ****/
    /******************************************************************************/

    @GET("/suggestionsByCategory/{id}")
    void listSuggestions(@Path("id") String categoryId, Callback<List<Suggestion>> callback);

    @FormUrlEncoded
    @POST("/propositions")
    void addProposition(@Field("libelle_fr") String libelle_fr, @Field("libelle_en") String libelle_en, @Field("categorie_id") String last, Callback<Integer> callback);
}
