package com.fourreau.readingchallenge.model;

/**
 * Created by Pierre on 08/07/2015.
 */
public class Suggestion {

    private String suggestion_id;
    private String suggestion_label;
    private String suggestion_label_fr;
    private String categorie_id;

    public Suggestion() {}

    public String getSuggestion_id() {
        return suggestion_id;
    }

    public void setSuggestion_id(String suggestion_id) {
        this.suggestion_id = suggestion_id;
    }

    public String getSuggestion_label() {
        return suggestion_label;
    }

    public void setSuggestion_label(String suggestion_label) {
        this.suggestion_label = suggestion_label;
    }

    public String getSuggestion_label_fr() {
        return suggestion_label_fr;
    }

    public void setSuggestion_label_fr(String suggestion_label_fr) {
        this.suggestion_label_fr = suggestion_label_fr;
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
                "suggestion_id='" + suggestion_id + '\'' +
                ", suggestion_label='" + suggestion_label + '\'' +
                ", suggestion_label_fr='" + suggestion_label_fr + '\'' +
                ", categorie_id='" + categorie_id + '\'' +
                '}';
    }
}
