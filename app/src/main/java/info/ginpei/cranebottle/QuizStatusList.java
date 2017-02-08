package info.ginpei.cranebottle;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class QuizStatusList extends ArrayList<QuizStatus> {
    protected int currentPosition = -1;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean add(Quiz quiz) {
        return add(new QuizStatus(quiz));
    }

    @Nullable
    public Quiz moveOnToNext() {
        Quiz quiz;
        if (currentPosition + 1 < size()) {
            currentPosition++;
            quiz = getCurrentQuiz();
        } else {
            quiz = null;
        }
        return quiz;
    }

    @Nullable
    public QuizStatus getCurrent() {
        QuizStatus status;
        if (isValidPosition()) {
            status = get(currentPosition);
        } else {
            status = null;
        }
        return status;
    }

    @Nullable
    public Quiz getCurrentQuiz() {
        Quiz quiz;
        if (isValidPosition()) {
            quiz = getQuiz(currentPosition);
        } else {
            quiz = null;
        }
        return quiz;
    }

    public Quiz getQuiz(int position) {
        return get(position).quiz;  // shouldn't be getQuiz()?
    }

    /**
     * Return true if the current position is valid.
     *
     * @return
     */
    public boolean isValidPosition() {
        return 0 <= currentPosition && currentPosition < size();
    }
}
