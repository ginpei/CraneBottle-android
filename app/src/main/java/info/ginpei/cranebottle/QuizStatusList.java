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
    public Quiz getCurrentQuiz() {
        return getQuiz(currentPosition);
    }

    @Nullable
    public Quiz getQuiz(int position) {
        Quiz quiz;
        if (0 <= position && position < size()) {
            quiz = get(position).quiz;  // shouldn't be getQuiz()?
        } else {
            quiz = null;
        }
        return quiz;
    }
}
