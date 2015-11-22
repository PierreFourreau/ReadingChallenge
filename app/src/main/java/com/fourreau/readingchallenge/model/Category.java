package com.fourreau.readingchallenge.model;

/**
 * Created by Pierre on 05/07/2015.
 */
public class Category {
    private String id;
    private String libelle_en;
    private String libelle_fr;
    private String description_en;
    private String description_fr;
    private String image;

    public Category() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle_en() {
        return libelle_en;
    }

    public void setLibelle_en(String libelle_en) {
        this.libelle_en = libelle_en;
    }

    public String getLibelle_fr() {
        return libelle_fr;
    }

    public void setLibelle_fr(String libelle_fr) {
        this.libelle_fr = libelle_fr;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_fr() {
        return description_fr;
    }

    public void setDescription_fr(String description_fr) {
        this.description_fr = description_fr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", libelle_en='" + libelle_en + '\'' +
                ", libelle_fr='" + libelle_fr + '\'' +
                ", description_en='" + description_en + '\'' +
                ", description_fr='" + description_fr + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
