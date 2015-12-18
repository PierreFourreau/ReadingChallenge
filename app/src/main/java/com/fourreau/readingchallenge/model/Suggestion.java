package com.fourreau.readingchallenge.model;

/**
 * Created by Pierre on 08/07/2015.
 */
public class Suggestion {

    private String id;
    private String libelle_en;
    private String libelle_fr;
    private String url_en;
    private String url_fr;
    private String categorie_id;

    public Suggestion() {}

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

    public String getUrl_en() {
        return url_en;
    }

    public void setUrl_en(String url_en) {
        this.url_en = url_en;
    }

    public String getUrl_fr() {
        return url_fr;
    }

    public void setUrl_fr(String url_fr) {
        this.url_fr = url_fr;
    }

    public String getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(String categorie_id) {
        this.categorie_id = categorie_id;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "id='" + id + '\'' +
                ", libelle_en='" + libelle_en + '\'' +
                ", libelle_fr='" + libelle_fr + '\'' +
                ", url_en='" + url_en + '\'' +
                ", url_fr='" + url_fr + '\'' +
                ", categorie_id='" + categorie_id + '\'' +
                '}';
    }
}
