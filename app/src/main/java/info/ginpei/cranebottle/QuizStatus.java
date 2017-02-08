package info.ginpei.cranebottle;

public class QuizStatus {
    public static final int STATUS_READY = 0;
    public static final int STATUS_CORRECT = 1;
    public static final int STATUS_INCORRECT = 2;

    protected Quiz quiz;
    protected int status;
    protected String userAnswer = null;

    public Quiz getQuiz() {
        return quiz;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public QuizStatus(Quiz quiz) {
        this.quiz = quiz;

        status = STATUS_READY;
    }

    public boolean isAnswered() {
        return status != STATUS_READY;
    }

    public boolean isCorrect() {
        return status == STATUS_CORRECT;
    }

    public boolean isIncorrect() {
        return status == STATUS_INCORRECT;
    }
}
