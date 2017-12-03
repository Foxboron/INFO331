package no.uib.info331.models.messages;

import no.uib.info331.models.Score;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class ScoreEvent {
    private final Score score;

    public ScoreEvent(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }
}
