package com.fourreau.readingchallenge.model;

/**
 * Created by Pierre on 08/07/2015.
 */
public class Suggestion {

    private String id;
    private String libelle_en;
    private String libelle_fr;
    private String user_language;
    private String user_email;
    private String categorie_id;

    public Suggestion() {
    }

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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_language() {
        return user_language;
    }

    public void setUser_language(String user_language) {
        this.user_language = user_language;
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
                ", user_language='" + user_language + '\'' +
                ", user_email='" + user_email + '\'' +
                ", categorie_id='" + categorie_id + '\'' +
                '}';
    }
}
