package ua.panchuk.osmos;

import java.io.Serializable;

public class Record implements Serializable, Comparable {
    private String author;
    private float score;

    public Record(String author, float score) {
        this.author = author;
        this.score = score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object another) {
        return (int)(((Record) another).getScore()-score);
    }
}
