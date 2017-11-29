package no.uib.info331.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class Score {
    @SerializedName("Score")
    private int score;

    public Score(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
