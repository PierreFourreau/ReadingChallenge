package com.fourreau.readingchallenge.service.impl;

import com.fourreau.readingchallenge.model.Category;
import com.fourreau.readingchallenge.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Pierre on 05/07/2015.
 */
public class CategoryServiceImpl implements CategoryService {

    @Inject
    public CategoryServiceImpl() {}

    @Override
    public List<Category> getAll() {

        List<Category> cat = new ArrayList<Category>();

        return cat;
    }
}
