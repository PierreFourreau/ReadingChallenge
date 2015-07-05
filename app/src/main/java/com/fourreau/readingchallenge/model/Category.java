package com.fourreau.readingchallenge.model;

/**
 * Created by Pierre on 05/07/2015.
 */
public class Category {
    private String categorie_id;
    private String categorie_label;

    public Category() {}


    @Override
    public String toString() {
        return "Category{" +
                "categorie_id='" + categorie_id + '\'' +
                ", categorie_label='" + categorie_label + '\'' +
                '}';
    }

    public String getCategorie_label() {
        return categorie_label;
    }

    public void setCategorie_label(String categorie_label) {
        this.categorie_label = categorie_label;
    }

    public String getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(String categorie_id) {
        this.categorie_id = categorie_id;
    }




}
