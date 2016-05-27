package com.benlewis.cbr2;

/**
 * Created by benlewis on 19/05/16.
 */
public class Hero {

    String hero;
    double similarity;

    public Hero (String hero, double similarity) {
        this.hero = hero;
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return ("Hero: " + hero + " Similarity: " + (int) similarity + "%");
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }
}
